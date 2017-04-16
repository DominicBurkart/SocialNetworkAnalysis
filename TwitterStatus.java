package SocialNetworkAnalysis;

import twitter4j.Status;
import twitter4j.UserMentionEntity;

/**
 * Specific class to refer to a tweet.
 * @author dominicburkart
 */
public class TwitterStatus extends Post {
	boolean possibly_sensitive;
	int retweet_count;
	int favorite_count;
	String source; // from api docs: "Utility used to post the Tweet, as an HTML-formatted string. Tweets from the Twitter website have a source value of web."
	
	
	@Override
	public int compareTo(Post o) {
		return super.compareTo(o);
	}
	
	private void checkIfRepost(Status s, User u){
		if (s.isRetweet() && s.getRetweetedStatus().getUser() != null && this.getAuthor() != null){
			if (verbose) System.out.println("retweet collected. Constructing Repost object.");
			User retweetee = new TwitterUser(s.getRetweetedStatus().getUser(), this.getAuthor().firstDepth + 1);
			retweetee.fromPost = true;
			if (u.fromPost) retweetee.fromFromPost = true;
			Post retweetedStatus;
			if (!sample.posts.containsKey(Long.toString(s.getRetweetedStatus().getId()))){
				retweetedStatus = new TwitterStatus(s.getRetweetedStatus(), retweetee);
				try {
					retweetee.addPost(retweetedStatus);
				} catch (RedundantEntryException e) {
					retweetedStatus = sample.posts.get(Long.toString(s.getRetweetedStatus().getId()));
				}
			} else{
				retweetedStatus = sample.posts.get(Long.toString(s.getRetweetedStatus().getId()));
			}
			new Repost(retweetedStatus, this.getAuthor(), retweetee);
		}
		else{
			checkIfAtted(s, u);
		}
	}
	
	public void checkIfAtted(Status s, User u){
		//if it's just an @!
		if (s.getUserMentionEntities() != null && s.getQuotedStatus() == null){
			if (verbose) System.out.println("Mentions collected. Constructing mention object(s).");
			for (UserMentionEntity m : s.getUserMentionEntities()){
				User mentioned;
				if (sample.users.get(Long.toString(m.getId())) != null){
					mentioned = sample.users.get(Long.toString(m.getId()));
				}
				else{
					mentioned = new TwitterUser(m.getScreenName(), Long.toString(m.getId()), this.getAuthor().firstDepth +1);
					mentioned.incomplete = true;
					mentioned.fromPost = true;
					if (u.fromPost) mentioned.fromFromPost = true;
				}
				new Mention(this, this.getAuthor(), mentioned);
			}
		}
	}

	public TwitterStatus(Status s, User u) {
		super(Long.toString(s.getId()), u.id, s.getText());
		set(s, u);
		if (sample != null) checkIfRepost(s, u);
		site = "twitter";
	}

	/**
	 * JUST FOR USE WITH TWITTERSTREAMER! NOT FOR THE REST API!
	 * @param s
	 */
	public TwitterStatus(Status s) {
		super(Long.toString(s.getId()), Long.toString(s.getUser().getId()), s.getText());
		set(s);
		site = "twitter";
		if (sample != null){ //sample is null when collecting streaming / not REST data.
			System.err.println("USE ERROR: this function should not be used in a sample-based collection! Quitting.");
			System.exit(0);
		}
	}

	//TODO change this when we fill out the remaining tweet fields
	@Override
	public String toString() {
		return super.toString()+"\t"+language+"\t"+possibly_sensitive+"\t"+retweet_count+"\t"+favorite_count+"\t"+source+"\t"+location;
	}
	
	
	private void set(Status s, User u) {
		//basic stuff
		setId(Long.toString(s.getId()));
		setNotes(s.getFavoriteCount() + s.getRetweetCount());
		setMessage(s.getText());
		if (s.isRetweet()){
			//TODO figure out an implementation for stitching together the tweet from the truncation.
			//setMessage(s.getText() + "    " + s.getRetweetedStatus().getText());
		}
		setTime(s.getCreatedAt());
		setAuthor(u);
		setOriginal(s.isRetweet());
		language = s.getLang();
		favorite_count = s.getFavoriteCount();
		retweet_count = s.getRetweetCount();
		
		// deal w/ location
		if (s.getPlace() != null){
			if (this.location != null){
				location.setName(s.getPlace().getFullName());
				location.setLocationType(s.getPlace().getPlaceType());
			}
			else{
				location = new Location();
				location.setName(s.getPlace().getFullName());
				location.setLocationType(s.getPlace().getPlaceType());
			}
		}
		if (s.getPlace().getGeometryCoordinates() != null){
			if (location != null){
				location = this.getLocation();
				location.setLatitude(s.getGeoLocation().getLatitude());
				location.setLongitude(s.getGeoLocation().getLongitude());
			}
			else{
				location = new Location();
				location.setLatitude(s.getGeoLocation().getLatitude());
				location.setLongitude(s.getGeoLocation().getLongitude());
			}
		}
		
		if (verbose)
			System.out.println("Twitter status collected:\t" + this.toString());
	}
	
	//TODO flesh this out w/ all variables given by twitter
	private void set(Status s) {
		//basic stuff
		setId(Long.toString(s.getId()));
		setNotes(s.getFavoriteCount() + s.getRetweetCount());
		setMessage(s.getText());
		if (s.isRetweet()){
			//TODO figure out an implementation for stitching together the tweet from the truncation.
			//setMessage(s.getText() + "    " + s.getRetweetedStatus().getText());
		}
		setTime(s.getCreatedAt());
		setAuthor(new TwitterUser(s.getUser(), 0));
		setOriginal(s.isRetweet());
		language = s.getLang();
		favorite_count = s.getFavoriteCount();
		retweet_count = s.getRetweetCount();

		// deal w/ location
		if (s.getPlace() != null){
			if (this.location != null){
				location.setName(s.getPlace().getFullName());
				location.setLocationType(s.getPlace().getPlaceType());
			}
			else{
				location = new Location();
				location.setName(s.getPlace().getFullName());
				location.setLocationType(s.getPlace().getPlaceType());
			}
		}
		if (s.getGeoLocation() != null){
			if (location != null){
				location = this.getLocation();
				location.setLatitude(s.getGeoLocation().getLatitude());
				location.setLongitude(s.getGeoLocation().getLongitude());
			}
			else{
				location = new Location();
				location.setLatitude(s.getGeoLocation().getLatitude());
				location.setLongitude(s.getGeoLocation().getLongitude());
			}
		}

		if (verbose)
			System.out.println("Twitter status collected:\t" + this.toString());
	}

}
