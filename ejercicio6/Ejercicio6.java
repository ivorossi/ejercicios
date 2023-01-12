package ejercicio6;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Ejercicio6 {

	public static void main(String[] args) throws IOException {

		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			List<Segment> inputSegment = new ArrayList<Segment>();
			AtomicInteger caseNumber = new AtomicInteger();
			AtomicInteger metesWall = new AtomicInteger();
			inputLines.forEach(line -> {
				if (!line.startsWith("#")) {
					if (!line.contains(" ")) {
						metesWall.set(Integer.parseInt(line));
					} else {
						String[] splitedLine = line.split(" ");
						inputSegment
								.add(new Segment(Integer.parseInt(splitedLine[0]), Integer.parseInt(splitedLine[1])));
					}
				} else {
					if (!inputSegment.isEmpty()) {
						Collections.sort(inputSegment);
						System.out.println(String.format("%s. %d", caseNumber,
								new Wall(metesWall.get()).totalCleanMeters(inputSegment)));
						inputSegment.clear();
					}
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
				}
			});
			Collections.sort(inputSegment);
			System.out.println(
					String.format("%s. %d", caseNumber, new Wall(metesWall.get()).totalCleanMeters(inputSegment)));

		} catch (IOException e) {
			throw new IOException("no se encontro el archivo");
		}
	}

}

class Wall {
	private final int metersLong;

	public Wall(int metersLong) {
		this.metersLong = metersLong;
	}

	public int totalCleanMeters(List<Segment> allGraffitis) {
		int currentGraffitiStart = 0;
		int currentGraffitiEnd = 0;
		int metersCovered = 0;
		for (Segment nextGraffiti : allGraffitis) {
			if (currentGraffitiEnd >= nextGraffiti.getStart()) {
				if (currentGraffitiEnd < nextGraffiti.getEnd()) {
					currentGraffitiEnd = nextGraffiti.getEnd();
				}
			} else {
				metersCovered += currentGraffitiEnd - currentGraffitiStart;
				currentGraffitiStart = nextGraffiti.getStart();
				currentGraffitiEnd = nextGraffiti.getEnd();
			}
		}
		metersCovered += currentGraffitiEnd - currentGraffitiStart;
		return metersLong - metersCovered;
	}
}

class Segment implements Comparable<Segment> {
	private final int start;
	private final int end;

	public Segment(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public Segment(Segment segment) {
		this.start = segment.getStart();
		this.end = segment.getEnd();
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	@Override
	public int compareTo(Segment other) {
		return Comparator.comparingInt(Segment::getStart)
				.thenComparingInt(Segment::getEnd)
				.compare(this, other);
	}
}
