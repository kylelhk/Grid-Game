package game.tests;

import game.Grid;
import game.GridImpl;
import game.PieceColour;;

public class GridTest extends Test {
    public static void main(String[] args) {
        Grid grid = new GridImpl(5); //size=5x5
        expect(5, grid.getSize());
        expect(PieceColour.NONE, grid.getPiece(0, 0)); //return colour
        expect(PieceColour.NONE, grid.getPiece(4, 4));
        expect(PieceColour.NONE, grid.getPiece(2, 3));
        expect(".....\n.....\n.....\n.....\n.....\n", grid.toString()); //string rep of grid
        grid.setPiece(2, 1, PieceColour.WHITE); //set colour
        expect(PieceColour.WHITE, grid.getPiece(2, 1)); //return the colour just set
        expect(".....\n.....\n.W...\n.....\n.....\n", grid.toString());
        grid.setPiece(0, 0, PieceColour.BLACK);
        expect(PieceColour.BLACK, grid.getPiece(0, 0));
        expect("B....\n.....\n.W...\n.....\n.....\n", grid.toString());
        grid.setPiece(4, 4, PieceColour.WHITE);
        expect(PieceColour.WHITE, grid.getPiece(4, 4));
        expect("B....\n.....\n.W...\n.....\n....W\n", grid.toString());
        boolean caught = false;
        try {
            grid.setPiece(4, 5, PieceColour.BLACK); //invalid col index 5 (out of bounds)
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        expect(true, caught);

        Grid copy = grid.copy(); //the grid returned should be a deep copy (i.e. a new object)
        expect(5, copy.getSize());
        expect(PieceColour.BLACK, copy.getPiece(0, 0));
        expect(PieceColour.WHITE, copy.getPiece(4, 4));
        expect(PieceColour.NONE, copy.getPiece(2, 3));
        expect("B....\n.....\n.W...\n.....\n....W\n", copy.toString());
        copy.setPiece(1, 2, PieceColour.BLACK);
        expect(PieceColour.BLACK, copy.getPiece(1, 2));
        expect("B....\n..B..\n.W...\n.....\n....W\n", copy.toString());
        
        //there is no way to modify the internal state of "grid" by modifying "copy".
        expect(PieceColour.NONE, grid.getPiece(1, 2));
        expect("B....\n.....\n.W...\n.....\n....W\n", grid.toString());
        caught = false;
        try {
            copy.getPiece(-1, 2);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        expect(true, caught);

        checkAllTestsPassed();
        

    }
}
