package com.teddycrane.springpractice.tests.servicetests;

import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.RacerFilterType;
import com.teddycrane.springpractice.error.RacerNotFoundException;
import com.teddycrane.springpractice.racer.model.RacerRepository;
import com.teddycrane.springpractice.racer.model.IRacerService;
import com.teddycrane.springpractice.racer.RacerService;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RacerServiceTest
{

	@Captor
	ArgumentCaptor<Racer> argument;
	private List<Racer> mockRacers;
	private Racer request;
	private IRacerService racerService;
	@Mock
	private RacerRepository racerRepository;

	@BeforeEach
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		racerService = new RacerService(racerRepository);
		mockRacers = new ArrayList<>();
		mockRacers.add(new Racer("Fname", "Lname"));
		mockRacers.add(new Racer("Test", "Test"));
		request = new Racer("firstName", "lastName");
	}

	@Test
	public void getAllRacersTest()
	{
		when(racerRepository.findAll()).thenReturn(mockRacers);

		// test
		List<Racer> result = racerService.getAllRacers();

		Assertions.assertEquals(result.size(), 2);
		Assertions.assertEquals(result.get(0), mockRacers.get(0));
		Assertions.assertEquals(result.get(1), mockRacers.get(1));
	}

	@Test
	public void getGetRacerByIdSuccessTest()
	{
		when(racerRepository.findById(mockRacers.get(0).getId())).thenReturn(Optional.of(mockRacers.get(0)));

		// test
		Racer result = racerService.getRacerById(mockRacers.get(0).getId());
		Assertions.assertEquals(result, mockRacers.get(0));
	}

	@Test
	public void getRacerByIdFailTest()
	{
		UUID random = UUID.randomUUID();
		when(racerRepository.findById(random)).thenReturn(Optional.empty());

		// test
		Assertions.assertThrows(RacerNotFoundException.class, () -> racerService.getRacerById(random));
	}

	@Test
	public void createRacerTest()
	{
		this.racerService.addRacer("firstName", "lastName");
		Mockito.verify(racerRepository).save(argument.capture());
		Racer result = argument.getValue();

		Assertions.assertNotNull(result);
		Assertions.assertEquals("firstName", result.getFirstName());
		Assertions.assertEquals("lastName", result.getLastName());
	}

	@Test
	public void updateRacerWhenSuccessful()
	{
		when(racerRepository.findById(request.getId())).thenReturn(Optional.of(request));

		// test
		this.racerService.updateRacer(request.getId(), "updatedFname", "updatedLname", Category.CAT1);
		Mockito.verify(racerRepository).save(argument.capture());
		Racer result = argument.getValue();

		Assertions.assertNotNull(result);
		Assertions.assertEquals("updatedFname", result.getFirstName());
		Assertions.assertEquals("updatedLname", result.getLastName());
		Assertions.assertEquals(Category.CAT1, result.getCategory());
	}

	@Test
	public void updateRacerWhenExceptionThrown()
	{
		when(racerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		// test
		Assertions.assertThrows(RacerNotFoundException.class, () -> racerService.updateRacer(UUID.randomUUID(), "test", "test", null));
	}

	@Test
	public void deleteRacerWhenSuccessful()
	{
		UUID random = UUID.randomUUID();
		// mocking
		when(racerRepository.findById(random)).thenReturn(Optional.of(request));

		this.racerService.deleteRacer(random);
		Mockito.verify(racerRepository).save(argument.capture());
		Racer result = argument.getValue();

		Assertions.assertNotNull(result);
		Assertions.assertTrue(result.getIsDeleted());
	}

	@Test
	public void deleteRacerWhenAlreadyDeleted()
	{
		UUID random = UUID.randomUUID();
		Racer response = new Racer(request);
		response.setIsDeleted(true);

		// mocking
		when(racerRepository.findById(random)).thenReturn(Optional.of(response));

		this.racerService.deleteRacer(random);
		Mockito.verify(racerRepository, times(1)).delete(argument.capture());
		Racer result = argument.getValue();

		Assertions.assertNotNull(result);
	}

	@Test
	public void deleteRacerWhenNoRacerIsFound()
	{
		when(racerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		// test
		Assertions.assertThrows(RacerNotFoundException.class, () -> racerService.deleteRacer(UUID.randomUUID()));
	}

	@Test
	public void getAllRacersWithDeletedSuccess()
	{
		request.setIsDeleted(true);
		mockRacers.add(request);
		when(racerRepository.findAll()).thenReturn(mockRacers);

		// test
		List<Racer> result = racerService.getAllRacersWithDeleted();

		Assertions.assertEquals(3, result.size());

		Assertions.assertTrue(result.stream().anyMatch(Racer::getIsDeleted));
	}

	@Test
	public void getRacersWithFilter()
	{
		when(racerRepository.findByCategory(Category.CAT1)).thenReturn(mockRacers);

		// test
		this.racerService.getRacersByType(RacerFilterType.CATEGORY, "CAT1");
		Mockito.verify(racerRepository).findByCategory(Category.CAT1);
	}
}
