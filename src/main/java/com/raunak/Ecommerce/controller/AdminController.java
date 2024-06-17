package com.raunak.Ecommerce.controller;
import com.raunak.Ecommerce.dto.ProductDTO;
import com.raunak.Ecommerce.model.Category;
import com.raunak.Ecommerce.model.Product;
import com.raunak.Ecommerce.service.CategoryService;
import com.raunak.Ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";
    private CategoryService categoryService;
    private ProductService productService;
    @Autowired
    public AdminController(CategoryService theCategoryService, ProductService theProductService) {
        categoryService = theCategoryService;
        productService = theProductService;
    }
    @GetMapping("/admin")
    public String adminHome() {
        return "adminHome";
    }
    @GetMapping("/admin/categories")
    public String getCategories(Model model) {
        List<Category> allCategories = categoryService.getAllCategory();
        model.addAttribute("categories", allCategories);
        return "categories";
    }
    @GetMapping("/admin/categories/add")
    public String addCategories(Model model) {
        Category tempCategory = new Category();
        model.addAttribute("category", tempCategory);
        return "categoriesAdd";
    }
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }
    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.findCategoryById(id);
        if(category.isPresent()) {
            Category obj = category.get();
            model.addAttribute("category", obj);
            return "categoriesAdd";
        }
        return "404";
    }
    @PostMapping("/admin/categories/add")
    public String saveCategory(@ModelAttribute("category") Category theCategory) {
        categoryService.addCategory(theCategory);
        // redirect to categories
        return "redirect:/admin/categories";
    }

    // Product routes handling
    @GetMapping("/admin/products")
    public String getProducts(Model model) {
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("products", allProducts);
        return "products";
    }
    @GetMapping("/admin/products/add")
    public String productAddGet(Model model) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("categories", categoryService.getAllCategory());
        return "productsAdd";
    }
    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO")ProductDTO productDTO,
                                 @RequestParam("productImagehello")MultipartFile file) throws IOException { // multipart throw krta hai IO Exception

        // apne paas frontend se productDTO object aa rha hai aur hume product object save krna hai
        // toh us productDTO se apan ko pahle product object banana padega
        Product product = new Product();

        product.setId(productDTO.getId());
        product.setName(productDTO.getName());

        // product dto se categoryId milegi, uska use krke category object lenge aur product me add set kr denge
        product.setCategory(categoryService.findCategoryById(productDTO.getCategoryId()).get());

        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDescription(productDTO.getDescription());

        String imageUUID = productDTO.getImageName(); // image ka naam

        if(!file.isEmpty()) {
            // matlab admin ne file daali hai
            // just save the image
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable Long id, Model model) {
        // ab idhar productDTO banana hai product ka use krke
        Optional<Product> op = productService.getProductById(id);
        if(op.isPresent()) {
            Product product = op.get();
            ProductDTO productDTO = new ProductDTO();

            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setCategoryId(product.getCategory().getId());
            productDTO.setPrice(product.getPrice());
            productDTO.setWeight(product.getWeight());
            productDTO.setDescription(product.getDescription());
            productDTO.setImageName(product.getImageName());

            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("productDTO", productDTO);
            return "productsAdd";
        }
        else {
            return "404";
        }
    }
}







//    @PostMapping("/admin/products/add")
//    public String productAddPost(@ModelAttribute("productDTO")ProductDTO productDTO,
//                                 @RequestParam("productImage")MultipartFile file,
//                                 @RequestParam("imgName")String imgName) throws IOException { // multipart throw krta hai IO Exception
//
//        // apne paas frontend se productDTO object aa rha hai aur hume product object save krna hai
//        // toh us productDTO se apan ko pahle product object banana padega
//        Product product = new Product();
//
//        product.setId(productDTO.getId());
//        product.setName(productDTO.getName());
//
//        // product dto se categoryId milegi, uska use krke category object lenge aur product me add set kr denge
//        product.setCategory(categoryService.findCategoryById(productDTO.getCategoryId()).get());
//
//        product.setPrice(productDTO.getPrice());
//        product.setWeight(productDTO.getWeight());
//        product.setDescription(productDTO.getDescription());
//
//        String imageUUID;
//
//        if(!file.isEmpty()) {
//            // matlab admin ne file daali hai
//            imageUUID = file.getOriginalFilename();
//            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
//            Files.write(fileNameAndPath, file.getBytes());
//        }
//        else {
//            // matlab admin ne koi file nhi daali, in case of updating product, jaruri nhi ki image update kro
//            imageUUID = imgName;
//        }
//
//        product.setImageName(imageUUID);
//        productService.addProduct(product);
//
//        return "redirect:/admin/products";
//    }
