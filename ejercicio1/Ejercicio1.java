package ejercicio1;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ejercicio1 {
	public static void main(String[] args) throws IOException {
		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			List<List<Character>> inputMatrix = new ArrayList<List<Character>>();
			AtomicInteger caseNumber = new AtomicInteger();
			inputLines.forEach(line -> {
				if (!line.startsWith("#")) {
					inputMatrix.add(line.chars()
							.mapToObj(c->(char)c)
							.collect(Collectors.toList()));
				} else {
					if (!inputMatrix.isEmpty()) {
						System.out.println(caseNumber + new StainFinder(inputMatrix).findPaintStain());
						inputMatrix.clear();
					}
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
				}
			});
			System.out.println(caseNumber + new StainFinder(inputMatrix).findPaintStain());
		} catch (IOException e) {
			throw new IOException("no se encontro el archivo");
		}
	}
}

class StainFinder {
	private static final Point[] DIRECTIONS = { new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0) };
	
	private final boolean[][] countedPositions;
	private final List<List<Character>> matrix;
	private final Queue<Point> positionPointer;

	public StainFinder(List<List<Character>> matrix) {
		this.matrix = new ArrayList<List<Character>>(matrix);
		this.countedPositions = new boolean[matrix.size()][matrix.get(0).size()];
		this.positionPointer = new ArrayDeque<Point>();
		
	}
	
	private char getColor(Point point) {
		return this.matrix.get(point.x).get(point.y);
	}
	
	private boolean isCounted(Point point) {
		return this.countedPositions[point.x][point.y];
	}
	
	private void setAsCounted(Point point) {
		this.countedPositions[point.x][point.y] = true;
	}

	private boolean shouldCountForStain(char color, Point increasedPosition) {
		return increasedPosition.x <= this.matrix.size() - 1 && increasedPosition.x >= 0&&
			increasedPosition.y <= this.matrix.get(0).size() - 1 && increasedPosition.y >= 0&& 
			color== this.getColor(increasedPosition)&&
			!this.isCounted(increasedPosition);
	}

	private int findByAllDirection(Point position) {
		int count = 0;
		this.setAsCounted(position);
		for (Point direction : DIRECTIONS) {
			Point increasedPoint = new Point(position);
			increasedPoint.translate(direction.x, direction.y);
			if (this.shouldCountForStain(this.getColor(position), increasedPoint)) {
				count++;
				this.setAsCounted(increasedPoint);
				this.positionPointer.add(new Point(increasedPoint));	
			}
		}
		return count;
	}

	public String findPaintStain() {
		char paintStain = 0;
		int paintStainSize = 0;
		for (int i = 0; i < this.matrix.size(); i++) {
			for (int j = 0; j < this.matrix.get(i).size(); j++) {
				Point Position = new Point(i,j);
				if (!this.isCounted(Position)) {
					this.positionPointer.add(Position);
					int count = 1;
					while (!this.positionPointer.isEmpty()) {
						count += this.findByAllDirection(this.positionPointer.poll());
					}
					if (paintStainSize < count) {
						paintStainSize = count;
						paintStain = this.getColor(Position);
					}
				}
			}
		}
		return String.format(". (%s, %d)", paintStain, paintStainSize);
	}
}
