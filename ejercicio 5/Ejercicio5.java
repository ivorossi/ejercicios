package ejercicio5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Ejercicio5 {

	public static void main(String[] args) throws IOException {

		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			AtomicInteger caseNumber = new AtomicInteger();
			inputLines.forEach(line -> {
				if (line.startsWith("#")) {
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
				} else {
					System.out.println(String.format("%s. %d", (caseNumber),
							Window.treeWindowsInRowWhithCourtain(Integer.parseInt(line))));
				}
			});
		} catch (IOException e) {
			throw new IOException("no se pudo encontrar la direccion del archivo");
		}
	}
}

class Window {
	
	static public int treeWindowsInRowWhithCourtain(int windows) {
		/*
		 * for all possible combinations of windows with or without curtains, returns
		 * the number of combinations where there are three windows in a row with
		 * curtains
		 */
		int amountMatch = 0;
		for (int i = 0; i < Math.pow(2, windows); i++) {
			if (Integer.toBinaryString(i).contains("111")) {
				amountMatch++;
			}
		}
		return amountMatch;
	}
}
