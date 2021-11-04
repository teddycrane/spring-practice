package com.teddycrane.springpractice.helper;

import com.teddycrane.springpractice.enums.Category;

import java.util.HashMap;
import java.util.Map;

public class EnumHelpers {
	private static final HashMap<Category, String> categoryMappings
			= new HashMap<>(Map.of(Category.CAT_1, "Category 1", Category.CAT_2, "Category 2", Category.CAT_3, "Category 3", Category.CAT_4, "Category 4", Category.CAT_5, "Category 5"));

	public static String getCategoryMapping(Category category) {
		return categoryMappings.get(category);
	}
}
