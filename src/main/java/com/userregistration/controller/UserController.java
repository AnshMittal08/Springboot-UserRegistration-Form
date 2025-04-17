package com.userregistration.controller;

import com.userregistration.dto.UserLookupDTO;
import com.userregistration.exception.DuplicateAadhaarException;
import com.userregistration.exception.UserNotFoundException;
import com.userregistration.model.User;
import com.userregistration.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/register";
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
            return "redirect:/register";
        } catch (DuplicateAadhaarException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
    
    @GetMapping("/lookup")
    public String showLookupForm(Model model) {
        model.addAttribute("lookupDTO", new UserLookupDTO());
        return "lookup";
    }
    
    @PostMapping("/lookup")
    public String lookupUser(@Valid @ModelAttribute("lookupDTO") UserLookupDTO lookupDTO,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            return "lookup";
        }
        
        try {
            User user = userService.findUserByAadhaarAndDob(lookupDTO);
            model.addAttribute("user", user);
            return "user-details";
        } catch (UserNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "lookup";
        }
    }
}