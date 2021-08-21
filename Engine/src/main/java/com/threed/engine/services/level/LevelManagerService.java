package com.threed.engine.services.level;

import com.threed.engine.factory.WindowFactory;

/**
 * The interface Display manager service.
 */
public interface LevelManagerService {
    /**
     * Create window long.
     *
     * @param windowFactory the window factory
     *
     * @return the long
     */
    public long createWindow(WindowFactory windowFactory);

    /**
     * Update window.
     *
     * @param window the window
     */
    public void updateWindow(long window);

    /**
     * Close window.
     *
     * @param window the window
     */
    public void closeWindow(long window);
}
