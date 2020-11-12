package com.levik.hibernate.spring.service;

import com.levik.hibernate.entity.Player;
import com.levik.hibernate.spring.api.dto.PlayerDto;
import com.levik.hibernate.spring.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player findOne(Long id) {
        return playerRepository.findByPlayerId(id);
    }

    public PlayerDto findAll() {
        log.info("Call method findAll begin");
        PlayerDto playerDto = new PlayerDto(playerRepository.findAll());
        log.info("Call method findAll end");
        return playerDto;
    }

    /**
     * SimpleJpaRepository#delete
     * @param id
     */
    @Transactional
    public void remove(Long id) {
        log.info("Call method remove " + id + " begin");

        log.info("findByPlayerId ---- begin");
        Player player = playerRepository.findByPlayerId(id);
        log.info("findByPlayerId ---- end");

        log.info("delete ---- end");
        playerRepository.delete(player);
        log.info("delete ---- end");

        log.info("Call method remove " + id + " begin");
    }
}
