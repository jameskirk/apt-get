package emerge.repo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import emerge.exception.InternalException;
import emerge.system.OsType;
import emerge.system.Path;

public class Serializer {
    
    @SuppressWarnings("unchecked")
    public <T> T read(Class<T> clazz, String filePath) throws InternalException {
	T dbFile = null;
	filePath = new Path(filePath, OsType.UNIX).getNativeValue();
	try {
	    FileInputStream fileIn = new FileInputStream(filePath);
	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    dbFile = (T) in.readObject();
	    in.close();
	    fileIn.close();
	    return dbFile;
	} catch (FileNotFoundException e) {
	    throw new InternalException(e);
	} catch (IOException e) {
	    throw new InternalException(e);
	} catch (ClassNotFoundException e) {
	    throw new InternalException(e);
	}
    }
    
    public <T> void write(Class<T> clazz, String filePath, T file) throws InternalException {
	try {
	    filePath = new Path(filePath, OsType.UNIX).getNativeValue();
	    FileOutputStream fileOut = new FileOutputStream(filePath);
	    ObjectOutputStream out = new ObjectOutputStream(fileOut);
	    out.writeObject(file);
	    out.close();
	    fileOut.close();
	} catch (IOException e) {
	    throw new InternalException(e);
	}
    }

}
