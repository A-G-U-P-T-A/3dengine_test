package com.threed.engine;

import com.threed.engine.factory.WindowFactory;
import com.threed.engine.services.level.LevelManagerServiceImpl;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

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
        LevelManagerServiceImpl displayManagerService = new LevelManagerServiceImpl();
        WindowFactory windowFactory = new WindowFactory(100, 100, "test");
        long window = displayManagerService.createWindow(windowFactory);
        displayManagerService.updateWindow(window);
        while (!glfwWindowShouldClose(window)) {
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
//            glfwSwapBuffers(window); // swap the color buffers
//            // Poll for window events. The key callback above will only be
//            // invoked during this call.
            glfwPollEvents();
        }
        displayManagerService.closeWindow(window);
    }
}
