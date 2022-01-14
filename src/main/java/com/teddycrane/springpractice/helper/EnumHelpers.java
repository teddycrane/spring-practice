package com.teddycrane.springpractice.helper;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.RaceFilterType;
import com.teddycrane.springpractice.enums.RacerFilterType;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class EnumHelpers {

  private static final HashMap<Category, String> categoryMappings =
      new HashMap<>(Map.of(Category.CAT1, "Category 1", Category.CAT2,
                           "Category 2", Category.CAT3, "Category 3",
                           Category.CAT4, "Category 4", Category.CAT5,
                           "Category 5"));

  /**
   * Takes in a String value to see if the FilterType enum contains the value.
   *
   * @param value The value to test for inclusion in the set of values for
   *     FilterType.
   * @return True if the value set for FilterType includes the test value, false
   *     if it does not.
   */
  private static boolean racerFilterTypesContains(@NotNull String value) {
    try {
      return EnumSet.allOf(RacerFilterType.class)
          .contains(RacerFilterType.valueOf(value.toUpperCase()));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private static boolean raceFilterTypesContains(@NotNull String value) {
    try {
      return EnumSet.allOf(RaceFilterType.class)
          .contains(RaceFilterType.valueOf(value.toUpperCase()));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Tests if a String value is contained in the set of Category enum values
   *
   * @param value The value to be tested
   * @return True if the value is contained in Category values, otherwise,
   *     false.
   */
  private static boolean categoryTypesContains(@NotNull String value) {
    try {
      return EnumSet.allOf(Category.class)
          .contains(Category.valueOf(value.toUpperCase()));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static boolean testEnumValue(@NotNull Class<?> testClass,
                                      String value) {
    switch (testClass.getSimpleName()) {
    case "Category":
      return categoryTypesContains(value);
    case "RacerFilterType":
      return racerFilterTypesContains(value);
    case "RaceFilterType":
      return raceFilterTypesContains(value);
    default:
      return false;
    }
  }

  public static String getCategoryMapping(Category category) {
    return categoryMappings.get(category);
  }
}
