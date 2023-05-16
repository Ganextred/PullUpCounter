package com.example.pullupcounter.controller;

import com.example.pullupcounter.model.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @Autowired
    GameService gameService;

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/account";
    }

    @PostMapping("/updateKey")
    public String updateKey(@RequestParam(name = "game", required = true) String game,
                            @RequestParam(name = "key", required = true) String key,
                            RedirectAttributes ra) {
        if (!gameService.updateKey(game, key))
            ra.addFlashAttribute("message", "Ключ " + key + " не є валідним ключем для " + game);
        return "redirect:/account";
    }
}
