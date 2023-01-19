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
					this.board.addPiece(new Point(i, j), matrix.get(i).get(j), (byte) 1);
				}
				if (matrix.get(i + 8).get(j) != '0') {
					this.board.addPiece(new Point(i, j), matrix.get(i + 8).get(j), (byte) -1);
				}
			}
		}
	}

	public String haveCheck() {
		return isCheck() ? "hay jaque" : "No hay jaque";
	}

	private boolean isCheck() {

		for (Piece piece : this.board.getPieces()) {
			if (piece.getDirection() > 0) {
				if (piece.isAttacking(this.board.getPositionBlackKing(), this.board))
					return true;
			} else {
				if (piece.isAttacking(this.board.getPositionWhiteKing(), this.board))
					return true;
			}
		}
		return false;
	}

	class Board {
		private Point positionBlackKing;
		private Point positionWhiteKing;
		private final List<Piece> pieces;

		public Board() {
			this.pieces = new ArrayList<Piece>();
		}

		public void addPiece(Point position, char value, byte direction) {

			if (value == 'P') {
				this.pieces.add(new Pawn(direction, position));
			} else if (value == 'A') {
				this.pieces.add(new Bishop(direction, position));
			} else if (value == 'C') {
				this.pieces.add(new Knight(direction, position));
			} else if (value == 'T') {
				this.pieces.add(new Rook(direction, position));
			} else if (value == 'D') {
				this.pieces.add(new Queen(direction, position));
			} else if (value == 'R') {
				this.pieces.add(new King(direction, position));
				if (direction > 0) {
					this.positionWhiteKing = position;
				} else {
					this.positionBlackKing = position;
				}
			}

		}

		public boolean isEmptyPositions(Point position) {
			for (Piece piece : this.pieces) {
				if (piece.getPosition().equals(position)) {
					return false;
				}
			}
			return true;
		}

		public Point getPositionBlackKing() {
			return positionBlackKing;
		}

		public Point getPositionWhiteKing() {
			return positionWhiteKing;
		}

		public List<Piece> getPieces() {
			return pieces;
		}

	}

	abstract class Piece {
		byte direction;
		Point position;

		public Piece(byte direction, Point position) {
			this.direction = direction;
			this.position = position;
		}

		public byte getDirection() {
			return direction;
		}

		public Point getPosition() {
			return this.position;
		}

		public abstract boolean isAttacking(Point otherPositions, Board board);

	}

	class Pawn extends Piece {

		public Pawn(byte direction, Point position) {
			super(direction, position);
		}

		@Override
		public boolean isAttacking(Point otherPositions, Board board) {

			return this.position.getX() == otherPositions.getX() - this.direction
					&& Math.abs(this.position.getY() - otherPositions.getY()) == 1;
		}

	}

	class Knight extends Piece {

		public Knight(byte direction, Point position) {
			super(direction, position);
		}

		@Override
		public boolean isAttacking(Point otherPositions, Board board) {
			return (Math.abs(this.position.getX() - otherPositions.getX()) == 2
					&& Math.abs(this.position.getY() - otherPositions.getY()) == 1)
					|| (Math.abs(this.position.getX() - otherPositions.getX()) == 1
							&& Math.abs(this.position.getY() - otherPositions.getY()) == 2);
		}
	}

	class Bishop extends Piece {

		public Bishop(byte direction, Point position) {
			super(direction, position);
		}

		@Override
		public boolean isAttacking(Point otherPositions, Board board) {
			return isAttackingDirectly(false, this.position, otherPositions, board);
		}
	}

	class Rook extends Piece {

		public Rook(byte direction, Point position) {
			super(direction, position);
		}

		@Override
		public boolean isAttacking(Point otherPositions, Board board) {
			return isAttackingDirectly(true, this.position, otherPositions, board);
		}
	}

	class Queen extends Piece {

		public Queen(byte direction, Point position) {
			super(direction, position);
		}

		@Override
		public boolean isAttacking(Point otherPositions, Board board) {
			return isAttackingDirectly(true, this.position, otherPositions, board)
					|| isAttackingDirectly(false, this.position, otherPositions, board);
		}

	}

	class King extends Piece {

		public King(byte direction, Point position) {
			super(direction, position);
		}

		@Override
		public boolean isAttacking(Point otherPositions, Board board) {
			return Math.abs(this.position.getX() - otherPositions.getX()) <= 1
					&& Math.abs(this.position.getY() - otherPositions.getY()) <= 1;
		}
	}

	static public boolean isAttackingDirectly(boolean asRook, Point thisPosition, Point otherPositions, Board board) {
		int dx;
		int dy;
		Point road = new Point(thisPosition);
		if (asRook) {
			if (thisPosition.getX() == otherPositions.getX()) {
				dx = 0;
				dy = thisPosition.getY() < otherPositions.getY() ? 1 : -1;
			} else if (thisPosition.getY() == otherPositions.getY()) {
				dy = 0;
				dx = thisPosition.getX() < otherPositions.getX() ? 1 : -1;
			} else {
				return false;
			}

		} else {
			if (Math.abs(thisPosition.getX() - otherPositions.getX()) == Math
					.abs(thisPosition.getY() - otherPositions.getY())) {
				dy = thisPosition.getY() < otherPositions.getY() ? 1 : -1;
				dx = thisPosition.getX() < otherPositions.getX() ? 1 : -1;
			} else {
				return false;
			}
		}

		road.translate(dx, dy);
		while (!road.equals(otherPositions)) {
			if (!board.isEmptyPositions(road)) {
				return false;
			}
			road.translate(dx, dy);
		}
		return true;
	}
}
