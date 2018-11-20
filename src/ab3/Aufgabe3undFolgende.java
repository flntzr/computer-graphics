package ab3;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.Arrays;
import java.util.stream.Stream;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

public class Aufgabe3undFolgende extends AbstractOpenGLBase {

	private ShaderProgram shaderProgram;
	private ShaderProgram phongProgram;
	private static int windowWidth = 700;
	private static int windowHeight = 700;
	private float rotation = 0f;
	private Matrix4 transformationMatrix;
	private Matrix4 projectionMatrix = new Matrix4(1f, 50f, 1f, 1f);
	private final float[] cPos = { -1f, 0f, 1f };
	private final float[] yPos = { 0f, 1f, 0f };
	private final float[] rPos = { 1f, 0f, 1f };
	private final float[] gPos = { 1f, 0f, -1f };
	private final float[] bPos = { -1f, 0f, -1f };
	
	private final float[] cColor = { 0f, 1f, 1f };
	private final float[] yColor = { 1f, 1f, 0f };
	private final float[] rColor = { 1f, 0f, 0f };
	private final float[] gColor = { 0f, 1f, 0f };
	private final float[] bColor = { 0f, 0f, 1f };
	
//	private final float[][] pixelCoords = { 
//			cPos, yPos, rPos, 
//			rPos, yPos, gPos, 
//			gPos, yPos, bPos, 
//			bPos, yPos, cPos,
//			bPos, cPos, rPos, 
//			bPos, rPos, gPos };
	private final float[][] pixelCoords = { 
			cPos, rPos,yPos, 
			rPos, gPos,yPos, 
			gPos, bPos, yPos,
			bPos, cPos,yPos,
			bPos, rPos,cPos, 
			bPos, gPos, rPos };
	private final float[][] colors = { 
			cColor, rColor, yColor,
			rColor, gColor, yColor,
			gColor, bColor, yColor,
			bColor, cColor, yColor,
			bColor, rColor, cColor,
			bColor, gColor, rColor };

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

//	private final float[] colors = {
//			0f, 1f, 1f, 
//			1f, 1f, 0f, 
//			1f, 0f, 0f,
//			
//			1f, 0f, 0f, 
//			1f, 1f, 0f, 
//			0f, 1f, 0f,
//			
//			0f, 1f, 0f, 
//			1f, 1f, 0f, 
//			0f, 0f, 1f,
//			
//			0f, 0f, 1f, 
//			1f, 1f, 0f,
//			0f, 1f, 1f, 
//			
//			0f, 0f, 1f, 
//			0f, 1f, 1f, 
//			1f, 0f, 0f,
//			
//			0f, 0f, 1f, 
//			1f, 0f, 0f,
//			0f, 1f, 0f 
//	};

	public static void main(String[] args) {
		new Aufgabe3undFolgende().start("CG Aufgabe 3", windowWidth, windowHeight);
	}

	@Override
	protected void init() {
		this.shaderProgram = new ShaderProgram("aufgabe3");
		this.phongProgram = new ShaderProgram("phong");

		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
		glUseProgram(this.shaderProgram.getId());
		int va = glGenVertexArrays();
		glBindVertexArray(va);
		int shaderNum = 0;
		this.bindShader(this.pixelCoords, 3, shaderNum++);
		this.bindShader(this.colors, 3, shaderNum++);
//		this.bindShader(this.normalVectors, 3, shaderNum++);

		glUseProgram(this.phongProgram.getId());
		va = glGenVertexArrays();
		glBindVertexArray(va);
		shaderNum = 0;
		this.bindShader(this.pixelCoords, 3, shaderNum++);
		this.bindShader(this.colors, 3, shaderNum++);
		this.bindShader(this.normalVectors, 3, shaderNum++);

		glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
		glEnable(GL_CULL_FACE); // backface culling aktivieren
	}

	private void bindShader(float[][] arr, int groupSize, int pos) {
		float[] result = new float[3 * arr.length];
		int index = 0;
		for (int i = 0; i < arr.length; i++) {
			result[index++] = arr[i][0];
			result[index++] = arr[i][1];
			result[index++] = arr[i][2];
		}
		this.bindShader(result, groupSize, pos);
	}

//	private float[] createNormalVectors(float[][] pixelCoords) {
//		float[] result = new float[3 * pixelCoords.length];
//		int index = 0;
//		for (int i = 0; i < pixelCoords.length; i+=3) {
//			float[] v1 = VectorUtils.subtractVec3(pixelCoords[i + 1], pixelCoords[i + 1]);
//			float[] v2 = VectorUtils.subtractVec3(pixelCoords[i], pixelCoords[i + 2]);
//			float[] n = VectorUtils.crossProductVec3(v1, v2);				
//			result[index++] = n[0];
//			result[index++] = n[1];
//			result[index++] = n[2];
//			v1 = VectorUtils.subtractVec3(pixelCoords[i], pixelCoords[i + 1]);
//			v2 = VectorUtils.subtractVec3(pixelCoords[i], pixelCoords[i + 2]);
//			n = VectorUtils.crossProductVec3(v1, v2);
//			result[index++] = n[0];
//			result[index++] = n[1];
//			result[index++] = n[2];
//			result[index++] = n[0];
//			result[index++] = n[1];
//			result[index++] = n[2];
//		}
//		return result;
//	}

	private void bindShader(float[] arr, int groupSize, int pos) {
		int vb = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vb);
		glBufferData(GL_ARRAY_BUFFER, arr, GL_STATIC_DRAW);
		glVertexAttribPointer(pos, groupSize, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(pos);
	}

	@Override
	public void update() {
		// Transformation durchführen (Matrix anpassen)
		Matrix4 mat = new Matrix4().rotateX(rotation * .3f).rotateY(rotation).translate(-1.5f, 0f, -6f);
		rotation += 0.03f;
		this.transformationMatrix = mat;
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glUseProgram(this.shaderProgram.getId());
		this.passMatrix("matrix", this.transformationMatrix);
		this.passMatrix("projection_matrix", this.projectionMatrix);
		glDrawArrays(GL_TRIANGLES, 0, this.pixelCoords.length);

		glUseProgram(this.phongProgram.getId());
		this.passMatrix("matrix", new Matrix4(this.transformationMatrix).translate(3, 0, 0));
		this.passMatrix("projection_matrix", this.projectionMatrix);
		glDrawArrays(GL_TRIANGLES, 0, this.pixelCoords.length);
		// Matrix an Shader übertragen
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
