package com.raunak.Ecommerce.controller;

import com.raunak.Ecommerce.global.GlobalData;
import com.raunak.Ecommerce.model.EmailDetails;
import com.raunak.Ecommerce.model.Product;
import com.raunak.Ecommerce.repository.UserDao;
import com.raunak.Ecommerce.service.EmailService;
import com.raunak.Ecommerce.service.EmailServiceImpl;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class RazorpayController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserDao userDao;
    @Value("${rzp_key_id}")
    private String appKey;
    @Value("${rzp_key_secret}")
    private String appSecret;
    @Value("${rzp_company_name}")
    private String companyName;
    @RequestMapping(value = {"/payment"}, method = RequestMethod.GET)
    public String payment(Model model) {
        model.addAttribute("amount", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("rzp_key_id", appKey);
        model.addAttribute("rzp_company_name", companyName);
        return "payment";
    }
    @GetMapping("/payment/createOrderId/{amount}")
    @ResponseBody
    public String createPaymentOrderId(@PathVariable String amount) {
        String orderId=null;
        try {
            RazorpayClient razorpay = new RazorpayClient(appKey, appSecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");

            Order order = razorpay.orders.create(orderRequest);
            orderId = order.get("id");
        } catch (RazorpayException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        }
        return orderId;
    }
    @PostMapping("/payment/success")
    public String paymentSuccess(@RequestParam Map<String, String> body) {
        String temp = "redirect:/successLaunch/" + body.get("razorpay_order_id") + "/" + body.get("razorpay_payment_id") + "/" + body.get("razorpay_signature");
        return temp;
        // return "successABCD";
    }

    @GetMapping("/successLaunch/{orderId}/{paymentId}/{signature}")
    public String showSuccess(Model model,
                              @PathVariable String orderId,
                              @PathVariable String paymentId,
                              @PathVariable String signature
                              )
    {
        model.addAttribute("razorpay_payment_id", paymentId);
        model.addAttribute("razorpay_order_id", orderId);
        model.addAttribute("razorpay_signature", signature);

        // Sending the user the email
        EmailDetails emailDetails = new EmailDetails();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String email = userDao.findByUserName(username).getEmail();
        emailDetails.setRecipient(email);
        String subject = "Crust & Crumbs: Your Order is Successfully Placed.";
        emailDetails.setSubject(subject);
        String body = "Thanks for shopping with us!. Your order with order id: "+ orderId +" and payment id: "+ paymentId +" is successfully placed";
        emailDetails.setMsgBody(body);

        emailService.sendMail(emailDetails);

        return "success";
    }
}
