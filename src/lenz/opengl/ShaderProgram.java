package lenz.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.InputStream;
import java.util.Scanner;

public class ShaderProgram {
	private int id;

	public ShaderProgram(String resourceNameWithoutExtension) {
		id = glCreateProgram();
		loadSourceAndCompileAndAttach(resourceNameWithoutExtension + "_v.glsl", GL_VERTEX_SHADER);
		loadSourceAndCompileAndAttach(resourceNameWithoutExtension + "_f.glsl", GL_FRAGMENT_SHADER);

		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
		}
	}

	public ShaderProgram(String vertexShaderSource, String fragmentResourceName) { // for assignment 1
		id = glCreateProgram();
		compileAndAttach("'vertex shader'", GL_VERTEX_SHADER, vertexShaderSource);
		loadSourceAndCompileAndAttach(fragmentResourceName, GL_FRAGMENT_SHADER);
		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
		}
	}

	public int getId() {
		return id;
	}

	private InputStream getInputStreamFromResourceName(String resourceName) {
		return getClass().getResourceAsStream("/res/shaders/" + resourceName);
	}

	private void loadSourceAndCompileAndAttach(String resourceName, int type) {
		InputStream inputStreamFromResourceName = getInputStreamFromResourceName(resourceName);
		if (inputStreamFromResourceName == null) {
			if (type != GL_GEOMETRY_SHADER) {
				throw new RuntimeException("Shader source file " + resourceName + " not found!");
			}
			return;
		}
		String source;
		try (Scanner in = new Scanner(inputStreamFromResourceName)) {
			source = in.useDelimiter("\\A").next();
		}
		compileAndAttach(resourceName, type, source);
	}

	private void compileAndAttach(String resourceName, int type, String source) {
		int shaderId = glCreateShader(type);
		glShaderSource(shaderId, source);
		glCompileShader(shaderId);

		String compileLog = glGetShaderInfoLog(shaderId, glGetShaderi(shaderId, GL_INFO_LOG_LENGTH));
		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Shader " + resourceName + " not compiled: " + compileLog);
		}
		if (!compileLog.isEmpty()) {
			System.err.println(resourceName + ": " + compileLog);
		}

		glAttachShader(id, shaderId);
	}
}
