package multiplayer.server;

import java.io.IOException;

import dataUtils.conversion.InConverter;

public interface InputReceiver {
	public abstract void useInput(InConverter in, byte ID) throws IOException;
	public abstract byte requestType();
}
