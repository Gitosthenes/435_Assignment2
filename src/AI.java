import java.lang.Math;

public class AI {

    private int myColor;

    public AI(int myColor) {
        this.myColor = myColor;
    }

    //Returns array containing Q/P to place piece (index 0 & 1 respectively) and Q/D to rotate (2 & 3 respectively)
    public int[] findBestMove(Board board) {
        int bestVal = Integer.MIN_VALUE;
        int moveQuad = 0;
        int movePos = 0;
        int rotateQuad = -2;
        int rotateDir = 0;
        int[][][] quadrants = board.getQuadrants();

        for (int quadNum = 1; quadNum <= 4; quadNum++) { //for each quadrant
            for (int posNum = 1; posNum <= 9; posNum++) { //for each space of that quadrant
                if (board.isSpaceEmpty(quadNum, posNum)) { //if the space is empty
                    //Create copy of board, then make move
                    Board boardCopy = new Board(quadrants[0], quadrants[1], quadrants[2], quadrants[3]);
                    boardCopy.placePiece(myColor, quadNum, posNum);
                    //Now to rotate
                    if (boardCopy.checkWinner() == 0) { //Check for win before rotating
                        int [][][] copyQuadrants = boardCopy.getQuadrants();
                        for (int quad = 1; quad <= 4; quad++) { //for each quadrant
                            for (int dir = 0; dir <= 1; dir++) { // for each direction you can rotate that quadrant
                                Board boardCopyCopy = new Board(copyQuadrants[0], copyQuadrants[1], copyQuadrants[2], copyQuadrants[3]);
                                boardCopyCopy.rotateQuadrant(quad, dir == 0 ? "L" : "R");
                                //Evaluate move
                                int moveVal = miniMax(boardCopyCopy, 0, true);
                                //If move is better than best move so far, update move and rotation variables to return.
                                if (moveVal > bestVal) {
                                    bestVal = moveVal;
                                    moveQuad = quadNum;
                                    movePos = posNum;
                                    rotateQuad = quad;
                                    rotateDir = dir;
                                }
                            }
                        }
                    } else {
                        System.out.println(boardCopy.checkWinner());
                        moveQuad = quadNum;
                        movePos = posNum;
                        rotateQuad = -1;
                        rotateDir = 0;
                    }
                }
            }
        }
        return new int[]{moveQuad, movePos, rotateQuad, rotateDir};
    }

    private int miniMax(Board board, int currentDepth, boolean isMyTurn) {
        int bestScore;
        int scoreOfCurBoard = evaluate(board);

        //Check if current board is one where AI wins, loses, is a tie or has looked a certain number of moves ahead already.
        if (scoreOfCurBoard == 10 || scoreOfCurBoard == -10 || scoreOfCurBoard == -5 || currentDepth == 1) {
            return scoreOfCurBoard;
        }

        int[][][] quadrants = board.getQuadrants();
        bestScore = isMyTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int quadNum = 1; quadNum <= 4; quadNum++) {
            for (int posNum = 1; posNum <= 9; posNum++) {
                if (board.isSpaceEmpty(quadNum, posNum)) {
                    //Create copy of board, then make move
                    Board boardCopy = new Board(quadrants[0], quadrants[1], quadrants[2], quadrants[3]);
                    boardCopy.placePiece(myColor, quadNum, posNum);
                    //For each piece we can place that isn't a win, loss or tie, we can also rotate each quadrant 2 ways.
                    if (boardCopy.checkWinner() == 0) {
                        int [][][] copyQuadrants = boardCopy.getQuadrants();
                        for (int quad = 1; quad <= 4; quad++) {
                            for (int rot = 0; rot <= 1; rot++) {
                                Board boardCopyCopy = new Board(copyQuadrants[0], copyQuadrants[1], copyQuadrants[2], copyQuadrants[3]);
                                //Make rotation
                                boardCopyCopy.rotateQuadrant(quad, rot == 0 ? "L" : "R");
                                //Recursively call minimax to find max/min value.
                                bestScore = isMyTurn ?
                                        Math.max(bestScore, miniMax(boardCopyCopy, currentDepth+1, !isMyTurn)) :
                                        Math.min(bestScore, miniMax(boardCopyCopy, currentDepth+1, !isMyTurn));
                            }
                        }
                    } else {
                        return evaluate(boardCopy);
                    }
                }
            }
        }
        return bestScore;
    }

   private int evaluate(final Board board) {
        int winningColor = board.checkWinner();
        if (myColor == winningColor) { //AI wins
            return 10;
        } else if ((myColor * -1) == winningColor) { //User wins
            return -10;
        } else if (winningColor == 2 || board.isFull()) { //Tie game
            return -5;
        } else { //No winners yet.
            return 0;
        }
    }
}
