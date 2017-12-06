package apt.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AptGetConsoleForEclipse {

    public static void main(String args[]) {
	AptGetConsole main = new AptGetConsole();
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String line = null;
	while (true) {
	    System.out.println("Enter command:");
	    try {
		line = br.readLine();
		if ("".equals(line)) {
		    break;
		}
		main.executeCommand(line.split(" "));
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}
