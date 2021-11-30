package com.teddycrane.springpractice.tests.controllertests;

import com.teddycrane.springpractice.controller.RaceController;
import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.models.AddRacerRequest;
import com.teddycrane.springpractice.models.CreateRaceRequest;
import com.teddycrane.springpractice.models.SetResultRequest;
import com.teddycrane.springpractice.models.UpdateRaceRequest;
import com.teddycrane.springpractice.service.IRaceService;
import com.teddycrane.springpractice.tests.helpers.ControllerTestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.teddycrane.springpractice.tests.helpers.ControllerTestHelper.generateRacer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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


	/**
	 * Maps the mock service layer results based on the provided inputs
	 *
	 * @param race The Race object to be returned from the service layer
	 */
	private void setupMockResponses(Race race)
	{
		when(raceService.getRace(requestUUID)).thenReturn(race);
		when(raceService.createRace(eq("test name"), any(Category.class))).thenReturn(race);
		when(raceService.updateRace(requestUUID, "test name", Category.CAT4)).thenReturn(race);
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
		racerList = ControllerTestHelper.generateRacerList(5);
		this.race = new Race();
		race.setName("default name");
		// generate race list
		raceList = ControllerTestHelper.generateRaceList(5);

		race.setRacers(racerList);

		// after all other set-up, create mock responses
		this.setupMockResponses(this.race);
	}

	// happy path tests

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

	@Test
	public void getRaceShouldReturnARace()
	{
		Race result = this.raceController.getRace(requestString);
		Assert.assertTrue(result.equals(race));
	}

	@Test
	public void shouldCreateRace()
	{
		// test
		Race result = this.raceController.createRace(new CreateRaceRequest("test name", Category.CAT5));
		Assert.assertTrue(result.equals(race));
	}

	@Test
	public void shouldUpdateRace()
	{
		Race result = this.raceController.updateRace(new UpdateRaceRequest("test name", Category.CAT4), requestString);
		Assert.assertTrue(result.equals(race));
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
