package game;

// A grid class implementing all the methods in the grid interface
public class GridImpl implements Grid {
  private int size;
  private PieceColour[][] grid;

  public GridImpl(int size) {
    this.size = size;

    // Creates a 2D grid in the given size 
    grid = new PieceColour[size][size];

    // Initialises every piece with PieceColour.NONE to represent an empty grid
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        grid[i][j] = PieceColour.NONE;
  }

  // Returns the size of the grid (i.e. the length of one side of the square)
  @Override
  public int getSize() {
    return size;
  }

  // Returns the colour at the given row and column
  @Override
  public PieceColour getPiece(int row, int col) {
    // Throws IllegalArgumentException if the row or column is out of bounds
    if (row < 0 || row >= size || col < 0 || col >= size)
      throw new IllegalArgumentException("The inputted row or column is out of bounds.");
    
    // Returns the colour of the specified piece
    // PieceColour.NONE (the initial colour) will be returned if the position is empty.
    return grid[row][col];
  }

  // Sets the colour at the given row and column
  @Override
  public void setPiece(int row, int col, PieceColour piece) {
    // Throws IllegalArgumentException if the row or column is out of bounds 
    if (row < 0 || row >= size || col < 0 || col >= size)
      throw new IllegalArgumentException("The inputted row or column is out of bounds.");
    
    // Throws IllegalArgumentException if the piece is not a valid colour
    // Though players can only use black or white pieces, PieceColour.NONE is included in the test below as this setPiece method is applied in the copy() method below for copying empty pieces.
    if (piece != PieceColour.NONE && piece != PieceColour.WHITE && piece != PieceColour.BLACK)
      throw new IllegalArgumentException("The inputted colour is invalid.");
    
    // Sets the colour of the specified piece
    grid[row][col] = piece;
  }

  // Creates and returns a copy of this.grid
  @Override
  public Grid copy() {
    // The grid returned is a deep copy (i.e. a new object "copy").
    // So that there is no way to modify this.grid by modifying the "copy" returned.
    GridImpl copy = new GridImpl(size);

    // Copies all pieces in this.grid to the "copy"
    for (int i = 0; i < size; i++)
      for (int j = 0; j < size; j++)
        copy.setPiece(i, j, getPiece(i, j));

    return copy;
  }

  // Returns the string representation of a Grid / GridImpl object in specified format
  // Overrides the default behaviour of the toString() method inherited from the Object class
  @Override
  public String toString() {
    String output = "";
    // For every piece in this.grid, assigns ".", "W" or "B" according to PieceColour
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        // Uses switch statement instead of if statement for faster performance
        switch (grid[i][j]) {
          case NONE:
            output += ".";
            break;
          case WHITE:
            output += "W";
            break;
          case BLACK:
            output += "B";
            break;
        }
      }
      // Append a newline character after finishing the loop for a row
      output += "\n";
    }
    return output;
  }
}