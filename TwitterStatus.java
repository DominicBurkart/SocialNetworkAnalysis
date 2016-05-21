package SocialNetworkAnalysis;

import twitter4j.Status;

public class TwitterStatus extends Post {

	@Override
	public int compareTo(Post o) {
		return super.compareTo(o);
	}

	public TwitterStatus(String id, User author, String message) {
		super(id, author, message);
		site = "twitter";
	}

	public TwitterStatus(Status s, User u) {
		super(Long.toString(s.getId()), u.id, s.getText());
		set(s, u);
	}

	public TwitterStatus(Status s) {
		super(Long.toString(s.getId()), Long.toString(s.getUser().getId()), s.getText());
		User u = sample.users.get(Long.toString(s.getId()));
		set(s, u); // yields null value for parent user when user hasn't been
					// collected (acceptable)
	}

	public TwitterStatus(String stringify) {
		super(stringify);
	}

	public String toString() {
		return super.toString();
	}

	private void set(Status s, User u) {
		this.setId(Long.toString(s.getId()));
		this.setNotes(s.getFavoriteCount());
		this.setMessage(s.getText());
		this.setTime(s.getCreatedAt());
		this.setAuthor(u);
		this.setOriginal(s.isRetweet());
		// deal w/ location
		if (s.getGeoLocation() != null) {
			Location l = new Location();
			l.setLatitude(s.getGeoLocation().getLatitude());
			l.setLongitude(s.getGeoLocation().getLongitude());
			l.setName(s.getPlace().getFullName());
			l.setLocationType(s.getPlace().getPlaceType());
			this.setLocation(l);
		}
		if (verbose)
			System.out.println("Twitter status collected:\t" + this.toString());
	}

}
