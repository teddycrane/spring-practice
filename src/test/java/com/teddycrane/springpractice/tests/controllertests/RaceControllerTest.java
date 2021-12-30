package com.teddycrane.springpractice.tests.controllertests;

import com.teddycrane.springpractice.event.request.SetResultRequest;
import com.teddycrane.springpractice.race.request.CreateRaceRequest;
import com.teddycrane.springpractice.race.RaceController;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.race.request.UpdateRaceRequest;
import com.teddycrane.springpractice.racer.request.AddRacerRequest;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.RaceNotFoundException;
import com.teddycrane.springpractice.error.UpdateException;
import com.teddycrane.springpractice.race.model.IRaceService;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.teddycrane.springpractice.tests.helpers.TestResourceGenerator.generateRacer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RaceControllerTest
{

	private final String requestString = UUID.randomUUID().toString();
	private final UUID requestUUID = UUID.fromString(requestString);
	private Race race;
	private RaceController raceController;
	private List<Racer> racerList;
	private List<Race> raceList;
	@Mock
	private IRaceService raceService;

	@Captor
	private ArgumentCaptor<UUID> idCaptor;


	/**
	 * Maps the mock service layer results based on the provided inputs
	 *
	 * @param race The Race object to be returned from the service layer
	 */
	private void setupMockResponses(Race race)
	{
		when(raceService.startRace(requestUUID)).thenReturn(race);
		when(raceService.endRace(requestUUID)).thenReturn(race);
		when(raceService.placeRacersInFinishOrder(eq(requestUUID), any(ArrayList.class))).thenReturn(race);
	}

	@BeforeEach
	public void init()
	{
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
	public void shouldGetAllRaces()
	{
		when(raceService.getAllRaces()).thenReturn(raceList);

		// test
		List<Race> result = this.raceController.getAllRaces();
		Assertions.assertEquals(5, result.size());

		for (int i = 0; i < result.size(); i++)
		{
			Assertions.assertTrue(result.get(i).equals(raceList.get(i)));
		}
	}

	// get single race

	@Test
	public void getRaceShouldReturnARace()
	{
		when(raceService.getRace(requestUUID)).thenReturn(race);

		// test
		Race result = this.raceController.getRace(requestString);
		verify(raceService).getRace(idCaptor.capture());
		UUID request = idCaptor.getValue();

		Assertions.assertTrue(result.equals(race));
		Assertions.assertEquals(requestUUID, request);
	}

	@Test
	public void shouldHandleServiceExceptions()
	{
		when(raceService.getRace(requestUUID)).thenThrow(RaceNotFoundException.class);

		// test
		Assertions.assertThrows(RaceNotFoundException.class, () -> this.raceController.getRace(requestString));
		Assertions.assertThrows(BadRequestException.class, () -> this.raceController.getRace("test bad uuid"));
	}

	// Create race

	@Test
	public void shouldCreateRace()
	{
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
	public void shouldHandleCreateErrors()
	{
		when(raceService.createRace("test name", Category.CAT5)).thenThrow(DuplicateItemException.class);

		Assertions.assertThrows(BadRequestException.class, () -> this.raceController.createRace(null));

		Assertions.assertThrows(DuplicateItemException.class,
				() -> this.raceController.createRace(new CreateRaceRequest("test name", Category.CAT5)));
	}

	// Update Race

	@Test
	public void shouldUpdateRace()
	{
		when(raceService.updateRace(requestUUID, "test name", Category.CAT4)).thenReturn(race);
		ArgumentCaptor<String> newName = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Category> newCategory = ArgumentCaptor.forClass(Category.class);

		// test
		Race result = this.raceController.updateRace(new UpdateRaceRequest("test name", Category.CAT4), requestString);
		verify(raceService).updateRace(idCaptor.capture(), newName.capture(), newCategory.capture());

		// should use the correct parameters
		Assertions.assertEquals(requestUUID, idCaptor.getValue());
		Assertions.assertEquals("test name", newName.getValue());
		Assertions.assertEquals(Category.CAT4, newCategory.getValue());
		Assertions.assertTrue(result.equals(race));
	}

	@Test
	public void shouldHandleUpdateErrors()
	{
		// both  params invalid
		Assertions.assertThrows(BadRequestException.class, () -> this.raceController.updateRace(new UpdateRaceRequest(null, null), UUID.randomUUID().toString()));

		// bad UUID
		Assertions.assertThrows(BadRequestException.class, () -> this.raceController.updateRace(new UpdateRaceRequest("test", Category.CAT5), "bad value"));

		// generic update exception
		when(raceService.updateRace(requestUUID, "test name", Category.CAT4)).thenThrow(UpdateException.class);

		Assertions.assertThrows(UpdateException.class, () -> this.raceController.updateRace(new UpdateRaceRequest("test name", Category.CAT4), requestString));

		when(raceService.updateRace(any(UUID.class), any(String.class), any(Category.class))).thenThrow(RaceNotFoundException.class);
		Assertions.assertThrows(RaceNotFoundException.class, () -> this.raceController.updateRace(new UpdateRaceRequest("test", Category.CAT5), requestString));
	}

	@Test
	public void shouldAddARacer()
	{
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
	public void shouldStartRace()
	{
		Race result = this.raceController.startRace(requestString);
		Assertions.assertTrue(result.equals(race));
	}

	@Test
	public void shouldEndRace()
	{
		Race result = this.raceController.endRace(requestString);
		Assertions.assertTrue(result.equals(race));
	}

	@Test
	public void shouldSetRacerResult()
	{
		String[] ids = new String[1];
		ids[0] = racerList.get(0).getId().toString();
		SetResultRequest request = new SetResultRequest(ids);
		Race result = this.raceController.setRacerResult(requestString, request);
		Assertions.assertTrue(result.equals(race));
	}
}
