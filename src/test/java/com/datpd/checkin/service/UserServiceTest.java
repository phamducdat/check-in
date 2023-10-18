package com.datpd.checkin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void testConcurrencyIssues() throws InterruptedException {
        long userId = 1L;

// Simulate concurrent check-ins by running multiple threads
        Runnable checkInTask = () -> {
            try {
                userService.checkInByUserId(1L);
                userService.checkInByUserId(2L);

            } catch (Exception e) {
                // Handle exceptions if needed
                e.printStackTrace();
            }
        };

        // Create and start multiple threads
        int numberOfThreads = 5; // You can adjust this based on your testing needs
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(checkInTask);
            threads[i].start();
        }

        // Wait for all threads to complete
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }
    }
}
