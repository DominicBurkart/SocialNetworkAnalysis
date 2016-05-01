package SocialNetworkAnalysis;

import java.util.LinkedList;

interface Ratelimit_Reached_Listener{
	void reached();
}

public class Ratelimit_Reached {
	LinkedList<Ratelimit_Reached_Listener> babies = new LinkedList<Ratelimit_Reached_Listener>();
	
	public void addListener(Ratelimit_Reached_Listener baby){
		babies.add(baby);
	}
	
	public void reached(){
		for (Ratelimit_Reached_Listener baby : babies){
			baby.reached();
		}
	}
}
