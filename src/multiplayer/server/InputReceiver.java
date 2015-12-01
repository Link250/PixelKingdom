package multiplayer.server;

import java.io.IOException;

import main.conversion.ConverterInStream;

public interface InputReceiver {
	public abstract void useInput(ConverterInStream in, byte ID) throws IOException;
	public abstract byte requestType();
}
