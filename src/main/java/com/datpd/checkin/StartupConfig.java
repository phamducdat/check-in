package com.datpd.checkin;

import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.repository.UserRepository;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfig {
    private final RedissonClient redissonClient;

    public StartupConfig(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Reset Redis
            redissonClient.getKeys().flushdb();

            // Check if user table is empty, then add a user.
            if(userRepository.count() == 0) {
                UserEntity user = new UserEntity();
                user.setName("John");
                user.setTurn(0);
                userRepository.save(user);
            }
        };
    }
}
