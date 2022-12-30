package ejercicio3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
	static Map<String,String> SAME_TIMES_IN_THE_MIRROR = new HashMap<String,String>();
	static{SAME_TIMES_IN_THE_MIRROR.putAll(
			Map.of(	"0", "0",
					"2", "5",
					"5", "2",
					"8", "8"));
	}

	static public String howItLooksInTheMirror(String time) {
		int midel = time.length()/2;
		for(int i = 0; i<midel;i++){
			String value = SAME_TIMES_IN_THE_MIRROR.get(String.valueOf(time.charAt(i)));
			if(!String.valueOf(time.charAt(time.length()-i-1)).equals(value)) {
				return "No se ve igual";
			}	
		}
		return"se ve igual";
	}
/*
 * hola creo que quedaria mejor si en vez de usar un map
 * uso dos char[] encadenados para trabajar todo con primitivos
 * pero me imagino que esta era la idea de la solucion no?
 * 
 */
}
