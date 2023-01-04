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
		 * curtains 0 0 1 3 8 20 47 107 238 520 1121
		 */
		return (int) Math.pow(2, windows) - Serie.getTribonacci(windows + 3);
	}
}

class Serie {

	static int getTribonacci(int element) {
		int[] tribonaciSerie = { 0, 0, 1 };
		if (element < 3) {
			return tribonaciSerie[element];
		} else {
			int tribonaciElement = 0;
			for (int i = 2; i < element; i++) {
				tribonaciElement = tribonaciSerie[2] + tribonaciSerie[1] + tribonaciSerie[0];
				/*
				 * no se queda bien asi, o si hago las asignaciones con un bucle for, son la
				 * misma canntidad de lineas supongo que no vale la pena, y tampoco se si vale
				 * la pena esta clase pero como son cosas distintas supongo que esta bien
				 * separarlos, despues borro el comentario.
				 */
				tribonaciSerie[0] = tribonaciSerie[1];
				tribonaciSerie[1] = tribonaciSerie[2];
				tribonaciSerie[2] = tribonaciElement;
			}
			return tribonaciElement;
		}
	}
}
