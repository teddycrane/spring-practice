package com.teddycrane.springpractice.tests.controllertests;

import static com.teddycrane.springpractice.tests.helpers.TestResourceGenerator.generateRacer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.RaceFilterType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.RaceNotFoundException;
import com.teddycrane.springpractice.error.RacerNotFoundException;
import com.teddycrane.springpractice.error.StartException;
import com.teddycrane.springpractice.error.UpdateException;
import com.teddycrane.springpractice.event.request.SetResultRequest;
import com.teddycrane.springpractice.models.Either;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.race.RaceController;
import com.teddycrane.springpractice.race.model.IRaceService;
import com.teddycrane.springpractice.race.model.RaceResult;
import com.teddycrane.springpractice.race.request.CreateRaceRequest;
import com.teddycrane.springpractice.race.request.UpdateRaceRequest;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.racer.request.AddRacerRequest;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

public class RaceControllerTest {

  private final String requestString = UUID.randomUUID().toString();
  private final UUID requestUUID = UUID.fromString(requestString);
  private Race race;
  private RaceController raceController;
  private List<Racer> racerList;
  private List<Race> raceList;
  @Mock private IRaceService raceService;

  @Captor private ArgumentCaptor<UUID> idCaptor;

  /**
   * Maps the mock service layer results based on the provided inputs
   *
   * @param race The Race object to be returned from the service layer
   */
  private void setupMockResponses(Race race) {
    when(raceService.startRace(requestUUID)).thenReturn(race);
    when(raceService.endRace(requestUUID)).thenReturn(race);
    when(raceService.placeRacersInFinishOrder(eq(requestUUID), any(ArrayList.class)))
        .thenReturn(race);
  }

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    raceController = new RaceController(raceService);
    // generate racers
    racerList = TestResourceGenerator.generateRacerList(5);
    this.race = new Race();
    race.setName("default name");
    // generate race list
    raceList = TestResourceGenerator.generateRaceList(5);

    race.setRacers(racerList);

    // after all other set-up, create mock responses
    this.setupMockResponses(this.race);
  }

  // get all races tests

  @Test
  public void shouldGetAllRaces() {
    when(raceService.getAllRaces()).thenReturn(raceList);

    // test
    List<Race> result = this.raceController.getAllRaces();
    Assertions.assertEquals(5, result.size());

    for (int i = 0; i < result.size(); i++) {
      Assertions.assertTrue(result.get(i).equals(raceList.get(i)));
    }
  }

  // get single race

  @Test
  public void getRaceShouldReturnARace() {
    when(raceService.getRace(requestUUID)).thenReturn(race);

    // test
    Race result = this.raceController.getRace(requestString);
    verify(raceService).getRace(idCaptor.capture());
    UUID request = idCaptor.getValue();

    Assertions.assertTrue(result.equals(race));
    Assertions.assertEquals(requestUUID, request);
  }

  @Test
  public void shouldHandleServiceExceptions() {
    when(raceService.getRace(requestUUID)).thenThrow(RaceNotFoundException.class);

    // test
    Assertions.assertThrows(
        RaceNotFoundException.class, () -> this.raceController.getRace(requestString));
    Assertions.assertThrows(
        BadRequestException.class, () -> this.raceController.getRace("test bad uuid"));
  }

  // Create race

  @Test
  public void shouldCreateRace() {
    when(raceService.createRace(eq("test name"), any(Category.class))).thenReturn(race);
    ArgumentCaptor<String> name = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Category> category = ArgumentCaptor.forClass(Category.class);

    // test
    Race result = this.raceController.createRace(new CreateRaceRequest("test name", Category.CAT5));
    verify(raceService).createRace(name.capture(), category.capture());

    Assertions.assertEquals("test name", name.getValue());
    Assertions.assertEquals(Category.CAT5, category.getValue());
    Assertions.assertTrue(result.equals(race));
  }

  @Test
  public void shouldHandleCreateErrors() {
    when(raceService.createRace("test name", Category.CAT5))
        .thenThrow(DuplicateItemException.class);

    Assertions.assertThrows(BadRequestException.class, () -> this.raceController.createRace(null));

    Assertions.assertThrows(
        DuplicateItemException.class,
        () -> this.raceController.createRace(new CreateRaceRequest("test name", Category.CAT5)));
  }

  // Update Race

  @Test
  public void shouldUpdateRace() {
    when(raceService.updateRace(requestUUID, "test name", Category.CAT4)).thenReturn(race);
    ArgumentCaptor<String> newName = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Category> newCategory = ArgumentCaptor.forClass(Category.class);

    // test
    Race result =
        this.raceController.updateRace(
            new UpdateRaceRequest("test name", Category.CAT4), requestString);
    verify(raceService).updateRace(idCaptor.capture(), newName.capture(), newCategory.capture());

    // should use the correct parameters
    Assertions.assertEquals(requestUUID, idCaptor.getValue());
    Assertions.assertEquals("test name", newName.getValue());
    Assertions.assertEquals(Category.CAT4, newCategory.getValue());
    Assertions.assertTrue(result.equals(race));
  }

  @Test
  public void shouldHandleUpdateErrors() {
    // both params invalid
    Assertions.assertThrows(
        BadRequestException.class,
        () ->
            this.raceController.updateRace(
                new UpdateRaceRequest(null, null), UUID.randomUUID().toString()));

    // bad UUID
    Assertions.assertThrows(
        BadRequestException.class,
        () ->
            this.raceController.updateRace(
                new UpdateRaceRequest("test", Category.CAT5), "bad value"));

    // generic update exception
    when(raceService.updateRace(requestUUID, "test name", Category.CAT4))
        .thenThrow(UpdateException.class);

    Assertions.assertThrows(
        UpdateException.class,
        () ->
            this.raceController.updateRace(
                new UpdateRaceRequest("test name", Category.CAT4), requestString));

    when(raceService.updateRace(any(UUID.class), any(String.class), any(Category.class)))
        .thenThrow(RaceNotFoundException.class);
    Assertions.assertThrows(
        RaceNotFoundException.class,
        () ->
            this.raceController.updateRace(
                new UpdateRaceRequest("test", Category.CAT5), requestString));
  }

  @Test
  public void shouldAddARacer() {
    ArrayList<UUID> ids = new ArrayList<>();
    ids.add(UUID.randomUUID());
    Race testResult = new Race();
    testResult.addRacer(generateRacer());
    UUID testUUID = UUID.randomUUID();
    when(raceService.addRacer(testUUID, ids)).thenReturn(testResult);

    // test
    Race result = this.raceController.addRacer(new AddRacerRequest(ids), testUUID.toString());
    Assertions.assertTrue(result.equals(testResult));
  }

  @Test
  public void addRace_ShouldHandleRacerNotFoundError() {
    UUID noRacer, raceId;
    noRacer = UUID.randomUUID();
    raceId = UUID.randomUUID();
    List<UUID> racerList = List.of(noRacer);

    AddRacerRequest request = new AddRacerRequest(racerList);

    when(this.raceService.addRacer(raceId, racerList)).thenThrow(RacerNotFoundException.class);

    Assertions.assertThrows(
        RacerNotFoundException.class,
        () -> this.raceController.addRacer(request, raceId.toString()));
  }

  @Test
  public void addRace_shouldHandleRaceNotFoundError() {
    UUID raceId, racerId;
    raceId = UUID.randomUUID();
    racerId = UUID.randomUUID();
    List<UUID> racerList = List.of(racerId);

    when(this.raceService.addRacer(raceId, racerList)).thenThrow(RaceNotFoundException.class);

    Assertions.assertThrows(
        RaceNotFoundException.class,
        () -> this.raceController.addRacer(new AddRacerRequest(racerList), raceId.toString()));
  }

  @Test
  public void addRace_shouldHandleBadUUID() {
    String bad = "test";
    Assertions.assertThrows(
        BadRequestException.class,
        () -> this.raceController.addRacer(new AddRacerRequest(List.of(UUID.randomUUID())), bad));
  }

  @Test
  public void shouldStartRace() {
    Race result = this.raceController.startRace(requestString);
    Assertions.assertTrue(result.equals(race));
  }

  @Test
  public void startRace_shouldHandleBadId() {
    String bad = "test";

    Assertions.assertThrows(BadRequestException.class, () -> this.raceController.startRace(bad));
  }

  @Test
  public void startRace_shouldHandleRaceNotFound() {
    when(this.raceService.startRace(any(UUID.class))).thenThrow(RaceNotFoundException.class);

    Assertions.assertThrows(
        RaceNotFoundException.class,
        () -> this.raceController.startRace(UUID.randomUUID().toString()));
  }

  @Test
  public void startRace_shouldHandleStartExceptions() {
    when(this.raceService.startRace(any(UUID.class))).thenThrow(StartException.class);
    Assertions.assertThrows(
        StartException.class, () -> this.raceController.startRace(UUID.randomUUID().toString()));
  }

  @Test
  public void shouldEndRace() {
    Race result = this.raceController.endRace(requestString);
    Assertions.assertTrue(result.equals(race));
  }

  @Test
  public void endRace_shouldHandleBadId() {
    String bad = "test";

    Assertions.assertThrows(BadRequestException.class, () -> this.raceController.endRace(bad));
  }

  @Test
  public void endRace_shouldHandleRaceNotFound() {
    when(this.raceService.endRace(any(UUID.class))).thenThrow(RaceNotFoundException.class);

    Assertions.assertThrows(
        RaceNotFoundException.class,
        () -> this.raceController.endRace(UUID.randomUUID().toString()));
  }

  @Test
  public void shouldSetRacerResult() {
    String[] ids = new String[1];
    ids[0] = racerList.get(0).getId().toString();
    SetResultRequest request = new SetResultRequest(ids);
    Race result = this.raceController.setRacerResult(requestString, request);
    Assertions.assertTrue(result.equals(race));
  }

  @Test
  public void setRacerResult_shouldHandleBadId() {
    String bad = "test";
    Assertions.assertThrows(
        BadRequestException.class,
        () -> this.raceController.setRacerResult(bad, new SetResultRequest()));
  }

  @Test
  public void getResults_shouldReturnValidData() {
    UUID raceId = race.getId();
    RaceResult expected = new RaceResult(race.getName(), race.getCategory(), race.getRacers());
    when(this.raceService.getResults(raceId)).thenReturn(expected);

    RaceResult result = this.raceController.getResults(raceId.toString());
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void getResults_shouldHandleBadId() {
    String bad = "test";
    Assertions.assertThrows(BadRequestException.class, () -> this.raceController.getResults(bad));
  }

  @Test
  public void getResults_shouldHandleRaceNotFound() {
    when(this.raceService.getResults(any(UUID.class))).thenThrow(RaceNotFoundException.class);

    Assertions.assertThrows(
        RaceNotFoundException.class,
        () -> this.raceController.getResults(UUID.randomUUID().toString()));
  }

  @Test
  public void deleteRacer_shouldDeleteActiveRace() {
    UUID raceId = race.getId();
    when(this.raceService.deleteRace(raceId)).thenReturn(race);

    Race actual = this.raceController.deleteRace(raceId.toString());
    Assertions.assertEquals(race, actual);
  }

  @Test
  public void deleteRace_shouldHandleBadId() {
    String bad = "test";
    Assertions.assertThrows(BadRequestException.class, () -> this.raceController.deleteRace(bad));
  }

  @Test
  public void deleteRace_shouldHandleRaceNotFound() {
    UUID badUUID = UUID.randomUUID();
    when(this.raceService.deleteRace(badUUID)).thenThrow(RaceNotFoundException.class);

    Assertions.assertThrows(
        RaceNotFoundException.class, () -> this.raceController.deleteRace(badUUID.toString()));
  }

  @Test
  public void filterRaces_shouldFilterByCategory() {
    when(this.raceService.filterRace(eq(RaceFilterType.CATEGORY), any(Either.class)))
        .thenReturn(raceList);

    Collection<Race> result = this.raceController.filterRaces("category", "cat1");
    Assertions.assertEquals(raceList, result);
  }

  @Test
  public void filterRaces_shouldFilterByName() {
    when(this.raceService.filterRace(eq(RaceFilterType.NAME), any(Either.class)))
        .thenReturn(raceList);

    Collection<Race> result = this.raceController.filterRaces("name", "test");
    Assertions.assertEquals(raceList, result);
  }

  @Test
  public void filterRaces_shouldHandleInvalidInputs() {
    Assertions.assertThrows(
        BadRequestException.class, () -> this.raceController.filterRaces("bad type", "bad value"));
    Assertions.assertThrows(
        BadRequestException.class, () -> this.raceController.filterRaces("category", "bad value"));
  }
}
