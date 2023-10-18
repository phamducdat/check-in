package com.datpd.checkin;

import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Check if user table is empty, then add a user.
            if(userRepository.count() == 0) {
                UserEntity user = new UserEntity();
                user.setName("Default User");
                user.setTurn(1);
                userRepository.save(user);
                System.out.println("Added default user to database.");
            }
        };
    }
}
