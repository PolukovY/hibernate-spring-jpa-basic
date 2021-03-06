package com.levik.hibernate.utils;

import com.levik.hibernate.entity.Player;
import com.levik.hibernate.utils.exception.PersistenceException;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.*;

import static com.levik.hibernate.utils.GenerateTestsDataUtils.createPlayer;
import static org.assertj.core.api.Assertions.assertThat;

class RepositoryUtilsTest {

    private static final String PERSISTENCE_UNIT_NAME = "PlayerEntityManagerUnit";

    @BeforeEach
    void setUp() {
        RepositoryUtils.creteEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @AfterEach
    void onDown() {
        RepositoryUtils.closeEntityManagerFactory();
    }

    @DisplayName("Should persist player")
    @Test
    void shouldPersistPlayer() {
        //given
        Player player = createPlayer();

        //when
        RepositoryUtils.execute(it -> it.persist(player));

        //then
        Statistics statistics = RepositoryUtils.getStatistics();

        assertThat(statistics.getFlushCount()).isEqualTo(1);
        assertThat(statistics.getEntityInsertCount()).isEqualTo(1);
        assertThat(statistics.getQueryExecutionCount()).isEqualTo(0);
    }

    @DisplayName("Should find player")
    @Test
    void shouldFindPlayer() {
        //given
        Player player = persistPlayer();
        Long playerId = player.getId();

        //when
        Player loadedPlayer = RepositoryUtils.executeWithResult(it -> it.find(Player.class, playerId));

        //then
        assertThat(loadedPlayer.getId()).isEqualTo(player.getId());
        assertThat(loadedPlayer.getEmail()).isEqualTo(player.getEmail());
        assertThat(loadedPlayer.getFirstName()).isEqualTo(player.getFirstName());
        assertThat(loadedPlayer.getLastName()).isEqualTo(player.getLastName());

        Statistics statistics = RepositoryUtils.getStatistics();

        assertThat(statistics.getFlushCount()).isEqualTo(2);
        assertThat(statistics.getEntityInsertCount()).isEqualTo(1);
        assertThat(statistics.getEntityLoadCount()).isEqualTo(1);
        assertThat(statistics.getQueryExecutionCount()).isEqualTo(0);
    }

    @DisplayName("Should remove player")
    @Test
    void shouldRemovePlayer() {
        //given
        Player player = persistPlayer();

        //when
        RepositoryUtils.execute(it -> {
            Player managedPlayer = it.merge(player);
            it.remove(managedPlayer);
        });

        Statistics statistics = RepositoryUtils.getStatistics();

        assertThat(statistics.getFlushCount()).isEqualTo(2);
        assertThat(statistics.getEntityInsertCount()).isEqualTo(1);
        assertThat(statistics.getEntityDeleteCount()).isEqualTo(1);
        assertThat(statistics.getQueryExecutionCount()).isEqualTo(0);
    }


    /**
     * DefaultPersistEventListener#onPersist -> switch DELETED
     * PersistenceContext -> StatefulPersistenceContext field EntityEntryContext
     */
    @DisplayName("Should throw exception when remove player")
    @Test
    void shouldThrowExceptionWhenRemovePlayer() {
        //given
        Player player = persistPlayer();

        //then
        PersistenceException exception = Assertions.assertThrows(PersistenceException.class, () -> {
            //when
            RepositoryUtils.execute(it -> it.remove(player));
        });


        assertThat(exception.getCause() instanceof IllegalArgumentException).isTrue();
        assertThat(exception.getCause()
                .getMessage().contains("Removing a detached instance com.levik.hibernate.entity.Player")).isTrue();
    }


    private Player persistPlayer() {
        Player player = createPlayer();
        RepositoryUtils.execute(it -> it.persist(player));
        return player;
    }
}