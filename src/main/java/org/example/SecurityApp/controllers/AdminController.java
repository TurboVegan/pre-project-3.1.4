package org.example.SecurityApp.controllers;


import org.example.SecurityApp.models.Role;
import org.example.SecurityApp.models.User;
import org.example.SecurityApp.repositories.RoleRepository;
import org.example.SecurityApp.services.UserService;
import org.example.SecurityApp.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserValidator userValidator;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public AdminController(UserValidator userValidator,
                           RoleRepository roleRepository, UserService userService) {
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping()
    public String index(ModelMap model, Principal principal) {

        User LoggedInUser = userService.findByUsername(principal.getName());

        model.addAttribute("LoggedInUser", LoggedInUser);
        model.addAttribute("users", userService.findAll());
        List<Role> roles = (List<Role>) roleRepository.findAll();
        model.addAttribute("allRoles", roles);

        return "admin/index";
    }

    @GetMapping("/user/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findOne(id));
        return "admin/show";
    }

    @GetMapping("/new")
    public ModelAndView newUser(Principal principal) {

        User user = new User();
        ModelAndView mav = new ModelAndView("admin/new");
        mav.addObject("user", user);
        User LoggedInUser = userService.findByUsername(principal.getName());

        mav.addObject("LoggedInUser", LoggedInUser);

        List<Role> roles = (List<Role>) roleRepository.findAll();

        mav.addObject("allRoles", roles);

        return mav;
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult, RedirectAttributes ra) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/admin/new";
        }

        userService.register(user);
        ra.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editUser(@PathVariable(name = "id") Integer id) {
        User user = userService.findOne(id);
        ModelAndView mav = new ModelAndView("admin/edit");
        mav.addObject("user", user);

        List<Role> roles = (List<Role>) roleRepository.findAll();

        mav.addObject("allRoles", roles);

        return mav;
    }

    @PatchMapping()
    public String update(@ModelAttribute("user") User user) {
        int id = user.getId();
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/user")
    public String delete(@ModelAttribute("user") User user) {
        int id = user.getId();
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/findOne")
    @ResponseBody
    public User findOne(Integer id) {
        return userService.findOne(id);
    }
}