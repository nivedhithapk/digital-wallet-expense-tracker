
package com.wallet.wallettracker.controller;

import com.wallet.wallettracker.entity.User;
import com.wallet.wallettracker.entity.RegisterResponse;
import com.wallet.wallettracker.entity.LoginResponse;
import com.wallet.wallettracker.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "http://localhost:5175",
        "http://127.0.0.1:5175"
})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password) {

        User user = userService.registerUser(name, email, password);

        return new RegisterResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                "Registration successful"
        );
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestParam String email,
            @RequestParam String password) {

        User user = userService.loginUser(email, password);

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                "Login successful"
        );
    }
}
