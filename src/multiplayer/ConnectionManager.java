package multiplayer;

import java.io.IOException;
import java.util.ArrayList;

import multiplayer.MapUpdater.UpdateList;

public interface ConnectionManager {
	public void sendMapUpdates(ArrayList<UpdateList> UpdateLists) throws IOException;
}
