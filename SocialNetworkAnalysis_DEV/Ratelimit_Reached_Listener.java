package SocialNetworkAnalysis.SocialNetworkAnalysis_DEV;

/**
 * Just reacts to ratelimit being reached somehow.
 * @author dominicburkart
 */
public interface Ratelimit_Reached_Listener {
	void reached(int i);
}