package com.radeckiy.chat.controllers;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
public class MainChatController {
    private Environment env;

    public MainChatController(Environment env) {
        this.env = env;
    }

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String username = (String) request.getSession().getAttribute("username");

        //Добавить "если пользователь не найден в списке"
        if (username == null || username.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("username", username);

        return "chat";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String showLoginPage() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, @RequestParam(defaultValue = "") String username) {
        String[] allowedUsers = env.getProperty("allowed_users", String[].class);
        boolean allow = false;
        username = username.trim();

        if (username.isEmpty() || allowedUsers == null) {
            return "login";
        }

        for (String allowedUser : allowedUsers) {
            if(allowedUser.equals(username)) {
                allow = true;
                break;
            }
        }

        if (allow)
            request.getSession().setAttribute("username", username);
        else
            return "login";

        return "redirect:/";
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession(true).invalidate();

        return "redirect:/";
    }
}