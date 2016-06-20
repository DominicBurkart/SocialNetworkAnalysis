package SocialNetworkAnalysis;

import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;

/**
 * This class deals with if we actually go over ratelimit.
 * 
 * @author dominicburkart
 */
public class Twitter_Ratelimit_Exceeded extends SNA_Root implements RateLimitStatusListener {

	@Override
	public void onRateLimitReached(RateLimitStatusEvent arg0) {
	}

	@Override
	public void onRateLimitStatus(RateLimitStatusEvent arg0) {
		if (arg0.getRateLimitStatus().getRemaining() <= 0) {
			int sleeptime = 15 * 60 * 1000;
			System.out.println("\nRatelimit surpassed. Sleeping for fifteen minutes.");
			Utilities.sleepFor(sleeptime);
			System.out.println("Awake! Resuming queries.\n");
			TwitterAuth.backNext();
		}
	}
}
