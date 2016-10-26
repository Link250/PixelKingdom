package dataUtils.conversion;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ConverterInStream extends ObjectInputStream implements InConverter{

	public ConverterInStream(InputStream in) throws IOException {
		super(in);
	}
	
	protected void readStreamHeader(){}
	
}
