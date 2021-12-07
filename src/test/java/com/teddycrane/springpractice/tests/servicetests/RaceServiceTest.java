package com.teddycrane.springpractice.tests.servicetests;

import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.race.model.RaceRepository;
import com.teddycrane.springpractice.racer.model.RacerRepository;
import com.teddycrane.springpractice.race.model.IRaceService;
import com.teddycrane.springpractice.race.RaceService;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RaceServiceTest
{

	private final UUID requestUUID = UUID.randomUUID();
	private final UUID badRequestUUID = UUID.randomUUID();
	private final String requestString = requestUUID.toString();
	@Mock
	private RaceRepository raceRepository;
	@Mock
	private RacerRepository racerRepository;
	private IRaceService raceService;
	private List<Race> raceList;
	private Race race;

	@Captor
	private ArgumentCaptor<Race> argument;

	@Before
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		this.raceService = new RaceService(raceRepository, racerRepository);
		this.raceList = TestResourceGenerator.generateRaceList(10);
		this.race = new Race();

		// DB response mocking
		when(raceRepository.findAll()).thenReturn(raceList);
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(raceList.get(0)));
	}

	@Test
	public void shouldGetAllRaces()
	{
		List<Race> result = this.raceService.getAllRaces();
		Assert.assertEquals(10, result.size());
		Assert.assertEquals(raceList, result);
	}

	@Test
	public void shouldGetASingleRace()
	{
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(raceList.get(0)));

		// test
		Race result = this.raceService.getRace(requestUUID);
		Assert.assertTrue(result.equals(raceList.get(0)));
	}

	@Test
	public void shouldThrowRaceNotFoundIfRaceIsNotFound()
	{
		when(raceRepository.findById(badRequestUUID)).thenReturn(Optional.empty());

		// test
		Assert.assertThrows(RaceNotFoundException.class, () -> this.raceService.getRace(badRequestUUID));
	}

	@Test
	public void shouldCreateRace()
	{
		when(raceRepository.findByName("name")).thenReturn(Optional.empty());

		// test
		this.raceService.createRace("name", Category.CAT5);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();
		Assert.assertEquals("name", result.getName());
		Assert.assertEquals(Category.CAT5, result.getCategory());
	}

	@Test
	public void shouldHandleNameCollisions()
	{
		Race test = new Race(race);
		test.setName("name");
		test.setCategory(Category.CAT5);
		when(raceRepository.findByName("name")).thenReturn(Optional.of(test));

		// test
		Assert.assertThrows(DuplicateItemException.class, () -> this.raceService.createRace("name", Category.CAT5));
	}

	@Test
	public void shouldCreateRaceWithStartTime()
	{
		Date startTime = new Date();
		when(raceRepository.findByName("name")).thenReturn(Optional.empty());

		// test
		this.raceService.createRace("name", Category.CAT5, startTime);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();
		Assert.assertEquals("name", result.getName());
		Assert.assertEquals(Category.CAT5, result.getCategory());
		Assert.assertEquals(startTime, result.getStartTime());
	}

	@Test
	public void shouldCreateRaceWithStartAndEndTime()
	{
		Date startTime = new Date();
		Date endTime = new Date();
		Race expected = new Race(race);
		expected.setName("name");
		expected.setCategory(Category.CAT5);
		expected.setStartTime(startTime);
		expected.setEndTime(endTime);
		when(raceRepository.findByName("name")).thenReturn(Optional.empty());

		// test
		this.raceService.createRace("name", Category.CAT5, startTime, endTime);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();

		Assert.assertEquals(expected.getName(), result.getName());
		Assert.assertEquals(expected.getCategory(), result.getCategory());
		Assert.assertEquals(expected.getStartTime(), result.getStartTime());
		Assert.assertEquals(expected.getEndTime(), result.getEndTime());
	}

	@Test
	public void shouldThrowErrorIfDuplicateWithStartTime()
	{
		Date startTime = new Date();
		Race test = new Race(race);
		test.setName("name");
		test.setCategory(Category.CAT5);
		test.setStartTime(startTime);
		when(raceRepository.findByName("name")).thenReturn(Optional.of(test));

		// test
		Assert.assertThrows(DuplicateItemException.class, () -> this.raceService.createRace("name", Category.CAT5, startTime));
	}

	@Test
	public void shouldThrowErrorIfDuplicateWithStartAndEndTime()
	{
		Date startTime = new Date();
		Date endTime = new Date();
		Race test = new Race(race);
		test.setName("name");
		test.setCategory(Category.CAT5);
		test.setStartTime(startTime);
		test.setEndTime(endTime);
		when(raceRepository.findByName("name")).thenReturn(Optional.of(test));

		// test
		Assert.assertThrows(DuplicateItemException.class, () -> this.raceService.createRace("name", Category.CAT5, startTime, endTime));
	}

	@Test
	public void shouldUpdateRace()
	{
		Race test = new Race(race);
		Race expected = new Race(test);
		expected.setName("New Name");
		expected.setCategory(Category.CAT4);

		when(raceRepository.findById(any(UUID.class))).thenReturn(Optional.of(test));
		when(raceRepository.findByName("New Name")).thenReturn(Optional.empty());

		// test
		this.raceService.updateRace(test.getId(), "New Name", Category.CAT4);
		Mockito.verify(raceRepository).save(argument.capture());
		Race argument = this.argument.getValue();
		Assert.assertTrue(argument.equals(expected));
	}

	@Test
	public void shouldHandleDuplicationErrors()
	{
		Race existing = new Race("New Name", Category.CAT5);
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(race));
		when(raceRepository.findByName("New Name")).thenReturn(Optional.of(existing));

		// test
		Assert.assertThrows(DuplicateItemException.class, () -> this.raceService.updateRace(requestUUID, "New Name", Category.CAT5));
	}

	@Test
	public void shouldHandleRaceNotFound()
	{
		when(raceRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		// test
		Assert.assertThrows(RaceNotFoundException.class, () -> this.raceService.updateRace(requestUUID, "test", null));
	}

	@Test
	public void shouldAddRacerToRace()
	{
		// setup
		List<UUID> racerIds = new ArrayList<>();
		for (int i = 0; i < 5; i++)
			racerIds.add(UUID.randomUUID());

		when(racerRepository.findAllById(any(Iterable.class))).thenReturn(TestResourceGenerator.generateRacerList(5));
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(race));

		// test
		this.raceService.addRacer(requestUUID, racerIds);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();

		Assert.assertNotNull(result);
		Assert.assertEquals(5, result.getRacers().size());
	}

	@Test
	public void shouldStartRace()
	{
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(race));

		// test
		this.raceService.startRace(requestUUID);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();

		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getStartTime());
		Assert.assertNull(result.getEndTime());
	}

	@Test
	public void shouldEndRace()
	{
		Date startTime = new Date();
		Race expected = new Race(race);
		expected.setStartTime(startTime);
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(expected));

		// test
		this.raceService.endRace(requestUUID);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();

		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getEndTime());
	}

	@Test
	public void shouldSetRacersInFinishPlaces()
	{
		List<Racer> racerList = TestResourceGenerator.generateRacerList(5);
		Date startTime = new Date();
		Race expected = new Race(race);
		expected.setStartTime(startTime);
		expected.setRacers(racerList);
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(expected));

		List<UUID> finisherList = new ArrayList<>();
		finisherList.add(racerList.get(0).getId());
		finisherList.add(racerList.get(2).getId());

		List<Racer> mappedList = new ArrayList<>();
		mappedList.add(racerList.get(0));
		mappedList.add(racerList.get(2));
		when(racerRepository.findAllById(finisherList)).thenReturn(mappedList);

		// test
		this.raceService.placeRacersInFinishOrder(requestUUID, finisherList);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();

		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.getFinishOrder().size());
		Assert.assertTrue(result.getFinishOrder().containsKey(racerList.get(0)));
	}


//	@Test
//	public void shouldThrowExceptionWhenAddingRacersToRaceThatHasAlreadyStarted()
//	{
//		List<UUID> racerList = TestResourceGenerator.generateRacerList(3).stream().map(Racer::getId).collect(Collectors.toList());
//		Date startTime = new Date();
//		Race existing = new Race(race);
//		existing.setStartTime(startTime);
//		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(existing));
//
//		// test
//		Assert.assertThrows(UpdateException.class, () -> this.raceService.addRacer(requestUUID, racerList));
//	}

	@Test
	public void shouldThrowExceptionWhenRaceIsNotFound()
	{
		List<UUID> racerList = new ArrayList<>();
		when(raceRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		// test
		Assert.assertThrows(RaceNotFoundException.class, () -> this.raceService.addRacer(requestUUID, racerList));
	}
}
