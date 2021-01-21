package org.launchcode.liftoffproject.controllers;

import org.launchcode.liftoffproject.models.*;
import org.launchcode.liftoffproject.models.data.ProductRepository;
import org.launchcode.liftoffproject.models.data.UserRepository;
import org.launchcode.liftoffproject.models.data.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("marketplace")
public class MarketPlaceController {

    static HashMap<String, String> searchChoices = new HashMap<>();

    public MarketPlaceController (){
        searchChoices.put("all", "All");
        searchChoices.put("type", "Type");
        searchChoices.put("location", "Location");
        searchChoices.put("vendor", "Vendor");
    }

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private UserRepository userRepository;

    //METHOD TO GET USER FROM SESSION
    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("user");
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }


    @RequestMapping("")
    public String list(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = getUserFromSession(session);
            model.addAttribute("user", user);
        }

        Iterable<Product> products;
        Iterable<Vendor> vendors;

        products = productRepository.findAll();
        vendors = vendorRepository.findAll();

        model.addAttribute("products", products);
        model.addAttribute("vendors", vendors);
        model.addAttribute("columns", searchChoices);


        return "marketplace";
    }

//    @PostMapping("users/profile/{vendorId}")
//    public String displayVendorProfile(Model model, @PathVariable int vendorId) {
//
//    }

    @PostMapping("results")
    public String listProductsByValue(Model model, @RequestParam String searchTerm, @RequestParam(required = false) String searchType) {
        Iterable<Product> products;
        Iterable<Vendor> vendors;

        products = productRepository.findAll();
        vendors = vendorRepository.findAll();


        if(searchTerm == null || searchTerm.toLowerCase().equals("all")){

            products = productRepository.findAll();
            vendors = vendorRepository.findAll();

        }
        products = ProductData.findByValue(searchTerm, productRepository.findAll());
        vendors = VendorData.findByValue(searchTerm, vendorRepository.findAll());
        model.addAttribute("products", products);
        model.addAttribute("vendors", vendors);

        return "results";
    }

}