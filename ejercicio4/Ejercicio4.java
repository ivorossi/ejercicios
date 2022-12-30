package ejercicio4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;



public class Ejercicio4 {
	public static void main(String[] args) throws IOException {
		try (Stream<String> inputLines = Files.lines(Paths.get("ejercicio4-input"))) {
			List<char[]> inputMatrix = new ArrayList<char[]>();
			AtomicInteger caseNumber = new AtomicInteger();
			inputLines.forEach(line -> {
				if (!line.startsWith("#")) {
					inputMatrix.add(line.toCharArray());
				} else {
					if (!inputMatrix.isEmpty()) {
						Chess a1 = new Chess(inputMatrix);
						for(int i = 0; i < a1.board.length; i++) {
							for(int j = 0; j < a1.board.length; j++) {
								System.out.print(a1.board[i][j] + " ");
								
							}
							System.out.println("");
						}
					}
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
				}
			});
		} catch (IOException e) {
			throw new IOException("no se pudo encontrar la direccion del archivo");
		}
	}
}
class Chess{
	final char[][] board;
	public Chess(List<char[]> matrix){
		this.board = new char[8][8];
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board.length; j++) {
				board[i][j] = matrix.get(i)[j];
				if(matrix.get(i+8)[j] != "0".charAt(0)) {
					board[i][j] = matrix.get(i+8)[j];
				}
			}
		}
	}
}

