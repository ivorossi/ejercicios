package ejercicio2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.TreeMap;

/**
 * @author rossi
 *
 */
public class Ejercicio2 {
	public static void main(String[] args) throws IOException {
		try (Stream<String> inputLines = Files.lines(Paths.get("ejercicio2-input.txt"))) {
			AtomicInteger caseNumber = new AtomicInteger();
			inputLines.forEach(line -> {
				if (line.startsWith("#")) {
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
				} else {
					try {
						System.out.println(String.format("%s. %s", (caseNumber),
								CardinalNumbers.decimalToCardina(Integer.parseInt(line))));
					} catch (IllegalStateException e) {
						System.out.println(String.format("%s. %s", (caseNumber), e));
					}
				}
			});
		} catch (IOException e) {
			throw new IOException(e);
		}
	}
}

class CardinalNumbers {
	private static final NavigableMap<Integer, String> MAP_DECIMAL_DIGIT_CARDINALS = new TreeMap<Integer, String>();

	public static String decimalToCardina(int number) {
		if (number == 0) {
			return "cero";
		}
		return findCardinal(number).trim();
	}

	private static String findCardinal(int number) {
		if (number == 0) {
			return "";
		}
		if (number > 0) {
			Entry<Integer, String> entry = MAP_DECIMAL_DIGIT_CARDINALS.floorEntry(number);
			return format(entry.getKey(), entry.getValue(), number);
		}
		throw new IllegalStateException("solo numeros naturales");
	}

	private static String format(int key, String value, int number) {
		if (number == key) {
			return value;
		}
		if (number < 30) {
			return "veinti" + findCardinal(number % key);
		}
		if (number < 100) {
			return value + " y " + findCardinal(number % key);
		}
		if (number < 200) {
			return value + "to " + findCardinal(number % key);
		}
		if (number < 2000) {
			return value + " " + findCardinal(number % key);
		}
		if (number < 1000000) {
			return findCardinal(number / key).replace("uno", "un") + " " + value + " " + findCardinal(number % key);
		}
		throw new IllegalStateException("fuera de rango");
	}

	static {
		MAP_DECIMAL_DIGIT_CARDINALS.putAll(
				Map.of(	1, "uno", 
						2, "dos", 
						3, "tres", 
						4, "cuatro", 
						5, "cinco", 
						6, "seis", 
						7, "siete", 
						8, "ocho", 
						9, "nueve"));
		MAP_DECIMAL_DIGIT_CARDINALS.putAll(
				Map.of(	10, "diez", 
						11, "once", 
						12, "doce", 
						13, "trece", 
						14, "catorce", 
						15, "quince", 
						16, "dieciseis", 
						17, "diecisiete", 
						18, "dieciocho", 
						19, "diecinueve"));
		MAP_DECIMAL_DIGIT_CARDINALS.putAll(
				Map.of(	20, "veinte", 
						30, "treinta", 
						40, "cuarenta", 
						50, "cincuenta", 
						60, "sesenta", 
						70, "setenta", 
						80, "ochenta", 
						90, "noventa", 
						100, "cien"));
		MAP_DECIMAL_DIGIT_CARDINALS.putAll(
				Map.of(	200, "doscientos", 
						300, "trecientos", 
						400, "cuatrocientos", 
						500, "quinientos", 
						600, "seiscientos", 
						700, "setecientos", 
						800, "ochocientos", 
						900, "novecientos", 
						1000, "mil"));
	}
}
