package SocialNetworkAnalysis;

/**
 * Interface for a class to handle ratelimits being reached.
 * 
 * @author dominicburkart
 */
public interface Ratelimit_Reached_Listener {
	void reached(int i);
}