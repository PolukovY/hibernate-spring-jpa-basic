package com.levik.hibernate.player;

import com.levik.hibernate.spring.api.dto.PlayerDto;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerApi {

    private static final String HOST = "http://localhost:%d%s";

    private final TestRestTemplate testRestTemplate;
    private final int port;

    public PlayerApi(int port) {
        this.testRestTemplate = new TestRestTemplate();
        this.port = port;
    }

    public ResponseEntity<PlayerDto> all() {
        //given
        String url = getHost("/players");


        //when
        return testRestTemplate.getForEntity(url, PlayerDto.class);
    }

    public void delete(Long playerId) {
        //given
        String deleterUrl = getHost("/players/" + playerId);

        //when
        testRestTemplate.delete(deleterUrl);
    }

    public PlayerApi assertThatResponseIsOkAndSizeEq(ResponseEntity<PlayerDto> response, long playerSize) {
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatResponseBodyNotNull(response);
        assertThat(response.getBody().getPlayers().size()).isEqualTo(playerSize);

        return this;
    }

    public PlayerApi assertThatPlayerRemoved(ResponseEntity<PlayerDto> response, Long removedPlayerId) {
        assertThatResponseBodyNotNull(response);
        boolean isRemovedPlayerIdFounded = response.getBody().getPlayers()
                .stream()
                .anyMatch(it -> it.getId().equals(removedPlayerId));

        assertThat(isRemovedPlayerIdFounded).isFalse();

        return this;
    }

    public PlayerApi assertThatResponseBodyNotNull(ResponseEntity<PlayerDto> response) {
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPlayers()).isNotNull();

        return this;
    }

    public String getHost(String path) {
        return String.format(HOST, port, path);
    }
}
