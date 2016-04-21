package SocialNetworkAnalysis;

import java.util.ArrayList;

public class PoliticalDataCollection {
	public static void main(String[] args) throws BadIDException{
		Sample s = new Sample();
		s.name = "PoliticalDataCollection";
		s.outDir = "";
		
		try {
			User bernie = new TwitterUser("216776631", 0);
			User hillary = new TwitterUser("1339835893", 0);
			User trump = new TwitterUser("25073877", 0);
			User cruz = new TwitterUser("23022687", 0);
			User[] bigs = {bernie, hillary, trump, cruz};
			
			for (User candidate : bigs){
				ArrayList<User> followers = candidate.getFollowers();
				followers.addAll(TwitterRequestHandler.getFollowers(candidate));
			}
			
			s.toCSV("/Users/dominicburkart/Desktop/testOut");
		} catch (RedundantEntryException e) {} //impossible here
	}
	
	ArrayList<TwitterUser> getFollowers;
	
}
