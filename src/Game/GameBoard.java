package Game;

public class GameBoard {
    private Cell[][] grid;

    public GameBoard(int width, int height) {
        this.grid = new Cell[height][width];
    }

    public Cell getCell(int x, int y) {
        return grid[y][x];
    }

    public void setCell(int x, int y, Cell cell) {
        grid[y][x] = cell;
    }

    public int getHeight() {
        return grid.length;
    }

    public int getWidth() {
        return grid[0].length;
    }
}