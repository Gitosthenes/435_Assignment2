import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/* TODO:
    - Check if piece is already in place
    - Retry inputs if failed instead of exceptions.
*/


public class Game {

    private static int player1;
    private static int player2;
    private static int currentPlayer;
    private static Board board;
    private static int turnNum = 1;

    public static void main(String[] args) throws InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Would you like to go first? Y/N/R (R for random): ");
        String moveChoice = scan.nextLine();
        System.out.print("Would you prefer to be White or Black? W/B/R (R for random): ");
        String colorChoice = scan.nextLine();
        System.out.print("Setting up your game");

        setupGame(moveChoice, colorChoice);
        for (int i = 0; i < 3; i++) {
            TimeUnit.MILLISECONDS.sleep(50);
            System.out.print(".");
            if (i == 2) {
                System.out.print("\n");
            }
        }

        //GAME LOOP
        while (!board.isFull()) {
            System.out.println("Turn " + turnNum + ": Player " + getCurrentPlayer() + "(" + getPlayerColor(getCurrentPlayer()) + ").");
            board.printBoard();

            //Current player places piece
            System.out.print("Place your piece where? (Quadrant/Location): ");
            int[] move = Arrays.stream(scan.nextLine().split("/")).mapToInt(Integer::parseInt).toArray();
            if (!isLegalMove(move[0], move[1])) {
                throw new IllegalArgumentException("Whoops, invalid space!");
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
            if (!isLegalRotation(Integer.parseInt(rotate[0]), rotate[1])) {
                throw new IllegalArgumentException("Whoops, invalid rotation!");
            }
            board.rotateQuadrant(rotate[1], Integer.parseInt(rotate[0]));
            board.printBoard();

            //check for winner
            if (board.checkWinner() != 0){
                endGame(board.checkWinner());
            }

            //Move to other player's turn
            System.out.println("Changing Player.....\n\n\n");
            turnNum++;
            changePlayer();
            clearScreen();

        }
    }

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
    }

    private static boolean isLegalMove(int quad, int pos) {
        return (quad >= 1 && quad <= 4) && (pos >= 1 && pos <= 9);
    }

    private static boolean isLegalRotation(int quad, final String dir) {
        return (quad >= 1 && quad <= 4) && ("L".equals(dir.toUpperCase()) || "R".equals(dir.toUpperCase()));
    }

    private static void setupGame(final String moveChoice, final String colorChoice) {
        if ((!("Y".equals(moveChoice.toUpperCase()))
                &&!("N".equals(moveChoice.toUpperCase()))
                &&!("R".equals(moveChoice.toUpperCase())))
          ||(!("W".equals(colorChoice.toUpperCase()))
                &&!("B".equals(colorChoice.toUpperCase()))
                &&!("R".equals(colorChoice.toUpperCase())))) {

            throw new IllegalArgumentException("Sorry, you must have mis-keyed!");
        } else {
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
    }

    private static void setupGame(int colorChoice, int moveChoice) {
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
    }

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

    private static int getCurrentPlayer() {
        return (currentPlayer == player1) ? 1 : 2;
    }

    private static void changePlayer() {
        currentPlayer *= -1;
    }

    //https://stackoverflow.com/questions/2979383/java-clear-the-console
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
