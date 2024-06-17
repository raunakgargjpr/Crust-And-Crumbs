package com.raunak.Ecommerce.controller;

import com.raunak.Ecommerce.global.GlobalData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showMyLoginPage() {
        GlobalData.cart.clear();
        return "login";
    }
    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }
}
