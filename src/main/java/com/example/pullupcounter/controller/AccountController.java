package com.example.pullupcounter.controller;


import com.example.pullupcounter.data.entity.Counter;
import com.example.pullupcounter.data.entity.Game;
import com.example.pullupcounter.data.entity.Multiplier;
import com.example.pullupcounter.data.entity.User;
import com.example.pullupcounter.data.repository.CounterRepository;
import com.example.pullupcounter.data.repository.GameRepository;
import com.example.pullupcounter.data.repository.MultiplierRepository;
import com.example.pullupcounter.data.repository.UserRepository;
import com.example.pullupcounter.model.ApiAccessException;
import com.example.pullupcounter.model.enums.GameName;
import com.example.pullupcounter.model.service.CounterService;
import com.example.pullupcounter.model.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;


@Controller
public class AccountController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    UserService userService;
    @Autowired
    GameRepository gameRepo;
    @Autowired
    CounterRepository counterRepo;
    @Autowired
    MultiplierRepository multiplierRepo;
    @Autowired
    CounterService counterService;


    @GetMapping("/account")
    public String account(Model model
    ) {
        OAuth2User oAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.getUserByUsername(oAuth2User.getAttribute("email"));

        Iterable<Game> gameItems = gameRepo.findAll();
        Map<String, List<Multiplier>> multipliersMap = new HashMap<>();
        gameItems.forEach(x -> {
            List<Multiplier> mList = multiplierRepo.getByUserAndGame(user, x);
            mList.sort(Comparator.comparing(a -> a.getExercise().getName()));
            multipliersMap.put(x.getName(), mList);
        });
        List<Counter> counterItems = counterRepo.getByUser(user);
        counterItems.sort(Comparator.comparing(a -> a.getExercise().getName()));

        model.addAttribute("user", user);
        model.addAttribute("gameItems", gameItems);
        model.addAttribute("multipliersMap", multipliersMap);
        model.addAttribute("counterItems", counterItems);
        return "account";
    }


    @PostMapping("/setMultiplier")
    public String setMultiplier(@RequestParam(name = "game", required = true) String game,
                                @RequestParam(name = "exercise", required = true) String exercise,
                                @RequestParam(name = "multiplier", required = true) Double multiplier,
                                @RequestParam(name = "user", required = true) User user,
                                RedirectAttributes ra
    ) {
        if (userService.setMultiplier(user, exercise, GameName.valueOf(game), multiplier) == null)
            ra.addFlashAttribute("message", "Помилка при оновлені множника");
        return "redirect:/account";
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

    @PostMapping("/reduceCounter")
    public String reduceCounter(@RequestParam(name = "counterId", required = true) Long counterId,
                                @RequestParam(name="number", required = true) Long number,
                                RedirectAttributes ra
    ) {
        Optional<Counter> counter = counterRepo.findById(counterId);
        counter.ifPresent(value -> counterService.addToCounter(value, -1.0 * number));
        return "redirect:/account";
    }

    @GetMapping("/updateAllCounters")
    public String updateAllCounters(RedirectAttributes ra
    ) {
        try {
            List<String> updates = userService.updateAllCounters();
            ra.addFlashAttribute("updates", updates);
        } catch (ApiAccessException e) {
            ra.addFlashAttribute("message", "Потрібно оновити ключ у грі " + e.gameName);
            return "redirect:/account";
        }
        return "redirect:/account";
    }



}
