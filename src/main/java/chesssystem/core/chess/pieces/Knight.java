package chesssystem.core.chess.pieces;

import chesssystem.core.board.Board;
import chesssystem.core.board.Position;
import chesssystem.core.chess.ChessPiece;
import chesssystem.core.chess.enums.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }


    @Override
    public String toString() {
        return "N";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        int[][] moves = {
                {-2, -1}, {-2, +1},
                {-1, -2}, {-1, +2},
                {+1, -2}, {+1, +2},
                {+2, -1}, {+2, +1}
        };

        for (int[] move : moves) {
            p.setValues(position.getRow() + move[0], position.getColumn() + move[1]);
            if (getBoard().positionExists(p) && canMove(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        return mat;
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }
}
