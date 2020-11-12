package com.levik.hibernate;

import com.levik.hibernate.spring.api.dto.PlayerDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = HibernateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HibernateApplicationTests {
	private static final String HOST = "http://localhost:%d%s";

	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		testRestTemplate = new TestRestTemplate();
	}


	@DisplayName("Should get all players")
	@Test
	@Order(1)
	void shouldGetAllPlayers() {
		//given
		String url = getHost("/players");

		//when
		ResponseEntity<PlayerDto> playerDtoResponseEntity = testRestTemplate.getForEntity(url, PlayerDto.class);

		//then
		generalAssertThat(playerDtoResponseEntity, 10);
	}

	@DisplayName("Should delete player")
	@Test
	@Order(2)
	void shouldDeleteThePlayer() {
		//given
		long playerId = 1L;
		String deleterUrl = getHost("/players/" + playerId);
		String url = getHost("/players");

		//when
		testRestTemplate.delete(deleterUrl);

		//then
		ResponseEntity<PlayerDto> playerDtoResponseEntity = testRestTemplate.getForEntity(url, PlayerDto.class);

		generalAssertThat(playerDtoResponseEntity, 9);

		boolean isRemovedPlayerIdFounded = playerDtoResponseEntity.getBody().getPlayers()
				.stream()
				.anyMatch(it -> it.getId().equals(playerId));

		assertThat(isRemovedPlayerIdFounded).isFalse();

	}

	private void generalAssertThat(ResponseEntity<PlayerDto> playerDtoResponseEntity, int playerSize) {
		assertThat(playerDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(playerDtoResponseEntity.getBody()).isNotNull();
		assertThat(playerDtoResponseEntity.getBody().getPlayers()).isNotNull();
		assertThat(playerDtoResponseEntity.getBody().getPlayers().size()).isEqualTo(playerSize);
	}

	public String getHost(String path) {
		return String.format(HOST, port, path);
	}
}
