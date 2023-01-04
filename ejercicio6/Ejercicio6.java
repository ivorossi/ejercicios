package ejercicio6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Ejercicio6 {

	public static void main(String[] args) throws IOException {

		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			Set<Segment> inputSegment = new TreeSet<Segment>();
			AtomicInteger caseNumber = new AtomicInteger();
			AtomicInteger metesWall = new AtomicInteger();
			inputLines.forEach(line -> {
				if (!line.startsWith("#")) {
					if (!line.contains(" ")) {
						metesWall.set(Integer.parseInt(line));
					} else {
						String[] splitedLine = line.split(" ");
						inputSegment.add(new Segment(Integer.parseInt(splitedLine[0]), Integer.parseInt(splitedLine[1])));
					}
				} else {
					if (!inputSegment.isEmpty()) {
						System.out.println(String.format("%s. %d", caseNumber,
								new Wall(metesWall.get()).totalCleanMeters(inputSegment)));
						inputSegment.clear();
					}
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
				}
			});
			System.out.println(
					String.format("%s. %d", caseNumber, new Wall(metesWall.get()).totalCleanMeters(inputSegment)));

		} catch (IOException e) {
			throw new IOException("no se encontro el archivo");
		}
	}

}

class Wall {
	private int metersLong;

	public Wall(int metersLong) {
		this.metersLong = metersLong;
	}

	public int totalCleanMeters(Set<Segment> allGraffitis) {
		Stack<Segment> splitedGraffitis = new Stack<Segment>();
		splitedGraffitis.add(new Segment(0, 0));

		for (Segment element : allGraffitis) {
			if (splitedGraffitis.peek().getEnd() >= element.getStart()) {
				if (splitedGraffitis.peek().getEnd() < element.getEnd()) {
					splitedGraffitis.peek().setEnd(element.getEnd());
				}
			} else {
				this.metersLong -= splitedGraffitis.pop().module();
				splitedGraffitis.add(element);
			}
		}
		return metersLong -= splitedGraffitis.pop().module();
	}
}

class Segment implements Comparable<Segment> {
	private int start;
	private int end;

	public Segment(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int module() {
		return end - start;
	}

	@Override
	public int compareTo(Segment o) {

		return Integer.compare(this.start, o.start) == 0 ? 
				Integer.compare(this.end, o.end)
				: Integer.compare(this.start, o.start);
	}
}
