package com.threed.engine;

import com.threed.engine.factory.WindowFactory;
import com.threed.engine.services.level.LevelManagerServiceImpl;

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
        WindowFactory windowFactory = new WindowFactory(1800, 900, "test", 1);
        displayManagerService.createWindow(windowFactory);
        displayManagerService.updateWindow(windowFactory);
        displayManagerService.closeWindow(windowFactory);
    }
}
