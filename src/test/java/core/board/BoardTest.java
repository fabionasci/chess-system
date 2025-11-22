package core.board;

import core.board.exceptions.BoardException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    // Classe auxiliar concreta para testar operações com Piece
    static class TestPiece extends Piece {
        public TestPiece(Board board) {
            super(board);
        }

        @Override
        public boolean[][] possibleMoves() {
            return new boolean[getBoard().getRows()][getBoard().getColumns()];
        }
    }

    @Test
    public void constructor_InvalidRows_ThrowsBoardException() {
        assertThrows(BoardException.class, () -> new Board(0, 5));
        assertThrows(BoardException.class, () -> new Board(5, 0));
        assertThrows(BoardException.class, () -> new Board(-1, -1));
    }

    @Test
    public void positionExists_ByIndices_Works() {
        Board board = new Board(3, 3);
        assertTrue(board.positionExists(new Position(0, 0)));
        assertTrue(board.positionExists(new Position(2, 2)));
        assertFalse(board.positionExists(new Position(3, 0)));
        assertFalse(board.positionExists(new Position(0, 3)));
        assertFalse(board.positionExists(new Position(-1, 0)));
    }

    @Test
    public void piece_ByIndices_InvalidPosition_Throws() {
        Board board = new Board(3, 3);
        assertThrows(BoardException.class, () -> board.piece(-1, 0));
        assertThrows(BoardException.class, () -> board.piece(0, 3));
    }

    @Test
    public void piece_ByPosition_InvalidPosition_Throws() {
        Board board = new Board(3, 3);
        assertThrows(BoardException.class, () -> board.piece(new Position(-1, 0)));
        assertThrows(BoardException.class, () -> board.piece(new Position(0, 3)));
    }

    @Test
    public void placePiece_PlacesAndSetsPosition() {
        Board board = new Board(3, 3);
        TestPiece p = new TestPiece(board);
        Position pos = new Position(1, 1);

        board.placePiece(p, pos);

        assertSame(p, board.piece(pos));
        assertEquals(pos, p.position); // campo package-private usado no mesmo pacote
    }

    @Test
    public void placePiece_AlreadyOccupied_Throws() {
        Board board = new Board(3, 3);
        TestPiece p1 = new TestPiece(board);
        TestPiece p2 = new TestPiece(board);
        Position pos = new Position(1, 1);

        board.placePiece(p1, pos);
        assertThrows(BoardException.class, () -> board.placePiece(p2, pos));
    }

    @Test
    public void removePiece_InvalidPosition_Throws() {
        Board board = new Board(3, 3);
        assertThrows(BoardException.class, () -> board.removePiece(new Position(-1, 0)));
        assertThrows(BoardException.class, () -> board.removePiece(new Position(0, 3)));
    }

    @Test
    public void removePiece_WhenEmpty_ReturnsNull() {
        Board board = new Board(3, 3);
        Position pos = new Position(1, 1);
        assertNull(board.removePiece(pos));
    }

    @Test
    public void removePiece_RemovesAndClearsPosition() {
        Board board = new Board(3, 3);
        TestPiece p = new TestPiece(board);
        Position pos = new Position(1, 1);

        board.placePiece(p, pos);
        Piece removed = board.removePiece(pos);

        assertSame(p, removed);
        assertNull(board.piece(pos));
        assertNull(removed.position);
    }

    @Test
    public void thereIsAPiece_InvalidPosition_Throws() {
        Board board = new Board(3, 3);
        assertThrows(BoardException.class, () -> board.thereIsAPiece(new Position(-1, 0)));
    }

    @Test
    public void thereIsAPiece_ReturnsTrueOrFalse() {
        Board board = new Board(3, 3);
        Position pos = new Position(1, 1);
        assertFalse(board.thereIsAPiece(pos));

        TestPiece p = new TestPiece(board);
        board.placePiece(p, pos);
        assertTrue(board.thereIsAPiece(pos));
    }
}
