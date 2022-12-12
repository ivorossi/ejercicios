package ejercicio1;
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
	static StainFinder stainFinder = new StainFinder();
	static String output = "";
	public static void main(String[] args) {
		try(Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			List<char[]> inputMatrix = new ArrayList<char[]>();
			AtomicInteger caseNumber = new AtomicInteger(1);
			inputLines.forEach(line -> {
				if (!line.startsWith("#")){
					inputMatrix.add(line.toCharArray());
				} else if(!inputMatrix.isEmpty()) {
					caseNumber.set(Integer.parseInt(line.replace("# Case ", "")));
					output += String.valueOf(caseNumber.decrementAndGet()) +
							stainFinder.findPaintStain(inputMatrix)+("\n");
					inputMatrix.clear();
					stainFinder = new StainFinder();					
				}
			});
			System.out.println(output += String.valueOf(caseNumber.incrementAndGet())+
										(stainFinder.findPaintStain(inputMatrix)));
		} catch (IOException e) {
			System.out.println("No se encontro la direccion del archivo");
		} 
	}
}

class StainFinder {
	
	private Queue<int[]> positionPointer = new ArrayDeque<int[]>();
	private char paintStain;
	private int paintStainSize;
	private int count = 1;
	private boolean[][] countedPosition;
	private int[][] directions = {{-1,0,},{0,-1},{0,1},{1,0}};

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

	private void findByAllDirection(int[] position, List<char[]> M1) {
		int i = position [0];
		int j = position [1];
		int di;
		int dj;
		this.countedPosition[i][j] = true;
		for( int[] direction : this.directions) {
			di = direction[0];
			dj = direction[1];
			if(i + di > M1.size() -1||i + di < 0 ) continue;
			if(j +dj > M1.get(i).length -1|| j + dj < 0) continue;
			if (M1.get(i)[j] != M1.get(i+ di)[j + dj])continue;
			if (countedPosition[i + di][j + dj])continue;
			count++;
			countedPosition[i + di][j + dj] = true;
			this.positionPointer.add(new int[] {i + di, j + dj});
		}
	}
	public String findPaintStain(List<char[]> M1) {
		this.setPointerCounted(M1.size(), M1.get(0).length);
		for (int i = 0; i < M1.size(); i++) {
			for (int j = 0; j < M1.get(i).length; j++) {
				if (!this.countedPosition[i][j]) {
					this.positionPointer.add(new int[] { i, j });
					
					while(!positionPointer.isEmpty()){
						this.findByAllDirection(this.positionPointer.poll(), M1);
					}
					if (this.paintStainSize < this.count) {
						this.paintStainSize = count;
						this.paintStain = M1.get(i)[j];
					}
					this.restartCounter();
				}
			}
		}
		return String.format(". (%s, %d)", paintStain, paintStainSize);
	}
}
