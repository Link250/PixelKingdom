package Multiplayer;

import java.io.IOException;
import java.io.InputStream;

public interface InputReceiver {
	public abstract boolean useInput(InputStream in) throws IOException;
	public abstract int requestType();
}
