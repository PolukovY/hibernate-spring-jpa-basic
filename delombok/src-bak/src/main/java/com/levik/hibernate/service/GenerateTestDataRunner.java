package com.levik.hibernate.spring.service;

import com.levik.hibernate.spring.repository.PlayerRepository;
import com.levik.hibernate.utils.GenerateTestsDataUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GenerateTestDataRunner implements CommandLineRunner {

    private static final int PLAYERS_SIZE = 10;

    private final PlayerRepository playerRepository;

    @Override
    public void run(String... args) {
        GenerateTestsDataUtils.generatePlayers(PLAYERS_SIZE)
                .forEach(playerRepository::save);
    }
}
