package multiplayer.client;

import java.io.IOException;

import dataUtils.conversion.InConverter;

public interface InputReceiver {
	public abstract void useInput(InConverter in) throws IOException;
	public abstract byte requestType();
}
