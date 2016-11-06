package pixel;

public class MultiPixel<UDSType extends UDS> extends Material<UDSType> {
	int width, height;
	
	public MultiPixel(UDSType uds, int width, int height) {
		super(uds);
		this.width = width;
		this.height = height;
	}

	protected void loadTexture() {
		loadTexture("/MapTextures/MultiPixel/"+name+".png");
	}
	
	public static class DataStorage extends UDS{
		int xOrigin, yOrigin;
	}
}
