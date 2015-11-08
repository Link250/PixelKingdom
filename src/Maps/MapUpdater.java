package Maps;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import Main.ConvertData;
import Main.IOConverter;
import Multiplayer.Request;
import Pixels.AdditionalData;
import Pixels.PixelList;

public class MapUpdater {

	private ArrayList<int[]> mapChanges = new ArrayList<>();
	private HashMap<int[],byte[]> ads = new HashMap<>();
	
	public boolean hasUpdates() {
		return !mapChanges.isEmpty();
	}
	
	public synchronized void addUpdate(int[] update, AdditionalData ad) {
		boolean found = false;
		for (int i = 0; i < mapChanges.size() && !found; i++) {
			if(		mapChanges.get(i)[2]==update[2] &&
					mapChanges.get(i)[1]==update[1] &&
					mapChanges.get(i)[0]==update[0]) {
				mapChanges.remove(i);
				found = true;
			}
		}if(ad!=null) {
			ads.put(update, ad.getData());
		}
		mapChanges.add(update);
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
					if(ads.containsKey(update)) {
						updateList.addUpdateAndAD(update[0]&0x3ff, update[1]&0x3ff, ads.get(update));
					}else {
						updateList.addUpdate(update[0]&0x3ff, update[1]&0x3ff);
					}
					added=true;break;
				}
			}
			if(!added && lists.add(new UpdateList(update)))
				if(ads.containsKey(update)) {
					lists.get(lists.size()-1).addUpdateAndAD(update[0]&0x3ff, update[1]&0x3ff, ads.get(update));
				}else {
					lists.get(lists.size()-1).addUpdate(update[0]&0x3ff, update[1]&0x3ff);
				}
		}
		byte[][] temp = new byte[lists.size()][];
		for (int i = 0; i < lists.size(); i++) {
			temp[i]=(lists.get(i).compress());
		}
		ads.clear();
		return temp;
	}
	
	public void decompUpdates(InputStream in, Map map, boolean skipcheck) {
		try {
			int n  = IOConverter.receiveInt(in),
				cx = IOConverter.receiveInt(in),
				cy = IOConverter.receiveInt(in);
			byte l = (byte) in.read();
			short x,y,ID = IOConverter.receiveShort(in);
			int adLength = PixelList.GetPixel(ID, l).adl;
			boolean hasAD = adLength>0;
			for (int i = 0; i < n; i++) {
				x = IOConverter.receiveShort(in);
				y = IOConverter.receiveShort(in);
				if(hasAD) {
					map.setID((cx*1024)+x, (cy*1024)+y, l, ID, new AdditionalData(in, adLength), skipcheck);
				}else{
					map.setID((cx*1024)+x, (cy*1024)+y, l, ID, null, skipcheck);
				}
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
		public void addUpdateAndAD(int rx, int ry, byte[] data) {
			byte[] temp = new byte[4+data.length];
			temp[0] = (byte) ((rx>>8)&0xff);
			temp[1] = (byte) ((rx   )&0xff);
			temp[2] = (byte) ((ry>>8)&0xff);
			temp[3] = (byte) ((ry   )&0xff);
			for (int i = 0; i < data.length; i++) {
				temp[i+4] = data[i];
			}
			coords.add(temp);
		}
		
		public byte[] compress() {
			int n = coords.size();
			int cLength = coords.get(0).length;
			//wir gehen davon aus, dass alle Elemente von coords die selbe länge haben
			//da normalerweise alle Pixel eines Materials einen AD der selben länge besitzen
			byte[] data = new byte[16+cLength*n];
			data[0]=Request.MAP_DATA;
			ConvertData.I2B(data, 1, n);
			ConvertData.I2B(data, 5, cx);
			ConvertData.I2B(data, 9, cy);
			data[13]=l;
			ConvertData.S2B(data, 14, ID);
			for (int i = 0; i < cLength*n; i++) {
				data[16+i]=coords.get(i/cLength)[i%cLength];
			}
			return data;
		}
	}
}
