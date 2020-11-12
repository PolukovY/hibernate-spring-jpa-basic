package com.levik.hibernate.spring.api;

import com.levik.hibernate.entity.Player;
import com.levik.hibernate.spring.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    public List<Player> getPlayers() {
        return playerService.findAll();
    }

    @DeleteMapping("/{playerId}")
    public void delete(@PathVariable("playerId") Long playerId) {
        playerService.remove(playerId);
    }


}
