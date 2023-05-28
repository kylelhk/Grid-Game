package game;

// For using classes from the java.util package, such as Collection and ArrayList
import java.util.*;

// A game class implementing all the methods in the game interface
public class GameImpl implements Game {
  private int size;
  private Grid grid;
  private PieceColour currentPlayer;

  // A constructor that takes a single parameter "size" defining the side length of the game grid
  public GameImpl (int size) {
    // Throws IllegalArgumentException if the grid size is less than 1
    if (size < 1)
      throw new IllegalArgumentException("The inputted size must be greater than zero.");
    this.size = size;
    
    // Creates an empty grid for starting a game
    grid = new GridImpl(size);
    
    // White player always starts first
    currentPlayer = PieceColour.WHITE;
  }

  // // Returns true if the game is over (i.e. when there is a winner or there are no more valid moves (a draw))
  @Override
  public boolean isOver() {
    // Returns true if a path (in either WHITE or BLACK) from the top row to the bottom row or from the left column to the right column is found
    // or the Collection "validMoves" returned by getMoves() is empty (i.e. all positions have been occupied and there are no more valid moves)
    return PathFinder.topToBottom(grid, PieceColour.WHITE) || PathFinder.leftToRight(grid, PieceColour.WHITE) || PathFinder.topToBottom(grid, PieceColour.BLACK) || PathFinder.leftToRight(grid, PieceColour.BLACK) || getMoves().isEmpty();
  }

  // Returns the colour of the winner
  @Override
  public PieceColour winner() {
    // Determines the winner by using the methods in the PathFinder class
    if (isOver()) {
      if (PathFinder.topToBottom(grid, PieceColour.WHITE) || PathFinder.leftToRight(grid, PieceColour.WHITE)) 
        return PieceColour.WHITE;
      if (PathFinder.topToBottom(grid, PieceColour.BLACK) || PathFinder.leftToRight(grid, PieceColour.BLACK))
        return PieceColour.BLACK;
    }

    // Returns PieceColour.NONE if the game is not over or is a draw
    return PieceColour.NONE;
  }

  // Returns the colour of the current player who will make the next move
  @Override
  public PieceColour currentPlayer() {
    // If the game is over, the output of this method does not matter (i.e. undefined).
    if (isOver())
      return PieceColour.NONE;

    return currentPlayer;
  }

  // Gets a Collection of all valid moves available to the current player
  // An invalid move is one where the position is already occupied or out of bounds.
  @Override
  public Collection<Move> getMoves() {
    // Creates an ArrayList "validMoves" for storing all valid moves currently available in the grid
    Collection<Move> validMoves = new ArrayList<>();

    // Adds a Move / MoveImpl object to "validMoves" if the relevant position is unoccupied (i.e. PieceColour = NONE)
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (grid.getPiece(row, col) == PieceColour.NONE)
          validMoves.add(new MoveImpl(row, col));
      }
    }

    // "validMoves" will be empty if there are no valid moves
    return validMoves;
  }

  // Executes a move for the current player and alternates turn
  @Override
  public void makeMove(Move move) {
    // If the game is over, the output of this method does not matter (i.e. undefined).
    if (isOver())
      System.out.println("Game Over");

    // Throws an IllegalArgumentException if the move is invalid
    // An invalid move is one where the position is already occupied or out of bounds.
    if (grid.getPiece(move.getRow(), move.getCol()) != PieceColour.NONE || move.getRow() < 0 || move.getRow() >= size || move.getCol() < 0 || move.getCol() >= size)
      throw new IllegalArgumentException("The move is invalid as the position is already occupied or out of bounds.");
    
    // Updates the grid to reflect the move
    grid.setPiece(move.getRow(), move.getCol(), currentPlayer);

    // Changes the current player to the other colour after the move is made
    currentPlayer = (currentPlayer == PieceColour.WHITE ? PieceColour.BLACK : PieceColour.WHITE);
  }

  // Returns a copy of the grid
  @Override
  public Grid getGrid() {
    // The grid returned is a deep copy (i.e. a new object).
    // So that there is no way to modify this.grid by modifying the grid copy returned.
    // Makes use of the copy() method in the GridImpl class to create a new object
    return grid.copy();
  }

  // Returns a copy of the game
  @Override
  public Game copy() {
    // The game returned is a deep copy (i.e. a new object).
    // So that there is no way to modify the internal state of the game by modifying the game copy returned.
    // Creates a game copy and set its size as this.size
    GameImpl gameCopy = new GameImpl(size);

    // Sets the values of the other two fields of the game copy
    gameCopy.grid = getGrid();
    gameCopy.currentPlayer = currentPlayer;

    return gameCopy;
  }
}