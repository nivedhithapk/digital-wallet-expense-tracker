
package com.wallet.wallettracker.service;

import com.wallet.wallettracker.entity.User;
import com.wallet.wallettracker.entity.Wallet;
import com.wallet.wallettracker.repository.UserRepository;
import com.wallet.wallettracker.repository.WalletRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            WalletRepository walletRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(String name, String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists.");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setOwnerName(name);

        walletRepository.save(wallet);

        return savedUser;
    }

    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException(
                    "Invalid email or password.");
        }

        return user;
    }
}
