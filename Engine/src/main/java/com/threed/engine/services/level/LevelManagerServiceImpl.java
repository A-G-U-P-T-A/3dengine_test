package com.threed.engine.services.level;

import com.threed.engine.factory.WindowFactory;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.ARBVertexShader.GL_VERTEX_SHADER_ARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

/**
 * The type Display manager service.
 */
public class LevelManagerServiceImpl implements LevelManagerService {

    private int mouseX, mouseY;
    private boolean viewing;


    private final Matrix4f mat = new Matrix4f();
    private final Quaternionf orientation = new Quaternionf();
    private final Vector3f position = new Vector3f(0, 2, 5).negate();
    private final boolean[] keyDown = new boolean[GLFW_KEY_LAST + 1];

    private int grid;
    private int gridProgram;
    private int gridProgramMatLocation;

    @Override
    public void createWindow(WindowFactory windowFactory) {

        long window;

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();


        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current windowFactory hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the windowFactory will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the windowFactory will be resizable

        // Create the windowFactory
        window = glfwCreateWindow(windowFactory.getWidth(), windowFactory.getHeight(), windowFactory.getTitle(), NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW windowFactory");
        }

        // set window in window factory.
        windowFactory.setWindow(window);


        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (isWindowInitialized, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(isWindowInitialized, true); // We will detect this in the rendering loop
            }
        });

        // Make the windowFactory visible
        initInput(windowFactory);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(windowFactory.getvSync());

        glfwShowWindow(window);

    }

    private void initInput(WindowFactory windowFactory) {
        AtomicInteger width = new AtomicInteger(windowFactory.getWidth());
        AtomicInteger height = new AtomicInteger(windowFactory.getHeight());

        glfwSetKeyCallback(windowFactory.getWindow(), (long window1, int key, int scancode, int action, int mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window1, true);
            } else if (key >= 0 && key <= GLFW_KEY_LAST) {
                this.keyDown[key] = action == GLFW_PRESS || action == GLFW_REPEAT;
            }
        });
        glfwSetFramebufferSizeCallback(windowFactory.getWindow(), (long window3, int w, int h) -> {
            if (w > 0 && h > 0) {
                width.set(w);
                height.set(h);
            }
        });
        glfwSetCursorPosCallback(windowFactory.getWindow(), (long window2, double xpos, double ypos) -> {
            if (this.viewing) {
                float deltaX = (float) xpos - this.mouseX;
                float deltaY = (float) ypos - this.mouseY;
                this.orientation.rotateLocalX(deltaY * 0.01f).rotateLocalY(deltaX * 0.01f);
            }
            this.mouseX = (int) xpos;
            this.mouseY = (int) ypos;
        });
        glfwSetMouseButtonCallback(windowFactory.getWindow(), (long window4, int button, int action, int mods) -> {
            this.viewing = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
        });
        try (MemoryStack stack = stackPush()) {
            IntBuffer framebufferSize = stack.mallocInt(2);
            nglfwGetFramebufferSize(windowFactory.getWindow(), memAddress(framebufferSize), memAddress(framebufferSize) + 4);
            width.set(framebufferSize.get(0));
            height.set(framebufferSize.get(1));
        }
    }

    @Override
    public void updateWindow(WindowFactory windowFactory) {
        long lastTime = System.nanoTime();
        //glfwMakeContextCurrent(windowFactory.getWindow());


        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLCapabilities caps = GL.createCapabilities();
        if (!caps.GL_ARB_shader_objects) {
            throw new UnsupportedOperationException("ARB_shader_objects unsupported");
        }
        if (!caps.GL_ARB_vertex_shader) {
            throw new UnsupportedOperationException("ARB_vertex_shader unsupported");
        }
        if (!caps.GL_ARB_fragment_shader) {
            throw new UnsupportedOperationException("ARB_fragment_shader unsupported");
        }

        createGridProgram();
        createGrid();

        while (!glfwWindowShouldClose(windowFactory.getWindow())) {
            glfwPollEvents();

            glClearColor(0.7f, 0.8f, 0.9f, 1);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);            // Set the clear color
//            glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
//            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glViewport(0, 0, windowFactory.getWidth(), windowFactory.getHeight());
            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
            //glfwSwapBuffers(windowFactory.getWindow()); // swap the color buffers
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glUseProgramObjectARB(this.gridProgram);
            long thisTime = System.nanoTime();
            float dt = (thisTime - lastTime) * 1E-9f;
            lastTime = thisTime;
            try (MemoryStack stack = stackPush()) {
                glUniformMatrix4fvARB(this.gridProgramMatLocation, false, updateMatrices(dt, windowFactory.getWidth(), windowFactory.getHeight()).get(stack.mallocFloat(16)));
            }
            glCallList(this.grid);
            glfwSwapBuffers(windowFactory.getWindow());
        }
    }

    @Override
    public void closeWindow(WindowFactory windowFactory) {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowFactory.getWindow());
        glfwDestroyWindow(windowFactory.getWindow());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private Matrix4f updateMatrices(float dt, int width, int height) {
        float rotateZ = 0f;
        float speed = 2f;
        if (this.keyDown[GLFW_KEY_LEFT_SHIFT]) {
            speed = 10f;
        }
        if (this.keyDown[GLFW_KEY_Q]) {
            rotateZ -= 1f;
        }
        if (this.keyDown[GLFW_KEY_E]) {
            rotateZ += 1f;
        }
        if (this.keyDown[GLFW_KEY_W]) {
            this.position.add(this.orientation.positiveZ(new Vector3f()).mul(dt * speed));
        }
        this.orientation.rotateLocalZ(rotateZ * dt * speed);
        return this.mat.setPerspective((float) Math.toRadians(60), (float) width / height, 0.1f, 1000.0f)
                .rotate(this.orientation)
                .translate(this.position);
    }

    private void createGridProgram() {
        this.gridProgram = glCreateProgramObjectARB();
        int vs = glCreateShaderObjectARB(GL_VERTEX_SHADER_ARB);
        glShaderSourceARB(vs,
                "#version 110\n" +
                        "uniform mat4 viewProjMatrix;\n" +
                        "varying vec4 wp;\n" +
                        "void main(void) {\n" +
                        "  wp = gl_Vertex;\n" +
                        "  gl_Position = viewProjMatrix * gl_Vertex;\n" +
                        "}");
        glCompileShaderARB(vs);
        glAttachObjectARB(this.gridProgram, vs);
        int fs = glCreateShaderObjectARB(GL_FRAGMENT_SHADER_ARB);
        glShaderSourceARB(fs,
                "#version 110\n" +
                        "varying vec4 wp;\n" +
                        "void main(void) {\n" +
                        "  vec2 p = wp.xz / wp.w;\n" +
                        "  vec2 g = 0.5 * abs(fract(p) - 0.5) / fwidth(p);\n" +
                        "  float a = min(min(g.x, g.y), 1.0);\n" +
                        "  gl_FragColor = vec4(vec3(a), 1.0 - a);\n" +
                        "}");
        glCompileShaderARB(fs);
        glAttachObjectARB(this.gridProgram, fs);
        glLinkProgramARB(this.gridProgram);
        this.gridProgramMatLocation = glGetUniformLocationARB(this.gridProgram, "viewProjMatrix");
    }

    private void createGrid() {
        this.grid = glGenLists(1);
        glNewList(this.grid, GL_COMPILE);
        glBegin(GL_TRIANGLES);
        glVertex4f(-1, 0, -1, 0);
        glVertex4f(-1, 0, 1, 0);
        glVertex4f(0, 0, 0, 1);
        glVertex4f(-1, 0, 1, 0);
        glVertex4f(1, 0, 1, 0);
        glVertex4f(0, 0, 0, 1);
        glVertex4f(1, 0, 1, 0);
        glVertex4f(1, 0, -1, 0);
        glVertex4f(0, 0, 0, 1);
        glVertex4f(1, 0, -1, 0);
        glVertex4f(-1, 0, -1, 0);
        glVertex4f(0, 0, 0, 1);
        glEnd();
        glEndList();
    }
}
