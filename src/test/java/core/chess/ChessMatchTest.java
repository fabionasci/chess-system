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
        // opcional: verificar mensagem específica
        // assertEquals("There is no piece on source position", ex.getMessage());
    }


    private int countNonNullRow(ChessPiece[][] pieces, int row) {
        int count = 0;
        for (int c = 0; c < 8; c++) {
            if (pieces[row][c] != null) count++;
        }
        return count;
    }
}
