package a1;

import static org.lwjgl.opengl.GL30.*;

import lenz.opengl.AbstractOpenGLBase;
import lenz.opengl.ShaderProgram;

public class Aufgabe1 extends AbstractOpenGLBase {

	public static void main(String[] args) {
		new Aufgabe1().start("CG Aufgabe 1", 700, 700);
	}

	@Override
	protected void init() {
		ShaderProgram shaderProgram = new ShaderProgram("#version 330\nlayout(location=0) in vec2 vtx;void main(){gl_Position=vec4(vtx,0.0,1.0);}",
						"aufgabe1.glsl");
		glUseProgram(shaderProgram.getId());

		glBindVertexArray(glGenVertexArrays());
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, glGenBuffers());
		glBufferData(GL_ARRAY_BUFFER, new int[] { -1, -1, 1, -1, -1, 1, 1, -1, 1, 1, -1, 1 }, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_INT, false, 0, 0);
	}

	@Override
	public void update() {
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT);

		glDrawArrays(GL_TRIANGLES, 0, 6);
	}

	@Override
	public void destroy() {
	}
}
