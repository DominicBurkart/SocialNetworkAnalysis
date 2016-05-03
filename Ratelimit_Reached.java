package SocialNetworkAnalysis;


interface Ratelimit_Reached_Listener {
	void reached();
}

public class Ratelimit_Reached {
	TwitterAuth.LimitReached l = new TwitterAuth.LimitReached();

	public void reached() {
		l.reached();
	}
}
