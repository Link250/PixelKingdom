package gfx;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_RENDERBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glDeleteFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glDeleteRenderbuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenRenderbuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glRenderbufferStorageEXT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glViewport;
import org.lwjgl.opengl.GL14;

import gfx.RessourceManager.OpenGLRessource;
import main.SinglePlayer;

import static main.Game.logInfo;

public class FrameBufferObject {
    public int colorTextureID = -1;
    public int framebufferID = -1;
    public int depthRenderBufferID = -1;
	
    public FrameBufferObject(int width, int height) {
    	reload(width, height);
	}
    
    public void reload(int width, int height) {
        // init our fbo
		if(framebufferID != -1)glDeleteFramebuffersEXT(framebufferID);
		framebufferID = glGenFramebuffersEXT();                                         // create a new framebuffer
		if(colorTextureID != -1)glDeleteTextures(colorTextureID);
		colorTextureID = glGenTextures();                                               // and a new texture used as a color buffer
		if(depthRenderBufferID != -1)glDeleteRenderbuffersEXT(depthRenderBufferID);
		depthRenderBufferID = glGenRenderbuffersEXT();                                  // And finally a new depthbuffer
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);                        // switch to the new framebuffer
		glViewport (0, 0, width, height);
		
		// initialize color texture
		glBindTexture(GL_TEXTURE_2D, colorTextureID);                                   // Bind the colorbuffer texture
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);               // make it linear filterd
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer
		
		// initialize depth renderbuffer
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);                // bind the depth renderbuffer
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height); // get the data space for it
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                                    // Swithch back to normal framebuffer rendering
    }
    
    protected void finalize() throws Throwable {
		RessourceManager.addRessource(new OpenGLRessource(){
			public void freeRessources() {
				if(SinglePlayer.debuginfo)logInfo("FrameBufferObject.finalize().new OpenGLRessource() {...}.freeRessources()");
				if(framebufferID != -1)glDeleteFramebuffersEXT(framebufferID);
				if(colorTextureID != -1)glDeleteTextures(colorTextureID);
				if(depthRenderBufferID != -1)glDeleteRenderbuffersEXT(depthRenderBufferID);
			}
		});    	super.finalize();
    }
}
