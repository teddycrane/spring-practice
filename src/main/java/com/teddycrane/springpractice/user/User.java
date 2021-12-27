package com.teddycrane.springpractice.user;

import com.teddycrane.springpractice.enums.UserType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Entity
public class User {

	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final UUID id;
	@NotNull
	@Column(unique = true)
	private String username;
	/**
	 * This is the user's hashed password. DO NOT STORE THE USER'S PASSWORD HERE IN
	 * PLAINTEXT
	 */
	@NotNull
	private String password;
	@NotNull
	private String email;
	private boolean isDeleted = false;
	private String firstName, lastName;
	@Enumerated(EnumType.STRING)
	private UserType type;

	public User() {
		this.id = UUID.randomUUID();
	}

	/**
	 * Deprecated. Constructs a user without a username
	 *
	 * @param type      The type of user.
	 * @param firstName The user's first name
	 * @param lastName  The user's last name
	 */
	@Deprecated
	public User(UserType type, String firstName, String lastName) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
	}

	private User(UserType type, String firstName, String lastName, String username) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
		this.username = username;
	}

	public User(UserType type, String firstName, String lastName, String username, String password) {
		this(type, firstName, lastName, username);
		this.password = password;
	}

	public User(User other) {
		this.id = other.id;
		this.firstName = other.firstName;
		this.lastName = other.lastName;
		this.type = other.type;
		this.isDeleted = other.isDeleted;
		this.username = other.username;
		this.password = other.password;
	}

	public UUID getId() {
		return id;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) throws IllegalArgumentException {
		// pattern matching
		if (email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
			this.email = email;
		} else {
			// should not get here as we should be validating emails earlier
			throw new IllegalArgumentException("Invalid email address provided!");
		}
	}

	public boolean equals(Object other) {
		if (other.getClass() == this.getClass()) {
			User otherUser = new User((User) other);
			return this.id.equals(otherUser.id) &&
					this.firstName.equals(otherUser.firstName) &&
					this.lastName.equals(otherUser.lastName) &&
					this.isDeleted == otherUser.isDeleted &&
					this.type == otherUser.type &&
					this.username.equals(otherUser.username) &&
					this.password.equals(otherUser.password);
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(String.format("    \"userName\" : \"%s\",\n", username));
		builder.append(String.format("    \"id\" : \"%s\",\n", id));
		builder.append(String.format("    \"firstName\" : \"%s\",\n", firstName));
		builder.append(String.format("    \"lastName\" : \"%s\",\n", lastName));
		builder.append(String.format("    \"type\" : \"%s\",\n", type));
		builder.append(String.format("    \"isDeleted\" : \"%s\"\n", isDeleted));
		builder.append("}");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, isDeleted, firstName, lastName, type, username, password);
	}
}
