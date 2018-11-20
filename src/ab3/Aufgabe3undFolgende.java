package ab3;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

public class Aufgabe3undFolgende extends AbstractOpenGLBase {

	private ShaderProgram shaderProgram;
	private static int windowWidth = 700;
	private static int windowHeight = 700;
	private float rotation = 0f;
	private Matrix4 transformationMatrix;
	private Matrix4 projectionMatrix = new Matrix4(1f, 50f, 1f, 1f);
	private final float[] pixelCoords = {
			-1f, 0f, 1f, // C
			0f, 1f, 0f, // Y
			1f, 0f, 1f, // R
			
			1f, 0f, 1f, // R
			0f, 1f, 0f, // Y
			1f, 0f, -1f, // G
			
			1f, 0f, -1f, // G
			0f, 1f, 0f, // Y
			-1f, 0f, -1f, // B
			
			-1f, 0f, -1f, // B
			0f, 1f, 0f, // Y
			-1f, 0f, 1f, // C
			
			-1f, 0f, -1f, // B
			-1f, 0f, 1f, // C
			1f, 0f, 1f, // R
			
			-1f, 0f, -1f, // B
			1f, 0f, 1f, // R
			1f, 0f, -1f // G
	};	
	
	private final float[] normalVectors = {
		-1f, -1f, 1f, // C
		0f, 1f, 0f, // Y
		1f, -1f, 0.5f, // R
		
		1f, -1f, 0.5f, // R
		0f, 1f, 0f, // Y
		1f, -1f, -1f, // G
		
		1f, -1f, -1f, // G
		0f, 1f, 0f, // Y
		-1f, -1f, -.5f, // B
		
		-1f, -1f, -.5f, // B
		0f, 1f, 0f, // Y
		-1f, -1f, 1f, // C
		
		-1f, -1f, -.5f, // B
		-1f, -1f, 1f, // C
		1f, -1f, 0.5f, // R
		
		-1f, -1f, -.5f, // B
		1f, -1f, 0.5f, // R
		1f, -1f, -1f // G
	};
	
	private final float[] colors = {
			0f, 1f, 1f, 
			1f, 1f, 0f, 
			1f, 0f, 0f,
			
			1f, 0f, 0f, 
			1f, 1f, 0f, 
			0f, 1f, 0f,
			
			0f, 1f, 0f, 
			1f, 1f, 0f, 
			0f, 0f, 1f,
			
			0f, 0f, 1f, 
			1f, 1f, 0f,
			0f, 1f, 1f, 
			
			0f, 0f, 1f, 
			0f, 1f, 1f, 
			1f, 0f, 0f,
			
			0f, 0f, 1f, 
			1f, 0f, 0f,
			0f, 1f, 0f 
	};

	public static void main(String[] args) {
		new Aufgabe3undFolgende().start("CG Aufgabe 3", windowWidth, windowHeight);
	}

	@Override
	protected void init() {
		shaderProgram = new ShaderProgram("aufgabe3");
		glUseProgram(shaderProgram.getId());

		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
		int va = glGenVertexArrays();
		glBindVertexArray(va);
		int shaderNum = 0;
		this.bindShader(this.pixelCoords, 3, shaderNum++);
		this.bindShader(this.colors, 3, shaderNum++);
		this.bindShader(this.normalVectors, 3, shaderNum++);

		glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
		glEnable(GL_CULL_FACE); // backface culling aktivieren
	}

	private void bindShader(float[] arr, int groupSize, int pos) {
		int vb = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vb);
		glBufferData(GL_ARRAY_BUFFER, arr, GL_STATIC_DRAW);
		glVertexAttribPointer(pos, groupSize, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(pos);
	}

	@Override
	public void update() {
		// Transformation durchf�hren (Matrix anpassen)		
		Matrix4 mat = new Matrix4().rotateX(rotation*.3f).rotateY(rotation).translate(0, 0f, -6f);
		rotation += 0.03f;
		this.transformationMatrix = mat;
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		this.passMatrix("matrix", this.transformationMatrix);
		this.passMatrix("projection_matrix", this.projectionMatrix);
		
		glDrawArrays(GL_TRIANGLES, 0, this.pixelCoords.length);
		// Matrix an Shader �bertragen
		// VAOs zeichnen
	}

	private void passMatrix(String name, Matrix4 m) {
		int loc = glGetUniformLocation(this.shaderProgram.getId(), name);
		glUniformMatrix4fv(loc, false, m.getValuesAsArray());
	}

	@Override
	public void destroy() {
	}
}
