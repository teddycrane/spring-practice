package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.models.CreateRacerRequest;
import com.teddycrane.springpractice.models.UpdateRacerRequest;
import com.teddycrane.springpractice.service.IRacerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
@RequestMapping(path = "/racer")
class RacerController implements IRacerController
{

	@Autowired
	private IRacerService racerService;

	@Override
	public List<Racer> getAllRacers(boolean isDeleted)
	{
		System.out.println("RacerController.getAllRacers called");
		if (isDeleted)
		{
			System.out.println("Returning all racers, including deleted entries");
			return racerService.getAllRacersWithDeleted();
		} else
		{
			System.out.println("Filtering out deleted entries");
			return racerService.getAllRacers();
		}
	}

	@Override
	public Racer getRacer(String id) throws RacerNotFoundException
	{
		try
		{
			System.out.printf("RacerController.getRacer called with id %s", id);

			UUID uuid = UUID.fromString(id);
			return this.racerService.getRacerById(uuid);

		} catch (RacerNotFoundException e)
		{
			System.out.println("Unable to find racer");
			throw new RacerNotFoundException(String.format("No racer found with id %s", id));
		}
	}

	@Override
	public Racer addRacer(CreateRacerRequest request) throws BadRequestException
	{
		System.out.println("RacerController.addRacer called");

		try
		{
			// verify required parameters
			return this.racerService.addRacer(request.getFirstName(), request.getLastName());
		} catch (Exception e)
		{
			throw new BadRequestException(String.format("Unable to create a racer with name %s %s", request.getFirstName(), request.getLastName()));
		}
	}

	/**
	 * Handles PATCH requests to /racer/update?id=racerId
	 *
	 * @param request The request object containing the fields to update
	 * @return The updated Racer object
	 * @throws UpdateException Thrown if the racer does not exist, or if the racer fails to update
	 */
	@Override
	public Racer updateRacer(UpdateRacerRequest request) throws RacerNotFoundException, BadRequestException
	{
		try
		{
			System.out.println("RacerController.updateRacer called");
			UUID uuid = UUID.fromString(request.getId());

			// validate that at least one of the parameters are not empty or null
			return this.racerService.updateRacer(uuid,
					request.getFirstName().isPresent() ? request.getFirstName().get() : null,
					request.getLastName().isPresent() ? request.getLastName().get() : null,
					request.getCategory().isPresent() ? request.getCategory().get() : null);
		} catch (BadRequestException e)
		{
			System.out.print("error");
			throw new BadRequestException(e.getMessage());
		} catch (RacerNotFoundException e)
		{
			System.out.println("No racer found!");
			throw new RacerNotFoundException(String.format("No racer found with id %s.", request.getId()));
		}
	}

	@Override
	public Racer deleteRacer(String id) throws RacerNotFoundException
	{
		System.out.println("RacerController.deleteRacer called");

		try
		{
			UUID uuid = UUID.fromString(id);
			return this.racerService.deleteRacer(uuid);
		} catch (NoSuchElementException e)
		{
			String message = String.format("No element found with id %s\n", id);
			System.out.println(message);
			throw new RacerNotFoundException(message);
		}
	}

	@Override
	public Racer restoreRacer(String id) throws RacerNotFoundException
	{
		System.out.println("RacerController.restoreRacer called");

		try
		{
			UUID uuid = UUID.fromString(id);
			return this.racerService.restoreRacer(uuid);
		} catch (RacerNotFoundException e)
		{
			throw new RacerNotFoundException(e.getMessage());
		}
	}
}
