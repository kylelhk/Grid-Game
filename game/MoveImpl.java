package game;

// Manages the moves by implementing all the methods in the move interface
public class MoveImpl implements Move {
  private int row, col;
  
  // A constructor consistent with game.tests.MoveTest
  public MoveImpl(int row, int col) {
    this.row = row;
    this.col = col;
  }

  // Returns the row of the move (zero-indexed)
  @Override
  public int getRow() {
    return row;
  }

  // Returns the column of the move (zero-indexed)
  @Override
  public int getCol() {
    return col;
  }

  // Returns the string representation of a Move / MoveImpl object in specified format
  // Override the default behaviour of the toString() method inherited from the Object class
  @Override
  public String toString() {
    return "(" + row + "," + col + ")";
  }
}