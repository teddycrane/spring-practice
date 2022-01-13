package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.RaceFilterType;
import com.teddycrane.springpractice.enums.RacerFilterType;
import com.teddycrane.springpractice.helper.EnumHelpers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnumHelperTest {

    @Test
    public void testEnumValue_shouldVerifyEnumValue() {
        Assertions.assertTrue(EnumHelpers.testEnumValue(Category.class, Category.CAT1.toString()));
        Assertions.assertTrue(EnumHelpers.testEnumValue(RaceFilterType.class, "name"));
        Assertions.assertTrue(EnumHelpers.testEnumValue(RacerFilterType.class, "category"));
    }

    @Test
    public void testEnumValue_shouldHandleBadName() {
        Assertions.assertFalse(EnumHelpers.testEnumValue(this.getClass(), "test"));
    }

}
