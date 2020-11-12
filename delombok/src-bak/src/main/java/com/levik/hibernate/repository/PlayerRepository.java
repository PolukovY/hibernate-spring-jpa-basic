package com.levik.hibernate.spring.repository;

import com.levik.hibernate.entity.Player;
import com.levik.hibernate.spring.service.exception.PlayerNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

     String PLAYER_NOT_FOUND_MESSAGE = "Player id %s not found";

    default Player findByPlayerId(Long id) {
        return this.findById(id)
                .orElseThrow(
                        () -> new PlayerNotFoundException(String.format(PLAYER_NOT_FOUND_MESSAGE, id))
                );
    }
}
