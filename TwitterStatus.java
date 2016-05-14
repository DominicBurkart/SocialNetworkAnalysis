package SocialNetworkAnalysis;

import twitter4j.Status;

public class TwitterStatus extends Post {

	@Override
	public int compareTo(Post o) {
		return super.compareTo(o);
	}
	
	public TwitterStatus(){
		site = "twitter";
	}
	
	public TwitterStatus(String id, User author, String message){
		super(id, author, message);
		site = "twitter";
	}
	
	public TwitterStatus(Status s, User u){
		set(s, u);
	}
	
	public TwitterStatus(Status s){
		User u = sample.users.get(Long.toString(s.getId()));
		set(s, u); //yields null value for parent user when user hasn't been collected
	}
	
	public TwitterStatus(String stringify){
		super(stringify);
	}
	
	public String toString(){
		return super.toString();
	}
	
	private void set(Status s, User u){
		this.setId(Long.toString(s.getId()));
		this.setNotes(s.getFavoriteCount());
		this.setMessage(s.getText());
		this.setTime(s.getCreatedAt());
		this.setAuthor(u);
		this.setOriginal(s.isRetweet());
		this.getLocation().setLatitude(s.getGeoLocation().getLatitude());
		this.getLocation().setLongitude(s.getGeoLocation().getLongitude());
		this.getLocation().setName(s.getPlace().getFullName());
		this.getLocation().setLocationType(s.getPlace().getPlaceType());
	}
	
}
