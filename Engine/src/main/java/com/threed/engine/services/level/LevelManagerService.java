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
    public void createWindow(WindowFactory windowFactory);

    /**
     * Update window.
     *
     * @param windowFactory the window
     */
    public void updateWindow(WindowFactory windowFactory);

    /**
     * Close window.
     *
     * @param windowFactory the window
     */
    public void closeWindow(WindowFactory windowFactory);
}
