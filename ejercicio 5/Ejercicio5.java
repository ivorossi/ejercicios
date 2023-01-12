package ejercicio5;

import java.io.IOException;
import java.math.BigInteger;
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
					System.out.println(String.format("%s. %s", (caseNumber),
							Window.treeWindowsInRowWhithCourtain(Integer.parseInt(line))));
				}
			});
		} catch (IOException e) {
			throw new IOException("no se pudo encontrar la direccion del archivo");
		}
	}
}

class Window {

	static public BigInteger treeWindowsInRowWhithCourtain(int windows) {
		BigInteger value = new BigInteger("2");
		/*
		 * for all possible combinations of windows with or without curtains, returns
		 * the number of combinations where there are three windows in a row with
		 * curtains 0 0 1 3 8 20 47 107 238 520 1121
		 */
		return value.pow(windows).subtract(Serie.getTribonacci(windows + 3));
	}
}

class Serie {

	static BigInteger getTribonacci(int element) {
		BigInteger[] tribonaciSerie = { new BigInteger("0"), new BigInteger("0"), new BigInteger("1") };
		if (element < 3) {
			return tribonaciSerie[element];
		} else {
			BigInteger tribonaciElement = new BigInteger("0");
			for (int i = 2; i < element; i++) {
				tribonaciElement = tribonaciSerie[2].add(tribonaciSerie[1]).add(tribonaciSerie[0]);
				tribonaciSerie[0] = tribonaciSerie[1];
				tribonaciSerie[1] = tribonaciSerie[2];
				tribonaciSerie[2] = tribonaciElement;
			}
			return tribonaciElement;
		}
	}
}
