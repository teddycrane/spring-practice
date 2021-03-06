package com.teddycrane.springpractice.generate;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.generate.model.IGenerateController;
import com.teddycrane.springpractice.generate.model.IGenerateService;
import com.teddycrane.springpractice.helper.EnumHelpers;
import com.teddycrane.springpractice.models.BaseController;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import java.util.Collection;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/generate")
public class GenerateController
    extends BaseController implements IGenerateController {

  private final IGenerateService generateService;

  public GenerateController(IGenerateService generateService) {
    super();
    this.generateService = generateService;
  }

  @Override
  public Collection<Racer> generateRacer(@Nullable String category,
                                         @Nullable Integer number)
      throws BadRequestException {
    logger.trace("generateRacer called");

    boolean validCategory =
        category != null && EnumHelpers.testEnumValue(Category.class, category);
    boolean validNumber = number != null;

    if (validNumber && validCategory) {
      logger.trace("Creating {} racers with category {}", number, category);
      return this.generateService.generateRacers(
          number, Category.valueOf(category.toUpperCase()));
    } else if (validNumber) {
      logger.trace("Creating {} racers", number);
      return this.generateService.generateRacers(number);
    } else if (validCategory) {
      logger.trace("Creating racer with category {}", category);
      return this.generateService.generateRacer(
          Category.valueOf(category.toUpperCase()));
    } else if (category != null &&
               !EnumHelpers.testEnumValue(Category.class, category)) {
      logger.error("{} is not a valid Category!", category);
      throw new BadRequestException("The provided category value is not valid");
    } else {
      logger.trace("returning single new Racer");
      return this.generateService.generateRacer();
    }
  }

  @Override
  public Collection<Race> generateRace(@Nullable Integer number) {
    logger.trace("generateRace called!");

    if (number != null) {
      logger.trace("Creating {} races", number);
      return this.generateService.generateRace(number);
    } else {
      logger.trace("Creating 1 race");
      return this.generateService.generateRace(1);
    }
  }
}
