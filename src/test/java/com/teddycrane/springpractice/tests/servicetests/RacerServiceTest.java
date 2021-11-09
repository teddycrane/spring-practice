package com.teddycrane.springpractice.tests.servicetests;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.repository.RacerRepository;
import com.teddycrane.springpractice.service.RacerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RacerServiceTest {

	private List<Racer> mockRacers;
	@InjectMocks
	private RacerService racerService;
	@Mock
	private RacerRepository racerRepository;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		mockRacers = new ArrayList<>();
		mockRacers.add(new Racer("Fname", "Lname"));
		mockRacers.add(new Racer("Test", "Test"));
	}

	@Test
	public void getAllRacersTest() {
		when(racerRepository.findAll()).thenReturn(mockRacers);

		// test
		List<Racer> result = racerService.getAllRacers();

		assertEquals(result.size(), 2);
		assertEquals(result.get(0), mockRacers.get(0));
		assertEquals(result.get(1), mockRacers.get(1));
	}

	@Test
	public void getGetRacerByIdSuccessTest() {
		when(racerRepository.findById(mockRacers.get(0).getId())).thenReturn(Optional.of(mockRacers.get(0)));

		// test
		Racer result = racerService.getRacerById(mockRacers.get(0).getId());
		assertEquals(result, mockRacers.get(0));
	}

	@Test
	public void getRacerByIdFailTest() {
		UUID random = UUID.randomUUID();
		when(racerRepository.findById(random)).thenReturn(Optional.empty());

		// test
		assertThrows(RacerNotFoundException.class, () -> racerService.getRacerById(random));
	}

}
