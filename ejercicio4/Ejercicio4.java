package ejercicio4;

import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ejercicio4 {
	public static void main(String[] args) throws IOException {
		try (Stream<String> inputLines = Files.lines(Paths.get(args[0]))) {
			List<List<Character>> inputMatrix = new ArrayList<List<Character>>();
			AtomicInteger caseNumber = new AtomicInteger();
			inputLines.forEach(line -> {
				if (!line.startsWith("#")) {
					inputMatrix.add(line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
				} else {
					if (!inputMatrix.isEmpty()) {
						System.out.println(String.format("%s. %s", caseNumber, new Chess(inputMatrix).haveCheck()));
						inputMatrix.clear();
					}
					caseNumber.set(Integer.parseInt(line.replaceAll("\\D+", "")));
				}
			});
			System.out.println(String.format("%s. %s", caseNumber, new Chess(inputMatrix).haveCheck()));
		} catch (IOException e) {
			throw new IOException("no se pudo encontrar la direccion del archivo");
		}
	}
}

class Chess {
	
	private final Board board;

	public Chess(List<List<Character>> matrix) {

		this.board = new Board();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (matrix.get(i).get(j) != '0') {
					this.board.addPiece(new Point(i, j), matrix.get(i).get(j), Color.WHITE);
				}
				if (matrix.get(i + 8).get(j) != '0') {
					this.board.addPiece(new Point(i, j), matrix.get(i + 8).get(j), Color.BLACK);
				}
			}
		}
	}

	public String haveCheck() {
		return isCheck() ? "hay jaque" : "No hay jaque";
	}

	private boolean isCheck() {
		for (Piece piece : this.board.getPieces()) {
			if (piece.getIsAttaking().ifAttacking(this.board.getOtherKingPosition(piece.getColor()), this.board)) {				
				return true;
			}
		}
	return false;
	}
}

class Board {
	private Point positionBlackKing;
	private Point positionWhiteKing;
	private final List<Piece> pieces;

	public Board() {
		this.pieces = new ArrayList<Piece>();
	}

	public void addPiece(Point position, char value, Color color) {

		if (value == 'P'){
			this.pieces.add(new Piece(color, position,(kingPositions, board) -> 
				position.getX() == kingPositions.getX() - color.direction()
				&& Math.abs(position.getY() - kingPositions.getY()) == 1
				));
		} else if (value == 'C'){
			this.pieces.add(new Piece(color, position,(kingPositions,board) -> 
				Math.abs(position.getX() - kingPositions.getX()) == 2
				&& Math.abs(position.getY() - kingPositions.getY()) == 1
				|| (Math.abs(position.getX() - kingPositions.getX()) == 1
				&& Math.abs(position.getY() - kingPositions.getY()) == 2)
				));
		} else if (value == 'T' || value == 'A' || value == 'D') {
			this.pieces.add(new Piece(color, position, (kingPositions, board) -> {
				int dx = 0;
				int dy = 0;
				Point road = new Point(position);
				if (value == 'T' || value == 'D') {
					if (position.getX() == kingPositions.getX()) {
						dy = position.getY() < kingPositions.getY() ? 1 : -1;
					} else if (position.getY() == kingPositions.getY()) {
						dx = position.getX() < kingPositions.getX() ? 1 : -1;
					} else {
						return false;
					}
				} else {
					if (Math.abs(position.getX() - kingPositions.getX()) == Math
							.abs(position.getY() - kingPositions.getY())) {
						dy = position.getY() < kingPositions.getY() ? 1 : -1;
						dx = position.getX() < kingPositions.getX() ? 1 : -1;
					} else {
						return false;
					}
				}
				road.translate(dx, dy);
				while (!road.equals(kingPositions)) {
					if (!board.isEmptyPositions(road)) {
						return false;
					}
					road.translate(dx, dy);
				}
				return true;
			}));
		} else if (value == 'R') {
			this.pieces.add(new Piece(color, position,(otherPositions, board) -> 
				Math.abs(position.getX() - otherPositions.getX()) <= 1
				&& Math.abs(position.getY() - otherPositions.getY()) <= 1));
			if (color.isWhite()) {
				this.positionWhiteKing = position;
			} else {
				this.positionBlackKing = position;
			}
		}

	}
	
	public Point getOtherKingPosition(Color color) {
		return color.isWhite()? this.positionBlackKing : this.positionWhiteKing;
	}

	public boolean isEmptyPositions(Point position) {
		for (Piece piece : this.pieces) {
			if (piece.getPosition().equals(position)) {
				return false;
			}
		}
		return true;
	}

	public List<Piece> getPieces() {
		return pieces;
	}
}

interface IsAttacking {
	
	public boolean ifAttacking(Point otherPositions, Board board);
}

class Piece {
	private final Color color;
	private final Point position;
	private final IsAttacking isAttaking;

	public Piece(Color color, Point position, IsAttacking isAttacking) {
		this.color = color;
		this.position = position;
		this.isAttaking = isAttacking;
	}

	public Color getColor() {
		return color;
	}

	public Point getPosition() {
		return this.position;
	}

	public IsAttacking getIsAttaking() {
		return isAttaking;
	}

}

enum Color {
	BLACK,
	WHITE;

	public boolean isWhite() {
		return this == WHITE;
	}

	public int direction() {
		return this == WHITE? 1 : -1; 
	}
}

