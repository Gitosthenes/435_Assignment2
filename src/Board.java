import java.util.ArrayList;
import java.util.List;

class Board {

    /* Global Variables */
    private int[][] quadrant1;
    private int[][] quadrant2;
    private int[][] quadrant3;
    private int[][] quadrant4;
    private int[][] fullBoard;

    /**
     * Constructor for a new, blank board.
     */
    Board() {
        fullBoard = new int[6][6];
        initializeGameBoard();
    }

    /**
     * Constructor for creating a copy of a board given its quadrants.
     */
    Board(int[][] quad1, int[][] quad2, int[][] quad3, int[][] quad4) {
        quadrant1 = deepCopy(quad1);
        quadrant2 = deepCopy(quad2);
        quadrant3 = deepCopy(quad3);
        quadrant4 = deepCopy(quad4);
        fullBoard = new int[6][6];
        assembleFullBoard();
    }

    /**
     * Updates the board to reflect the move made by the specified player in the specified quadrant/position.
     */
    void placePiece(int player, int quadrant, int pos) {
        int[][] changedQuadrant = new int[0][0];
        int x = getPositionCoordinates(pos)[0];
        int y = getPositionCoordinates(pos)[1];
        switch (quadrant) {
            case 1:
                changedQuadrant = quadrant1;
                break;
            case 2:
                changedQuadrant = quadrant2;
                break;
            case 3:
                changedQuadrant = quadrant3;
                break;
            case 4:
                changedQuadrant = quadrant4;
                break;
        }
        if (player == -1 || player == 1) {
            changedQuadrant[x][y] =  player;
        } else {
            throw new IllegalArgumentException("Invalid player: " + player);
        }
        assembleFullBoard();
    }

    /**
     * Updates the board to reflect rotation of specified quadrant in specified direction.
     */
    void rotateQuadrant(final int quadrantNum, final String direction) {
        if ("R".equals(direction.toUpperCase())) {
            if (quadrantNum >= 1 && quadrantNum <= 4) {
                switch (quadrantNum) {
                    case 1:
                        quadrant1 = rotateCW(quadrant1);
                        break;
                    case 2:
                        quadrant2 = rotateCW(quadrant2);
                        break;
                    case 3:
                        quadrant3 = rotateCW(quadrant3);
                        break;
                    case 4:
                        quadrant4 = rotateCW(quadrant4);
                        break;
                }
            } else {
                throw new IllegalArgumentException("Invalid Quadrant Number: " + quadrantNum);
            }
        } else if ("L".equals(direction.toUpperCase())) {
            if (quadrantNum >= 1 && quadrantNum <= 4) {
                switch (quadrantNum) {
                    case 1:
                        quadrant1 = rotateCCW(quadrant1);
                        break;
                    case 2:
                        quadrant2 = rotateCCW(quadrant2);
                        break;
                    case 3:
                        quadrant3 = rotateCCW(quadrant3);
                        break;
                    case 4:
                        quadrant4 = rotateCCW(quadrant4);
                        break;
                }
            } else {
                throw new IllegalArgumentException("Invalid Quadrant Number: " + quadrantNum);
            }
        } else {
            throw new IllegalArgumentException("Invalid Direction: " + direction);
        }
        assembleFullBoard();
    }

    /**
     * Prints the board to the console.
     */
    void printBoard() {
        String topBot = "+-------+-------+";
        System.out.println(topBot);
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int num : quadrant1[i]) {
                if (num == -1) {
                    System.out.print("B ");
                } else if (num == 1) {
                    System.out.print("W ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.print("|");
            for (int num : quadrant2[i]) {
                if (num == -1) {
                    System.out.print(" B");
                } else if (num == 1) {
                    System.out.print(" W");
                } else {
                    System.out.print(" .");
                }
            }
            System.out.print(" |\n");
        }
        System.out.println(topBot);
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int num : quadrant3[i]) {
                if (num == -1) {
                    System.out.print("B ");
                } else if (num == 1) {
                    System.out.print("W ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.print("|");
            for (int num : quadrant4[i]) {
                if (num == -1) {
                    System.out.print(" B");
                } else if (num == 1) {
                    System.out.print(" W");
                } else {
                    System.out.print(" .");
                }
            }
            System.out.print(" |\n");
        }
        System.out.println(topBot);
    }

    /**
     * Checks all 28 possible win conditions for either player. Also checks for tie games if both players have a win.
     */
    int checkWinner() {
        int winner = 0;
        List<Integer> winPossibilities = new ArrayList<>();

        /* Horizontal win check */
        //Possible 1st row horizontal win
        winPossibilities.add(allEqual(fullBoard[0][0], fullBoard[0][1], fullBoard[0][2], fullBoard[0][3], fullBoard[0][4]) ? fullBoard[0][1] : 0);
        winPossibilities.add(allEqual(fullBoard[0][1], fullBoard[0][2], fullBoard[0][3], fullBoard[0][4], fullBoard[0][5]) ? fullBoard[0][1] : 0);
        //Possible 2nd row horizontal win
        winPossibilities.add(allEqual(fullBoard[1][0], fullBoard[1][1], fullBoard[1][2], fullBoard[1][3], fullBoard[1][4]) ? fullBoard[1][1] : 0);
        winPossibilities.add(allEqual(fullBoard[1][1], fullBoard[1][2], fullBoard[1][3], fullBoard[1][4], fullBoard[1][5]) ? fullBoard[1][1] : 0);
        //Possible 3rd row horizontal win
        winPossibilities.add(allEqual(fullBoard[2][0], fullBoard[2][1], fullBoard[2][2], fullBoard[2][3], fullBoard[2][4]) ? fullBoard[2][1] : 0);
        winPossibilities.add(allEqual(fullBoard[2][1], fullBoard[2][2], fullBoard[2][3], fullBoard[2][4], fullBoard[2][5]) ? fullBoard[2][1] : 0);
        //Possible 4th row horizontal win
        winPossibilities.add(allEqual(fullBoard[3][0], fullBoard[3][1], fullBoard[3][2], fullBoard[3][3], fullBoard[3][4]) ? fullBoard[3][1] : 0);
        winPossibilities.add(allEqual(fullBoard[3][1], fullBoard[3][2], fullBoard[3][3], fullBoard[3][4], fullBoard[3][5]) ? fullBoard[3][1] : 0);
        //Possible 5th row horizontal win
        winPossibilities.add(allEqual(fullBoard[4][0], fullBoard[4][1], fullBoard[4][2], fullBoard[4][3], fullBoard[4][4]) ? fullBoard[4][1] : 0);
        winPossibilities.add(allEqual(fullBoard[4][1], fullBoard[4][2], fullBoard[4][3], fullBoard[4][4], fullBoard[4][5]) ? fullBoard[4][1] : 0);
        //Possible 6th row horizontal win
        winPossibilities.add(allEqual(fullBoard[5][0], fullBoard[5][1], fullBoard[5][2], fullBoard[5][3], fullBoard[5][4]) ? fullBoard[5][1] : 0);
        winPossibilities.add(allEqual(fullBoard[5][1], fullBoard[5][2], fullBoard[5][3], fullBoard[5][4], fullBoard[5][5]) ? fullBoard[5][1] : 0);

        /* Vertical win check */
        //Possible 1st column win
        winPossibilities.add(allEqual(fullBoard[0][0], fullBoard[1][0], fullBoard[2][0], fullBoard[3][0], fullBoard[4][0]) ? fullBoard[1][0] : 0);
        winPossibilities.add(allEqual(fullBoard[1][0], fullBoard[2][0], fullBoard[3][0], fullBoard[4][0], fullBoard[5][0]) ? fullBoard[1][0] : 0);
        //Possible 2nd column win
        winPossibilities.add(allEqual(fullBoard[0][1], fullBoard[1][1], fullBoard[2][1], fullBoard[3][1], fullBoard[4][1]) ? fullBoard[1][1] : 0);
        winPossibilities.add(allEqual(fullBoard[1][1], fullBoard[2][1], fullBoard[3][1], fullBoard[4][1], fullBoard[5][1]) ? fullBoard[1][1] : 0);
        //Possible 3rd column win
        winPossibilities.add(allEqual(fullBoard[0][2], fullBoard[1][2], fullBoard[2][2], fullBoard[3][2], fullBoard[4][2]) ? fullBoard[1][2] : 0);
        winPossibilities.add(allEqual(fullBoard[1][2], fullBoard[2][2], fullBoard[3][2], fullBoard[4][2], fullBoard[5][2]) ? fullBoard[1][2] : 0);
        //Possible 4th column win
        winPossibilities.add(allEqual(fullBoard[0][3], fullBoard[1][3], fullBoard[2][3], fullBoard[3][3], fullBoard[4][3]) ? fullBoard[1][3] : 0);
        winPossibilities.add(allEqual(fullBoard[1][3], fullBoard[2][3], fullBoard[3][3], fullBoard[4][3], fullBoard[5][3]) ? fullBoard[1][3] : 0);
        //Possible 5th column win
        winPossibilities.add(allEqual(fullBoard[0][4], fullBoard[1][4], fullBoard[2][4], fullBoard[3][4], fullBoard[4][4]) ? fullBoard[1][4] : 0);
        winPossibilities.add(allEqual(fullBoard[1][4], fullBoard[2][4], fullBoard[3][4], fullBoard[4][4], fullBoard[5][4]) ? fullBoard[1][4] : 0);
        //Possible 6th column win
        winPossibilities.add(allEqual(fullBoard[0][5], fullBoard[1][5], fullBoard[2][5], fullBoard[3][5], fullBoard[4][5]) ? fullBoard[1][5] : 0);
        winPossibilities.add(allEqual(fullBoard[1][5], fullBoard[2][5], fullBoard[3][5], fullBoard[4][5], fullBoard[5][5]) ? fullBoard[1][5] : 0);

        /* Diagonal win check */
        winPossibilities.add(allEqual(fullBoard[0][1], fullBoard[1][2], fullBoard[2][3], fullBoard[3][4], fullBoard[4][5]) ? fullBoard[0][1] : 0);
        winPossibilities.add(allEqual(fullBoard[0][0], fullBoard[1][1], fullBoard[2][2], fullBoard[3][3], fullBoard[4][4]) ? fullBoard[1][1] : 0);
        winPossibilities.add(allEqual(fullBoard[1][1], fullBoard[2][2], fullBoard[3][3], fullBoard[4][4], fullBoard[5][5]) ? fullBoard[1][1] : 0);
        winPossibilities.add(allEqual(fullBoard[1][0], fullBoard[2][1], fullBoard[3][2], fullBoard[4][3], fullBoard[5][4]) ? fullBoard[1][0] : 0);

        boolean blackWins = winPossibilities.contains(-1) && !winPossibilities.contains(1);
        boolean whiteWins = winPossibilities.contains(1) && !winPossibilities.contains(-1);
        boolean tieGame = winPossibilities.contains(-1) && winPossibilities.contains(1);

        if (blackWins) {
            winner = -1;
        } else if (whiteWins) {
            winner = 1;
        } else if (tieGame) {
            winner = 2;
        }
        return winner;
    }

    /**
     * Returns true if the board is full; false otherwise.
     */
    boolean isFull() {
        boolean ans = true;

        for (int[] row : fullBoard) {
            for (int space : row) {
                if (space == 0) {
                    ans = false;
                }
            }
        }

        return ans;
    }

    /**
     * Returns true if the board space at the given quadrant and position is empty; false otherwise.
     */
    boolean isSpaceEmpty(int quad, int pos) {
        int[][] quadrant = getQuadrants()[quad-1];
        int[] posCoordinates = getPositionCoordinates(pos);

        return quadrant[posCoordinates[0]][posCoordinates[1]] == 0;
    }

    /**
     * Returns a 6x6 2D array of the entire board state.
     */
    private int[][] getFullBoard() {
        return deepCopy(fullBoard);
    }

    /**
     * Returns an array of all 4 quadrants of this board state.
     */
    int[][][] getQuadrants() {
        return new int[][][]{deepCopy(quadrant1), deepCopy(quadrant2), deepCopy(quadrant3), deepCopy(quadrant4)};
    }

    /**
     * Returns true if all quadrants and the full board of the given board is equal to this one's; false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Board)) {
            return false;
        } else {
            boolean ans = true;
            Board otherBoard = (Board) other;
            int[][] otherFullBoard = otherBoard.getFullBoard();
            int[][][] thisQuadrants = getQuadrants();
            int[][][] otherQuadrants = otherBoard.getQuadrants();
            //check full board state:
            for (int i = 0; i < fullBoard.length; i++) {
                for (int j = 0; j < fullBoard[i].length; j++) {
                    if (fullBoard[i][j] != otherFullBoard[i][j]) {
                        ans = false;
                        break;
                    }
                }
            }
            //check individual quadrant states;
            for (int i = 0; i < thisQuadrants.length; i++) {
                for (int j = 0; j < thisQuadrants[i].length; j++) {
                    for (int k = 0; k < thisQuadrants[i][j].length; k++) {
                        if (thisQuadrants[i][j][k] != otherQuadrants[i][j][k]) {
                            ans = false;
                            break;
                        }
                    }
                }
            }
            return ans;
        }
    }

    /**
     * Takes a given quadrant and changes all elements to reflect a clockwise rotation.
     */
    private int[][] rotateCW(int[][] quadrant) {
        int[][] rotated = new int[3][3];
        rotated[0][0] = quadrant[2][0];
        rotated[0][1] = quadrant[1][0];
        rotated[0][2] = quadrant[0][0];
        rotated[1][0] = quadrant[2][1];
        rotated[1][1] = quadrant[1][1];
        rotated[1][2] = quadrant[0][1];
        rotated[2][0] = quadrant[2][2];
        rotated[2][1] = quadrant[1][2];
        rotated[2][2] = quadrant[0][2];
        return rotated;
    }

    /**
     * Takes a given quadrant and changes all elements to reflect a counter-clockwise rotation.
     */
    private int[][] rotateCCW(int[][] quadrant) {
        int[][] rotated = new int[3][3];
        rotated[0][0] = quadrant[0][2];
        rotated[0][1] = quadrant[1][2];
        rotated[0][2] = quadrant[2][2];
        rotated[1][0] = quadrant[0][1];
        rotated[1][1] = quadrant[1][1];
        rotated[1][2] = quadrant[2][1];
        rotated[2][0] = quadrant[0][0];
        rotated[2][1] = quadrant[1][0];
        rotated[2][2] = quadrant[2][0];
        return rotated;
    }

    /**
     * Initializes all quadrants to blank spaces and uses them to assemble the full board.
     */
    private void initializeGameBoard() {
        quadrant1 = new int[][]{{0,0,0},{0,0,0},{0,0,0}};
        quadrant2 = new int[][]{{0,0,0},{0,0,0},{0,0,0}};
        quadrant3 = new int[][]{{0,0,0},{0,0,0},{0,0,0}};
        quadrant4 = new int[][]{{0,0,0},{0,0,0},{0,0,0}};

        assembleFullBoard();
    }

    /**
     * Uses the quadrants to put together the full 6x6 board.
     */
    private void assembleFullBoard() {
        for (int i = 0; i < fullBoard.length; i++) {
            if (i < 3) {
                System.arraycopy(quadrant1[i], 0, fullBoard[i], 0, 3);
                System.arraycopy(quadrant2[i], 0, fullBoard[i], 3, 3);
            } else {
                System.arraycopy(quadrant3[i-3], 0, fullBoard[i], 0, 3);
                System.arraycopy(quadrant4[i-3], 0, fullBoard[i], 3, 3);
            }
        }
    }

    /**
     * Returns true if all given parameters are equal; false otherwise.
     */
    private boolean allEqual(int x1, int x2, int x3, int x4, int x5) {
        boolean ans = true;
        int[] arr = new int[]{x1,x2,x3,x4,x5};
        for (int num : arr) {
            if (arr[0] != num) {
                ans = false;
            }
        }
        return ans;
    }

    /**
     * Takes a given position of a quadrant (1-9) and returns the 2D coordinates corresponding to that position.
     */
    private int[] getPositionCoordinates(int pos) {
        int[] coordinates = new int[2];
        switch (pos) {
            case 1:
                coordinates = new int[]{0,0};
                break;
            case 2:
                coordinates = new int[]{0,1};
                break;
            case 3:
                coordinates = new int[]{0,2};
                break;
            case 4:
                coordinates = new int[]{1,0};
                break;
            case 5:
                coordinates = new int[]{1,1};
                break;
            case 6:
                coordinates = new int[]{1,2};
                break;
            case 7:
                coordinates = new int[]{2,0};
                break;
            case 8:
                coordinates = new int[]{2,1};
                break;
            case 9:
                coordinates = new int[]{2,2};
                break;
        }
        return coordinates;
    }

    /**
     * Takes a 2D array and creates a separate copy of it, then returns the copy.
     */
    private int[][] deepCopy(final int[][] input) {
        int[][] copy = new int[input.length][input.length];
        for (int i = 0; i < input.length; i++) {
            System.arraycopy(input[i], 0, copy[i], 0, input[i].length);
        }
        return copy;
    }
}