package com.threed.engine.factory;

/**
 * The type WindowFactory.
 */
public class WindowFactory {
    /**
     * The Width.
     */
    private final int width;
    /**
     * The Height.
     */
    private final int height;
    /**
     * The Title.
     */
    private final String title;

    /**
     * The Window.
     */
    private long window;

    /**
     * The V sync.
     */
    private int vSync;


    /**
     * Instantiates a new WindowFactory.
     *
     * @param width  the width
     * @param height the height
     * @param title  the title
     */
    public WindowFactory(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    /**
     * Instantiates a new Window factory.
     *
     * @param width  the width
     * @param height the height
     * @param title  the title
     * @param vSync  the v sync
     */
    public WindowFactory(int width, int height, String title, int vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets window.
     *
     * @return the window
     */
    public long getWindow() {
        return this.window;
    }

    /**
     * Gets sync.
     *
     * @return the sync
     */
    public int getvSync() {
        return this.vSync;
    }

    /**
     * Sets window.
     *
     * @param window the window
     */
    public void setWindow(long window) {
        this.window = window;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "WindowFactory{" +
                "width=" + this.width +
                ", height=" + this.height +
                ", title='" + this.title + '\'' +
                ", window=" + this.window +
                '}';
    }
}
