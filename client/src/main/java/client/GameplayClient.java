package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.websocket.GameHandler;
import client.websocket.WebSocketFacade;
import model.GameData;
import model.result.ListGamesResult;
import server.ResponseException;
import server.ServerFacade;
import webSocketMessages.serverMessages.LoadGame;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class GameplayClient implements GameHandler {

    private final ServerFacade server;
    private GameData gameData;
    private final String authToken;
    private final String username;
    private final WebSocketFacade ws;

    public GameplayClient(String serverURL, String auth, GameData gameToDisplay, String user) throws ResponseException {
        gameData = gameToDisplay;
        authToken = auth;
        server = new ServerFacade(serverURL);
        username = user;
        ws = new WebSocketFacade(serverURL, this);
        if(Objects.equals(gameData.blackUsername(), username)) {
            ws.joinPlayer(authToken, gameData.gameID(), BLACK);
        } else if(Objects.equals(gameData.whiteUsername(), username)) {
            ws.joinPlayer(authToken, gameData.gameID(), WHITE);
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
//                case "highlight" -> listGames();
//                case "move" -> joinGame(params);
//                case "resign" -> observeGame(params);
                case "leave" -> "leave";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String redraw() throws ResponseException {
        ListGamesResult listedGames = server.listGames(authToken);
        var games = listedGames.games();
        for(GameData game: games) {
            if(game.gameID() == gameData.gameID()) {
                gameData = game;
                return "\n" + draw();
            }
        }
        throw new ResponseException(404, "Game not found");
    }

    private String draw() {
        if(Objects.equals(gameData.blackUsername(), username)) {
            return displayBlackGame();
        }
        return displayWhiteGame();
    }

    public String displayWhiteGame() {
        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();
        String returnString = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        boolean isBlack = true;

        for(int i = 1; i <= 8; i++) {
            String line = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + String.format(" %s ", i);
            for(int j = 1; j <= 8; j++) {
                line += alternateColors(isBlack);
                isBlack = !isBlack;
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if(piece == null) {
                    line += "   ";
                } else {
                    line += String.format(" %s ", returnCorrectPiece(piece));
                }
            }
            isBlack = !isBlack;
            line += SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + String.format(" %s ", i) + RESET_BG_COLOR + "\n";
            returnString = line + returnString;
        }
        returnString = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    a  b  c  d  e  f  g  h    " +
                RESET_BG_COLOR + "\n" + returnString;
        return returnString;
    }

    public String displayBlackGame() {
        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();
        String returnString = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";
        boolean isBlack = true;

        for(int i = 8; i >= 1; i--) {
            String line = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + String.format(" %s ", i);
            for(int j = 8; j >= 1; j--) {
                line += alternateColors(isBlack);
                isBlack = !isBlack;
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if(piece == null) {
                    line += "   ";
                } else {
                    line += String.format(" %s ", returnCorrectPiece(piece));
                }
            }
            isBlack = !isBlack;
            line += SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + String.format(" %s ", i) + RESET_BG_COLOR + "\n";
            returnString = line + returnString;
        }
        returnString = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "    h  g  f  e  d  c  b  a    " +
                RESET_BG_COLOR + "\n" + returnString;
        return returnString;
    }

    private String returnCorrectPiece (ChessPiece piece) {
        if(piece.getTeamColor() == WHITE) {
            return SET_TEXT_COLOR_RED + pieceToLetter(piece);
        } else if(piece.getTeamColor() == BLACK) {
            return SET_TEXT_COLOR_BLUE + pieceToLetter(piece);
        }
        else return "   ";
    }

    private String pieceToLetter (ChessPiece piece) {
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return "P";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            return "R";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            return "N";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            return "B";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KING) {
            return "K";
        }
        if(piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            return "Q";
        }
        return "?";
    }

    private String alternateColors (boolean isBlack) {
        if(isBlack) {
            return SET_BG_COLOR_BLACK;
        } else {
            return SET_BG_COLOR_WHITE;
        }
    }

    public String help() {
        return """
                    - create <GAME NAME>
                    - list
                    - join <ID> [WHITE | BLACK]
                    - observe <ID>
                    - logout
                    """;
    }

    @Override
    public void updateGame(LoadGame loadGame) throws ResponseException {
        if(loadGame.getGame() == gameData.gameID()) {
            System.out.println(redraw());
        }
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }
}
