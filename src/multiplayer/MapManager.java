package multiplayer;

import java.io.IOException;

import dataUtils.conversion.InConverter;

public class MapManager implements multiplayer.client.InputReceiver, multiplayer.server.InputReceiver{

	private MapUpdater mapUpdater;
	private ConnectionManager manager;
	
	public MapManager(MapUpdater mapUpdater, ConnectionManager manager) {
		this.mapUpdater = mapUpdater;
		this.manager = manager;
	}
	
	public void useInput(InConverter in) throws IOException { //is used when the Server sends Map data
		switch(in.readByte()) {
		case Request.MAP_UPDATE_PXL:
			this.mapUpdater.decompPixelUpdates(in);
			break;
		case Request.MAP_UPDATE_AD:
			this.mapUpdater.decompADUpdates(in);
			break;
		}
	}
	
	public void useInput(InConverter in, byte ID) throws IOException { //is used when a Client sends Map data
		switch(in.readByte()) {
		case Request.MAP_UPDATE_PXL:
			this.mapUpdater.decompPixelUpdates(in);
			break;
		case Request.MAP_UPDATE_AD:
			this.mapUpdater.decompADUpdates(in);
			break;
		}
	}
	
	public void sendMapUpdates() throws IOException {
		if(mapUpdater.hasUpdates()) {
			this.manager.sendMapUpdates(mapUpdater.compUpdates());
		}
	}
	
	public byte requestType() {return Request.MAP_DATA;}
}
