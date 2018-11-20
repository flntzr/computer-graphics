package lenz.opengl;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public abstract class AbstractOpenGLBase {
	static {
		System.setProperty("java.awt.headless", "true");
	}

	protected abstract void init();

	protected abstract void update();

	protected abstract void render();

	protected abstract void destroy();

	public void start(String title, int width, int height) {
		System.out.println("LWJGL " + Version.getVersion());

		long window = openWindow(title, width, height);
		GL.createCapabilities(); // internally connects OpenGL and GLFW's current context
		System.out.println("OpenGL " + glGetString(GL_VERSION));

		init(); // custom user initialization

		while (!glfwWindowShouldClose(window)) {
			update(); // update internal states
			render(); // fill OpenGL buffer with image

			glfwSwapBuffers(window); // double buffering (displays image)

			glfwPollEvents(); // poll window events like key presses
		}

		destroy(); // custom user destruction

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private long openWindow(String title, int width, int height) {
		GLFWErrorCallback.createPrint(System.err).set(); // print errors to syserr
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will NOT be resizable

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // otherwise macos only supports OpenGL 2
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		long window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); // get desktop resolution
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2); // center window

		glfwMakeContextCurrent(window);

		glfwSwapInterval(1); // v-sync

		glfwShowWindow(window);
		return window;
	}
}
