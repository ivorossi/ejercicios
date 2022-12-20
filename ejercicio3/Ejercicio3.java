package ejercicio3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


public class Ejercicio3 {

	public static void main(String[] args) throws IOException {
		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			AtomicInteger caseNumber = new AtomicInteger();
			inputLines.forEach(line -> {
				if (line.startsWith("#")) {
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
				} else {
					System.out.println(String.format("%s. %s", caseNumber, DigitalClock.howItLooksInTheMirror(line)));
				}
			});
		} catch (IOException e) {
			throw new IOException("no se pudo encontrar la direccion del archivo");
		}
	}
}
class DigitalClock{
	static final String[]SAME_TIMES_IN_THEE_MIRROR = {"00:00", "02:50", "05:20", "20:05"};
	static public String howItLooksInTheMirror(String time) {
		for(String sameTime :SAME_TIMES_IN_THEE_MIRROR) {
			if(time.equals(sameTime)){
				return "Se ve igual";
			}
		}
		return "No se ve igual";
	}
}
