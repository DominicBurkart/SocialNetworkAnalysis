package SocialNetworkAnalysis;

import twitter4j.Status;
import twitter4j.UserMentionEntity;

/**
 * Specific class to refer to a tweet.
 * @author dominicburkart
 */
public class TwitterStatus extends Post {

	@Override
	public int compareTo(Post o) {
		return super.compareTo(o);
	}
	
	private void checkIfRepost(Status s){
		if (s.isRetweet() && s.getRetweetedStatus().getUser() != null && this.getAuthor() != null){
			if (verbose) System.out.println("retweet collected. Constructing Repost object.");
			User retweetee = new TwitterUser(s.getRetweetedStatus().getUser(), this.getAuthor().firstDepth + 1);
			retweetee.fromPost = true;
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
			checkIfAtted(s);
		}
	}
	
	public void checkIfAtted(Status s){
		//if it's just an @!
		if (s.getUserMentionEntities() != null && s.getQuotedStatus() == null){
			for (UserMentionEntity m : s.getUserMentionEntities()){
				User mentioned;
				if (sample.users.get(Long.toString(m.getId())) != null){
					mentioned = sample.users.get(Long.toString(m.getId()));
				}
				else{
					mentioned = new TwitterUser(m.getScreenName(), Long.toString(m.getId()), this.getAuthor().firstDepth +1);
					mentioned.incomplete = true;
					mentioned.fromPost = true;
					Utilities.userCompleter(mentioned);
				}
				new Mention(this, this.getAuthor(), mentioned);
			}
		}
	}

	public TwitterStatus(Status s, User u) {
		super(Long.toString(s.getId()), u.id, s.getText());
		set(s, u);
		if (sample != null) checkIfRepost(s);
		site = "twitter";
	}

	/**
	 * JUST FOR USE WITH TWITTERSTREAMER! NOT FOR THE REST API!
	 * @param s
	 */
	public TwitterStatus(Status s) {
		super(Long.toString(s.getId()), Long.toString(s.getUser().getId()), s.getText());
		site = "twitter";
		if (sample != null){ //sample is null when collecting streaming / not REST data.
			System.err.println("USE ERROR: this function should not be used in a sample-based collection! Quitting.");
			System.exit(0);
		}
	}

	public TwitterStatus(String stringify) {
		super(stringify);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	private void set(Status s, User u) {
		setId(Long.toString(s.getId()));
		setNotes(s.getFavoriteCount());
		setMessage(s.getText());
		setTime(s.getCreatedAt());
		setAuthor(u);
		setOriginal(s.isRetweet());
		// deal w/ location
		if (s.getGeoLocation() != null) {
			Location l = new Location();
			l.setLatitude(s.getGeoLocation().getLatitude());
			l.setLongitude(s.getGeoLocation().getLongitude());
			this.setLocation(l);
		}
		if (s.getPlace() != null){
			if (this.getLocation() != null){
				Location l = this.getLocation();
				l.setName(s.getPlace().getFullName());
				l.setLocationType(s.getPlace().getPlaceType());
			}
			else{
				Location l = new Location();
				l.setName(s.getPlace().getFullName());
				l.setLocationType(s.getPlace().getPlaceType());
				this.setLocation(l);
			}
		}
		if (verbose)
			System.out.println("Twitter status collected:\t" + this.toString());
	}

}
