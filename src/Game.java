public class Game {

    public static void main(String[] args) {
        Board board = new Board();
        //board.printBoard();

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j < 9; j++) {
                if (j%2==0) {
                    board.placePiece(1, i, j);
                } else {
                    board.placePiece(-1, i, j);
                }
            }
        }
        //board.printBoard();

        Board board2 = new Board(
                board.getQuadrants()[0],
                board.getQuadrants()[1],
                board.getQuadrants()[2],
                board.getQuadrants()[3]);

        board.printBoard();
        board2.printBoard();

        System.out.println(board.equals(board2));

        board2.rotateQuadrant("r", 1);

        System.out.println(board.equals(board2));
    }
}
