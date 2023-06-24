package org.example.SecurityApp.controllers;

import org.example.SecurityApp.models.Role;
import org.example.SecurityApp.models.User;
import org.example.SecurityApp.repositories.RoleRepository;
import org.example.SecurityApp.services.RegistrationService;
import org.example.SecurityApp.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RoleRepository roleRepository;
    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(RoleRepository roleRepository, RegistrationService registrationService, UserValidator userValidator) {
        this.roleRepository = roleRepository;
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public ModelAndView registrationPage() {
        User user = new User();
        ModelAndView mav = new ModelAndView("auth/registration");
        mav.addObject("user", user);

        List<Role> roles = (List<Role>) roleRepository.findAll();

        mav.addObject("allRoles", roles);

        return mav;
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        registrationService.register(user);
        return "redirect:/login";
    }
}
