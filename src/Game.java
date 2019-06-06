import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {

    /* Global Variables */
    private static int player1;
    private static int player2;
    private static int currentPlayer;
    private static Board board;
    private static AI opponent;
    private static int turnNum = 1;
    private static Scanner scan;

    /**
     * Main method and controller for the program. Gets initial input from user,
     * sets up game and runs game loop.
     */
    public static void main(String[] args) throws InterruptedException {
        scan = new Scanner(System.in);
        //Get move and color preference from user.
        System.out.print("Would you like to go first? Y/N/R (R for random): ");
        String moveChoice = scan.nextLine();
        while (!("Y".equals(moveChoice.toUpperCase()))
                &&!("N".equals(moveChoice.toUpperCase()))
                &&!("R".equals(moveChoice.toUpperCase()))) {
            System.out.print("Sorry, I didn't catch that...\nWould you like to go first? Y/N/R (R for random): ");
            moveChoice = scan.nextLine();
        }
        System.out.print("Would you prefer to be White or Black? W/B/R (R for random): ");
        String colorChoice = scan.nextLine();
        while (!("W".equals(colorChoice.toUpperCase()))
                &&!("B".equals(colorChoice.toUpperCase()))
                &&!("R".equals(colorChoice.toUpperCase()))) {
            System.out.print("Sorry, I didn't catch that...\nWould you prefer to be White or Black? W/B/R (R for random): ");
            colorChoice = scan.nextLine();
        }
        System.out.print("Setting up your game");
        //Setup initial game state.
        setupGame(moveChoice, colorChoice);
        for (int i = 0; i < 5; i++) {
            TimeUnit.MILLISECONDS.sleep(250);
            System.out.print(".");
            if (i == 4) {
                System.out.print("\n");
            }
        }

        //GAME LOOP
        boolean firstTurn = true;
        while (!board.isFull()) {
            System.out.println("Turn " + turnNum + ": Player " + getCurrentPlayer() + "(" + getPlayerColor(getCurrentPlayer()) + ").");
            if (firstTurn) {
                board.printBoard();
                firstTurn = false;
            }
            //Decide who's turn it is.
            if (getCurrentPlayer() == 1) { //If currently player1's turn (user)
                userTurn();
            } else {    //else it's player2's turn (A.I.)
                aiTurn();
            }
            //Move to other player's turn
            System.out.println("Changing Player.....\n\n\n");
            turnNum++;
            currentPlayer *= -1;
        }
        endGame(2); //board is full before someone wins.
    }

    /**
     * Gets user input and makes move for human player.
     */
    private static void userTurn() {
        //User selects where to place piece.
        System.out.print("Place your piece where? (Quadrant/Location): ");
        int[] move = Arrays.stream(scan.nextLine().split("/")).mapToInt(Integer::parseInt).toArray();
        while (!isLegalMove(move[0], move[1]) || !board.isSpaceEmpty(move[0], move[1])) {
            System.out.print("INVALID SPACE. Place your piece where? (Quadrant/Location): ");
            move = Arrays.stream(scan.nextLine().split("/")).mapToInt(Integer::parseInt).toArray();
        }
        board.placePiece(currentPlayer, move[0], move[1]);
        board.printBoard();

        //check for winner
        if (board.checkWinner() != 0){
            endGame(board.checkWinner());
        }

        //Current player rotates board
        System.out.print("Which quadrant(1-4) to rotate and which way(L/R)? (Quadrant/Direction): ");
        String[] rotate = scan.nextLine().split("/");
        while (!isLegalRotation(Integer.parseInt(rotate[0]), rotate[1])) {
            System.out.print("INVALID ROTATION. Which quadrant(1-4) to rotate and which way(L/R)? (Quadrant/Direction): ");
            rotate = scan.nextLine().split("/");
        }
        board.rotateQuadrant(Integer.parseInt(rotate[0]), rotate[1]);
        board.printBoard();

        //check for winner
        if (board.checkWinner() != 0){
            endGame(board.checkWinner());
        }
    }

    /**
     * Makes move for AI.
     */
    private static void aiTurn() throws InterruptedException {
        System.out.println("Your opponent is thinking...");

        //Get AI's best move and rotation
        int[] aiMove = opponent.findBestMove(board);

        //AI makes move
        board.placePiece(player2, aiMove[0], aiMove[1]);
        board.printBoard();

        //Check for win
        if (board.checkWinner() != 0) {
            endGame(board.checkWinner());
        }

        //AI rotates board
        System.out.print("Your opponent is rotating");
        for (int i = 0; i < 3; i++) {
            TimeUnit.MILLISECONDS.sleep(300);
            System.out.print(".");
            if (i == 2) {
                System.out.print("\n");
            }
        }
        board.rotateQuadrant(aiMove[2], aiMove[3]==0 ? "L" : "R");
        board.printBoard();

        //Check for win
        if (board.checkWinner() != 0) {
            endGame(board.checkWinner());
        }
    }

    /**
     * If board state is shown to have a winner or tie, this method conveys that to the
     * user and exits the program.
     */
    private static void endGame(int winner) {
        String winningColor;
        switch (winner) {
            case -1:
                winningColor = "B";
                break;
            case 1:
                winningColor = "W";
                break;
            case 2:
                winningColor = "T";
                break;
            default:
                throw new IllegalArgumentException("Invalid Winner: "+ winner);
        }
        if (!"T".equals(winningColor)) {
            if (getPlayerColor(1).equals(winningColor)) {
                System.out.println("You Win!");
            } else {
                System.out.println("You Lose...");
            }
        } else {
            System.out.println("It's a Tie!");
        }
        System.exit(0);
    }

    /**
     * Checks whether the given quadrant and position is within the board.
     */
    private static boolean isLegalMove(int quad, int pos) {
        return (quad >= 1 && quad <= 4) && (pos >= 1 && pos <= 9);
    }

    /**
     * Checks whether the given quadrant and direction are legal within the context of
     * Pentago rules.
     */
    private static boolean isLegalRotation(int quad, final String dir) {
        return (quad >= 1 && quad <= 4) && ("L".equals(dir.toUpperCase()) || "R".equals(dir.toUpperCase()));
    }

    /**
     * Converts the move choice and color choice made by the user form letters to numbers
     * to be used when setting up the game.
     */
    private static void setupGame(final String moveChoice, final String colorChoice) {
        int move = 0;
        int color = 0;

        if ("Y".equals(moveChoice.toUpperCase())) {
            move = 1;
        } else if ("N".equals(moveChoice.toUpperCase())) {
            move = 2;
        }
        if ("W".equals(colorChoice.toUpperCase())) {
            color = 1;
        } else if ("B".equals(colorChoice.toUpperCase())) {
            color = -1;
        }
        setupGame(move,color);
    }

    /**
     * Initializes the game board, first player, and AI.
     */
    private static void setupGame(int moveChoice, int colorChoice) {
        int[] list = new int[]{-1, 1};
        Random rand = new Random();
        //Set player colors (user is always player 1)
        player1 = (colorChoice != 0) ? colorChoice : list[rand.nextInt(list.length)];
        player2 = player1 * -1;
        //Set who goes first
        if (moveChoice == 1) {
            currentPlayer = player1;
        } else if (moveChoice == 2) {
            currentPlayer = player2;
        } else {
            currentPlayer = list[rand.nextInt(list.length)];
        }
        //Initialize game board
        board = new Board();
        //Initialize AI opponent.
        opponent = new AI(player2);
    }

    /**
     * Returns the color of the given player.
     */
    private static String getPlayerColor(int playerNum) {
        int player;
        if (playerNum == 1) {
            player = player1;
        } else if (playerNum == 2) {
            player = player2;
        } else {
            throw new IllegalArgumentException("Invalid player number: " + playerNum);
        }
        if (player == -1) {
            return "B";
        } else {
            return "W";
        }
    }

    /**
     * Returns the number of the current player (1 or 2).
     */
    private static int getCurrentPlayer() {
        return (currentPlayer == player1) ? 1 : 2;
    }
}
