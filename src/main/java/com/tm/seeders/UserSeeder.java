package com.tm.seeders;

import com.tm.model.User;
import com.tm.repository.UserRepository;
import com.tm.security.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserSeeder {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void seed() {
        if (userRepository.count() == 0) { // Prevent duplicate seed
            List<User> users = Arrays.asList(
                    new User("admin", "admin@gmail.com", passwordEncoder.encode("123123"), Role.Admin),
                    new User("staff", "staff@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("LaFamiliar", "1@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("RamenKorewa", "2@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("PizzaHut", "3@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("Spartan", "4@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("McDonalds", "5@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("DominosPizza", "6@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("KFC", "7@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("PizzaDelivery", "8@gmail.com", passwordEncoder.encode("123123"), Role.Staff)
            );
            userRepository.saveAll(users);
            System.out.println("Users seeded!");
        }
    }
}