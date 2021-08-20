import factory.WindowFactory;
import services.displaymanagerservice.DisplayManagerService;
import services.displaymanagerservice.DisplayManagerServiceImpl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * The type Test application.
 */
public class TestApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        DisplayManagerService displayManagerService = new DisplayManagerServiceImpl();
        WindowFactory windowFactory = new WindowFactory(100, 100, "test");
        long window = displayManagerService.createWindow(windowFactory);
        displayManagerService.updateWindow(window);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glfwSwapBuffers(window); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
        displayManagerService.closeWindow(window);
    }
}
