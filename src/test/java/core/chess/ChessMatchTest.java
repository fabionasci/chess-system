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

    @Test
    public void enPassant_shouldSetVulnerability_whenPawnAdvancesTwoSquares() {
        // prepare: white pawn e2 -> e4 (two-square advance)
        match.performChessMove(new ChessPosition('e', 2), new ChessPosition('e', 4));

        // the pawn on e4 must be the enPassantVulnerable piece
        ChessPiece[][] pieces = match.getPieces();
        int row = 8 - 4; // 4
        int col = 'e' - 'a'; // 4
        ChessPiece pawnOnE4 = pieces[row][col];

        assertNotNull(match.getEnPassantVulnerable(), "enPassantVulnerable should not be null after two-square pawn move");
        assertSame(pawnOnE4, match.getEnPassantVulnerable(), "enPassantVulnerable must reference the pawn that moved two squares");
    }

    @Test
    public void enPassant_capture_shouldRemoveCapturedPawn_andUpdateBoard() {
        // set up: white pawn to e5 (two moves) so it can capture en passant
        match.performChessMove(new ChessPosition('e', 2), new ChessPosition('e', 4)); // white
        match.performChessMove(new ChessPosition('a', 7), new ChessPosition('a', 6)); // black dummy
        match.performChessMove(new ChessPosition('e', 4), new ChessPosition('e', 5)); // white pawn now on e5

        // black moves pawn two squares from d7 to d5 -> this pawn becomes enPassantVulnerable
        match.performChessMove(new ChessPosition('d', 7), new ChessPosition('d', 5)); // black

        // white performs en passant capture: pawn e5 -> d6 (should capture the pawn that was on d5)
        ChessPiece captured = match.performChessMove(new ChessPosition('e', 5), new ChessPosition('d', 6));

        // captured piece must be a black pawn
        assertNotNull(captured, "en passant capture must return the captured piece");
        assertTrue(captured instanceof core.chess.pieces.Pawn, "captured piece must be a Pawn");
        assertEquals(core.chess.enums.Color.BLACK, captured.getColor(), "captured pawn must be black");

        // board: square d5 (where the captured pawn was) must be empty
        ChessPiece[][] pieces = match.getPieces();
        int d5Row = 8 - 5; // 3
        int d5Col = 'd' - 'a'; // 3
        assertNull(pieces[d5Row][d5Col], "square d5 should be empty after en passant capture");

        // the capturing white pawn must be on d6
        int d6Row = 8 - 6; // 2
        int d6Col = 'd' - 'a'; // 3
        assertNotNull(pieces[d6Row][d6Col], "capturing pawn must be on d6 after en passant");
        assertTrue(pieces[d6Row][d6Col] instanceof core.chess.pieces.Pawn, "piece on d6 must be a Pawn");
        assertEquals(core.chess.enums.Color.WHITE, pieces[d6Row][d6Col].getColor(), "piece on d6 must be white");

        // enPassant vulnerability must be cleared after the capture
        assertNull(match.getEnPassantVulnerable(), "enPassantVulnerable must be null after en passant capture");
    }

    @Test
    public void promotion_shouldSetPromoted_andReplacePromotedPieceForAllTypes() throws Exception {
        // obter board e piecesOnTheBoard via reflexão
        java.lang.reflect.Field boardField = ChessMatch.class.getDeclaredField("board");
        boardField.setAccessible(true);
        core.board.Board board = (core.board.Board) boardField.get(match);

        java.lang.reflect.Field piecesField = ChessMatch.class.getDeclaredField("piecesOnTheBoard");
        piecesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.List<core.board.Piece> piecesOnTheBoard = (java.util.List<core.board.Piece>) piecesField.get(match);

        // limpar a8 (destino da promoção) e a7 (origem que vamos usar)
        core.board.Position a8 = new ChessPosition('a', 8).toPosition();
        core.board.Piece removedA8 = board.removePiece(a8);
        if (removedA8 != null) piecesOnTheBoard.remove(removedA8);

        core.board.Position a7 = new ChessPosition('a', 7).toPosition();
        core.board.Piece removedA7 = board.removePiece(a7);
        if (removedA7 != null) piecesOnTheBoard.remove(removedA7);

        // colocar um peão branco em a7
        core.chess.pieces.Pawn pawn = new core.chess.pieces.Pawn(board, core.chess.enums.Color.WHITE, match);
        board.placePiece(pawn, a7);
        piecesOnTheBoard.add(pawn);

        // executar movimento de promoção: a7 -> a8
        core.chess.ChessPiece captured = match.performChessMove(new ChessPosition('a', 7), new ChessPosition('a', 8));
        assertNull(captured, "movimento de promoção não deve capturar (no cenário preparado)");

        // após o performChessMove a implementação já substitui por Queen por padrão
        core.chess.ChessPiece promoted = match.getPromoted();
        assertNotNull(promoted, "promoted deve ser setado após o peão chegar na última linha");
        assertTrue(promoted instanceof core.chess.pieces.Queen, "por padrão a promoção é substituída por Queen");

        // verificar que no tabuleiro há uma Queen em a8
        core.board.Piece pieceOnA8 = board.piece(a8);
        assertTrue(pieceOnA8 instanceof core.chess.pieces.Queen, "deve haver uma Queen em a8 após promoção padrão");

        // agora testar replacePromotedPiece para todos os tipos válidos
        core.chess.ChessPiece replaced;

        replaced = match.replacePromotedPiece("B");
        assertTrue(replaced instanceof core.chess.pieces.Bishop, "replacePromotedPiece('B') deve criar um Bishop");
        assertTrue(board.piece(a8) instanceof core.chess.pieces.Bishop, "board deve conter Bishop em a8 após substituição");

        replaced = match.replacePromotedPiece("N");
        assertTrue(replaced instanceof core.chess.pieces.Knight, "replacePromotedPiece('N') deve criar um Knight");
        assertTrue(board.piece(a8) instanceof core.chess.pieces.Knight, "board deve conter Knight em a8 após substituição");

        replaced = match.replacePromotedPiece("R");
        assertTrue(replaced instanceof core.chess.pieces.Rook, "replacePromotedPiece('R') deve criar um Rook");
        assertTrue(board.piece(a8) instanceof core.chess.pieces.Rook, "board deve conter Rook em a8 após substituição");

        replaced = match.replacePromotedPiece("Q");
        assertTrue(replaced instanceof core.chess.pieces.Queen, "replacePromotedPiece('Q') deve criar uma Queen");
        assertTrue(board.piece(a8) instanceof core.chess.pieces.Queen, "board deve conter Queen em a8 após substituição");
    }


    private int countNonNullRow(ChessPiece[][] pieces, int row) {
        int count = 0;
        for (int c = 0; c < 8; c++) {
            if (pieces[row][c] != null) count++;
        }
        return count;
    }
}
