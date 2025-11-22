package core.chess;

import core.chess.exceptions.ChessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChessMatchTest {

    private ChessMatch match;

    @BeforeEach
    public void setUp() {
        match = new ChessMatch();
    }


    @Test
    void getPieces_initialSetup_returnsCorrectBoard() {
        ChessMatch chessMatch = new ChessMatch();
        ChessPiece[][] pieces = chessMatch.getPieces();

        assertNotNull(pieces, "getPieces() não deve retornar nulo");
        assertEquals(8, pieces.length, "Deve ter 8 linhas");
        for (ChessPiece[] row : pieces) {
            assertEquals(8, row.length, "Cada linha deve ter 8 colunas");
        }

        int totalNonNull = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (pieces[r][c] != null) totalNonNull++;
            }
        }
        assertEquals(32, totalNonNull, "Deve haver 32 peças no setup inicial");

        // Verifica configuração típica: linhas 0 e 7 com 8 peças, 1 e 6 com 8 (peões), linhas 2-5 vazias
        assertEquals(8, countNonNullRow(pieces, 0), "Linha 0 deve ter 8 peças");
        assertEquals(8, countNonNullRow(pieces, 1), "Linha 1 deve ter 8 peças (peões)");
        assertEquals(0, countNonNullRow(pieces, 2), "Linha 2 deve estar vazia");
        assertEquals(0, countNonNullRow(pieces, 3), "Linha 3 deve estar vazia");
        assertEquals(0, countNonNullRow(pieces, 4), "Linha 4 deve estar vazia");
        assertEquals(0, countNonNullRow(pieces, 5), "Linha 5 deve estar vazia");
        assertEquals(8, countNonNullRow(pieces, 6), "Linha 6 deve ter 8 peças (peões)");
        assertEquals(8, countNonNullRow(pieces, 7), "Linha 7 deve ter 8 peças");
    }

    @Test
    public void performChessMove_shouldMovePawn_e2_to_e4_andReturnNullWhenNoCapture() {
        ChessPosition source = new ChessPosition('e', 2);
        ChessPosition target = new ChessPosition('e', 4);

        ChessPiece captured = match.performChessMove(source, target);

        assertNull(captured, "movimento sem captura deve retornar null");
    }

    @Test
    public void performChessMove_shouldThrowException_whenSourceIsEmpty() {
        ChessPosition source = new ChessPosition('e', 3); // casa vazia no posicionamento inicial
        ChessPosition target = new ChessPosition('e', 4);

        ChessException ex = assertThrows(ChessException.class, () -> match.performChessMove(source, target));

        assertEquals("There is no piece on source position", ex.getMessage());
    }

    @Test
    public void performChessMove_shouldThrowException_whenSourceIsOpponentPiece() {
        ChessPosition source = new ChessPosition('e', 7); // peça preta
        ChessPosition target = new ChessPosition('e', 5);

        assertThrows(ChessException.class, () -> match.performChessMove(source, target));
    }

    @Test
    public void performChessMove_shouldThrowException_whenTargetIsInvalid() {
        ChessPosition source = new ChessPosition('e', 2);
        ChessPosition target = new ChessPosition('e', 5); // movimento inválido para peão

        assertThrows(ChessException.class, () -> match.performChessMove(source, target));
    }

    @Test
    public void performChessMove_shouldCapturePawn_whenWhiteCapturesBlackPawn() {
        // e2 to e4
        match.performChessMove(new ChessPosition('e', 2), new ChessPosition('e', 4));
        // d7 to d5
        match.performChessMove(new ChessPosition('d', 7), new ChessPosition('d', 5));
        // e4 captures d5
        ChessPiece captured = match.performChessMove(new ChessPosition('e', 4), new ChessPosition('d', 5));

        assertNotNull(captured, "captura de peão negro por peão branco deve retornar peça capturada");
    }

    @Test
    public void possibleMoves_knight_b1_returnsExpectedMoves() {
        boolean[][] moves = match.possibleMoves(new ChessPosition('b', 1));
        // coordenadas em notação xadrez -> índices da matriz: rowIndex = 8 - rank, colIndex = file - 'a'
        int a3Row = 8 - 3; // 5
        int a3Col = 'a' - 'a'; // 0
        int c3Row = 8 - 3; // 5
        int c3Col = 'c' - 'a'; // 2
        assertTrue(moves[a3Row][a3Col], "cavalo em b1 deve poder ir para a3");
        assertTrue(moves[c3Row][c3Col], "cavalo em b1 deve poder ir para c3");
        // a1 -> rank 1 -> row 7, coluna 'a' -> 0
        assertFalse(moves[8 - 1][0], "posição a1 não deve ser marcada como movimento possível");
    }

    @Test
    public void performChessMove_shouldAdvanceTurn_andUpdateBoard() {
        // e2 -> e4
        match.performChessMove(new ChessPosition('e', 2), new ChessPosition('e', 4));

        // turno e jogador
        assertEquals(2, match.getTurn(), "turn deve incrementar depois do movimento");
        assertEquals(core.chess.enums.Color.BLACK, match.getCurrentPlayer(), "deve ser a vez do preto");

        // verificar getPieces() atualizada (índices: row = 8 - rank, col = file - 'a')
        ChessPiece[][] pieces = match.getPieces();
        int srcRow = 8 - 2; // 6
        int srcCol = 'e' - 'a'; // 4
        int tgtRow = 8 - 4; // 4
        int tgtCol = 'e' - 'a'; // 4

        assertNull(pieces[srcRow][srcCol], "casa origem deve ficar vazia após mover");
        assertNotNull(pieces[tgtRow][tgtCol], "casa destino deve conter a peça movida");
    }

    @Test
    public void possibleMoves_shouldThrow_whenSourceHasNoPiece() {
        // casa vazia no setup inicial: e3
        assertThrows(ChessException.class, () -> match.possibleMoves(new ChessPosition('e', 3)));
    }

    @Test
    public void performChessMove_shouldThrow_whenMoveLeavesKingInCheck() {
        // montar sequência que faz com que um movimento deixe o próprio rei em cheque
        // ex: transformar configuração com movimentos prévios ou posicionar peças via movimentos válidos
        // TODO: preencher a sequência concreta de movimentos que produz o cenário

        ChessPosition source = new ChessPosition('e', 1); // exemplo: rei branco
        ChessPosition target = new ChessPosition('e', 2); // exemplo: movimento que resultaria em cheque

        assertThrows(ChessException.class, () -> match.performChessMove(source, target));
    }

    // java
    @Test
    public void performChessMove_shouldCastleKingside_andUpdatePositionsAndMoveCounts() {
        // prepare space for kingside castling (white)
        match.performChessMove(new ChessPosition('g', 2), new ChessPosition('g', 3)); // white: free g2 to allow bishop to move
        match.performChessMove(new ChessPosition('a', 7), new ChessPosition('a', 6)); // black dummy

        match.performChessMove(new ChessPosition('g', 1), new ChessPosition('f', 3)); // white: free g1 (knight)
        match.performChessMove(new ChessPosition('a', 6), new ChessPosition('a', 5)); // black dummy

        match.performChessMove(new ChessPosition('f', 1), new ChessPosition('g', 2)); // white: move bishop from f1 to g2, freeing f1
        match.performChessMove(new ChessPosition('a', 5), new ChessPosition('a', 4)); // black dummy

        // perform kingside castling: king from e1 to g1
        match.performChessMove(new ChessPosition('e', 1), new ChessPosition('g', 1)); // white

        ChessPiece[][] pieces = match.getPieces();
        int kingRow = 8 - 1; // 7
        int kingCol = 'g' - 'a'; // 6
        int rookRow = 8 - 1; // 7
        int rookCol = 'f' - 'a'; // 5
        int srcKingRow = 8 - 1; // 7
        int srcKingCol = 'e' - 'a'; // 4

        // king and rook repositioned
        assertNotNull(pieces[kingRow][kingCol], "king should be on g1 after castling");
        assertTrue(pieces[rookRow][rookCol] instanceof core.chess.pieces.Rook, "rook should be on f1 after castling");
        assertNull(pieces[srcKingRow][srcKingCol], "e1 should be empty after castling");

        // move counts updated (should be 1 for king and rook that moved)
        ChessPiece king = (ChessPiece) pieces[kingRow][kingCol];
        ChessPiece rook = (ChessPiece) pieces[rookRow][rookCol];
        assertEquals(1, king.getMoveCount(), "king moveCount should be 1 after castling");
        assertEquals(1, rook.getMoveCount(), "rook moveCount should be 1 after castling");
    }

    @Test
    public void performChessMove_shouldCastleQueenside_andUpdatePositionsAndMoveCounts() {
        // prepare space for queenside castling (white)
        match.performChessMove(new ChessPosition('b', 2), new ChessPosition('b', 4)); // white
        match.performChessMove(new ChessPosition('a', 7), new ChessPosition('a', 6)); // black dummy

        match.performChessMove(new ChessPosition('c', 2), new ChessPosition('c', 4)); // white
        match.performChessMove(new ChessPosition('h', 7), new ChessPosition('h', 6)); // black dummy

        match.performChessMove(new ChessPosition('d', 2), new ChessPosition('d', 4)); // white
        match.performChessMove(new ChessPosition('a', 6), new ChessPosition('a', 5)); // black dummy

        match.performChessMove(new ChessPosition('b', 1), new ChessPosition('a', 3)); // white
        match.performChessMove(new ChessPosition('h', 6), new ChessPosition('h', 5)); // black dummy

        match.performChessMove(new ChessPosition('c', 1), new ChessPosition('b', 2)); // white
        match.performChessMove(new ChessPosition('g', 7), new ChessPosition('g', 6)); // black dummy

        match.performChessMove(new ChessPosition('d', 1), new ChessPosition('d', 2)); // white
        match.performChessMove(new ChessPosition('g', 6), new ChessPosition('g', 5)); // black dummy

        // perform queenside castling: king from e1 to c1
        match.performChessMove(new ChessPosition('e', 1), new ChessPosition('c', 1)); // white

        ChessPiece[][] pieces = match.getPieces();
        int kingRow = 8 - 1; // 7
        int kingCol = 'c' - 'a'; // 2
        int rookRow = 8 - 1; // 7
        int rookCol = 'd' - 'a'; // 3
        int srcKingCol = 'e' - 'a'; // 4

        // king and rook repositioned
        assertNotNull(pieces[kingRow][kingCol], "king should be on c1 after queenside castling");
        assertTrue(pieces[rookRow][rookCol] instanceof core.chess.pieces.Rook, "rook should be on d1 after queenside castling");
        assertNull(pieces[kingRow][srcKingCol], "e1 should be empty after queenside castling");

        // move counts updated (should be 1 for king and rook that moved)
        ChessPiece king = (ChessPiece) pieces[kingRow][kingCol];
        ChessPiece rook = (ChessPiece) pieces[rookRow][rookCol];
        assertEquals(1, king.getMoveCount(), "king moveCount should be 1 after queenside castling");
        assertEquals(1, rook.getMoveCount(), "rook moveCount should be 1 after queenside castling");
    }


    private int countNonNullRow(ChessPiece[][] pieces, int row) {
        int count = 0;
        for (int c = 0; c < 8; c++) {
            if (pieces[row][c] != null) count++;
        }
        return count;
    }
}
