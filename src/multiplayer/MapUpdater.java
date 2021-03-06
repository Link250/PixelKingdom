package multiplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import dataUtils.conversion.ConverterQueue;
import dataUtils.conversion.InConverter;
import map.Chunk;
import map.Map;
import pixel.UDS;
import pixel.PixelList;

public class MapUpdater {

	/**this HashMap stores the the UDS Objects linked to the Map Updates*/
	private HashMap<int[],UDS> udsMap = new HashMap<>();
	private ArrayList<int[]> mapChanges = new ArrayList<>();
	
	/**this HashMap stores the the UDS Objects linked to the UDS Updates*/
	private HashMap<int[],UDS> udsChanges = new HashMap<>();
	
	public int gametype;
	private Map map;
	
	public MapUpdater(Map map, int gametype) {
		this.gametype = gametype;
		this.map = map;
		map.setMapUpdater(this);
	}
	
	public boolean hasUpdates() {
		return !mapChanges.isEmpty() || !udsChanges.isEmpty();
	}
	
	public synchronized void addUpdatePixel(int[] update, UDS uds) {
		boolean found = false;
		for (int i = 0; i < mapChanges.size() && !found; i++) {
			if(		mapChanges.get(i)[2]==update[2] &&
					mapChanges.get(i)[1]==update[1] &&
					mapChanges.get(i)[0]==update[0]) {
				mapChanges.remove(i);
				found = true;
			}
		}if(uds!=null) {
			udsMap.put(update, uds);
		}
		mapChanges.add(update);
	}
	
	public synchronized void addUpdateUDS(int[] coords, UDS uds) {
		boolean found = false;
		for(java.util.Map.Entry<int[], UDS> entry : this.udsChanges.entrySet()) {
			if(		entry.getKey()[2]==coords[2] &&
					entry.getKey()[1]==coords[1] &&
					entry.getKey()[0]==coords[0]) {
				entry.setValue(uds);
				found = true;
			}
		}
		if(!found)udsChanges.put(coords, uds);
	}
	
	public synchronized ArrayList<UpdateList> compUpdates(){
		ArrayList<UpdateListPixel> listsPixel = compUpdatesPixel();
		ArrayList<UpdateListUDS> listsUDS = compUpdatesUDS();
		ArrayList<UpdateList> lists = new ArrayList<>();
		
		while(listsPixel.size() > 0) {
			lists.add(listsPixel.remove(0));
		}
		while(listsUDS.size() > 0) {
			lists.add(listsUDS.remove(0));
		}
		return lists;
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
					//checks if this Update has an UDS
					if(udsMap.containsKey(update)) {
						updateList.addUpdateAndUDS(update[0]&0x3ff, update[1]&0x3ff, udsMap.get(update));
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
				if(udsMap.containsKey(update)) {
					list.addUpdateAndUDS(update[0]&0x3ff, update[1]&0x3ff, udsMap.get(update));
				}else {
					list.addUpdate(update[0]&0x3ff, update[1]&0x3ff);
				}
			}
		}
		udsMap.clear();
		return lists;
	}
	
	public synchronized ArrayList<UpdateListUDS> compUpdatesUDS(){
		ArrayList<UpdateListUDS> lists = new ArrayList<>();
		boolean added;
		for(java.util.Map.Entry<int[], UDS> entry : this.udsChanges.entrySet()) {
			int[] coords = entry.getKey();
			added=false;
			//try to add this update to a list
			for (UpdateListUDS list : lists) {
				if(list.isInside(coords)) {
					list.addUDS(coords[0]%Chunk.width, coords[1]%Chunk.height, entry.getValue());
					added=true;break;
				}
			}
			//no matching list found ? create a new one !
			if(!added) {
				UpdateListUDS list = new UpdateListUDS(coords);
				lists.add(list);
				list.addUDS(coords[0]%Chunk.width, coords[1]%Chunk.height, entry.getValue());
			}
		}
		udsChanges.clear();
		return lists;
	}
	
	public void decompPixelUpdates(InConverter in) {
		try {
			int n  = in.readInt(),
				cx = in.readInt(),
				cy = in.readInt();
			byte l = in.readByte();
			short x,y,ID = in.readShort();
			boolean canHaveUDS = PixelList.GetPixel(ID, l).canHaveUDS();
			for (int i = 0; i < n; i++) {
				x = in.readShort();
				y = in.readShort();
				map.setID((cx*1024)+x, (cy*1024)+y, l, ID,
						canHaveUDS ? PixelList.GetPixel(ID,l).getNewUDS().load(in) : null, gametype==Map.GT_CLIENT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void decompUDSUpdates(InConverter in) {
		try {
			UDS uds = null;
			int n  = in.readInt(),
				cx = in.readInt(),
				cy = in.readInt();
			byte l = in.readByte();
			short x,y;
			for (int i = 0; i < n; i++) {
				x = in.readShort();
				y = in.readShort();
				if((uds = map.getUDS((cx*1024)+x, (cy*1024)+y, l)) != null)uds.load(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public interface UpdateList{
		public ConverterQueue compress();
		public int[] getCoords();
	}
	
	public class UpdateListPixel implements UpdateList{
		private ArrayList<byte[]> coords = new ArrayList<>();
		
		int cx,cy;
		byte l;
		short ID;
		
		public UpdateListPixel(int[] update) {
			cx=update[0]>>10; cy=update[1]>>10; l=(byte) update[2]; ID=(short) update[3];
		}
		
		public int[] getCoords() {
			return new int[] {cx,cy,l};
		}
		
		public boolean isInside(int[] update) {
			return (update[3]==ID && update[2]==l && (update[1]>>10)==cy && (update[0]>>10)==cx);
		}
		
		public void addUpdate(int rx, int ry) {
			coords.add(new byte[] {	(byte) ((rx>>8)&0xff),(byte) ((rx)&0xff),
									(byte) ((ry>>8)&0xff),(byte) ((ry)&0xff)});
		}
		public void addUpdateAndUDS(int rx, int ry, UDS uds) {
			ConverterQueue data = new ConverterQueue();
			data.addShort((short)rx);
			data.addShort((short)ry);
			uds.save(data,false);
			byte[] temp = new byte[data.length()];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = data.pollByte();
			}
			coords.add(temp);
		}
		
		public ConverterQueue compress() {
			ConverterQueue data = new ConverterQueue();
			int n = coords.size();
			int cLength = coords.get(0).length;
			//wir gehen davon aus, dass alle Elemente von coords die selbe l�nge haben
			//da normalerweise alle Pixel eines Materials einen UDS der selben l�nge besitzen
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
			return data;
		}
	}
	
	public class UpdateListUDS implements UpdateList{
		private ConverterQueue data = new ConverterQueue();
		private byte[] arrayData;
		private int numberUDSs = 0;
		
		private int cx,cy;
		private byte l;
		
		public UpdateListUDS(int[] update) {
			cx=update[0]>>Chunk.wLog; cy=update[1]>>Chunk.hLog; l=(byte) update[2];
		}
		
		public int[] getCoords() {
			return new int[] {cx,cy,l};
		}
		
		public boolean isInside(int[] update) {
			return (update[2]==l && (update[1]>>Chunk.hLog)==cy && (update[0]>>Chunk.wLog)==cx);
		}
		
		public void addUDS(int rx, int ry, UDS uds) {
			data.addShort((short)rx);
			data.addShort((short)ry);
			uds.save(data,false);
			numberUDSs++;
		}
		
		public ConverterQueue compress() {
			ConverterQueue tempData = new ConverterQueue();
			tempData.addByte(Request.MAP_DATA);
			tempData.addByte(Request.MAP_UPDATE_UDS);
			tempData.addInt(numberUDSs);
			tempData.addInt(cx);
			tempData.addInt(cy);
			tempData.addByte(l);
			if(data.length()>0) {
				arrayData = data.emptyToArray();
			}
			for (int i = 0; i < arrayData.length; i++) {
				tempData.addByte(arrayData[i]);
			}
			return tempData;
		}
	}
}
