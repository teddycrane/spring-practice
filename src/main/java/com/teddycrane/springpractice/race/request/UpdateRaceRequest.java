package com.teddycrane.springpractice.race.request;

import com.teddycrane.springpractice.enums.Category;
import java.util.Optional;
import org.springframework.lang.Nullable;

public class UpdateRaceRequest {

  @Nullable private final String name;

  @Nullable private final Category category;

  public UpdateRaceRequest(@Nullable String name, @Nullable Category category) {
    this.name = name;
    this.category = category;
  }

  public Optional<String> getName() {
    return Optional.ofNullable(name);
  }

  public Optional<Category> getCategory() {
    return Optional.ofNullable(category);
  }

  @Override
  public String toString() {
    return "{\n"
        + String.format("    \"name\" : \"%s\",\n", name)
        + String.format("    \"category\" : \"%s\"\n", category)
        + "}";
  }
}
