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
	private final boolean[][] COUNTED_POSITIONS;

	private List<char[]> matrix;
	private Queue<Point> positionPointer = new ArrayDeque<Point>();

	public StainFinder(List<char[]> matrix) {
		this.matrix = matrix;
		this.COUNTED_POSITIONS = new boolean[matrix.size()][matrix.get(0).length];
	}

	private boolean shouldCountForStain(int i, int j, int di, int dj) {
		if (i + di > this.matrix.size() - 1 || i + di < 0) {
			return true;
		}
		if (j + dj > this.matrix.get(i).length - 1 || j + dj < 0) {
			return true;
		}
		if (this.matrix.get(i)[j] != this.matrix.get(i + di)[j + dj]) {
			return true;
		}
		if (this.COUNTED_POSITIONS[i + di][j + dj]) {
			return true;
		}
		return false;
	}

	private int findByAllDirection(Point position) {
		int count = 0;
		int i = position.x;
		int j = position.y;
		int di;
		int dj;
		this.COUNTED_POSITIONS[i][j] = true;
		for (Point direction : DIRECTIONS) {
			di = direction.x;
			dj = direction.y;
			if (this.shouldCountForStain(i, j, di, dj)) {
				continue;
			}
			count++;
			this.COUNTED_POSITIONS[i + di][j + dj] = true;
			this.positionPointer.add(new Point(i + di, j + dj));
		}
		return count;
	}

	public String findPaintStain() {
		char paintStain = 0;
		int paintStainSize = 0;
		for (int i = 0; i < this.matrix.size(); i++) {
			for (int j = 0; j < this.matrix.get(i).length; j++) {
				if (!this.COUNTED_POSITIONS[i][j]) {
					this.positionPointer.add(new Point(i, j));
					int count = 1;
					while (!this.positionPointer.isEmpty()) {
						count += this.findByAllDirection(this.positionPointer.poll());
					}
					if (paintStainSize < count) {
						paintStainSize = count;
						paintStain = this.matrix.get(i)[j];
					}
				}
			}
		}
		return String.format(". (%s, %d)", paintStain, paintStainSize);
	}
}
