package com.teddycrane.springpractice.entity;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.helper.EnumHelpers;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.*;

@Entity
public class Race
{

	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final UUID id;

	@Column(unique = true)
	private String name = "";

	private Category category = Category.CAT_5;

	@OneToMany
	private List<Racer> racers;

	@ElementCollection
	private Map<Racer, Date> finishOrder;

	private Date startTime, endTime;

	public Race()
	{
		this.id = UUID.randomUUID();
		this.racers = new ArrayList<>();
		this.startTime = null;
		this.endTime = null;
		this.finishOrder = new HashMap<>();
	}

	public Race(@NotNull Race other)
	{
		this.id = other.id;
		this.name = other.name;
		this.category = other.category;
		this.racers = new ArrayList<>(other.racers);
		this.startTime = other.startTime;
		this.endTime = other.endTime;
		this.finishOrder = new HashMap<>(other.finishOrder);
	}

	public Race(String name)
	{
		this();
		this.name = name;
	}

	public Race(String name, Category category)
	{
		this();
		this.name = name;
		this.category = category;
	}

	public Race(String name, Category category, Date startTime)
	{
		this(name, category);
		this.startTime = new Date(startTime.getTime());
	}

	public Race(String name, Category category, Date startTime, Date endTime)
	{
		this(name, category, startTime);
		this.endTime = new Date(endTime.getTime());
	}

	public List<Racer> getRacers()
	{
		return racers;
	}

	public void setRacers(List<Racer> racers)
	{
		this.racers = new ArrayList<>(racers);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public Map<Racer, Date> getFinishOrder()
	{
		return new HashMap<>(this.finishOrder);
	}

	// we may not want to expose the setter fully todo TC
	public void setFinishOrder(Map<Racer, Date> finishOrder)
	{
		this.finishOrder = new HashMap<>(finishOrder);
	}

	public UUID getId()
	{
		return this.id;
	}

	public Racer addRacer(Racer r)
	{
		this.racers.add(new Racer(r));
		return r;
	}

	public boolean equals(@NotNull Race other)
	{
		boolean primitiveCompare = this.id == other.id && this.name.equals(other.name) && this.category == other.category;

		// skip expensive list comparison if the other race does not have the same ID as this one.
		if (primitiveCompare && this.racers.size() == other.racers.size())
		{
			// todo update this to actually compare the order as well
			return this.racers.containsAll(other.racers) && this.finishOrder.equals(other.finishOrder);
		}

		return primitiveCompare;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(String.format("    \"name\": \"%s\",\n", this.name));
		builder.append(String.format("    \"id\": \"%s\",\n", this.id.toString()));
		builder.append(String.format("    \"category\": \"%s\",\n", EnumHelpers.getCategoryMapping(this.category)));
		builder.append("    \"riders\": [\n");
		if (racers.size() > 0)
		{
			for (int i = 0; i < this.racers.size(); i++)
			{
				builder.append(String.format("        %s", this.racers.get(i).toString()));
				if (i != this.racers.size()) builder.append(",");
				builder.append("\n");
			}
		}
		builder.append("    ]\n");
		builder.append("}");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 31 * hash + this.id.hashCode();
		hash = 31 * hash + this.name.hashCode();
		hash = 31 * hash + this.category.hashCode();
		hash = 31 * hash + this.racers.hashCode();
		hash = 31 * hash + this.finishOrder.hashCode();

		return hash;
	}
}
