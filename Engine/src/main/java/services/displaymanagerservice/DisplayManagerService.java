package services.displaymanagerservice;

import factory.WindowFactory;

public interface DisplayManagerService {
    public long createWindow(WindowFactory windowFactory);
    public void updateWindow(long window);
    public void closeWindow(long window);
}
