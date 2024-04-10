package client;

import chess.*;
import client.websocket.GameHandler;
import client.websocket.WebSocketFacade;
import model.GameData;
import model.result.ListGamesResult;
import server.ResponseException;
import server.ServerFacade;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.userCommands.Resign;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;
import static ui.EscapeSequences.*;

public class GameplayClient implements GameHandler {

    private final ServerFacade server;
    private GameData gameData;
    private final String authToken;
    private final String username;
    private final HashMap<String, Integer> lettersToNumbers = new HashMap<>();
    private final WebSocketFacade ws;

    public GameplayClient(String serverURL, String auth, GameData gameToDisplay, String user) throws ResponseException {
        gameData = gameToDisplay;
        authToken = auth;
        server = new ServerFacade(serverURL);
        username = user;
        populateLettersToNumbers();
        ws = new WebSocketFacade(serverURL, this);
        if(Objects.equals(gameData.blackUsername(), username)) {
            ws.joinPlayer(authToken, gameData.gameID(), BLACK, username);
        } else if(Objects.equals(gameData.whiteUsername(), username)) {
            ws.joinPlayer(authToken, gameData.gameID(), WHITE, username);
        } else {
            ws.joinObserver(authToken, gameData.gameID());
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "move" -> makeMove(params);
//                case "highlight" -> listGames();
                case "resign" -> resignGame();
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

    public String makeMove(String... params) throws ResponseException {
        if(params.length == 2) {
            String[] preMoveArray = params[0].split(",");
            String[] postMoveArray = params[1].split(",");
            ChessPosition startingPosition =
                    new ChessPosition(Integer.parseInt(preMoveArray[0]), lettersToNumbers.get(preMoveArray[1]));
            ChessPosition endingPosition =
                    new ChessPosition(Integer.parseInt(postMoveArray[0]), lettersToNumbers.get(postMoveArray[1]));
            ChessMove chessMove = new ChessMove(startingPosition, endingPosition, null);
            ws.makeMove(authToken, gameData.gameID(), chessMove);
            return "";
        }
        throw new ResponseException(400, "Expected: ROW,COLUMN(starting position) ROW,COLUMN(ending position)");
    }

    public String resignGame() throws ResponseException {
        ws.resignGame(authToken, gameData.gameID());
        return "";
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
                    - redraw
                    - move ROW,COLUMN(starting position) ROW,COLUMN(ending position)
                    - highlight
                    - resign
                    - leave
                    """;
    }

    private void populateLettersToNumbers() {
        lettersToNumbers.put("a", 1);
        lettersToNumbers.put("b", 2);
        lettersToNumbers.put("c", 3);
        lettersToNumbers.put("d", 4);
        lettersToNumbers.put("e", 5);
        lettersToNumbers.put("f", 6);
        lettersToNumbers.put("g", 7);
        lettersToNumbers.put("h", 8);
    }

    @Override
    public void updateGame(LoadGame loadGame) throws ResponseException {
        if(loadGame.getGame() == gameData.gameID()) {
            System.out.println(redraw());
            System.out.print("\n" + SET_TEXT_COLOR_YELLOW + "[GAMEPLAY]>>> " + SET_TEXT_COLOR_GREEN);
        }
    }

    @Override
    public void printMessage(String message) {
        System.out.println(SET_TEXT_COLOR_RED + message);
        System.out.print("\n" + SET_TEXT_COLOR_YELLOW + "[GAMEPLAY]>>> " + SET_TEXT_COLOR_GREEN);
    }
}
