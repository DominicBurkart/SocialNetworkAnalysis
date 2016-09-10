package SocialNetworkAnalysis;

import java.util.ArrayDeque;
import java.util.Arrays;

import SocialNetworkAnalysis.Sample.ToUser;
import twitter4j.TwitterException;

/**
 * Gives us the abstract framework of a User plus specialized functions for
 * twitter network stuff! Needs to be used with the REST API / an instantiated 
 * TwitterSample object.
 * 
 * @author dominicburkart
 */
public class TwitterUser extends User {
	
	int favouritesCount;
	
	public int followersCount;
	
	public int friendsCount;
	
	boolean isTranslator;
	
	String language; // BCP 47 code, eg "en"
	
	int listedCount; //number of lists that a user is featured on 
	
	String location;
	
	String name;
	
	int statusCount; // number of statuses a  user has made
	
	String timeZone;
	
	String url; //user-defined url associated with their account 
	
	boolean verified;
	
	String withheldInCountries; //countries where this user's tweets are withheld
	
	
	private void checkToLinks(){
		if (TwitterSample.toLinkFriends.containsKey(this.id)){
			ArrayDeque<User> uL = TwitterSample.toLinkFriends.get(this.id);
			if (verbose) System.out.println("While instantiating user "+this.id+" toLinkFriends detected. number of links: "+uL.size());
			for (User u : uL){
				new Follow(u, this);
			}
			TwitterSample.toLinkFriends.remove(this.id); //keeps toLinkFriends small.
		}
		if (TwitterSample.toLinkFollowers.containsKey(this.id)){
			ArrayDeque<User> uL = TwitterSample.toLinkFollowers.get(this.id);
			if (verbose) System.out.println("While instantiating user "+this.id+" toLinkFollowers detected. number of links: "+uL.size());
			for (User u : uL){
				new Follow(this, u);
			}
			TwitterSample.toLinkFollowers.remove(this.id); 
		}
	}

	
	public TwitterUser(String username, String id, int depth){
		super(username, id, depth, false);
		if (sample != null) checkToLinks();
	}
	
	public TwitterUser(String id, int depth){
		super(id, depth, false);
		if (sample != null) checkToLinks();
	}
	
	public TwitterUser(twitter4j.User u, int depth){
		super(Long.toString(u.getId()), depth, false);
		username = u.getScreenName();
		description = Utilities.cleanstring(u.getDescription());
		favouritesCount = u.getFavouritesCount();
		followersCount = u.getFollowersCount();
		friendsCount = u.getFriendsCount();
		isTranslator = u.isTranslator();
		language = u.getLang();
		listedCount = u.getListedCount();
		location = Utilities.cleanstring(u.getLocation());
		name = u.getName();
		statusCount = u.getStatusesCount();
		timeZone = u.getTimeZone();
		url = u.getURL();
		verified = u.isVerified();
		withheldInCountries = Arrays.toString(u.getWithheldInCountries());
		incomplete = false;
		if (sample != null) checkToLinks();
	}

	public ToUser getFriends() throws APIException {
		try {
			return TwitterRequestHandler.getFriends(User.sample.new ToFollow(this));
		} catch (BadIDException e) {
			System.err.println("TwitterUser.getFollowers() recieved a bad ID: " + this.id);
			return null;
		} catch (TwitterException e) {
			throw new APIException(e);
		}
	}

	@Override
	public String toString() {
		if (location != null) {
			return super.toString() + "\t"+ favouritesCount+ "\t"+ followersCount+"\t"+ friendsCount+"\t"+isTranslator+ "\t"+language+ "\t"+listedCount+ "\t" + location.toString()+ "\t"+name+ "\t"+statusCount+ "\t"+timeZone+ "\t"+url+ "\t" +verified+"\t"+withheldInCountries;
		} else {
			return super.toString() + "\t" + "null";
		}
	}

}