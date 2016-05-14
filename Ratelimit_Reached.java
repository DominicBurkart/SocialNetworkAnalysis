package SocialNetworkAnalysis;

/**
 * Calls TwiterAuth's .LimitReached() when triggered via .reached()
 * @author dominicburkart
 */
public class Ratelimit_Reached extends SNA_Root{
	Ratelimit_Reached_Listener l;
	
	public Ratelimit_Reached(){
		this(new TwitterAuth.LimitReached());
	}
	
	public Ratelimit_Reached(Ratelimit_Reached_Listener r){
		l = r;
	}

	/**
	 * @param i: familly that is being ratelimited (0 = tweets, 1 = users, 2 = following, 3 = search)
	 */
	public void reached(int i) {
		l.reached(i);
	}
}
