package SocialNetworkAnalysis;

/**
 * Just reacts to ratelimit being reached somehow.
 * 
 * @author dominicburkart
 */
interface Ratelimit_Reached_Listener {
	void reached();
}

/**
 * Calls TwiterAuth's .LimitReached() when triggered via .reached()
 * @author dominicburkart
 */
public class Ratelimit_Reached {
	TwitterAuth.LimitReached l = new TwitterAuth.LimitReached();

	public void reached() {
		l.reached();
	}
}
