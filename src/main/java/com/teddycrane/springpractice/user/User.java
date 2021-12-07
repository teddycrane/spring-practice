package com.teddycrane.springpractice.user;

import com.teddycrane.springpractice.enums.UserType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Entity
public class User
{

	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final UUID id;
	@NotNull
	@Column(unique = true)
	private String userName;
	@NotNull
	private String password;
	private boolean isDeleted = false;
	private String firstName, lastName;
	@Enumerated(EnumType.STRING)
	private UserType type;

	public User()
	{
		this.id = UUID.randomUUID();
	}

	/**
	 * Deprecated.  Constructs a user without a username
	 *
	 * @param firstName The user's first name
	 * @param lastName  The user's last name
	 * @param type      The type of user.
	 */
	@Deprecated
	public User(String firstName, String lastName, UserType type)
	{
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
	}

	public User(String firstName, String lastName, UserType type, String userName)
	{
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		this.userName = userName;
	}

	public User(String firstName, String lastName, UserType type, String userName, String password)
	{
		this(firstName, lastName, type, userName);
		// this is stored in plaintext, but we should use the getters and setters to obfuscate it
		this.password = password;
	}

	public User(User other)
	{
		this.id = other.id;
		this.firstName = other.firstName;
		this.lastName = other.lastName;
		this.type = other.type;
		this.isDeleted = other.isDeleted;
		this.userName = other.userName;
		this.password = other.password;
	}

	public UUID getId()
	{
		return id;
	}

	public boolean getIsDeleted()
	{
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public UserType getType()
	{
		return type;
	}

	public void setType(UserType type)
	{
		this.type = type;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean equals(Object other)
	{
		if (other.getClass() == this.getClass())
		{
			User otherUser = new User((User) other);
			return this.id.equals(otherUser.id) &&
					this.firstName.equals(otherUser.firstName) &&
					this.lastName.equals(otherUser.lastName) &&
					this.isDeleted == otherUser.isDeleted &&
					this.type == otherUser.type &&
					this.userName.equals(otherUser.userName) &&
					this.password.equals(otherUser.password);
		}
		return false;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(String.format("    \"userName\" : \"%s\",\n", userName));
		builder.append(String.format("    \"id\" : \"%s\",\n", id));
		builder.append(String.format("    \"firstName\" : \"%s\",\n", firstName));
		builder.append(String.format("    \"lastName\" : \"%s\",\n", lastName));
		builder.append(String.format("    \"type\" : \"%s\",\n", type));
		builder.append(String.format("    \"isDeleted\" : \"%s\"\n", isDeleted));
		builder.append("}");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, isDeleted, firstName, lastName, type, userName, password);
	}
}
