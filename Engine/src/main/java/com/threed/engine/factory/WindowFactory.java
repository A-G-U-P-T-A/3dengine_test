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
                '}';
    }
}
