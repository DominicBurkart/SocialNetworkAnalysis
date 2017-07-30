package SocialNetworkAnalysis;

import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Gives the twitter authorization to TwitterRequestHandler and
 * gives access to Twitter4j's ConfigurationBuilders so we can 
 * make authenticated streaming connections. This version just keeps
 * your Twitter credentials as plaintext (you'll need to enter them
 * below); consider storing them more securely.
 * 
 * @author dominicburkart
 */
public class TwitterAuth extends SNA_Root {
	static ArrayList<TwitterFactory> facs = new ArrayList<TwitterFactory>();
	static int next = 0;
	static TwitterWrapper cur;

	public static void backNext() {
		next--;
	}

	public static int size() {
		return facs.size();
	}
	
	/**
	 * ONLY FOR USE IN STREAMING! using this for REST calls will mess up
	 * our ratelimiting system!
	 */
	public static ArrayList<ConfigurationBuilder> getConfigurationBuilders(){
		return resetFigs();
	}
	
	private static ArrayList<ConfigurationBuilder> resetFigs(){
		ArrayList<ConfigurationBuilder> figs = new ArrayList<ConfigurationBuilder>();
		
		ConfigurationBuilder c0 = new ConfigurationBuilder();
		c0.setDebugEnabled(true).setOAuthConsumerKey("doo ba doo")
		.setOAuthConsumerSecret("la di da")
		.setOAuthAccessToken("dee doo wop")
		.setOAuthAccessTokenSecret("wahdoo");
		figs.add(c0);

		ConfigurationBuilder c1 = new ConfigurationBuilder();
		c1.setDebugEnabled(true).setOAuthConsumerKey("doo ba doo")
		.setOAuthConsumerSecret("la di da")
		.setOAuthAccessToken("dee doo wop")
		.setOAuthAccessTokenSecret("wahdoo");
		figs.add(c1);

		// more ConfigurationBuilders can be added as needed.
		
		return figs;
	}

	public TwitterAuth() {
		for (ConfigurationBuilder fig : resetFigs())
			facs.add(new TwitterFactory(fig.build()));
	}

	private static int getLast() {
		int last;
		last = next - 1;
		if (last == -1) {
			last = facs.size() - 1;
		}
		return last;
	}

	private static Twitter getInstance() {
		if (next == facs.size())
			next = 0;
		Twitter t = facs.get(next++).getInstance();
		t.addRateLimitStatusListener(new Twitter_Ratelimit_Exceeded());
		return t;
	}

	public Twitter getTwitter() {
		TwitterWrapper out = new TwitterWrapper(getInstance(), getLast());
		// ^ getLast() gives the index of the instance in the array of twitter factories we maintain.
		return out;
	}

	public static class LimitReached implements Ratelimit_Reached_Listener {
		 static String[] fams = { "getuserTimeline", "showUser", "getRetweeterIDs", "getFollowersIDs", "getFriendsIDs", "search" };
		 TwitterSample.Listener l = new TwitterSample.Listener();
		
		@Override
		public void reached(int i) {
			if (verbose) System.out.println(fams[i] + " resource is sleeping");
			int f = Utilities.resourceToFamily(i); //families here are booleans in TwitterSample (post, user, following)
			TwitterSample.open[f] = Utilities.least(TwitterWrapper.getLimTimes(i)) + (15 * 60 * 1000);
			TwitterWrapper.timeManager();
			if (verbose) System.out.println("family reached: "+f);
			l.reached(f);
		}

	}
}
