package apt.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainForEclipse {

	public static void main(String args[]) {
		Main main = new Main();
		main.init();
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
