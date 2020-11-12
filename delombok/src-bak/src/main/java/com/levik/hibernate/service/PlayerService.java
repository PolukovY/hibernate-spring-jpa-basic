package com.levik.hibernate.spring.service;

import com.levik.hibernate.entity.Player;
import com.levik.hibernate.spring.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public List<Player> findAll() {
        return playerRepository.findAll();
    }


    @Transactional
    public void remove(Long id) {
        Player player = playerRepository.findByPlayerId(id);
        playerRepository.delete(player);
    }
}
