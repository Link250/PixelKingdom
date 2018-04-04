package map;

import java.util.ArrayList;

public class UpdateManager {

	private ArrayList<int[]> data;
	private ArrayList<int[]> nextdata;
	private int count;
	
	public UpdateManager(){
		nextdata = new ArrayList<int[]>();
		data = new ArrayList<int[]>();
		count = 0;
	}
	
	public synchronized boolean addUpdate(int x, int y, int l){
		count++;
		int[] u = {x,y,l};
		if(nextdata.size()<count){
			nextdata.add(u);
		}else{
			nextdata.set(count-1, u);
		}
		return true;
	}
	
	public synchronized int startUpdate(){
		for(int i = 0; i < count; i++){
			if(data.size()<(i+1)) data.add(nextdata.get(i));
			else data.set(i, nextdata.get(i));
		}
		int c = count; count = 0;
		return c;
	}
	
	public int[] activate(int index){
		return data.get(index);
	}
	
	public int length(){
		return count;
	}
	
}
