package com.levik.hibernate.spring.api;

import com.levik.hibernate.entity.Player;
import com.levik.hibernate.spring.api.dto.PlayerDto;
import com.levik.hibernate.spring.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public PlayerDto getPlayerById() {
        return playerService.findAll();
    }

    @GetMapping("/{playerId}")
    public Player getPlayerById(@PathVariable("playerId") Long playerId) {
        return playerService.findOne(playerId);
    }

    @DeleteMapping("/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("playerId") Long playerId) {
        playerService.remove(playerId);
    }


}
