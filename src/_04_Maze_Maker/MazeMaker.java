package _04_Maze_Maker;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeMaker {

	private static int rows;
	private static int cols;

	private static Maze maze;

	private static Random randGen = new Random();
	private static Stack<Cell> uncheckedCells = new Stack<Cell>();

	public static Maze generateMaze(int r, int c) {
        rows = r;
        cols = c;
        maze = new Maze(rows, cols);

        // 1. Pick a random cell along the border and remove its exterior wall.
        //    This will be the starting point. Then select a random cell along
        //    the opposite wall and remove its exterior wall. This will be the
        //    finish line.
        int startRow = randGen.nextInt(rows);
        int startCol = 0;
        int endRow = randGen.nextInt(rows);
        int endCol = cols - 1;
        maze.cells[startRow][startCol].setWestWall(false);
        maze.cells[endRow][endCol].setEastWall(false);
        // 2. select a random cell in the maze to start 
        selectNextPath(maze.getCell(startRow, startCol));
        // 3. call the selectNextPath method with the randomly selected cell
        return maze;
    }

	// 4. Complete the selectNextPathMethod
	private static void selectNextPath(Cell currentCell) {
		// A. SET currentCell as visited
		currentCell.setBeenVisited(true);
		// B. check for unvisited neighbors using the cell
		ArrayList<Cell> unvisited = getUnvisitedNeighbors(currentCell);
		// C. if has unvisited neighbors,
		if (!getUnvisitedNeighbors(currentCell).isEmpty()) {
			// C1. select one at random.
			int pushedCell = randGen.nextInt(unvisited.size());
			// C2. push it to the stack
			uncheckedCells.push(unvisited.get(pushedCell));
			// C3. remove the wall between the two cells
			removeWalls(currentCell, unvisited.get(pushedCell));
			// C4. make the new cell the current cell and SET it as visited
			currentCell = unvisited.get(pushedCell);
			currentCell.setBeenVisited(true);
			// C5. call the selectNextPath method with the current cell
			selectNextPath(currentCell);
		}
		// D. if all neighbors are visited
		if (getUnvisitedNeighbors(currentCell).size() == 0) {
			// D1. if the stack is not empty
			if (!uncheckedCells.isEmpty()) {
				// D1a. pop a cell from the stack
				currentCell = uncheckedCells.pop();
				// D1b. make that the current cell
				// D1c. call the selectNextPath method with the current cell
				selectNextPath(currentCell);
			}
		}
	}

	// This method will check if c1 and c2 are adjacent.
	// If they are, the walls between them are removed.
	private static void removeWalls(Cell c1, Cell c2) {
		if (c1.getRow() == c2.getRow()) {
			if (c1.getCol() > c2.getCol()) {
				c1.setNorthWall(false);
				c2.setSouthWall(false);
			} else {
				c2.setNorthWall(false);
				c1.setSouthWall(false);
			}
		} else {
			if (c1.getRow() > c2.getRow()) {
				c1.setWestWall(false);
				c2.setEastWall(false);
			} else {
				c2.setWestWall(false);
				c1.setEastWall(false);
			}
		}
	}

	// This method returns a list of all the neighbors around the specified
	// cell that have not been visited. There are up to 4 neighbors per cell.
	// 1
	// 2 cell 3
	// 4
	private static ArrayList<Cell> getUnvisitedNeighbors(Cell c) {
		int row = c.getRow();
		int col = c.getCol();

		ArrayList<Cell> unvisitedNeighbors = new ArrayList<Cell>();

		if (row > 0 && !maze.getCell(row - 1, col).hasBeenVisited()) {
			unvisitedNeighbors.add(maze.getCell(row - 1, col));
		}

		if (col > 0 && !maze.getCell(row, col - 1).hasBeenVisited()) {
			unvisitedNeighbors.add(maze.getCell(row, col - 1));
		}

		if (row < rows - 1 && !maze.getCell(row + 1, col).hasBeenVisited()) {
			unvisitedNeighbors.add(maze.getCell(row + 1, col));
		}

		if (col < cols - 1 && !maze.getCell(row, col + 1).hasBeenVisited()) {
			unvisitedNeighbors.add(maze.getCell(row, col + 1));
		}

		return unvisitedNeighbors;
	}
}
