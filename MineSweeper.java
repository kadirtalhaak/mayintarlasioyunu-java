import java.util.Random;
import java.util.Scanner;

public class MineSweeper {
    private final int rowCount;
    private final int colCount;
    private final int mineCount;
    private final int[][] mines;
    private final char[][] board;
    private final boolean[][] revealed;
    private int revealedCount;

    public MineSweeper(int rowCount, int colCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.mineCount = (rowCount * colCount) / 4;
        this.mines = new int[rowCount][colCount];
        this.board = new char[rowCount][colCount];
        this.revealed = new boolean[rowCount][colCount];
        this.revealedCount = 0;
        initializeBoard();
        placeMines();
    }

    private void initializeBoard() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                board[i][j] = '-';
            }
        }
    }

    private void placeMines() {
        Random rand = new Random();
        for (int i = 0; i < mineCount; i++) {
            int r, c;
            do {
                r = rand.nextInt(rowCount);
                c = rand.nextInt(colCount);
            } while (mines[r][c] == 1);
            mines[r][c] = 1;
        }
    }

    public boolean select(int row, int col) {
        if (row < 0 || row >= rowCount || col < 0 || col >= colCount) {
            return false;
        }

        if (mines[row][col] == 1) {
            revealAllMines();
            return true;
        }

        reveal(row, col);
        return false;
    }

    private void reveal(int row, int col) {
        if (revealed[row][col]) return;

        revealed[row][col] = true;
        revealedCount++;

        int mineCount = countAdjacentMines(row, col);
        board[row][col] = mineCount > 0 ? (char) ('0' + mineCount) : '0';

        if (mineCount == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) continue;
                    int newRow = row + i, newCol = col + j;
                    if (newRow >= 0 && newRow < rowCount && newCol >= 0 && newCol < colCount) {
                        reveal(newRow, newCol);
                    }
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i, newCol = col + j;
                if (newRow >= 0 && newRow < rowCount && newCol >= 0 && newCol < colCount) {
                    count += mines[newRow][newCol];
                }
            }
        }
        return count;
    }

    private void revealAllMines() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                if (mines[i][j] == 1) {
                    board[i][j] = '*';
                }
            }
        }
    }

    public boolean isFinished() {
        return revealedCount == rowCount * colCount - mineCount;
    }

    public void printBoard() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Mayin Tarlasi Oyununa Hosgeldiniz!");
        System.out.print("Satir sayisini giriniz: ");
        int rows = scanner.nextInt();
        System.out.print("Sutun sayisini giriniz: ");
        int cols = scanner.nextInt();

        MineSweeper game = new MineSweeper(rows, cols);

        boolean gameOver = false;
        while (!gameOver && !game.isFinished()) {
            game.printBoard();
            System.out.print("Satir seciniz: ");
            int selectedRow = scanner.nextInt();
            System.out.print("Sutun seciniz: ");
            int selectedCol = scanner.nextInt();

            if (!game.select(selectedRow, selectedCol)) {
                System.out.println("Basarili bir secim!");
            } else {
                System.out.println("Mayina bastiniz! Oyunu kaybettiniz!");
                gameOver = true;
            }
        }

        if (!gameOver) {
            System.out.println("Tebrikler! Mayin Tarlasini basariyla temizlediniz!");
        }

        game.printBoard();
        scanner.close();
    }
}
