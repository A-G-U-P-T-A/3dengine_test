package factory;

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
        return width;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "WindowFactory{" +
                "width=" + width +
                ", height=" + height +
                ", title='" + title + '\'' +
                '}';
    }
}
