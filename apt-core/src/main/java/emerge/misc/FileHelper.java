package emerge.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import emerge.entity.PackageName;
import emerge.exception.InternalException;
import emerge.system.OsType;
import emerge.system.Path;

public class FileHelper {

    public static String readFile(String fileName) throws InternalException {
	return readFile(fileName, "UTF-8");
    }

    public static String readFile(String fileName, String encoding) throws InternalException {
	fileName = new Path(fileName, OsType.UNIX).getNativeValue();
	try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding))) {
	    String line;
	    StringBuilder fileAsString = new StringBuilder();
	    while ((line = reader.readLine()) != null) {
		fileAsString.append(line + "\n");
	    }
	    return fileAsString.toString();

	} catch (UnsupportedEncodingException e) {
	    throw new InternalException(e);
	} catch (FileNotFoundException e) {
	    throw new InternalException(e);
	} catch (IOException e) {
	    throw new InternalException(e);
	}
    }

    public static void writeFile(String fileName, String fileAsString) throws InternalException {
	fileName = new Path(fileName, OsType.UNIX).getNativeValue();
	try (BufferedWriter reader = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))) {
	    reader.write(fileAsString);
	    reader.flush();
	} catch (UnsupportedEncodingException e) {
	    throw new InternalException(e);
	} catch (FileNotFoundException e) {
	    throw new InternalException(e);
	} catch (IOException e) {
	    throw new InternalException(e);
	}
    }

    public static String findByPattern(String str, Pattern p) {
	Matcher m = p.matcher(str);
	if (m.find()) {
	    return (m.group());
	} else {
	    // System.out.println("pattern '" + p + "' not found in string ");
	    return null;
	}
    }
    
    public static List<String> findAllByPattern(String str, Pattern p) {
	Matcher m = p.matcher(str);
	List<String> retVal = new ArrayList<String>();
	while (m.find()) {
	    retVal.add(m.group());
	}

	if (retVal.isEmpty()) {
	    // System.out.println("pattern not found in string: " + str);
	}
	return retVal;
    }
    
    public static boolean isFindByPattern(String str, Pattern p) {
	Matcher m = p.matcher(str);
	return m.find();
    }
    
    public static String getEbuildPath(PackageName packageId) {
	return "/" + packageId.getCategory() + "/" + packageId.getPackageName() + "/" + packageId.getPackageName() + "-" + packageId.getVersion();
    }

    public static String getEbuildPathFirst(PackageName packageId) {
	return "/" + packageId.getCategory() + "/" + packageId.getPackageName();
    }

}
