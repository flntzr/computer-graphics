package a2;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

public class Aufgabe2 extends AbstractOpenGLBase {

	public static void main(String[] args) {
		new Aufgabe2().start("CG Aufgabe 2", 700, 700);
	}

	@Override
	protected void init() {
		// folgende Zeile lädt automatisch "aufgabe2_v.glsl" (vertex) und
		// "aufgabe2_f.glsl" (fragment)
		ShaderProgram shaderProgram = new ShaderProgram("aufgabe2");
		glUseProgram(shaderProgram.getId());

		int va = glGenVertexArrays();
		glBindVertexArray(va);
		int shaderNum = 0;
		this.bindShader(new float[] { -.6f, -.6f, .6f, -.6f, 0f, .6f }, 2, shaderNum++);
		this.bindShader(new float[] { 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f }, 3, shaderNum++);

		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
	}

	private void bindShader(float[] arr, int groupSize, int pos) {
		int vb = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vb);
		glBufferData(GL_ARRAY_BUFFER, arr, GL_STATIC_DRAW);
		glVertexAttribPointer(pos, groupSize, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(pos);
	}

//	private float[] createShape(float[] coordsX, float[] coordsY, ) {
//		FloatBuffer buffer = BufferUtils.createFloatBuffer(coordsX.length * 2);
//		for (int i = 0; i < coordsX.length; i++) {
//			buffer.put(coordsX[i]);
//			buffer.put(coordsY[i]);
//		}
//		return buffer;
//	}

	@Override
	public void update() {

	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT); // Zeichenfl�che leeren
		glDrawArrays(GL_TRIANGLES, 0, 3);
		// hier vorher erzeugte VAOs zeichnen
	}

	@Override
	public void destroy() {
	}
}
