package com.tm.seeders;

import com.tm.model.User;
import com.tm.repository.UserRepository;
import com.tm.security.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) { // Prevent duplicate seed
            List<User> users = Arrays.asList(
                    new User("admin", "admin@gmail.com", passwordEncoder.encode("123123"), Role.Admin),
                    new User("staff", "staff@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("1", "1@gmail.com", passwordEncoder.encode("123123"), Role.Admin),
                    new User("LaFamiliar", "2@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("RamenKorewa", "3@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("PizzaHut", "4@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("Spartan", "5@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("McDonalds", "6@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("DominosPizza", "7@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("KFC", "8@gmail.com", passwordEncoder.encode("123123"), Role.Staff),
                    new User("PizzaDelivery", "9@gmail.com", passwordEncoder.encode("123123"), Role.Staff)
            );
            userRepository.saveAll(users);
            System.out.println("Users seeded!");
        }
    }
}
