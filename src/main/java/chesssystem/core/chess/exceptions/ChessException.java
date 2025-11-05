package chesssystem.core.chess.exceptions;

import chesssystem.core.board.exceptions.BoardException;

public class ChessException extends BoardException {

    private static final long serialVersionUID = 1L;

    public ChessException(String message) {
        super(message);
    }
}
