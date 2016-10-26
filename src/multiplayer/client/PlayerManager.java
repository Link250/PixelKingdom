package multiplayer.client;

import java.io.IOException;
import java.util.ArrayList;

import dataUtils.conversion.InConverter;
import entities.MPlayer;
import main.Game;
import multiplayer.Request;

	public class PlayerManager implements InputReceiver{
		private ArrayList<MPlayer> plrs;
		public PlayerManager(ArrayList<MPlayer> players) {
			plrs = players;
		}
		
		public void useInput(InConverter in) throws IOException {
			byte request = in.readByte(),
				 ID = in.readByte();
//			Game.logWarning("pm"+request+" "+ID);
			switches: switch(request) {
			case Request.PLAYER_NEW:
				plrs.add(new MPlayer(ID));
				Game.logInfo("new player Nr. "+ID);
				break switches;
			case Request.PLAYER_DELETE:
				for (MPlayer mPlayer : plrs) {
					if(mPlayer.number==ID) {
						Game.logInfo("player "+ID+" left");
						plrs.remove(mPlayer);
						break switches;
					}
				}
				break;
			case Request.PLAYER_COLOR:
				for (MPlayer mPlayer : plrs) {
					if(mPlayer.number==ID) {
						mPlayer.color = in.readInt();
						break switches;
					}
				}
				break;
			case Request.PLAYER_REFRESH:
				for (MPlayer mPlayer : plrs) {
					if(mPlayer.number==ID) {
						mPlayer.x = in.readInt();
						mPlayer.y = in.readInt();
						mPlayer.anim = in.readByte();
						mPlayer.setDir(in.readByte());
						break switches;
					}
				}
				break;
			}
		}

		public byte requestType() {return Request.PLAYER_DATA;}
	}