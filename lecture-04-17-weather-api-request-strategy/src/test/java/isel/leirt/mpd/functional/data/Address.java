package isel.leirt.mpd.functional.data;

public class Address   {
	private final String city;
	private final String zipCode;

	public Address(String city, String zipCode) {
		this.city = city;
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public String getZipCode() {
		return zipCode;
	}
	
	@Override
	public String toString() {
		return "{ " +
			city +
			", " + zipCode +
			"}";
	}
}
