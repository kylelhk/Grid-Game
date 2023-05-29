package game.tests;

import java.util.Collection;
import game.*;

public class GameTest extends Test {
  public static void main(String[] args) {

    // Section 1 - Tests for constructor provided initially

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

    // Section 2 - Tests for a range of grid sizes starting from 2

    int start = 2, end = 12;
    for (int size = start; size <= end; size++) {

      // Section 2.1 - Tests for constructor and initial fields

      // The only Constructor parameter "size" should be larger than zero (without upper limit).
      // The sizes of 0 and 5 have been tested by the tests provided at the beginning of this main method.
      caught = false;
      try {
        new GameImpl(size);
      } catch (IllegalArgumentException e) {
        caught = true;
      }
      expect(false, caught);

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

      // Section 2.2 - Tests for isOver() and winner() methods

      // At the start of a game, false for isOver() and PieceColour.NONE for winner() should be returned.
      test = new GameImpl(size);
      expect(false, test.isOver());
      expect(PieceColour.NONE, test.winner());

      // If the game is a draw, true for isOver() and PieceColour.NONE for winner() should be returned.
      if (size % 2 == 1) {
        for (int i = 0; i < size; i++)
          for (int j = 0; j < size; j++)
            test.makeMove(new MoveImpl(i, j));
      } else {
        for (int i = 0; i < size - 1; i++)
          for (int j = 0; j < size; j++)
            test.makeMove(new MoveImpl(i, j));
        for (int j = 1; j < size; j++)
          test.makeMove(new MoveImpl(size - 1, j));
        test.makeMove(new MoveImpl(size - 1, 0));
      }
      expect(true, test.isOver());
      expect(PieceColour.NONE, test.winner());

      // If the grid is not fully occupied and there is no winner, false for isOver() and PieceColour.NONE for winner() should be returned.
      test = new GameImpl(size);
      for (int i = 0; i < size - 1; i++)
        for (int j = 0; j < size - 1; j++)
          test.makeMove(new MoveImpl(i, j));
      expect(false, test.isOver());
      expect(PieceColour.NONE, test.winner());
      
      // If the grid is not fully occupied and a player wins by occupying a column, true for isOver() and the winner's colour for winner() should be returned.
      test = new GameImpl(size);
      for (int i = 0; i < size - 1; i++)
        for (int j = 0; j < 2; j++)
          test.makeMove(new MoveImpl(i, j));
      expect(false, test.isOver());
      expect(PieceColour.NONE, test.winner());

      test.makeMove(new MoveImpl(size - 1, 0));
      expect(true, test.isOver());
      expect(PieceColour.WHITE, test.winner());

      // If the grid is not fully occupied and a player wins by occupying a row, true for isOver() and the winner's colour for winner() should be returned.
      test = new GameImpl(size);
      for (int j = 0; j < size - 1; j++)
        for (int i = 0; i < 2; i++)
          test.makeMove(new MoveImpl(i, j));
      expect(false, test.isOver());
      expect(PieceColour.NONE, test.winner());

      test.makeMove(new MoveImpl(0, size - 1));
      expect(true, test.isOver());
      expect(PieceColour.WHITE, test.winner());

      // ***[any others???]



      // Section 2.3 - Tests for getMoves() method

      // Initial empty grid - all positions should be available as valid moves
      test = new GameImpl(size);
      expect(size * size, test.getMoves().size());

      // Partially occupied grid - valid moves should be reduced by any moves made
      test.makeMove(new MoveImpl(0, 0));
      expect(size * size - 1, test.getMoves().size());

      // Fully occupied grid - no valid move should be available
      test = new GameImpl(size);
      if (size % 2 == 1) {
        for (int i = 0; i < size; i++)
          for (int j = 0; j < size; j++)
            test.makeMove(new MoveImpl(i, j));
      } else {
        for (int i = 0; i < size - 1; i++)
          for (int j = 0; j < size; j++)
            test.makeMove(new MoveImpl(i, j));
        for (int j = 1; j < size; j++)
          test.makeMove(new MoveImpl(size - 1, j));
        test.makeMove(new MoveImpl(size - 1, 0));
      }
      expect(0, test.getMoves().size());

      // The elements in the Collection should correspond to the unoccupied positions in the grid
      test = new GameImpl(size);
      test.makeMove(new MoveImpl(size - 1, size - 1));
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
      
      // Section 2.4 - Tests for makeMove() and currentPlayer() methods
      
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
      
      // No exeception if a move is one where the position is in bounds
      test = new GameImpl(size);
      // Creates an array of valid positions
      int[][] validPos = {
        {0, 0}, {size - 1, 0}, {0, size - 1}
      };
      // Creates an array of expected test results - all elements are false as no exceptions should be caught for all the valid positions
      boolean[] validPosResults = {false, false, false};
      // Tests in-bounds moves
      for (int i = 0; i < validPos.length; i++) {
        caught = false;
        try {
          test.makeMove(new MoveImpl(validPos[i][0], validPos[i][1]));
        } catch (IllegalArgumentException e) {
          caught = true;
        }
        expect(validPosResults[i], caught);
      }

      // Throws exeception if a move is one where the position is out of bounds
      test = new GameImpl(size);
      // Creates an array of invalid positions
      int[][] invalidPos = {
        {-1, 0}, {size, 0}, {size + 1, 0},
        {0, -1}, {0, size}, {0, size + 1}
      };
      // Creates an array of expected test results - all elements are true as exceptions should be caught for all the invalid positions
      boolean[] invalidPosResults = {true, true, true, true, true, true};
      // Tests out-of-bounds moves
      for (int i = 0; i < invalidPos.length; i++) {
        caught = false;
        try {
          test.makeMove(new MoveImpl(invalidPos[i][0], invalidPos[i][1]));
        } catch (IllegalArgumentException e) {
          caught = true;
        }
        expect(invalidPosResults[i], caught);
      }

      // The grid should be updated correctly to reflect the move.
      test = new GameImpl(size);
      test.makeMove(new MoveImpl(0, 0));
      test.makeMove(new MoveImpl(size - 1, size - 1));
      expect(PieceColour.WHITE, test.getGrid().getPiece(0, 0));
      expect(PieceColour.BLACK, test.getGrid().getPiece(size - 1, size - 1));

      // The current player should be changed to the other colour after the move is made.
      test = new GameImpl(size);
      test.makeMove(new MoveImpl(0, 0));
      expect(PieceColour.BLACK, test.currentPlayer());
      test.makeMove(new MoveImpl(size - 1, size - 1));
      expect(PieceColour.WHITE, test.currentPlayer());

      // Section 2.5 - Tests for getGrid() and copy() methods
      
      test = new GameImpl(size);
      test.makeMove(new MoveImpl(0, 0));
      test.makeMove(new MoveImpl(size - 1, size - 1));
      Game copy = test.copy();

      // The grid sizes of the copy and the original should be the same.
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
      test = new GameImpl(size);
      copy = test.copy();
      copy.makeMove(new MoveImpl(0, 0));
      copy.makeMove(new MoveImpl(size - 1, size - 1));
      expect(PieceColour.NONE, test.getGrid().getPiece(0, 0));
      expect(PieceColour.NONE, test.getGrid().getPiece(size - 1, size - 1));

      // Section 2.6 - Other tests

      // [any???]
    
    
    }

    // Section 3 - Tests for specific sizes and moves

    // Negative size for constructor
    caught = false;
    try {
      new GameImpl(-1);
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    expect(true, caught);

    // Tests for a grid in size 1
    GameImpl test = new GameImpl(1);
    expect(1, test.getGrid().getSize());
    expect(PieceColour.NONE, test.getGrid().getPiece(0, 0));
    expect(PieceColour.WHITE, test.currentPlayer());
    expect(1, test.getMoves().size());
    Game copy = test.copy();
    expect(test.getGrid().getSize(), copy.getGrid().getSize());
    expect(test.getGrid().getPiece(0, 0), copy.getGrid().getPiece(0, 0));
    expect(test.currentPlayer(), copy.currentPlayer());
    test.makeMove(new MoveImpl(0, 0));
    expect(PieceColour.NONE, copy.getGrid().getPiece(0, 0));
    // The first player (i.e. white player) should always win.
    expect(true, test.isOver());
    expect(PieceColour.WHITE, test.winner());

    // If a player wins by the last move upon which the grid is fully occupied, true for isOver() and the winner's colour for winner() should be returned.
    test = new GameImpl(3);
    int[][] winAtLast = {
      {0, 0}, {0, 1}, {1, 1}, {1, 2}, {1, 0}, {0, 2}, {2, 2}, {2, 1}
    };
    for (int i = 0; i < winAtLast.length; i++)
      test.makeMove(new MoveImpl(winAtLast[i][0], winAtLast[i][1]));
    expect(false, test.isOver());
    expect(PieceColour.NONE, test.winner());

    test.makeMove(new MoveImpl(2, 0));
    expect(true, test.isOver());
    expect(PieceColour.WHITE, test.winner());

    // If the grid is not fully occupied and a player wins by occupying a row and a column at the same time, true for isOver() and the winner's colour for winner() should be returned.
    test = new GameImpl(4);
    int[][] rowAndCol = {
      {0, 1}, {0, 2}, {1, 1}, {1, 2}, {2, 0}, {0, 3}, {2, 3}, {1, 0},
      {2, 2}, {0, 0}, {3, 1}, {3, 2}
    };
    for (int i = 0; i < rowAndCol.length; i++)
      test.makeMove(new MoveImpl(rowAndCol[i][0], rowAndCol[i][1]));
    expect(false, test.isOver());
    expect(PieceColour.NONE, test.winner());

    test.makeMove(new MoveImpl(2, 1));
    expect(true, test.isOver());
    expect(PieceColour.WHITE, test.winner());

    test = new GameImpl(5);
    int[][] rowAndCol2 = {
      {0, 4}, {2, 2}, {1, 4}, {2, 1}, {2, 4}, {3, 2}, {4, 2}, {3, 3},
      {4, 3}, {1, 1}, {3, 4}, {2, 0}, {4, 0}, {3, 0}, {4, 1}, {0, 0}
    };
    for (int i = 0; i < rowAndCol2.length; i++)
      test.makeMove(new MoveImpl(rowAndCol2[i][0], rowAndCol2[i][1]));
    expect(false, test.isOver());
    expect(PieceColour.NONE, test.winner());

    test.makeMove(new MoveImpl(4, 4));
    expect(true, test.isOver());
    expect(PieceColour.WHITE, test.winner());

    // If the grid is not fully occupied and a player wins by creating a path from the top row to the bottom row and another one from the left column to the right column at the same time, true for isOver() and the winner's colour for winner() should be returned.
    test = new GameImpl(4);
    int[][] twoPath = {
      {0, 1}, {0, 0}, {0, 2}, {1, 0}, {0, 3}, {1, 2}, {1, 1}, {3, 1},
      {2, 0}, {3, 0}, {2, 2}, {1, 3}, {3, 2}, {2, 3}
    };
    for (int i = 0; i < twoPath.length; i++)
      test.makeMove(new MoveImpl(twoPath[i][0], twoPath[i][1]));
    expect(false, test.isOver());
    expect(PieceColour.NONE, test.winner());

    test.makeMove(new MoveImpl(2, 1));
    expect(true, test.isOver());
    expect(PieceColour.WHITE, test.winner());

    // ** test special cases re bugs in PathFinder, e.g. topToBottom() and leftToRight()
    

    // [debug by playing with AI?]

    

    checkAllTestsPassed();
  }
}