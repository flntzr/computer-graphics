package ab3;

import static org.lwjgl.opengl.GL33.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL33.GL_CULL_FACE;
import static org.lwjgl.opengl.GL33.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL33.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL33.GL_FLOAT;
import static org.lwjgl.opengl.GL33.GL_TRIANGLES;
import static org.lwjgl.opengl.GL33.glClear;
import static org.lwjgl.opengl.GL33.glDrawArrays;
import static org.lwjgl.opengl.GL33.glEnable;
import static org.lwjgl.opengl.GL33.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL33.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL33.glBindBuffer;
import static org.lwjgl.opengl.GL33.glBufferData;
import static org.lwjgl.opengl.GL33.glGenBuffers;
import static org.lwjgl.opengl.GL33.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL33.glGetUniformLocation;
import static org.lwjgl.opengl.GL33.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL33.glUseProgram;
import static org.lwjgl.opengl.GL33.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glBindVertexArray;
import static org.lwjgl.opengl.GL33.glGenVertexArrays;

import org.lwjgl.opengl.GL33;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;
import lenz.opengl.Texture;

public class Aufgabe3undFolgende extends AbstractOpenGLBase {

	private ShaderProgram shaderProgram;
	private ShaderProgram phongProgram;
	private static int windowWidth = 800;
	private static int windowHeight = 800;
	private String textureName = "turquoise.jpg";
	
	private float rotation = 0f;
	private Matrix4 transformationMatrix;
	private Matrix4 projectionMatrix = new Matrix4(1f, 50f, 1f, 1f);
	private final float[] cPos = { -1f, 0f, 1f };
	private final float[] rPos = { 1f, 0f, 1f };
	private final float[] gPos = { 1f, 0f, -1f };
	private final float[] bPos = { -1f, 0f, -1f };
	private final float[] yPos = { 0f, 1f, 0f };
	
	private final float[] aPos = { -1f, 1f, 1f };
	private final float[] sPos = { 1f, 1f, 1f };
	private final float[] dPos = { 1f, 1f, -1f };
	private final float[] fPos = { -1f, 1f, -1f };
	private final float[] hPos = { -1f, -1f, 1f };
	private final float[] jPos = { 1f, -1f, 1f };
	private final float[] kPos = { 1f, -1f, -1f };
	private final float[] lPos = { -1f, -1f, -1f };
	
	private final float[] cColor = { 0f, 1f, 1f };
	private final float[] yColor = { 1f, 1f, 0f };
	private final float[] rColor = { 1f, 0f, 0f };
	private final float[] gColor = { 0f, 1f, 0f };
	private final float[] bColor = { 0f, 0f, 1f };
	
	private final float[] cryNormalVector = { 0f, 1f, 1f };
	private final float[] rgyNormalVector = { 1f, 1f, 0f };
	private final float[] bgyNormalVector = { 0f, 1f, -1f };
	private final float[] bcyNormalVector = { -1f, 1f, 0f };
	private final float[] crgbNormalVector = { 0f, -1f, 0f };
	
	private final float[][] cubeVertices = {
			lPos, jPos, hPos, 
			lPos, kPos, jPos,
			hPos, jPos, aPos,
			jPos, sPos, aPos,
			jPos, kPos, sPos,
			kPos, dPos, sPos,
			fPos, kPos, lPos,
			fPos, dPos, kPos,
			lPos, aPos, fPos,
			lPos, hPos, aPos,
			aPos, sPos, fPos,
			sPos, dPos, fPos
	};

	private final float[][] pyramidVertices = { 
			cPos, rPos,yPos, 
			rPos, gPos,yPos, 
			gPos, bPos, yPos,
			bPos, cPos,yPos,
			bPos, rPos,cPos, 
			bPos, gPos, rPos 
	};
	
	private final float[][] colors = { 
			cColor, rColor, yColor,
			rColor, gColor, yColor,
			gColor, bColor, yColor,
			bColor, cColor, yColor,
			bColor, rColor, cColor,
			bColor, gColor, rColor 
	};
	
	private final float[][] normalVectors = { 
			cryNormalVector, cryNormalVector, cryNormalVector,
			rgyNormalVector, rgyNormalVector, rgyNormalVector,
			bgyNormalVector, bgyNormalVector, bgyNormalVector,
			bcyNormalVector, bcyNormalVector, bcyNormalVector,
			crgbNormalVector, crgbNormalVector, crgbNormalVector,
			crgbNormalVector, crgbNormalVector, crgbNormalVector
	};
	
	private final float[][] uvCoords = { 
		{0,0}, {1,1}, {0,1},
		{0,0}, {1,0}, {1,1},
		{0,1}, {1,1}, {0,0},
		{0,0}, {1,0}, {1,1},
		{0,1}, {1,1}, {0,0},
		{0,0}, {1,0}, {1,1},
		{0,0}, {1,1}, {0,1},
		{0,0}, {1,0}, {1,1},
		{0,0}, {1,1}, {0,1},
		{0,0}, {1,0}, {1,1},
		{0,1}, {1,1}, {0,0},
		{0,0}, {1,0}, {1,1},
	};

	public static void main(String[] args) {
		new Aufgabe3undFolgende().start("CG Aufgabe 3", windowWidth, windowHeight);
	}

	@Override
	protected void init() {
		// Koordinaten, VAO, VBO, ... hier anlegen und im Grafikspeicher ablegen
		
		this.shaderProgram = new ShaderProgram("aufgabe3");
		this.phongProgram = new ShaderProgram("phong");
		
		glUseProgram(this.phongProgram.getId());
		int va = glGenVertexArrays();
		glBindVertexArray(va);
		int shaderNum = 0;
		
		this.bindShader(this.pyramidVertices, 3, shaderNum++);
		this.bindShader(this.normalVectors, 3, shaderNum++);
		this.bindShader(this.uvCoords, 2, shaderNum++);
		this.bindShader(this.cubeVertices, 3, shaderNum++);
		
		Texture texture = new Texture(this.textureName);
		int textureId = texture.getId();
		GL33.glBindTexture(textureId, GL33.GL_TEXTURE0);
		
		glEnable(GL_DEPTH_TEST); // z-Buffer aktivieren
		glEnable(GL_CULL_FACE); // backface culling aktivieren
	}

	private void bindShader(float[][] arr, int groupSize, int pos) {
		float[] result = new float[groupSize * arr.length];
		int index = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < groupSize; j++) {
				result[index++] = arr[i][j];				
			}
		}
		this.bindShader(result, groupSize, pos);
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
		// Transformation durchführen (Matrix anpassen)
		Matrix4 mat = new Matrix4().rotateX(rotation * .3f).rotateY(rotation).translate(-1.5f, 0f, -6f);
		rotation += 0.03f;
		this.transformationMatrix = mat;
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		int shaderProgId = this.shaderProgram.getId();
		glUseProgram(shaderProgId);
		this.passMatrix(shaderProgId, "matrix", this.transformationMatrix);
		this.passMatrix(shaderProgId, "projection_matrix", this.projectionMatrix);
		glDrawArrays(GL_TRIANGLES, 0, this.cubeVertices.length);

		int phongProgId = this.phongProgram.getId();
		glUseProgram(phongProgId);
		this.passMatrix(phongProgId, "matrix", new Matrix4(this.transformationMatrix).translate(3, 0, 0));
		this.passMatrix(phongProgId, "projection_matrix", this.projectionMatrix);
		glDrawArrays(GL_TRIANGLES, 0, this.pyramidVertices.length);
		// Matrix an Shader übertragen
		// VAOs zeichnen
	}

	private void passMatrix(int shaderProgId, String name, Matrix4 m) {
		int loc = glGetUniformLocation(shaderProgId, name);
		glUniformMatrix4fv(loc, false, m.getValuesAsArray());
	}

	@Override
	public void destroy() {
	}
}
