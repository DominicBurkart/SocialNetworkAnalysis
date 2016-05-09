package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

/**
 * Calls TwiterAuth's .LimitReached() when triggered via .reached()
 * @author dominicburkart
 */
public class Ratelimit_Reached {
	Ratelimit_Reached_Listener l;
	
	public Ratelimit_Reached(){
		this(new TwitterAuth.LimitReached());
	}
	
	public Ratelimit_Reached(Ratelimit_Reached_Listener r){
		l = r;
	}

	public void reached(int i) {
		l.reached(i);
	}
}
