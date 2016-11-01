package gfx;


import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader {
	private int programObject;
	private int vertexShaderObject;
	private int fragmentShaderObject;
	
	public Shader(String filename) {
		programObject = glCreateProgram();
		
		vertexShaderObject = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShaderObject, readFile(filename+".vs"));
		glCompileShader(vertexShaderObject);
		if(glGetShaderi(vertexShaderObject, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(vertexShaderObject));
			System.exit(1);
		}
		
		fragmentShaderObject = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShaderObject, readFile(filename+".fs"));
		glCompileShader(fragmentShaderObject);
		if(glGetShaderi(fragmentShaderObject, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(fragmentShaderObject));
			System.exit(1);
		}
		
		glAttachShader(programObject, vertexShaderObject);
		glAttachShader(programObject, fragmentShaderObject);
		
		glBindAttribLocation(programObject, 0, "vertices");
		glBindAttribLocation(programObject, 1, "textures");
		
		glLinkProgram(programObject);
		if(glGetProgrami(programObject, GL_LINK_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(programObject));
			System.exit(1);
		}
		glValidateProgram(programObject);
		if(glGetProgrami(programObject, GL_VALIDATE_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(programObject));
			System.exit(1);
		}
	}

	protected void finalize() throws Throwable {
		glDetachShader(programObject, vertexShaderObject);
		glDetachShader(programObject, fragmentShaderObject);
		glDeleteShader(vertexShaderObject);
		glDeleteShader(fragmentShaderObject);
		glDeleteProgram(programObject);
		super.finalize();
	}
	
	public void setUniform(String uniformName, int value) {
		int location = glGetUniformLocation(programObject, uniformName);
		if(location != -1)
			glUniform1i(location, value);
	}
	
	public void setUniform(String uniformName, float a, float r, float g, float b) {
		int location = glGetUniformLocation(programObject, uniformName);
		if(location != -1)
			glUniform4f(location, r, g, b, a);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
		int location = glGetUniformLocation(programObject, uniformName);
		FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
		value.get(matrixData);
		if(location != -1)
			glUniformMatrix4fv(location, false, matrixData);
	}
	
	public void bind() {
		glUseProgram(programObject);
	}
	
	private String readFile(String filename) {
		StringBuilder outputString = new StringBuilder();
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/shaders/"+filename)));
			String line;
			while((line = bufferedReader.readLine()) != null) {
				outputString.append(line);
				outputString.append("\n");
			}
			bufferedReader.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return outputString.toString();
	}
}
