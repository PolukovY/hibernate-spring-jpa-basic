package com.levik.hibernate.utils;

import com.levik.hibernate.entity.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateTestsDataUtilsTest {

    @DisplayName("Should Generate Random Player Data")
    @Test
    void shouldGenerateRandomPlayerData() {
        //given
        int playerSize = 10;

        //when
        List<Player> players = GenerateTestsDataUtils.generatePlayers(playerSize);


        //then
        assertThat(players.size()).isEqualTo(10);

        players.forEach(it ->  {
            assertThat(it.getId()).isNull();
            assertThat(it.getEmail()).isNotNull();
            assertThat(it.getFirstName()).isNotNull();
            assertThat(it.getEmail()).isNotNull();
            assertThat(it.getLocalDateTime()).isNotNull();
        });
    }
}