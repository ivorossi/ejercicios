package ejercicio1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Ejercicio1 {
	public static void main(String[] args) {

		Stream<String> imput = null;
		try {
			imput = Files.lines(Paths.get(args[0]));
			StringBuilder output = new StringBuilder();
			ArrayList<char[]> M1 = new ArrayList<char[]>();
			AtomicBoolean flag = new AtomicBoolean(false);
			Finder finder1 = new Finder();
			imput.forEach(line -> {
				if (line.charAt(0) == 35) {
					if (flag.getAndSet(true)) {
						output.append(finder1.findPaintStain(M1)).append("\n");
					}
					output.append(line.substring(line.length() - 1))
						.append(". ");
					M1.clear();
					finder1.clear();
				} else {
					M1.add(line.toCharArray());
				}
			});
			System.out.println(output.append(finder1.findPaintStain(M1)));
		} catch (IOException e) {
			System.out.println("No se encontro la direccion del archivo");
		} 
	}
}

class Finder {
	
	private ArrayList<int[]> positionPointer = new ArrayList<int[]>();
	private char paintStain;
	private int paintStainSize;
	private int count = 1;
	private boolean[][] countedPosition;

	private void restartCounter() {
		this.positionPointer.clear();
		this.count = 1;
	}

	public void clear() {
		this.restartCounter();
		this.countedPosition = null;
		this.paintStainSize = 0;
		this.paintStain = 0;
	}

	private void setPointerCounted(int m, int n) {
		countedPosition = new boolean[m][n];
	}

	private void byRight(int i, int j, ArrayList<char[]> M1) {
		if (M1.get(i).length - 2 < j)return;
		if (M1.get(i)[j] != M1.get(i)[j + 1])return;
		if (countedPosition[i][j + 1])return;
		this.count++;
		this.countedPosition[i][j + 1] = true;
		this.positionPointer.add(new int[] { i, j + 1 });

	}

	private void byDown(int i, int j, ArrayList<char[]> M1) {
		if (M1.size() - 2 < i)return;
		if (M1.get(i)[j] != M1.get(i + 1)[j])return;
		if (countedPosition[i + 1][j])return;
		count++;
		countedPosition[i + 1][j] = true;
		this.positionPointer.add(new int[] { i + 1, j });
	}

	private void byUp(int i, int j, ArrayList<char[]> M1) {
		if (1 > i)return;
		if (M1.get(i)[j] != M1.get(i - 1)[j])return;
		if (countedPosition[i - 1][j])return;
		count++;
		countedPosition[i - 1][j] = true;
		this.positionPointer.add(new int[] { i - 1, j });
	}

	private void byLeft(int i, int j, ArrayList<char[]> M1) {
		if (1 > j)return;
		if (M1.get(i)[j] != M1.get(i)[j - 1])return;
		if (countedPosition[i][j - 1])return;
		count++;
		countedPosition[i][j - 1] = true;
		this.positionPointer.add(new int[] { i, j - 1 });
	}

	private void findByAllDireccion(int i, int j, ArrayList<char[]> M1) {
		this.countedPosition[i][j] = true;
		this.byRight(i, j, M1);
		this.byLeft(i, j, M1);
		this.byDown(i, j, M1);
		this.byUp(i, j, M1);
	}

	public StringBuilder findPaintStain(ArrayList<char[]> M1) {
		this.setPointerCounted(M1.size(), M1.get(0).length);
		for (int i = 0; i < this.countedPosition.length; i++) {
			for (int j = 0; j < this.countedPosition[i].length; j++) {
				if (!this.countedPosition[i][j]) {
					this.positionPointer.add(new int[] { i, j });
					for (int k = 0; k < positionPointer.size(); k++) {
						this.findByAllDireccion(this.positionPointer.get(k)[0], this.positionPointer.get(k)[1], M1);
					}
					if (this.paintStainSize < this.count) {
						this.paintStainSize = count;
						this.paintStain = M1.get(i)[j];
					}
					this.restartCounter();
				}
			}
		}
		return new StringBuilder("(")
				.append(this.paintStain)
				.append(", ")
				.append(this.paintStainSize)
				.append(")");
	}
}
