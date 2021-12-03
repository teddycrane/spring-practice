package com.teddycrane.springpractice.tests.controllertests;

import com.teddycrane.springpractice.controller.RaceController;
import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.models.*;
import com.teddycrane.springpractice.service.IRaceService;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import org.hibernate.sql.Update;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.teddycrane.springpractice.tests.helpers.TestResourceGenerator.generateRacer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

	@Before
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
		Assert.assertEquals(5, result.size());

		for (int i = 0; i < result.size(); i++)
		{
			Assert.assertTrue(result.get(i).equals(raceList.get(i)));
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

		Assert.assertTrue(result.equals(race));
		Assert.assertEquals(requestUUID, request);
	}

	@Test
	public void shouldHandleServiceExceptions()
	{
		when(raceService.getRace(requestUUID)).thenThrow(RaceNotFoundException.class);

		// test
		Assert.assertThrows(RaceNotFoundException.class, () -> this.raceController.getRace(requestString));
		Assert.assertThrows(BadRequestException.class, () -> this.raceController.getRace("test bad uuid"));
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

		Assert.assertEquals("test name", name.getValue());
		Assert.assertEquals(Category.CAT5, category.getValue());
		Assert.assertTrue(result.equals(race));
	}

	@Test
	public void shouldHandleCreateErrors()
	{
		when(raceService.createRace("test name", Category.CAT5)).thenThrow(DuplicateItemException.class);

		Assert.assertThrows(BadRequestException.class, () -> this.raceController.createRace(null));

		Assert.assertThrows(DuplicateItemException.class,
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
		Assert.assertEquals(requestUUID, idCaptor.getValue());
		Assert.assertEquals("test name", newName.getValue());
		Assert.assertEquals(Category.CAT4, newCategory.getValue());
		Assert.assertTrue(result.equals(race));
	}

	@Test
	public void shouldHandleUpdateErrors()
	{
		// both  params invalid
		Assert.assertThrows(BadRequestException.class, () -> this.raceController.updateRace(new UpdateRaceRequest(null, null), UUID.randomUUID().toString()));

		// bad UUID
		Assert.assertThrows(BadRequestException.class, () -> this.raceController.updateRace(new UpdateRaceRequest("test", Category.CAT5), "bad value"));

		// generic update exception
		when(raceService.updateRace(requestUUID, "test name", Category.CAT4)).thenThrow(UpdateException.class);

		Assert.assertThrows(UpdateException.class, () -> this.raceController.updateRace(new UpdateRaceRequest("test name", Category.CAT4), requestString));

		when(raceService.updateRace(any(UUID.class), any(String.class), any(Category.class))).thenThrow(RaceNotFoundException.class);
		Assert.assertThrows(RaceNotFoundException.class, () -> this.raceController.updateRace(new UpdateRaceRequest("test", Category.CAT5), requestString));
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
		Assert.assertTrue(result.equals(testResult));
	}

	@Test
	public void shouldStartRace()
	{
		Race result = this.raceController.startRace(requestString);
		Assert.assertTrue(result.equals(race));
	}

	@Test
	public void shouldEndRace()
	{
		Race result = this.raceController.endRace(requestString);
		Assert.assertTrue(result.equals(race));
	}

	@Test
	public void shouldSetRacerResult()
	{
		String[] ids = new String[1];
		ids[0] = racerList.get(0).getId().toString();
		SetResultRequest request = new SetResultRequest(ids);
		Race result = this.raceController.setRacerResult(requestString, request);
		Assert.assertTrue(result.equals(race));
	}
}
