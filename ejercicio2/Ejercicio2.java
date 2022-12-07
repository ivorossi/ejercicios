package ejercicio2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.TreeMap;

/**
 * @author rossi
 *
 */
public class Ejercicio2 {
	static CardinalNumbers cardinalNumber = new CardinalNumbers();
	static String output = "";

	public static void main(String[] args) {
		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]));) {
			AtomicInteger caseNumber = new AtomicInteger(1);
			inputLines.forEach(line -> {
				if (line.startsWith("#")) {
					caseNumber.set(Integer.parseInt(line.replace("# Case ", "")));
				} else{
					System.out.println(String.valueOf(caseNumber.get())+ ". "+
							cardinalNumber.decimalToCardinal(Integer.parseInt(line)));
				}
			});
			
		} catch (IOException e) {
			System.out.println("No se encontro la direccion del archivo");
		}
	}
}

class CardinalNumbers {
	
	Map<Integer, String> mapDecimalDigitCardinals = new TreeMap<Integer, String>(Collections.reverseOrder());
	public  CardinalNumbers() {
		this.mapDecimalDigitCardinals.putAll(
				Map.of(0, "cero",
						1, "uno",
						2, "dos", 
						3, "tres", 
						4, "cuatro", 
						5, "cinco", 
						6, "seis", 
						7, "siete", 
						8, "ocho", 
						9, "nueve"));
		this.mapDecimalDigitCardinals.putAll(
				Map.of(10, "diez",
						11, "once", 
						12, "doce", 
						13, "trece", 
						14, "catorce", 
						15, "quince", 
						16, "dieciseis", 
						17, "diecisiete", 
						18, "dieciocho", 
						19, "diecinueve"));
		this.mapDecimalDigitCardinals.putAll(
				Map.of(20, "veinte", 
						30, "treinta", 
						40, "cuarenta", 
						50, "cincuenta", 
						60, "secenta", 
						70, "setenta", 
						80, "ochenta", 
						90, "noventa", 
						100, "cien"));
		this.mapDecimalDigitCardinals.putAll(
				Map.of(200, "doscientos", 
						300, "trecientos", 
						400, "cuatrocientos", 
						500, "quinientos", 
						600, "seiscientos", 
						700, "setecientos", 
						800, "ochocientos", 
						900, "novecientos", 
						1000, "mil"));
	}

	public String decimalToCardinal(int number) {

		for (Entry<Integer, String> entry : mapDecimalDigitCardinals.entrySet()) {
			if (entry.getKey() <= number) {
				return format(entry.getKey(), entry.getValue(), number);
			}
		}
		return "solo numeros naturales";
	}

	private String format(int key, String value, int number) {
		if (number == key)
			return value;
		if (number < 30)
			return "veinti" + decimalToCardinal(number % key);
		if (number < 100)
			return value + " y " + decimalToCardinal(number % key);
		if (number < 200)
			return value + "to " + decimalToCardinal(number % key);
		if (number < 1000)
			return value + " " + decimalToCardinal(number % key);
		if (number < 1000000)
			return decimalToCardinal(number / key) + value + " " + decimalToCardinal(number % key);
		return "fuera de rango";
	}
}
