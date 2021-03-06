package com.teddycrane.springpractice.generate.model;

import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import java.util.Collection;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(path = "/generate")
public interface IGenerateController {

  @PostMapping(path = "/racer")
  Collection<Racer>
  generateRacer(@RequestParam(required = false) String category,
                @RequestParam(required = false) Integer number)
      throws BadRequestException;

  @PostMapping(path = "/race")
  Collection<Race> generateRace(@RequestParam(required = false) Integer number);
}
