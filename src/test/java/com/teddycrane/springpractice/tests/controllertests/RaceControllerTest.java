package com.teddycrane.springpractice.tests.controllertests;

import com.teddycrane.springpractice.controller.RaceController;
import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.models.AddRacerRequest;
import com.teddycrane.springpractice.models.CreateRaceRequest;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RaceControllerTest
{

	private Race race;
	private RaceController raceController;

	private List<Racer> racerList;
	private List<Race> raceList;

	@Mock
	private IRaceService raceService;

	@Before
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		raceController = new RaceController(raceService);
		race = new Race();

		// generate racers
		racerList = ControllerTestHelper.generateRacerList(5);

		// generate race list
		raceList = ControllerTestHelper.generateRaceList(5);
	}

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
		when(raceService.getRace(any(UUID.class))).thenReturn(race);

		// test
		Race result = this.raceController.getRace(UUID.randomUUID().toString());
		Assert.assertTrue(result.equals(race));
	}

	@Test
	public void shouldCreateRace()
	{
		when(raceService.createRace(any(String.class), any(Category.class))).thenReturn(race);

		// test
		Race result = this.raceController.createRace(new CreateRaceRequest("name", Category.CAT_5));
		Assert.assertTrue(result.equals(race));
	}

	@Test
	public void shouldUpdateRace()
	{
		when(raceService.updateRace(any(UUID.class), any(String.class), any(Category.class))).thenReturn(race);

		// test
		Race result = this.raceController.updateRace(new UpdateRaceRequest("name", Category.CAT_5), UUID.randomUUID().toString());
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
}
