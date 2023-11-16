package backtracking;


public class Sudoku {
    private static final int ROWS = 9;
    private static final int COLS = 9;
    private static final int EMPTY_CELL = 0;
    public static final int HARD = 17;
    public static final int MEDIUM = 25;
    public static final int EASY = 38;

    int[][] sudokuTable;

    public Sudoku() {
        sudokuTable = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++)
                sudokuTable[i][j] = EMPTY_CELL;
    }

    private boolean valid(int row, int col) {
        int numToCheck = sudokuTable[row][col];

        for (int coli = 0; coli < COLS; coli++) {
            if (coli != col
                    && sudokuTable[row][coli] != EMPTY_CELL
                    && numToCheck == sudokuTable[row][coli]) {
                return false;
            }
        }
        for (int rowi = 0; rowi < ROWS; rowi++) {
            if (rowi != row
                    && sudokuTable[rowi][col] != EMPTY_CELL
                    && numToCheck == sudokuTable[rowi][col]) {
                return false;
            }
        }

        return validInBlock(row, col);
    }

    private boolean validInBlock(int row, int col) {
        int numToCheck = sudokuTable[row][col];
        int rowMin = 0;
        int rowMax = 0;
        int colMin = 0;
        int colMax = 0;

        // find the block where the cell belongs and set the limits
        for (int i = 2; i < ROWS; i += 2) {
            if (row <= i) {
                rowMax = i;
                rowMin = i - 2;
                break;
            }
        }

        for (int i = 2; i < COLS; i += 2) {
            if (col <= i) {
                colMin = i;
                colMax = i - 2;
                break;
            }
        }

        for (int i = rowMin; i <= rowMax; i++) {
            for (int j = colMin; j <= colMax; j++) {
                if (i != row && j != col
                        && sudokuTable[i][j] != EMPTY_CELL
                        && sudokuTable[i][j] == numToCheck) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean setCell(int num, int row, int col) {
        if (sudokuTable[row][col] != EMPTY_CELL
                || row >= ROWS
                || col >= COLS
                || row < 0
                || col < 0
                || num < 1
                || num > 9) {
            return false;
        }
        sudokuTable[row][col] = num;
        return true;
    }

    public void unsetCell(int row, int col) {
        if (row < ROWS && col < COLS && row >= 0 && col >= 0) {
            sudokuTable[row][col] = EMPTY_CELL;
        }
    }


    public boolean validTable() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (sudokuTable[i][j] == EMPTY_CELL) {
                    return false;
                }
                if (!valid(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                sb.append(sudokuTable[i][j]);
                sb.append("  ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public void generateRandomTable(int numbersToGenerate) {
        int generatedNumbersCount = 0;
        int randomNum;
        int randomRow;
        int randomCol;

        while (generatedNumbersCount < numbersToGenerate) {
            randomNum = (int) Math.floor(Math.random() * 10) % 9 + 1;
            randomRow = (int) Math.floor(Math.random() * 10) % 9;
            randomCol = (int) Math.floor(Math.random() * 10) % 9;

            if (setCell(randomNum, randomRow, randomCol)) {
                if(valid(randomRow,randomCol)){
                    generatedNumbersCount++;
                }else{
                    unsetCell(randomRow,randomCol);
                }
            }
        }
    }

    // we start with the root cell 0,0
    private void solveSudokuBackTrack(int row, int col) {
        int newCol = col;
        int newRow = row;

        // solo trabaja en la fila superior y rightmost
        if(col >= COLS){
            if(row >= ROWS )
                return;
            newRow++;
            newCol = 0;
        }else{
            newCol++;
        }

        if(sudokuTable[row][col] != EMPTY_CELL){
            solveSudokuBackTrack(newRow,newCol);
            return;
        }

        for (int i = 1; i < 10; i++) {
            setCell(i, row, col);
            if(valid(row,col)){
                solveSudokuBackTrack(newRow,newCol);
            }else{
                unsetCell(row,col);
            }
        }

    }

    public void solveSudoku() {
        solveSudokuBackTrack(0, 0);
    }
}
