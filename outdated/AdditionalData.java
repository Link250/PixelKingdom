package pixel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import main.ConvertData;

public class AdditionalData{
	private byte[] data;
	
	public AdditionalData(InputStream in, int length){
		data = new byte[length];
		try {
			for (int i = 0; i < data.length; i++) {
				data[i] = (byte) in.read();
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	public AdditionalData(int length){
		data = new byte[length];
	}
	public AdditionalData(){
		
	}
	
	public byte getbyte(int i){
		return data[i];
	}
	public short getshort(int i){
		return ConvertData.B2S(data, i*2);
	}
	public byte[] getArrayData() {
		return this.data;
	}

	public void setbyte(int index, byte n){
		data[index]=n;
	}
	public void setshort(int index, short n){
		ConvertData.S2B(data, index*2, n);
	}
	public void setint(int index, int n){
		ConvertData.I2B(data, index*4, n);
	}
	
	public void save(ArrayList<Byte> file) {
		ConvertData.S2B(file, (short) 32767);
		file.add((byte) data.length);
		for(int i = 0; i < data.length; i++){
			file.add(data[i]);
		}
	}

	public AdditionalData load(ArrayList<Byte> file) {
		data = new byte[file.remove(0)];
		for(int i = 0; i < data.length; i++){
			data[i] = file.remove(0);
		}
		return this;
	}
}
