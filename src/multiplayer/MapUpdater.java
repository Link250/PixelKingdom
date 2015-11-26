package multiplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import main.conversion.ConverterInStream;
import main.conversion.ConverterList;
import map.Chunk;
import map.Map;
import pixel.AD;
import pixel.PixelList;

public class MapUpdater {

	/**this HashMap stores the the ADs linked to the Map Updates*/
	private HashMap<int[],AD> adsMap = new HashMap<>();
	private ArrayList<int[]> mapChanges = new ArrayList<>();
	
	/**this HashMap stores the the ADs linked to the AD Updates*/
	private HashMap<int[],AD> ads = new HashMap<>();
	
	public boolean hasUpdates() {
		return !mapChanges.isEmpty() || !ads.isEmpty();
	}
	
	public synchronized void addUpdatePixel(int[] update, AD ad) {
		boolean found = false;
		for (int i = 0; i < mapChanges.size() && !found; i++) {
			if(		mapChanges.get(i)[2]==update[2] &&
					mapChanges.get(i)[1]==update[1] &&
					mapChanges.get(i)[0]==update[0]) {
				mapChanges.remove(i);
				found = true;
			}
		}if(ad!=null) {
			adsMap.put(update, ad);
		}
		mapChanges.add(update);
	}
	
	public synchronized void addUpdateAD(int[] coords, AD ad) {
		boolean found = false;
		for(java.util.Map.Entry<int[], AD> entry : this.ads.entrySet()) {
			if(		entry.getKey()[2]==coords[2] &&
					entry.getKey()[1]==coords[1] &&
					entry.getKey()[0]==coords[0]) {
				entry.setValue(ad);
				found = true;
			}
		}
		if(!found)ads.put(coords, ad);
	}
	
	public synchronized byte[][] compUpdates(){
		ArrayList<UpdateListPixel> listsPixel = compUpdatesPixel();
//		ArrayList<UpdateListAD> listsAD = compUpdatesAD();
//		byte[][] temp = new byte[listsPixel.size()+listsAD.size()][];
		byte[][] temp = new byte[listsPixel.size()][];
		
		int i = 0;
		while(listsPixel.size()>0) {
			temp[i] = listsPixel.remove(0).compress();
			i++;
		}
//		while(listsAD.size()>0) {
//			temp[i] = listsAD.remove(0).compress();
//			i++;
//		}
		ads.clear();
		return temp;
	}
	
	public synchronized ArrayList<UpdateListPixel> compUpdatesPixel(){
		ArrayList<UpdateListPixel> lists = new ArrayList<>();
		int[] update;
		boolean added;
		while(mapChanges.size()!=0) {
			update = mapChanges.remove(0);
			if(update==null)continue;
			added=false;
			//try to add this update to a list
			for (UpdateListPixel updateList : lists) {
				if(updateList.isInside(update)) {
					//checks if this Update has an AD
					if(adsMap.containsKey(update)) {
						updateList.addUpdateAndAD(update[0]&0x3ff, update[1]&0x3ff, adsMap.get(update));
					}else {
						updateList.addUpdate(update[0]&0x3ff, update[1]&0x3ff);
					}
					added=true;break;
				}
			}
			//no matching list found ? create a new one !
			if(!added) {
				UpdateListPixel list = new UpdateListPixel(update);
				lists.add(list);
				if(adsMap.containsKey(update)) {
					list.addUpdateAndAD(update[0]&0x3ff, update[1]&0x3ff, adsMap.get(update));
				}else {
					list.addUpdate(update[0]&0x3ff, update[1]&0x3ff);
				}
			}
		}
		adsMap.clear();
		return lists;
	}
	
	public synchronized ArrayList<UpdateListAD> compUpdatesAD(){
		ArrayList<UpdateListAD> lists = new ArrayList<>();
		boolean added;
		for(java.util.Map.Entry<int[], AD> entry : this.ads.entrySet()) {
			int[] coords = entry.getKey();
			added=false;
			//try to add this update to a list
			for (UpdateListAD list : lists) {
				if(list.isInside(coords)) {
					list.addAD(coords[0]%Chunk.width, coords[1]%Chunk.height, entry.getValue());
					added=true;break;
				}
			}
			//no matching list found ? create a new one !
			if(!added) {
				UpdateListAD list = new UpdateListAD(coords);
				lists.add(list);
				list.addAD(coords[0]%Chunk.width, coords[1]%Chunk.height, entry.getValue());
			}
		}
		ads.clear();
		return lists;
	}
	
	public void decompPixelUpdates(ConverterInStream in, Map map, boolean skipcheck) {
		try {
			int n  = in.readInt(),
				cx = in.readInt(),
				cy = in.readInt();
			byte l = in.readByte();
			short x,y,ID = in.readShort();
			boolean canHaveAD = PixelList.GetPixel(ID, l).canHaveAD();
			for (int i = 0; i < n; i++) {
				x = in.readShort();
				y = in.readShort();
				map.setID((cx*1024)+x, (cy*1024)+y, l, ID,
						canHaveAD ? PixelList.GetPixel(ID,l).getNewAD().load(in) : null, skipcheck);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void decompADUpdates(ConverterInStream in, Map map, boolean skipcheck) {
		try {
			int n  = in.readInt(),
				cx = in.readInt(),
				cy = in.readInt();
			byte l = in.readByte();
			short x,y;
			for (int i = 0; i < n; i++) {
				x = in.readShort();
				y = in.readShort();
				try{
					map.getAD((cx*1024)+x, (cy*1024)+y, l).load(in);
				}catch(NullPointerException e) {
					System.out.println(map.getID((cx*1024)+x, (cy*1024)+y, l));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class UpdateListPixel{
		private ArrayList<byte[]> coords = new ArrayList<>();
		
		int cx,cy;
		byte l;
		short ID;
		
		public UpdateListPixel(int[] update) {
			cx=update[0]>>10; cy=update[1]>>10; l=(byte) update[2]; ID=(short) update[3];
		}
		
		public boolean isInside(int[] update) {
			return (update[3]==ID && update[2]==l && (update[1]>>10)==cy && (update[0]>>10)==cx);
		}
		
		public void addUpdate(int rx, int ry) {
			coords.add(new byte[] {	(byte) ((rx>>8)&0xff),(byte) ((rx)&0xff),
									(byte) ((ry>>8)&0xff),(byte) ((ry)&0xff)});
		}
		public void addUpdateAndAD(int rx, int ry, AD ad) {
			ConverterList data = new ConverterList();
			data.addShort((short)rx);
			data.addShort((short)ry);
			ad.save(data,false);
			byte[] temp = new byte[data.length()];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = data.pollByte();
			}
			coords.add(temp);
		}
		
		public byte[] compress() {
			ConverterList data = new ConverterList();
			int n = coords.size();
			int cLength = coords.get(0).length;
			//wir gehen davon aus, dass alle Elemente von coords die selbe länge haben
			//da normalerweise alle Pixel eines Materials einen AD der selben länge besitzen
			data.addByte(Request.MAP_DATA);
			data.addByte(Request.MAP_UPDATE_PXL);
			data.addInt(n);
			data.addInt(cx);
			data.addInt(cy);
			data.addByte(l);
			data.addShort(ID);
			for (int i = 0; i < cLength*n; i++) {
				data.addByte(coords.get(i/cLength)[i%cLength]);
			}
			byte[] returnData = new byte[data.length()];
			for (int i = 0; i < returnData.length; i++) {
				returnData[i] = data.pollByte();
			}
			return returnData;
		}
	}
	
	private class UpdateListAD{
		ConverterList data = new ConverterList();
		int numberADs = 0;
		
		int cx,cy;
		byte l;
		
		public UpdateListAD(int[] update) {
			cx=update[0]>>Chunk.wLog; cy=update[1]>>Chunk.hLog; l=(byte) update[2];
		}
		
		public boolean isInside(int[] update) {
			return (update[2]==l && (update[1]>>Chunk.hLog)==cy && (update[0]>>Chunk.wLog)==cx);
		}
		
		public void addAD(int rx, int ry, AD ad) {
			data.addShort((short)rx);
			data.addShort((short)ry);
			ad.save(data,false);
			numberADs++;
		}
		
		public byte[] compress() {
			ConverterList tempData = new ConverterList();
			tempData.addByte(Request.MAP_DATA);
			tempData.addByte(Request.MAP_UPDATE_AD);
			tempData.addInt(numberADs);
			tempData.addInt(cx);
			tempData.addInt(cy);
			tempData.addByte(l);
			while(data.length() > 0) {
				tempData.addByte(data.pollByte());
			}
			byte[] returnData = new byte[tempData.length()];
			for (int i = 0; tempData.length() > 0; i++) {
				returnData[i] = tempData.pollByte();
			}
			return returnData;
		}
	}
}
