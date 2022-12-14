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
	public static void main(String[] args) throws IOException{
		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			List<char[]> inputMatrix = new ArrayList<char[]>();
			AtomicInteger caseNumber = new AtomicInteger();
			inputLines.forEach(line -> {
				if (!line.startsWith("#")){
					inputMatrix.add(line.toCharArray());
				} else if(!inputMatrix.isEmpty()) { 
					StainFinder stainFinder = new StainFinder();
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
					System.out.println(String.format("%d%s",(caseNumber.decrementAndGet()),
							stainFinder.findPaintStain(inputMatrix)));
					inputMatrix.clear();
					stainFinder = new StainFinder();					
				}
			});
			StainFinder stainFinder = new StainFinder();
			System.out.println(String.valueOf(caseNumber.incrementAndGet())+
										(stainFinder.findPaintStain(inputMatrix)));
		}catch(IOException e){
			throw new IOException("no se encontro el archivo");
		}		
	}
}

class StainFinder {
	static final private int[][] DIRECTIONS = {{-1,0,},{0,-1},{0,1},{1,0}};

	private Queue<int[]> positionPointer = new ArrayDeque<int[]>();
	private char paintStain;
	private int paintStainSize;
	private int count = 1;
	private boolean[][] countedPosition;

	private void restartCounter() {
		this.positionPointer.clear();
		this.count = 1;
	}

	private void setPointerCounted(int m, int n) {
		countedPosition = new boolean[m][n];
	}
	private boolean shouldCountForStain(int i, int j, int di, int dj, List<char[]> matrix) {
		if(i + di > matrix.size() -1||i + di < 0 ) return true;
		if(j +dj > matrix.get(i).length -1|| j + dj < 0) return true;
		if (matrix.get(i)[j] != matrix.get(i+ di)[j + dj])return true;
		if (countedPosition[i + di][j + dj])return true;
		return false;
	}
	private void findByAllDirection(int[] position, List<char[]> matrix) {
		int i = position [0];
		int j = position [1];
		int di;
		int dj;
		this.countedPosition[i][j] = true;
		for( int[] direction : DIRECTIONS) {
			di = direction[0];
			dj = direction[1];
			if(shouldCountForStain(i, j, di, dj, matrix) ) continue;
			count++;
			countedPosition[i + di][j + dj] = true;
			this.positionPointer.add(new int[] {i + di, j + dj});
		}
	}
	public String findPaintStain(List<char[]> matrix) {
		this.setPointerCounted(matrix.size(), matrix.get(0).length);
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).length; j++) {
				if (!this.countedPosition[i][j]) {
					this.positionPointer.add(new int[] { i, j });
					while(!positionPointer.isEmpty()){
						this.findByAllDirection(this.positionPointer.poll(), matrix);
					}
					if (this.paintStainSize < this.count) {
						this.paintStainSize = count;
						this.paintStain = matrix.get(i)[j];
					}
					this.restartCounter();
				}
			}
		}
		return String.format(". (%s, %d)", paintStain, paintStainSize);
	}
}
