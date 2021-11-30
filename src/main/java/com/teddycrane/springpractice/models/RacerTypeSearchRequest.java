package com.teddycrane.springpractice.models;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RacerTypeSearchRequest
{
	@NotEmpty
	private final List<UUID> racerIds;

	public RacerTypeSearchRequest()
	{
		racerIds = new ArrayList<>();
	}

	public List<UUID> getRacerIds()
	{
		return new ArrayList<>(racerIds);
	}
}
