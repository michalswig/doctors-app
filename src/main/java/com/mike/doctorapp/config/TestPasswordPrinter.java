package com.mike.doctorapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestPasswordPrinter implements CommandLineRunner {

    private final PasswordEncoder encoder;
    protected final Logger logger = LoggerFactory.getLogger(TestPasswordPrinter.class);

    public TestPasswordPrinter(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        String rawPassword = "admin123";
        logger.info("Raw password:");
        logger.info(rawPassword);
        String encodedPassword = encoder.encode(rawPassword);
        logger.info("Encoded password:");
        logger.info(encodedPassword);
    }

}
