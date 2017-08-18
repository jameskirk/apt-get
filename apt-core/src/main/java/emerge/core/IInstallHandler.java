package emerge.core;

import emerge.exception.GenericException;

public interface IInstallHandler extends ActionHandler {
    
    public void checkIfInstalled() throws GenericException;
    
    public void checkFlags() throws GenericException;
    
    public void install() throws GenericException;

    public void writeInInstalledDbFile() throws GenericException;

}
