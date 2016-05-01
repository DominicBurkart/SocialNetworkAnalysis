package SocialNetworkAnalysis;

import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;

public class Twitter_Ratelimit_Exceeded implements RateLimitStatusListener {

	@Override
	public void onRateLimitReached(RateLimitStatusEvent arg0) {
	}

	@Override
	public void onRateLimitStatus(RateLimitStatusEvent arg0) {
		if (arg0.getRateLimitStatus().getRemaining() <= 0) {
			int sleeptime = arg0.getRateLimitStatus().getResetTimeInSeconds();
			System.out.println("Ratelimit surpassed. Sleeping for " + sleeptime / 60 + " minutes.");
			while (sleeptime > 0) {
				try {
					if (sleeptime % 60 == 0)
						System.out.println(sleeptime / 60 + " minutes remaining.");
					Thread.sleep(1000 * sleeptime--);
				} catch (InterruptedException e) {
					System.err.println("Error while attempting to sleep.");
					System.exit(0);
				}
			}
		}
	}
}
