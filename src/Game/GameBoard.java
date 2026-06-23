package Game;

import Units.Unit;

import java.util.ArrayList;
import java.util.List;

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

    public List<Unit> getUnitsInRange(Position position, double range) {
        List<Unit> units = new ArrayList<>();

        int rangeInt = (int) Math.ceil(range);
        int startX = Math.max(0, position.getX() - rangeInt);
        int endX = Math.min(getWidth() - 1, position.getX() + rangeInt);
        int startY = Math.max(0, position.getY() - rangeInt);
        int endY = Math.min(getHeight() - 1, position.getY() + rangeInt);

        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                Cell cell = grid[y][x];

                if (cell != null) {

                    if (position.range(cell.getPos()) <= range) {

                        Unit occupant = cell.getOccupant();

                        if (occupant != null) {
                            units.add(occupant);
                        }
                    }
                }
            }
        }

        return units;
    }
}