package Maps;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Main.IOConverter;
import Multiplayer.Request;

public class MapUpdater {

	/** variablen beschreibung, whaaaaat ??? */
	private ArrayList<int[]> mapChanges = new ArrayList<>();
	
	public boolean hasUpdates() {
		return !mapChanges.isEmpty();
	}
	
	public synchronized void addUpdate(int[] update) {
		boolean found = false;
		for (int i = 0; i < mapChanges.size() && !found; i++) {
			if(mapChanges.get(i)[2]==update[2] && mapChanges.get(i)[1]==update[1] && mapChanges.get(i)[0]==update[0]) {
				mapChanges.remove(i);
				found = true;
			}
		}mapChanges.add(update);
	}
	
	public synchronized byte[][] compUpdates(){
		ArrayList<UpdateList> lists = new ArrayList<>();
		int[] update;
		boolean added;
		while(mapChanges.size()!=0) {
			update = mapChanges.remove(0);
			if(update==null)continue;
			added=false;
			for (UpdateList updateList : lists) {
				if(updateList.isInside(update)) {
					updateList.addUpdate(update[0]&0x3ff, update[1]&0x3ff);
					added=true;break;
				}
			}
			if(!added && lists.add(new UpdateList(update)))
				lists.get(lists.size()-1).addUpdate(update[0]&0x3ff, update[1]&0x3ff);
		}
		byte[][] temp = new byte[lists.size()][];
		for (int i = 0; i < lists.size(); i++) {
			temp[i]=(lists.get(i).compress());
		}
		return temp;
	}
	
	public void decompUpdates(InputStream in, Map map, boolean skipcheck) {
		try {
			int n = IOConverter.receiveInt(in),
				cx = IOConverter.receiveInt(in),
				cy = IOConverter.receiveInt(in);
			byte l = (byte) in.read();
			short x,y,ID = IOConverter.receiveShort(in);
			for (int i = 0; i < n; i++) {
				x = IOConverter.receiveShort(in);
				y = IOConverter.receiveShort(in);
				map.setID((cx*1024)+x, (cy*1024)+y, l, ID, null, skipcheck);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private class UpdateList{
		private ArrayList<byte[]> coords = new ArrayList<>();
		
		int cx,cy;
		byte l;
		short ID;
		
		public UpdateList(int[] update) {
			cx=update[0]>>10; cy=update[1]>>10; l=(byte) update[2]; ID=(short) update[3];
		}
		
		public boolean isInside(int[] update) {
			return (update[3]==ID && update[2]==l && (update[1]>>10)==cy && (update[0]>>10)==cx);
		}
		
		public void addUpdate(int rx, int ry) {
			coords.add(new byte[] {	(byte) ((rx>>8)&0xff),(byte) ((rx)&0xff),
									(byte) ((ry>>8)&0xff),(byte) ((ry)&0xff)});
		}
		
		public byte[] compress() {
			int n = coords.size();
			byte[] data = new byte[16+4*n];
			data[0]=Request.MAP_DATA;
			data[1]=(byte) ((n>>24)&0xff); data[2]=(byte) ((n>>16)&0xff); data[3]=(byte) ((n>>8)&0xff); data[4]=(byte) (n&0xff);
			data[5]=(byte) ((cx>>24)&0xff); data[6]=(byte) ((cx>>16)&0xff); data[7]=(byte) ((cx>>8)&0xff); data[8]=(byte) (cx&0xff);
			data[9]=(byte) ((cy>>24)&0xff); data[10]=(byte)((cy>>16)&0xff); data[11]=(byte)((cy>>8)&0xff); data[12]=(byte)(cy&0xff);
			data[13]=l; data[14]=(byte)((ID>>8)&0xff); data[15]=(byte)(ID&0xff);
			for (int i = 0; i < 4*n; i++) {
				data[16+i]=coords.get(i>>2)[i&0b11];
			}
			return data;
		}
	}
}
