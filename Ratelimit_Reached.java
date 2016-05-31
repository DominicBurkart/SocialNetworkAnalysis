package SocialNetworkAnalysis;

/**
 * If no listener is given to the constructor, calls TwiterAuth's
 * .LimitReached(int family_number) when triggered via .reached(int family_number)
 * 
 * Otherwise, calls the assigned listener's .reached(int i) method.
 * 
 * @author dominicburkart
 */
public class Ratelimit_Reached extends SNA_Root {
	Ratelimit_Reached_Listener l;

	public Ratelimit_Reached() {
		this(new TwitterAuth.LimitReached());
	}

	public Ratelimit_Reached(Ratelimit_Reached_Listener r) {
		l = r;
	}

	public void reached(int i) {
		l.reached(i);
	}
}
