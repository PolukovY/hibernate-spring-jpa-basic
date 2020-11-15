package com.levik.hibernate;

import com.levik.hibernate.player.PlayerApi;
import com.levik.hibernate.spring.api.dto.PlayerDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = HibernateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HibernateApplicationTests {
	private PlayerApi playerApi;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		playerApi = new PlayerApi(port);
	}


	@DisplayName("Should get all players")
	@Test
	@Order(1)
	void shouldGetAllPlayers() {
		//given
		long playerSize = 10L;

		//when
		ResponseEntity<PlayerDto> all = playerApi.all();

		//then
		playerApi.assertThatResponseIsOkAndSizeEq(all, playerSize);
	}

	@DisplayName("Should delete player")
	@Test
	@Order(2)
	void shouldDeleteThePlayer() {
		//given
		long playerId = 1L;
		long playerSize = 9L;

		//when
		playerApi.delete(playerId);

		//then
		ResponseEntity<PlayerDto> all = playerApi.all();

		playerApi.assertThatResponseIsOkAndSizeEq(all, playerSize)
				.assertThatPlayerRemoved(all, playerId);
	}
}
