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
			int sleeptime = 15 * 60;
			System.out.println("\nRatelimit surpassed. Sleeping for " + sleeptime / 60 + " minutes.");
			User.sample.toTSV();
			while (sleeptime > 0) {
				try {
					if (sleeptime % 60 == 0) { // just so we can minimize
												// unnecessary computations
						System.out.println(sleeptime / 60 + " minutes of sleep remaining.");
						Thread.sleep(1000 * 60);
						sleeptime -= 60;
					} else { // in case we fall out of our regular sleeping
								// pattern or start at a weird time
						Thread.sleep(1000);
						sleeptime--;
					}
				} catch (InterruptedException e) {
					System.err.println("Error while attempting to sleep in Twitter_Ratelimit_Exceeded.");
					System.exit(0);
				}
			}
			System.out.println("Awake! Resuming queries.\n");
			TwitterAuth.backNext();
		}
	}
}
