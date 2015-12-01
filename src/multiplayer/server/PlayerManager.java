package multiplayer.server;

import java.io.IOException;
import entities.MPlayer;
import main.conversion.ConverterInStream;
import multiplayer.Request;

	public class PlayerManager implements InputReceiver{
		private ClientsManager clientsManager;
		public PlayerManager(ClientsManager clients) {
			this.clientsManager = clients;
		}
		
		public void useInput(ConverterInStream in, byte ID) throws IOException {
			byte request = in.readByte();
//			Game.logWarning("pm"+request+" "+ID);
			switch(request) {
			case Request.PLAYER_COLOR:
				int color = in.readInt();
				clientsManager.getClient(ID).player.color = color;
				clientsManager.sendPlayerColor(ID, color);
				break;
			case Request.PLAYER_REFRESH:
				MPlayer player = clientsManager.getClient(ID).player;
				player.x = in.readInt();
				player.y = in.readInt();
				player.anim = in.readByte();
				player.setDir(in.readByte());
				clientsManager.sendPlayerRefresh(ID);
				break;
			}
		}

		public byte requestType() {return Request.PLAYER_DATA;}
	}