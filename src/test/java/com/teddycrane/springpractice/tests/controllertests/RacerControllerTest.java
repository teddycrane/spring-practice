package com.teddycrane.springpractice.tests.controllertests;

import com.teddycrane.springpractice.controller.RacerController;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.models.CreateRacerRequest;
import com.teddycrane.springpractice.models.UpdateRacerRequest;
import com.teddycrane.springpractice.service.IRacerService;
import com.teddycrane.springpractice.tests.helpers.ControllerTestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RacerControllerTest
{

	private Racer racer;

	private List<Racer> racerList;

	private RacerController racerController;

	@Mock
	private IRacerService racerService;

	@Before
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		racerController = new RacerController(racerService);
		racer = new Racer("Firstname", "Lastname");
		racerList = new ArrayList<>();
		for (int i = 0; i < 5; i++)
		{
			racerList.add(ControllerTestHelper.generateRacer());
		}
	}

	@Test
	public void shouldGetAllRacersWithoutDeleted()
	{
		when(racerService.getAllRacers()).thenReturn(racerList);

		// test
		List<Racer> result = this.racerController.getAllRacers(false);
		Assert.assertEquals(5, result.size());

		for (int i = 0; i < result.size(); i++)
		{
			Assert.assertTrue(result.get(i).equals(racerList.get(i)));
		}
	}

	@Test
	public void shouldGetAllRacersWithDeleted()
	{
		when(racerService.getAllRacersWithDeleted()).thenReturn(racerList);

		// test
		List<Racer> result = this.racerController.getAllRacers(true);
		Assert.assertEquals(5, result.size());

		for (int i = 0; i < result.size(); i++)
		{
			Assert.assertTrue(result.get(i).equals(racerList.get(i)));
		}
	}

	@Test
	public void shouldGetASingleRacer()
	{
		when(racerService.getRacerById(any(UUID.class))).thenReturn(racer);

		// test
		Racer result = this.racerController.getRacer(UUID.randomUUID().toString());
		Assert.assertTrue(result.equals(racer));
	}

	@Test
	public void getSingleRacerShouldHandleServiceErrors()
	{
		when(racerService.getRacerById(any(UUID.class))).thenThrow(RacerNotFoundException.class);

		// test
		Assert.assertThrows(RacerNotFoundException.class, () -> this.racerController.getRacer(UUID.randomUUID().toString()));
	}

	@Test
	public void shouldAddARacer()
	{
		when(racerService.addRacer(any(String.class), any(String.class))).thenReturn(racer);

		// test
		Racer result = this.racerController.addRacer(new CreateRacerRequest("Firstname", "Lastname"));
		Assert.assertTrue(result.equals(racer));
	}

	@Test
	public void shouldHandleServiceErrorsWhenAddingRacer()
	{
		when(racerService.addRacer(any(String.class), any(String.class))).thenThrow(IllegalArgumentException.class);

		// test
		Assert.assertThrows(BadRequestException.class, () -> this.racerController.addRacer(new CreateRacerRequest("firstname", "lastname")));
	}

	@Test
	public void shouldUpdateRacer()
	{
		when(racerService.updateRacer(any(UUID.class), any(String.class), any(String.class), any(Category.class))).thenReturn(racer);

		// test
		Racer result = this.racerController.updateRacer(new UpdateRacerRequest("firstName", "lastName", Category.CAT5), UUID.randomUUID().toString());
		Assert.assertTrue(result.equals(racer));
	}

	@Test
	public void shouldThrowBadRequestExceptionIfTheRequestIsInvalid()
	{
		when(racerService.updateRacer(any(UUID.class), nullable(String.class), nullable(String.class), nullable(Category.class))).thenThrow(BadRequestException.class);

		// test
		UpdateRacerRequest request = new UpdateRacerRequest(null, null, null);
		Assert.assertThrows(BadRequestException.class, () -> this.racerController.updateRacer(request, UUID.randomUUID().toString()));
	}

	@Test
	public void updateShouldThrowRacerNotFoundException()
	{
		when(racerService.updateRacer(any(UUID.class), any(String.class), any(String.class), nullable(Category.class))).thenThrow(RacerNotFoundException.class);

		// test
		UpdateRacerRequest request = new UpdateRacerRequest("Firstname", "lastname", null);
		Assert.assertThrows(RacerNotFoundException.class, () -> this.racerController.updateRacer(request, UUID.randomUUID().toString()));
	}

	@Test
	public void updateShouldThrowBadRequestExceptionWhenTheUUIDIsBad()
	{
		// test
		UpdateRacerRequest request = new UpdateRacerRequest("Firstname", "LastName", Category.CAT5);
		Assert.assertThrows(BadRequestException.class, () -> this.racerController.updateRacer(request, "z!"));
	}

	@Test
	public void deleteRacerTest()
	{
		when(racerService.deleteRacer(any(UUID.class))).thenReturn(racer);

		// test
		Racer response = this.racerController.deleteRacer(UUID.randomUUID().toString());
		Assert.assertTrue(response.equals(racer));
	}

	@Test
	public void deleteRacerShouldThrowException()
	{
		when(racerService.deleteRacer(any(UUID.class))).thenThrow(NoSuchElementException.class);

		// test
		Assert.assertThrows(RacerNotFoundException.class, () -> this.racerController.deleteRacer(UUID.randomUUID().toString()));
	}

	@Test
	public void restoreRacerTest()
	{
		when(racerService.restoreRacer(any(UUID.class))).thenReturn(racer);

		// test
		Racer response = this.racerController.restoreRacer(UUID.randomUUID().toString());
		Assert.assertTrue(response.equals(racer));
	}

	@Test
	public void restoreRacerShouldThrowException()
	{
		when(racerService.restoreRacer(any(UUID.class))).thenThrow(RacerNotFoundException.class);

		// test
		Assert.assertThrows(RacerNotFoundException.class, () -> this.racerController.restoreRacer(UUID.randomUUID().toString()));
	}
}
