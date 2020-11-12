package com.levik.hibernate.utils;

import com.levik.hibernate.entity.Player;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateTestsDataUtils {

    public static List<Player> generatePlayers(int size) {
        return Stream.generate(GenerateTestsDataUtils::createPlayer)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static Player createPlayer() {
        //dummy
        Person person = Fairy.create().person();

        return toPlayer(person);
    }

    private static Player toPlayer(Person source) {
        Player target = new Player();
        target.setEmail(source.getEmail());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setLocalDateTime(LocalDateTime.now().minusDays(generateRandomNumber(1, 28)));

        return target;
    }

    public static int generateRandomNumber(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
}
