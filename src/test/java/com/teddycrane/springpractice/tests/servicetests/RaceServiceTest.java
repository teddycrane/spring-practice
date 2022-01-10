package com.teddycrane.springpractice.tests.servicetests;

import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.EndException;
import com.teddycrane.springpractice.error.RaceNotFoundException;
import com.teddycrane.springpractice.error.StartException;
import com.teddycrane.springpractice.error.UpdateException;
import com.teddycrane.springpractice.race.model.RaceRepository;
import com.teddycrane.springpractice.racer.model.RacerRepository;
import com.teddycrane.springpractice.race.model.IRaceService;
import com.teddycrane.springpractice.race.RaceService;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import org.mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RaceServiceTest {

	private final UUID requestUUID = UUID.randomUUID();
	private final UUID badRequestUUID = UUID.randomUUID();

	@Mock
	private RaceRepository raceRepository;

	@Mock
	private RacerRepository racerRepository;
	private IRaceService raceService;
	private List<Race> raceList;
	private Race race;

	@Captor
	private ArgumentCaptor<Race> argument;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.raceService = new RaceService(raceRepository, racerRepository);
		this.raceList = TestResourceGenerator.generateRaceList(10);
		this.race = new Race();

		// DB response mocking
		when(raceRepository.findAll()).thenReturn(raceList);
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(raceList.get(0)));
	}

	@Test
	public void getAllRaces_shouldGetAllRaces() {
		List<Race> result = this.raceService.getAllRaces();
		Assertions.assertEquals(10, result.size());
		Assertions.assertEquals(raceList, result);
	}

	@Test
	public void getRace_shouldGetASingleRace() {
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(raceList.get(0)));

		// test
		Race result = this.raceService.getRace(requestUUID);
		Assertions.assertTrue(result.equals(raceList.get(0)));
	}

	@Test
	public void getRace_shouldThrowRaceNotFoundIfRaceIsNotFound() {
		when(raceRepository.findById(badRequestUUID)).thenReturn(Optional.empty());

		// test
		Assertions.assertThrows(RaceNotFoundException.class, () -> this.raceService.getRace(badRequestUUID));
	}

	@Test
	public void createRace_shouldCreateRace() {
		when(raceRepository.findByName("name")).thenReturn(Optional.empty());

		// test
		this.raceService.createRace("name", Category.CAT5);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();
		Assertions.assertEquals("name", result.getName());
		Assertions.assertEquals(Category.CAT5, result.getCategory());
	}

	@Test
	public void createRace_shouldHandleNameCollisions() {
		Race test = new Race(race);
		test.setName("name");
		test.setCategory(Category.CAT5);
		when(raceRepository.findByName("name")).thenReturn(Optional.of(test));

		// test
		Assertions.assertThrows(DuplicateItemException.class, () -> this.raceService.createRace("name", Category.CAT5));
	}

	@Test
	public void createRace_shouldCreateRaceWithStartTime() {
		Date startTime = new Date();
		when(raceRepository.findByName("name")).thenReturn(Optional.empty());

		// test
		this.raceService.createRace("name", Category.CAT5, startTime);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();
		Assertions.assertEquals("name", result.getName());
		Assertions.assertEquals(Category.CAT5, result.getCategory());
		Assertions.assertEquals(startTime, result.getStartTime());
	}

	@Test
	public void createRace_shouldCreateRaceWithStartAndEndTime() {
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

		Assertions.assertEquals(expected.getName(), result.getName());
		Assertions.assertEquals(expected.getCategory(), result.getCategory());
		Assertions.assertEquals(expected.getStartTime(), result.getStartTime());
		Assertions.assertEquals(expected.getEndTime(), result.getEndTime());
	}

	@Test
	public void createRace_shouldThrowErrorIfDuplicateWithStartTime() {
		Date startTime = new Date();
		Race test = new Race(race);
		test.setName("name");
		test.setCategory(Category.CAT5);
		test.setStartTime(startTime);
		when(raceRepository.findByName("name")).thenReturn(Optional.of(test));

		// test
		Assertions.assertThrows(DuplicateItemException.class,
				() -> this.raceService.createRace("name", Category.CAT5, startTime));
	}

	@Test
	public void createRace_shouldThrowErrorIfDuplicateWithStartAndEndTime() {
		Date startTime = new Date();
		Date endTime = new Date();
		Race test = new Race(race);
		test.setName("name");
		test.setCategory(Category.CAT5);
		test.setStartTime(startTime);
		test.setEndTime(endTime);
		when(raceRepository.findByName("name")).thenReturn(Optional.of(test));

		// test
		Assertions.assertThrows(DuplicateItemException.class,
				() -> this.raceService.createRace("name", Category.CAT5, startTime, endTime));
	}

	@Test
	public void updateRace_shouldUpdateRace() {
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
		Assertions.assertTrue(argument.equals(expected));
	}

	@Test
	public void updateRace_shouldHandleDuplicationErrors() {
		Race existing = new Race("New Name", Category.CAT5);
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(race));
		when(raceRepository.findByName("New Name")).thenReturn(Optional.of(existing));

		// test
		Assertions.assertThrows(DuplicateItemException.class,
				() -> this.raceService.updateRace(requestUUID, "New Name", Category.CAT5));
	}

	@Test
	public void updateRace_shouldHandleRaceNotFound() {
		when(raceRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		// test
		Assertions.assertThrows(RaceNotFoundException.class,
				() -> this.raceService.updateRace(requestUUID, "test", null));
	}

	@Test
	public void addRacer_shouldAddRacerToRace() {
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

		Assertions.assertNotNull(result);
		Assertions.assertEquals(5, result.getRacers().size());
	}

	@Test
	public void addRacer_shouldNotAddRacerIfRaceIsStarted() {
		race.setStartTime(new Date(System.currentTimeMillis() - 1000));
		when(this.raceRepository.findById(any(UUID.class))).thenReturn(Optional.of(race));

		Assertions.assertThrows(UpdateException.class,
				() -> this.raceService.addRacer(UUID.randomUUID(), List.of(UUID.randomUUID())));
	}

	@Test
	public void addRacer_shouldThrowExceptionWhenRaceIsNotFound() {
		List<UUID> racerList = new ArrayList<>();
		when(raceRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		// test
		Assertions.assertThrows(RaceNotFoundException.class, () -> this.raceService.addRacer(requestUUID, racerList));
	}

	@Test
	public void startRace_shouldStartRace() {
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(race));

		// test
		this.raceService.startRace(requestUUID);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();

		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getStartTime());
		Assertions.assertNull(result.getEndTime());
	}

	@Test
	public void startRace_shouldErrorIfStartTimeIsNotNull() {
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(race));
		race.setStartTime(new Date());
		Assertions.assertNotNull(race.getStartTime());

		Assertions.assertThrows(StartException.class, () -> this.raceService.startRace(requestUUID));
	}

	@Test
	public void startRace_shouldErrorIfEndTimeIsNotNull() {
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(race));
		race.setEndTime(new Date());

		Assertions.assertThrows(StartException.class, () -> this.raceService.startRace(requestUUID));
	}

	@Test
	public void startRace_shouldErrorIfBadId() {
		when(this.raceRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
		Assertions.assertThrows(RaceNotFoundException.class, () -> this.raceService.startRace(UUID.randomUUID()));
	}

	@Test
	public void endRace_shouldEndRace() {
		Date startTime = new Date();
		Race expected = new Race(race);
		expected.setStartTime(startTime);
		when(raceRepository.findById(requestUUID)).thenReturn(Optional.of(expected));

		// test
		this.raceService.endRace(requestUUID);
		Mockito.verify(raceRepository).save(argument.capture());
		Race result = argument.getValue();

		Assertions.assertNotNull(result);
		Assertions.assertNotNull(result.getEndTime());
	}

	@Test
	public void placeRacersInFinishOrder_shouldSetRacersInFinishPlaces() {
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

		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.getFinishOrder().size());
		Assertions.assertTrue(result.getFinishOrder().containsKey(racerList.get(0)));
	}
}
