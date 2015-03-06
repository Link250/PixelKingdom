package Pixels;

import java.util.ArrayList;

import Main.ConvertData;

public class AdditionalData{
	private byte[] data;
	
	public AdditionalData(AdditionalData ad){
		if(ad!=null && ad.length()>0){
			data = new byte[ad.length()];
			for(int i=0; i<ad.length(); i++){
				data[i] = ad.getbyte(i);
			}
		}
	}
	public AdditionalData(int length){
		data = new byte[length];
	}
	public AdditionalData(){
		
	}
	
	public int length(){
		if(data!=null)return data.length;
		else return 0;
	}
	
	public byte getbyte(int i){
		return data[i];
	}
	public short getshort(int i){
		return ConvertData.B2S(data[i*2], data[i*2+1]);
	}
	public int getint(int i){
		return ConvertData.B2I(data[i*4], data[i*4+1], data[i*4+2], data[i*4+3]);
	}
	
	public void setbyte(int index, byte n){
		data[index]=n;
	}
	public void setshort(int index, short n){
		byte[] temp = ConvertData.S2B(n);
		data[index*2+0]= temp[0];
		data[index*2+1]= temp[1];
	}
	public void setint(int index, int n){
		byte[] temp = ConvertData.I2B(n);
		data[index*2+0]= temp[0];
		data[index*2+1]= temp[1];
		data[index*2+2]= temp[2];
		data[index*2+3]= temp[3];
	}
	
	public void save(ArrayList<Byte> file) {
		ConvertData.S2B(file, (short) 32767);
		file.add((byte) data.length);
		for(int i = 0; i < data.length; i++){
			file.add(data[i]);
		}
	}

	public void load(ArrayList<Byte> file) {
		data = new byte[file.remove(0)];
		for(int i = 0; i < data.length; i++){
			data[i] = file.remove(0);
		}
	}

}
