package net.jewczuk.openbip.to;

public class RedactorTO {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;

	public static class Builder {
		private String firstName;
		private String lastName;
		private String email;
		private String phone;

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

		public RedactorTO build() {
			return new RedactorTO(this);
		}

	}

	private RedactorTO(Builder builder) {
		firstName = builder.firstName;
		lastName = builder.lastName;
		email = builder.email;
		phone = builder.phone;
	}

	public RedactorTO() {

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

}
