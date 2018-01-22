package gfx;

import java.util.LinkedList;
import java.util.Queue;

public class RessourceManager {
	
	private static Queue<OpenGLRessource> ressources = new LinkedList<>();
	
	public static synchronized void addRessource(OpenGLRessource res) {
		ressources.add(res);
	}
	
	public static synchronized void freeRessources() {
		while (!ressources.isEmpty()) {
			ressources.poll().freeRessources();
		}
	}
	
	public static interface OpenGLRessource{
		public void freeRessources();
	}
}
