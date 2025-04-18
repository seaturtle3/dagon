package kroryi.dagon.controller;

import kroryi.dagon.entity.Product;
import kroryi.dagon.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("product", new Product());
        return "partnerShipRegister";
    }

    @PostMapping("/register")
    public String registerProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/product/list";
    }

}
