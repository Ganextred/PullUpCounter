package com.example.pullupcounter.controller;


import com.example.pullupcounter.data.entity.Game;
import com.example.pullupcounter.data.entity.User;
import com.example.pullupcounter.data.repository.GameRepository;
import com.example.pullupcounter.data.repository.UserRepository;
import com.example.pullupcounter.model.ApiAccessException;
import com.example.pullupcounter.model.enums.ExerciseName;
import com.example.pullupcounter.model.enums.GameName;
import com.example.pullupcounter.model.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class AccountController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    UserService userService;
    @Autowired
    GameRepository gameRepo;


    @GetMapping("/account")
    public String account(Model model
    ) {
        OAuth2User oAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.getUserByUsername(oAuth2User.getAttribute("email"));
        Iterable<Game> gameItems = gameRepo.findAll();
        model.addAttribute("user", user);
        model.addAttribute("gameItems", gameItems);
        return "account";
    }


    @PostMapping("/setMultiplier")
    public ResponseEntity<String> setMultiplier(@RequestParam(name = "game", required = true) String game,
                                                @RequestParam(name = "exercise", required = true) String exercise,
                                                @RequestParam(name = "multiplier", required = true) Double multiplier,
                                                @RequestParam(name = "user", required = true) User user
    ) {
        if (userService.setMultiplier(user, ExerciseName.valueOf(exercise), GameName.valueOf(game), multiplier) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Multiplier not found");
        else return ResponseEntity.ok("redirect:/account");
    }

    @PostMapping("/bindInGameAccount")
    public String bindInGameAccount(@RequestParam(name = "game", required = true) String game,
                                    @RequestParam(name = "accountName", required = true) String accountName,
                                    @RequestParam(name = "user", required = true) User user,
                                    RedirectAttributes ra
    ) {
        try {
            if (userService.bindInGameAccount(user, accountName, GameName.valueOf(game)) == null) {
                ra.addFlashAttribute("message", "Не знайдено акаунту для " + game);
            }
            return "redirect:/account";
        } catch (ApiAccessException e) {
            ra.addFlashAttribute("message", "Потрібно оновити ключ для " + game);
            return "redirect:/account";
        }
    }

    @GetMapping("/test")
    public String test(Model model) throws JsonProcessingException {
        userService.test();
        return "index";
    }

}
