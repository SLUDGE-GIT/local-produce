package org.launchcode.liftoffproject.controllers;

import org.launchcode.liftoffproject.models.Product;
import org.launchcode.liftoffproject.models.User;
import org.launchcode.liftoffproject.models.Vendor;
import org.launchcode.liftoffproject.models.data.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

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
    public String index(Model model, HttpServletRequest request){
        model.addAttribute("title", "Local Produce");

        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = getUserFromSession(session);
            model.addAttribute("user", user);
        }

        return "index";
    }

    @GetMapping("users/favorites")
    public String displayFavoriteVendors(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            //get user from session
            User user = getUserFromSession(session);
            model.addAttribute("user", user);

            //get user's favoriteVendors list
            List<Vendor> favorites = user.getFavoriteVendors();
            model.addAttribute("favoriteVendors", favorites);
        }

        return "users/favorites";
    }

    @GetMapping("users/profile/{vendorId}")
    public String displayViewVendor(Model model, @PathVariable int vendorId, HttpServletRequest request) {

        //Check for session and get user
        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = getUserFromSession(session);
            model.addAttribute("user", user);
        }

        //Get the right vendor profile
        Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
        if (optionalVendor.isPresent()) {
            Vendor vendor = (Vendor) optionalVendor.get();
            model.addAttribute("vendor", vendor);

            Iterable<Product> vendorProducts = vendor.getProducts();
            vendorProducts = productRepository.findByVendor(vendor);
            model.addAttribute("vendorProducts", vendorProducts);

            return "users/profile";
        } else {
            return "redirect:../";
        }
    }

    @PostMapping("users/profile/{vendorId}")
    public String processViewVendorButton(Model model, @PathVariable int vendorId,HttpServletRequest request) {

        //Check for session and get user
        HttpSession session = request.getSession(false);

        //get user from session
        User user = getUserFromSession(session);
        model.addAttribute("user", user);

        //get user's favoriteVendors list
        List<Vendor> favorites = user.getFavoriteVendors();

        //If button is clicked, add Vendor to user's fav list
        Optional<Vendor> optionalVendor = vendorRepository.findById(vendorId);
        if (optionalVendor.isPresent()) {
            Vendor vendor = (Vendor) optionalVendor.get();
            favorites.add(vendor);
            model.addAttribute("vendor", vendor);

        }

        model.addAttribute("favoriteVendors", favorites);

        return "redirect:../profile/{vendorId}";

    }
}