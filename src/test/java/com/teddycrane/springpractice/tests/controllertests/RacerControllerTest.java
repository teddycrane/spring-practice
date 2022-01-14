package com.teddycrane.springpractice.tests.controllertests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.RacerFilterType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.RacerNotFoundException;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.racer.RacerController;
import com.teddycrane.springpractice.racer.model.IRacerService;
import com.teddycrane.springpractice.racer.request.CreateRacerRequest;
import com.teddycrane.springpractice.racer.request.UpdateRacerRequest;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

public class RacerControllerTest {

  private Racer racer;

  private List<Racer> racerList;

  private RacerController racerController;

  @Mock private IRacerService racerService;

  @Captor private ArgumentCaptor<RacerFilterType> filterArg;

  @Captor private ArgumentCaptor<String> valueArg;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    racerController = new RacerController(racerService);
    racer = new Racer("Firstname", "Lastname");
    racerList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      racerList.add(TestResourceGenerator.generateRacer());
    }
  }

  @Test
  public void shouldGetAllRacersWithoutDeleted() {
    when(racerService.getAllRacers()).thenReturn(racerList);

    // test
    List<Racer> result = this.racerController.getAllRacers(false);
    Assertions.assertEquals(5, result.size());

    for (int i = 0; i < result.size(); i++) {
      Assertions.assertTrue(result.get(i).equals(racerList.get(i)));
    }
  }

  @Test
  public void shouldGetAllRacersWithDeleted() {
    when(racerService.getAllRacersWithDeleted()).thenReturn(racerList);

    // test
    List<Racer> result = this.racerController.getAllRacers(true);
    Assertions.assertEquals(5, result.size());

    for (int i = 0; i < result.size(); i++) {
      Assertions.assertTrue(result.get(i).equals(racerList.get(i)));
    }
  }

  @Test
  public void shouldGetASingleRacer() {
    when(racerService.getRacerById(any(UUID.class))).thenReturn(racer);

    // test
    Racer result = this.racerController.getRacer(UUID.randomUUID().toString());
    Assertions.assertTrue(result.equals(racer));
  }

  @Test
  public void getSingleRacerShouldHandleServiceErrors() {
    when(racerService.getRacerById(any(UUID.class)))
        .thenThrow(RacerNotFoundException.class);

    // test
    Assertions.assertThrows(
        RacerNotFoundException.class,
        () -> this.racerController.getRacer(UUID.randomUUID().toString()));
  }

  @Test
  public void shouldAddARacer() {
    when(racerService.addRacer(any(String.class), any(String.class)))
        .thenReturn(racer);

    // test
    Racer result = this.racerController.addRacer(
        new CreateRacerRequest("Firstname", "Lastname"));
    Assertions.assertTrue(result.equals(racer));
  }

  @Test
  public void shouldHandleServiceErrorsWhenAddingRacer() {
    when(racerService.addRacer(any(String.class), any(String.class)))
        .thenThrow(IllegalArgumentException.class);

    // test
    Assertions.assertThrows(
        BadRequestException.class,
        ()
            -> this.racerController.addRacer(
                new CreateRacerRequest("firstname", "lastname")));
  }

  @Test
  public void shouldUpdateRacer() {
    when(racerService.updateRacer(any(UUID.class), any(String.class),
                                  any(String.class), any(Category.class)))
        .thenReturn(racer);

    // test
    Racer result = this.racerController.updateRacer(
        new UpdateRacerRequest("firstName", "lastName", Category.CAT5),
        UUID.randomUUID().toString());
    Assertions.assertTrue(result.equals(racer));
  }

  @Test
  public void shouldThrowBadRequestExceptionIfTheRequestIsInvalid() {
    when(racerService.updateRacer(any(UUID.class), nullable(String.class),
                                  nullable(String.class),
                                  nullable(Category.class)))
        .thenThrow(BadRequestException.class);

    // test
    UpdateRacerRequest request = new UpdateRacerRequest(null, null, null);
    Assertions.assertThrows(BadRequestException.class,
                            ()
                                -> this.racerController.updateRacer(
                                    request, UUID.randomUUID().toString()));
  }

  @Test
  public void updateShouldThrowRacerNotFoundException() {
    when(racerService.updateRacer(any(UUID.class), any(String.class),
                                  any(String.class), nullable(Category.class)))
        .thenThrow(RacerNotFoundException.class);

    // test
    UpdateRacerRequest request =
        new UpdateRacerRequest("Firstname", "lastname", null);
    Assertions.assertThrows(RacerNotFoundException.class,
                            ()
                                -> this.racerController.updateRacer(
                                    request, UUID.randomUUID().toString()));
  }

  @Test
  public void updateShouldThrowBadRequestExceptionWhenTheUUIDIsBad() {
    // test
    UpdateRacerRequest request =
        new UpdateRacerRequest("Firstname", "LastName", Category.CAT5);
    Assertions.assertThrows(
        BadRequestException.class,
        () -> this.racerController.updateRacer(request, "z!"));
  }

  @Test
  public void deleteRacerTest() {
    when(racerService.deleteRacer(any(UUID.class))).thenReturn(racer);

    // test
    Racer response =
        this.racerController.deleteRacer(UUID.randomUUID().toString());
    Assertions.assertTrue(response.equals(racer));
  }

  @Test
  public void deleteRacerShouldThrowException() {
    when(racerService.deleteRacer(any(UUID.class)))
        .thenThrow(NoSuchElementException.class);

    // test
    Assertions.assertThrows(
        RacerNotFoundException.class,
        () -> this.racerController.deleteRacer(UUID.randomUUID().toString()));
  }

  @Test
  public void restoreRacerTest() {
    when(racerService.restoreRacer(any(UUID.class))).thenReturn(racer);

    // test
    Racer response =
        this.racerController.restoreRacer(UUID.randomUUID().toString());
    Assertions.assertTrue(response.equals(racer));
  }

  @Test
  public void restoreRacerShouldThrowException() {
    when(racerService.restoreRacer(any(UUID.class)))
        .thenThrow(RacerNotFoundException.class);

    // test
    Assertions.assertThrows(
        RacerNotFoundException.class,
        () -> this.racerController.restoreRacer(UUID.randomUUID().toString()));
  }

  @Test
  public void shouldFilterRacersByCategory() {
    when(racerService.getRacersByType(RacerFilterType.CATEGORY,
                                      Category.CAT1.toString()))
        .thenReturn(racerList);

    // test
    this.racerController.getRacersByType("category", "cat1");
    Mockito.verify(racerService)
        .getRacersByType(filterArg.capture(), valueArg.capture());
    RacerFilterType filter = filterArg.getValue();
    String value = valueArg.getValue();

    Assertions.assertEquals(RacerFilterType.CATEGORY, filter);
    Assertions.assertEquals("CAT1", value);
  }

  @Test
  public void shouldHandleBadFilterTypes() {
    Assertions.assertThrows(
        BadRequestException.class,
        () -> this.racerController.getRacersByType("bad value", "bad value"));

    Assertions.assertThrows(
        BadRequestException.class,
        () -> this.racerController.getRacersByType("category", "bad value"));
  }
}
