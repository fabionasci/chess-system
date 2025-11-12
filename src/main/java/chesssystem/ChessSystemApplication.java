package chesssystem;

import chesssystem.core.chess.ChessMatch;
import chesssystem.core.chess.ChessPiece;
import chesssystem.core.chess.ChessPosition;
import chesssystem.core.chess.exceptions.ChessException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ChessSystemApplication {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch match = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while (true) {
            try {
                UI.clearScreen();
                UI.printMatch(match, captured);
                System.out.println();
                System.out.println("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = match.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(match.getPieces(), possibleMoves);

                System.out.println();
                System.out.println("Target: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = match.performChessMove(source, target);
            } catch (ChessException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
    }
}
