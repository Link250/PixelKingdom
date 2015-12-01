package multiplayer.client;

import java.io.IOException;

import main.conversion.ConverterInStream;

public interface InputReceiver {
	public abstract void useInput(ConverterInStream in) throws IOException;
	public abstract byte requestType();
}
