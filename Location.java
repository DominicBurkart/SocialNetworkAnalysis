package SocialNetworkAnalysis;

/**
 * Simple class to hold a geographic location. 
 * 
 * @author dominicburkart
 */
public class Location {
	private double latitude;
	private double longitude;
	private String name;
	private String locationType;
	
	public Location(){}
	
	/**
	 * ONLY FOR LOADING AN OBJECT STRINGIFIED 
	 * BY THIS CLASS'S toString() METHOD.
	 */
	public Location(String Stringified){
		String s = Stringified;
		if (s != "null"){
			int tab1 = s.indexOf("|||");
			setName(s.substring(0, tab1));
			int tab2 = s.indexOf("|||", tab1);
			setLocationType(s.substring(tab1+1, tab2));
			tab1 = s.indexOf("|||", tab2);
			setLatitude(Double.parseDouble(s.substring(tab2 +1, tab1)));
			tab2 = s.indexOf("|||", tab1);
			setLongitude(Double.parseDouble(s.substring(tab1 +1, tab2)));
		}
	}
	
	public String toString(){
		if (getName() != null && getLocationType() != null){
			return (getName() + "|||" +getLocationType()+ "|||" + getLatitude() + "|||"+getLongitude());
		}
		else{
			return "null";
		}
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
}