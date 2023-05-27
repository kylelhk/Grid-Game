package game;

import java.util.*;

public class PathFinder {
    private static class Position {
        int row, col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        //compare the fields (row and col) of obj with those of the Position object calling this method
        public boolean equals(Object obj) {
            Position other = (Position) obj; //cast (force) the type of Object to Position
            return row == other.row && col == other.col;
        }
    }

    private static Collection<Position> getOnRow(Grid grid, int row, PieceColour piece) {
        Collection<Position> positions = new ArrayList<>();
        for (int col = 0; col < grid.getSize(); col++) //for every piece in the given row
            if (grid.getPiece(row, col) == piece) //if that piece is in the given color (piece)
                positions.add(new Position(row, col)); //add that piece to the array "positions"
        return positions;
    }

    private static Collection<Position> getOnCol(Grid grid, int col, PieceColour piece) {
        Collection<Position> positions = new ArrayList<>();
        for (int row = 0; row < grid.getSize(); row++)
            if (grid.getPiece(row, col) == piece)
                positions.add(new Position(row, col));
        return positions;
    }

    //this is a Breadth-first search (BFS) algorithm - a type of graph traversal algorithm that visits all the neighbors of a node before moving on to the next level. In this case, a 'node' is a position on the grid, and two positions are 'neighbors' if they're adjacent (up, down, left, or right) and have the same color.
    private static boolean findPath(Grid grid, Collection<Position> starts, Collection<Position> ends) {
        Queue<Position> queue = new ArrayDeque<>(); //create an array "queue" (FIFO)
        boolean[][] visited = new boolean[grid.getSize()][grid.getSize()]; //create a 2D array with the same size as the grid, for storing "visited pieces". Initially, all positions are marked as unvisited (default = false).
        for (Position start : starts) { //for every element in the "starts" array
            queue.offer(start); //add that element to the "queue" array and return true if successful, return false if the element cannot be added
            visited[start.row][start.col] = true; //in the "visited" array, set that element as true (i.e. visisted) by using the row and col of that element
        }
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        //above, below, left, rigt
        while (!queue.isEmpty()) { //while the "queue" array is NOT empty
            Position pos = queue.poll(); //remove and return the 1st element in the "queue" array, assign that element to a variable "pos"
            if (ends.contains(pos)) //true if the "ends" array contains a element whose row and col are the same as those of "pos" - initially, "pos" is the starting pos in the "start" array, but after running the below for loop, "pos" will keep changing to the 4-neighbour(s), which will be the head of the current path
                return true; //a path from one of the start positions to one of the end positions is found
            for (int d = 0; d < dirs.length; d++) { //for 0 to 3
                int row = pos.row + dirs[d][0];
                int col = pos.col + dirs[d][1];
                //the 3 lines above effectively check the piece above, below, at the left of and at the right of "pos" ("4-neighbour(s)")
                if (row < 0 || row >= grid.getSize() || col < 0 || col >= grid.getSize())
                //if the row or col of the 4-neighbour(s) is out of bound
                    continue; //end the current loop and go to the next loop
                if (visited[row][col]) //if the 4-neighbour(s) has been visited before
                    continue; //end the current loop and go to the next loop
                PieceColour piece = grid.getPiece(row, col); //assign the color of the 4-neighbour(s) to "piece"
                if (piece != grid.getPiece(pos.row, pos.col)) //if the color of the 4-neighbour(s) is NOT the same as that of "pos" (centre)
                    continue; //end the current loop and go to the next loop
                // if all the above tests are passed:
                queue.offer(new Position(row, col)); //add that 4-neighbour(s) to the "queue" array
                visited[row][col] = true; //in the "visited" array, set that 4-neighbour(s) as true (i.e. visisted)
            }
        }
        return false; //there's no path from any of the start positions to any of the end positions
    }

    // Returns true if there is a path from the top row to the bottom row
    // consisting of pieces of the given player's colour.
    public static boolean topToBottom(Grid grid, PieceColour player) {
        Collection<Position> starts = getOnRow(grid, 0, player);
        Collection<Position> ends = getOnRow(grid, grid.getSize() - 1, player);
        return findPath(grid, starts, ends);
    }


    // Returns true if there is a path from the left column to the right column
    // consisting of pieces of the given player's colour.
    public static boolean leftToRight(Grid grid, PieceColour player) {
        Collection<Position> starts = getOnCol(grid, 0, player);
        Collection<Position> ends = getOnCol(grid, grid.getSize() - 1, player);
        return findPath(grid, starts, ends);
    }
}