package chesssystem.core.chess.pieces;

import chesssystem.core.board.Board;
import chesssystem.core.chess.ChessPiece;
import chesssystem.core.chess.enums.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }


    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return mat;
    }
}
