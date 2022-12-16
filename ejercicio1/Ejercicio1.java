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
import java.util.stream.Stream;

public class Ejercicio1 {
	public static void main(String[] args) throws IOException {
		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			List<char[]> inputMatrix = new ArrayList<char[]>();
			AtomicInteger caseNumber = new AtomicInteger();
			inputLines.forEach(line -> {
				if (!line.startsWith("#")) {
					inputMatrix.add(line.toCharArray());
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
	private final List<char[]> matrix;
	private final Queue<Point> positionPointer;

	public StainFinder(List<char[]> matrix) {
		this.matrix = matrix;
		this.countedPositions = new boolean[matrix.size()][matrix.get(0).length];
		this.positionPointer = new ArrayDeque<Point>();
	}
	
	private char getColor(Point point) {
		return this.matrix.get(point.x)[point.y];
	}
	
	private boolean getCountedPositions(Point point) {
		return this.countedPositions[point.x][point.y];
	}
	
	private void setCountedPositions(Point point) {
		this.countedPositions[point.x][point.y] = true;
	}

	private boolean shouldCountForStain(Point position, Point increasedPosition) {
		return (increasedPosition.x <= this.matrix.size() - 1 && increasedPosition.x >= 0)&&
			(increasedPosition.y <= this.matrix.get(0).length - 1 && increasedPosition.y >= 0)&& 
			(this.getColor(position)== this.getColor(increasedPosition))&&
			(!this.getCountedPositions(increasedPosition));
	}

	private int findByAllDirection(Point position) {
		int count = 0;
		this.setCountedPositions(position);
		for (Point direction : DIRECTIONS) {
			Point increasedPoint = new Point(position);
			increasedPoint.translate(direction.x, direction.y);
			if (this.shouldCountForStain(position, increasedPoint)) {
				count++;
				this.setCountedPositions(increasedPoint);
				this.positionPointer.add(new Point(increasedPoint));	
			}
		}
		return count;
	}

	public String findPaintStain() {
		char paintStain = 0;
		int paintStainSize = 0;
		for (int i = 0; i < this.matrix.size(); i++) {
			for (int j = 0; j < this.matrix.get(i).length; j++) {
				Point Position = new Point(i,j);
				if (!this.getCountedPositions(Position)) {
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
