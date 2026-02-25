package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BalanceController {
    private final UserRepository userRepository;

    public BalanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ResponseBody
    @GetMapping("/balance")
    public Balance balance(@RequestParam long userId) {
        boolean userExists = userRepository.existsById(userId);
        UserRecord user;

        if (userExists) {
            user = userRepository.findById(userId);
            return new Balance(user.getBalance());
        }
        else {
            return new Balance(0);
        }
    }
}
