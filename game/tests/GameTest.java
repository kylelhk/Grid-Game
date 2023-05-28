package game.tests;

import java.util.Collection;
import game.*;

public class GameTest extends Test {
  public static void main(String[] args) {
    // Part 1 - Tests for Constructor and Initial Fields
    
    // The only Constructor parameter "size" should be larger than zero (without upper limit).
    boolean caught = false;
    try {
      new GameImpl(0);
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);

    caught = false;
    try {
      new GameImpl(5);
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(false, caught);

    // Sets a typical grid size for tests
    int size = 5;

    // In the initial grid, the colour of all pieces should be PieceColour.NONE.
    boolean result = true;
    GameImpl test = new GameImpl(size);
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        if (test.getGrid().getPiece(i, j) != PieceColour.NONE) {
          result = false;
          break;
        }
    expect(true, result);

    // The initial player should be white player.
    expect(PieceColour.WHITE, test.currentPlayer());


    // Part 2 - Tests for isOver() and winner() Methods

    // If the grid size is 1, the first player (i.e. white player) should always win.
    test = new GameImpl(1);
    test.makeMove(new MoveImpl(0, 0));
    expect(true, test.isOver());
    expect(PieceColour.WHITE, test.winner());
    
    // At the start of a game, false for isOver() and PieceColour.NONE for winner() should be returned.
    test = new GameImpl(size);
    expect(false, test.isOver());
    expect(PieceColour.NONE, test.winner());

    // If the game is a draw, true for isOver() and PieceColour.NONE for winner() should be returned.
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        test.makeMove(new MoveImpl(i, j));
    expect(true, test.isOver());
    expect(PieceColour.NONE, test.winner());

    // If the grid has not been fully occupied and there is no winner, false for isOver() and PieceColour.NONE for winner() should be returned.
    test = new GameImpl(size);
    for (int i = 0; i < size - 1; i++)
      for (int j = 0; j < size - 1; j++)
        test.makeMove(new MoveImpl(i, j));
    expect(false, test.isOver());
    expect(PieceColour.NONE, test.winner());
    
    // If the grid has not been fully occupied but there is a winner, true for isOver() and the winner's colour for winner() should be returned.
    test = new GameImpl(size);
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 2; j++)
        test.makeMove(new MoveImpl(i, j));
    test.makeMove(new MoveImpl(4, 0));
    expect(true, test.isOver());
    expect(PieceColour.WHITE, test.winner());

    // If a player wins by the last move upon which the grid is fully occupied, true for isOver() and the winner's colour for winner() should be returned.
    test = new GameImpl(3);
    test.makeMove(new MoveImpl(0, 0));
    test.makeMove(new MoveImpl(0, 1));
    test.makeMove(new MoveImpl(1, 1));
    test.makeMove(new MoveImpl(1, 2));
    test.makeMove(new MoveImpl(1, 0));
    test.makeMove(new MoveImpl(0, 2));
    test.makeMove(new MoveImpl(2, 2));
    test.makeMove(new MoveImpl(2, 1));
    expect(false, test.isOver());
    expect(PieceColour.NONE, test.winner());
    test.makeMove(new MoveImpl(2, 0));
    expect(true, test.isOver());
    PieceColour colour = (test.getGrid().getSize() % 2 == 1 ? PieceColour.WHITE : PieceColour.BLACK);
    expect(colour, test.winner());
    
    // ** test special cases re bugs in PathFinder, e.g. topToBottom() and leftToRight()
    // ** e.g. what if there cases where a path is constructed at a point where both the criteria in these 2 methods are satisfied?

    // [ ]



    // Part 3 - Tests for getMoves() Method

    // Initial empty grid - all positions should be available as valid moves
    test = new GameImpl(size);
    Collection<Move> moves = test.getMoves();
    expect(size * size, moves.size());

    // Partially occupied grid - valid moves should be reduced by any moves made
    test.makeMove(new MoveImpl(0, 0));
    moves = test.getMoves();
    expect(size * size - 1, moves.size());

    // Fully occupied grid - no valid move should be available
    test = new GameImpl(size);
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        test.makeMove(new MoveImpl(i, j));
    moves = test.getMoves();
    expect(0, moves.size());

    // The elements in the Collection should correspond to the unoccupied positions in the grid
    test = new GameImpl(size);
    test.makeMove(new MoveImpl(2, 2));
    Collection<Move> collection = test.getMoves();
    // Ensures that every move in the collection is valid:
    result = true;
    for (Move move : collection) {
      if (test.getGrid().getPiece(move.getRow(), move.getCol()) != PieceColour.NONE) {
        result = false;
        break;
      }
    }
    // Ensures that the collection includes all valid moves:
    int validCount = 0;
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        if (test.getGrid().getPiece(i, j) == PieceColour.NONE)
          validCount++;
    if (validCount != collection.size())
      result = false;
    expect(true, result);
    

    // Part 4 - Tests for makeMove() and currentPlayer() Methods
    
    // Throws exeception if a move is one where the position is already occupied
    test = new GameImpl(size);
    test.makeMove(new MoveImpl(0, 0));
    caught = false;
    try {
      test.makeMove(new MoveImpl(0, 0));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);
    
    // Throws exeception if a move is one where the position is out of bounds
    test = new GameImpl(size);
    caught = false;
    try {
      test.makeMove(new MoveImpl(-1, 0));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);

    caught = false;
    try {
      test.makeMove(new MoveImpl(size, 0));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);

    caught = false;
    try {
      test.makeMove(new MoveImpl(size + 1, 0));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);

    caught = false;
    try {
      test.makeMove(new MoveImpl(0, 0));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(false, caught);

    caught = false;
    try {
      test.makeMove(new MoveImpl(size - 1, 0));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(false, caught);

    caught = false;
    try {
      test.makeMove(new MoveImpl(0, -1));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);

    caught = false;
    try {
      test.makeMove(new MoveImpl(0, size));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);

    caught = false;
    try {
      test.makeMove(new MoveImpl(0, size + 1));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);

    caught = false;
    try {
      test.makeMove(new MoveImpl(0, size - 1));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(false, caught);

    // The grid should be updated correctly to reflect the move.
    test = new GameImpl(size);
    test.makeMove(new MoveImpl(0, 0));
    test.makeMove(new MoveImpl(1, 1));
    test.makeMove(new MoveImpl(size - 1, size - 1));
    expect(PieceColour.WHITE, test.getGrid().getPiece(0, 0));
    expect(PieceColour.BLACK, test.getGrid().getPiece(1, 1));
    expect(PieceColour.WHITE, test.getGrid().getPiece(size - 1, size - 1));

    // The current player should be changed to the other colour after the move is made.
    test = new GameImpl(size);
    test.makeMove(new MoveImpl(0, 0));
    expect(PieceColour.BLACK, test.currentPlayer());
    test.makeMove(new MoveImpl(1, 1));
    expect(PieceColour.WHITE, test.currentPlayer());
    test.makeMove(new MoveImpl(size - 1, size - 1));
    expect(PieceColour.BLACK, test.currentPlayer());


    // Part 5 - Tests for getGrid() and copy() Methods
    
    // ** Ask ChatGPT - Seems no need separate tests for getGrid()???
    /* test = new GameImpl(size);
    Grid grid = test.getGrid();
    expect(grid, test.getGrid()); */

    // The grid sizes of the copy and the original should be the same.
    test = new GameImpl(size);
    Game copy = test.copy();
    expect(test.getGrid().getSize(), copy.getGrid().getSize());

    // The grid contents of the copy and the original should be the same.
    result = true;
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        if (test.getGrid().getPiece(i, j) != copy.getGrid().getPiece(i, j)) {
          result = false;
          break;
        }
    expect(true, result);

    // The current players of the copy and the original should be the same.
    expect(test.currentPlayer(), copy.currentPlayer());

    // The copy should be independent of the original.
    copy.makeMove(new MoveImpl(0, 0));
    expect(PieceColour.NONE, test.getGrid().getPiece(0, 0));


    // Part 6 - Other Tests

    // [any???]



    checkAllTestsPassed();
  }
}