package com.teddycrane.springpractice.tests.helpers;

import com.github.javafaker.Faker;
import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.event.Event;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.user.User;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

public class TestResourceGenerator {
  private static Faker faker = new Faker();

  public static @NotNull Racer generateRacer() {
    String firstName = RandomStringUtils.randomAlphabetic(10);
    String lastName = RandomStringUtils.randomAlphabetic(10);
    return new Racer(firstName, lastName);
  }

  public static @NotNull List<Racer> generateRacerList(int length) {
    List<Racer> result = new ArrayList<>(length);
    for (int i = 0; i < length; i++)
      result.add(i, generateRacer());
    return result;
  }

  public static @NotNull Race generateRace() {
    String name = RandomStringUtils.randomAlphabetic(10);
    ArrayList<Racer> temp = new ArrayList<>();
    for (int i = 0; i < 5; i++)
      temp.add(generateRacer());

    Race result = new Race(name);
    result.setRacers(temp);
    return result;
  }

  public static @NotNull List<Race> generateRaceList(int length) {
    List<Race> result = new ArrayList<>(length);
    for (int i = 0; i < length; i++) {
      Race temp = generateRace();
      temp.setRacers(generateRacerList(5));
      result.add(i, temp);
    }
    return result;
  }

  public static @NotNull Event generateEvent() {
    Event e = new Event();
    e.setName(RandomStringUtils.randomAlphabetic(10));
    return e;
  }

  public static @NotNull List<Event> generateEventList(int size) {
    List<Event> result = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      result.add(generateEvent());
    }

    return result;
  }

  public static User generateUser() {
    return new User(UserType.USER, faker.name().firstName(),
                    faker.name().lastName(), faker.name().username(),
                    faker.idNumber().toString(),
                    faker.bothify("????##@fake.fake"));
  }

  public static Iterable<User> generateUserList(int length) {
    ArrayList<User> result = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      result.add(generateUser());
    }

    return result;
  }
}
