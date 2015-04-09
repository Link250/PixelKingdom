package Multiplayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Main.Game;
import Main.IOConverter;
import entities.MPlayer;

	public class PlayerManager implements InputReceiver{
		private Game game;
		private ArrayList<MPlayer> plrs;
		public PlayerManager(Game g, ArrayList<MPlayer> players) {
			game = g;
			plrs = players;
			Client.server.requests.add(this);
		}
		
		public boolean useInput(InputStream in) throws IOException {
			int i = 0,n;
			try {i = in.read();} catch (IOException e) {e.printStackTrace();}
			switch(i) {
			case 1:
				plrs.add(new MPlayer(game.client.map, game, in.read()));
				System.out.println("new player");
				break;
			case 2:
				n = in.read();
				for(int j = 0; j < plrs.size(); j++) {
					if(plrs.get(j).number==n) {
						plrs.remove(j);
						System.out.println("player left");
						break;
					}
				}
				break;
			case 3:
				n = 0;
				try {n = in.read();} catch (IOException e) {}
				for(int j = 0; j < plrs.size(); j++) {
					if(plrs.get(j).number==n) {
						plrs.get(j).x = IOConverter.receiveInt(in);
						plrs.get(j).y = IOConverter.receiveInt(in);
					}
				}
				break;
			}
			return false;
		}

		public int requestType() {return Request.PLAYER_DATA;}
	}