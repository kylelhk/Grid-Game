package game;

// A game class implementing all the methods in the game interface
public class GameImpl implements Game {
  // [Set the "size" field as default so that it can be accessed by other classes in the same package]
  int size;
  
  // A constructor that takes a single parameter "size" defining the side length of the game grid
  public GameImpl (int size) {
    this.size = size;
  }

  
  // The game is over when there is a winner or there are no more moves (a draw)
  @Override
  boolean isOver() {
    
    // Return true if the game is over

  }

  // This method returns the colour of the winner.
  // Uses the PathFinder class to determine the winner
  @Override
  PieceColour winner() {

    // Returns PieceColour.NONE if the game is not over

    // Returns PieceColour.NONE if the game is a draw
  }

  // The colour of the current player (the player who will make the next move)
  @Override
  PieceColour currentPlayer() {

    // If the game is over, the output of this method does not matter (i.e. undefined).

  }

  // Gets a Collection of all valid moves for the current player
  @Override
  Collection<Move> getMoves() {
    // Empty if there are no valid moves for the current player

    // To use ArrayList

  }

  // Executes a move for the current player
  @Override
  void makeMove(Move move) {
    // Updates the internal game state [the grid?] to reflect the move

    // Throws an IllegalArgumentException if the move is invalid
    // An invalid move is one where the position is already occupied or out of bounds.

    // Changes the current player to the other colour after the move is made
    
    // If the game is over, the result of this method does not matter (i.e. undefined).

  }

  // Returns a copy of the grid
  @Override
  Grid getGrid() {
    // The grid returned should be a deep copy (i.e. a new object).

    // There is no way to modify the internal state of the game by modifying the grid returned.

  }

  // Returns a copy of the game
  @Override
  Game copy() {
    // The game returned should be a deep copy (i.e. a new object).

    // There is no way to modify the internal state of the game by modifying the game returned.

  }

  public static void main(String[] args) {

  }
}