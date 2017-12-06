package apt.core;

import apt.exception.GenericException;

public interface IRemoveHandler extends ActionHandler {
    
    public void checkIfInstalled() throws GenericException;
    
    public void remove() throws GenericException;

    public void writeInInstalledDbFile() throws GenericException;

}
