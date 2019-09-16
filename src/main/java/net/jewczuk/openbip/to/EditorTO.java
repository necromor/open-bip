package net.jewczuk.openbip.to;

public class EditorTO {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private boolean active;
	private boolean passGeneric;

	public static class Builder {
		private String firstName;
		private String lastName;
		private String email;
		private String phone;
		private boolean active;
		private boolean passGeneric;

		public Builder() {

		}

		public Builder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder phone(String phone) {
			this.phone = phone;
			return this;
		}
		
		public Builder active(boolean active) {
			this.active = active;
			return this;
		}
		
		public Builder passGeneric(boolean passGeneric) {
			this.passGeneric = passGeneric;
			return this;
		}

		public EditorTO build() {
			return new EditorTO(this);
		}
	}

	private EditorTO(Builder builder) {
		firstName = builder.firstName;
		lastName = builder.lastName;
		email = builder.email;
		phone = builder.phone;
		active = builder.active;
		passGeneric = builder.passGeneric;
	}

	public EditorTO() {

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isPassGeneric() {
		return passGeneric;
	}

	public void setPassGeneric(boolean passGeneric) {
		this.passGeneric = passGeneric;
	}

}
