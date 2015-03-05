/***********************************************************************************************************************
************************************************************************************************************************
******** LAST UPDATED: March 4, 2015                                                                                  **
********   WRITTEN BY: Bretton Auerbach                                                                               **
********  DESCRIPTION: BinaryBoardTest.java contains assertion method based tests of the BinaryBoard class contained  **
********************** in BinaryBoard.java. There is test coverage of all of BinaryBoard class's member methods.      **
** SUMMARY OF METHODS:                                                                                                **
** 1) testNewDefault()    asserts BinaryBoard()   instantiates a new board with state equal to the correct sum of     **
**                                                move values (0) and correct starting player (x)                     **
**                                                (this is the default constructor).                                  **
** 2) testNew()           asserts BinaryBoard()   instantiates a new board with state equal to the value of the state **
**                                                passed in and player equal to the player passed in.                 **
**                                                (constructor with parameters).                                      **
** 3) testMove()          asserts move()          returns a new board with the correct move value added and           **
**                                                the player equal to opposite of the player passed in.               **
** 4) testPossibleMoves() asserts possibleMoves() returns an integer value representing the sum of the values of      **
**                                                the remaining possible moves.                                       **
** 5) testWin()           asserts win()           returns a boolean true if x or o has won, returns false if no one   **
**                                                has won the game yet.                                               **
** 6) testMinimaxmove()   asserts minimaxmove()   returns an int array containing the heuristic score of the best     **
**                                                available move and the index of the best available move.            **
** 7) testBestMove()      asserts bestMove()      returns an int representing the best available move.                **
** 8) testGameEnd()       asserts gameEnd()       returns a boolean if a player has won or the game is a draw.        **
************************************************************************************************************************
***********************************************************************************************************************/

package ttt;

import static org.junit.Assert.*;                                     // Import junit Assert (contains assert methods)
import org.junit.Test;                                                // Allows public void methods to run as test cases

public class BinaryBoardTest {                                        // All tests are of BinaryBoard -> grouped in a
                                                                      // single class
  @Test // 1
  public void testNewDefault() throws Exception {                     // Default constructor expected to create:
    // Case 1
    BinaryBoard board = new BinaryBoard();                            // a new board object with
    assertEquals(0, board.state);                                     // int state == 0 and
    assertEquals('x', board.turn);                                    // char turn == 'x'
  }

  @Test // 2
  public void testNew() throws Exception {                            // Constructor expected to create:
    // Case 1
    BinaryBoard board = new BinaryBoard(7, 'o');                      // a new board object
    assertEquals(7, board.state);                                     // a new board object with
    assertEquals('o', board.turn);                                    // int state == stateIn and
  }                                                                   // char turn == turnIn

  @Test // 3
  public void testMove() throws Exception {                           // move expected to return a new board:
    // Case 1 [new board, place x in top left box (index 0)]
    BinaryBoard board = new BinaryBoard().move(0);                    // when called with argument 0 on a new board
    assertEquals(1, board.state);                                     // 'x' should place a move at 0, 2^0 == 1
    assertEquals('o', board.turn);                                    // after value of move is added to state, 'o' turn
    // Case 2 [new board, place o in bottom right box (index 8)]
    BinaryBoard board2 = new BinaryBoard().move(8);                   // when called with argument 8 on a new board
    assertEquals(256, board2.state);                                  // 'x' should place a move at index 8, 2^8 == 256
    assertEquals('o', board2.turn);                                   // after value of move is added to state, 'o' turn
    // Case 3 [new board o's turn in top left box (index 0)]
    BinaryBoard board3 = new BinaryBoard(0, 'o').move(0);             // when called with argument 0 on a new board with
    assertEquals(512, board3.state);                                  // 'o' as active player, place move at 0, 2^9 ==
    assertEquals('x', board3.turn);                                   // 512, after move, 'x's turn
    // Case 4 [new board, place o in bottom right box (index 8)]
    BinaryBoard board4 = new BinaryBoard(0, 'o').move(8);             // when called with argument 8 on a new board with
    assertEquals(131072, board4.state);                               // 'o' as active player, place move at 8 == 2^17
    assertEquals('x', board4.turn);                                   // == 131,072, after move 'x's turn
    // Case 5 [new board, place x mid left, place o top left]
    BinaryBoard board5 = new BinaryBoard().move(3).move(0);           // when called with arg 3 on a new board followed
    assertEquals(520, board5.state);                                  // by by a call on with arg 0, state == 2^3 + 2^9
    assertEquals('x', board5.turn);                                   // == 520, after 'x' & 'o' play, it's 'x's turn
  }

  @Test // 4
  public void testPossibleMoves() throws Exception {                  // possibleMoves expected to return:
    BinaryBoard board = new BinaryBoard().move(1).move(3).move(4);    // sum of all moves of the form 2^0 to 2^8 (511) -
    assertEquals(511 - 26, board.possibleMoves());                    // sum of all moves made (2^1 + 2^3 + 2^4)
  }

  @Test // 5
  public void testWin() throws Exception {                            // win expected to return:
    // Case 1 [board is empty]
    assertFalse(new BinaryBoard().win('x'));                          // false if there's no wining permutation board
    // Case 2 ['x' won top row (0,1,2)]
    assertTrue(new BinaryBoard().move(0).move(3).move(1).move(4)      // true if there's a winning permutation for 'x'
        .move(2).win('x'));                                           // on the top row
    // Case 3 ['x' won middle row 3,4,5]
    assertTrue(new BinaryBoard().move(3).move(0).move(4).move(1)      // true if there's a winning permutation for 'x'
        .move(5).win('x'));                                           // on the middle row
    // Case 4 ['x' won bottom row 6,7,8]
    assertTrue(new BinaryBoard().move(6).move(1).move(7).move(2)      // true if there's a winning permutation for 'x'
        .move(8).win('x'));                                           // on the bottom row
    // Case 5 ['x' won left column (0,3,6)]
    assertTrue(new BinaryBoard().move(0).move(1).move(3).move(2)      // true if there's a winning permutation for 'x'
        .move(6).win('x'));                                           // on the top column
    // Case 6 ['x' won middle column (1,4,7)]
    assertTrue(new BinaryBoard().move(1).move(2).move(4).move(3)      // true if there's a winning permutation for 'x'
        .move(7).win('x'));                                           // on the middle column
    // Case 7 ['x' won right column (2,5,8)]
    assertTrue(new BinaryBoard().move(2).move(0).move(5).move(1)      // true if there's a winning permutation for 'x'
        .move(8).win('x'));                                           // on the bottom column
    // Case 8 ['x' won left to right diagonal (0,4,8)]
    assertTrue(new BinaryBoard().move(0).move(1).move(4).move(2)      // true if there's a winning permutation for 'x'
        .move(8).win('x'));                                           // on the left to right diagonal
    // Case 9 ['x' won right to left diagonal (2,4,6)]
    assertTrue(new BinaryBoard().move(2).move(0).move(4).move(1)      // true if there's a winning permutation for 'x'
        .move(6).win('x'));                                           // on the right to left diagonal
    // Case 10 ['o' won top row (0,1,2)]
    assertTrue(new BinaryBoard(0, 'o').move(0).move(3).move(1)        // true if there's a winning permutation for 'o'
        .move(4).move(2).win('o'));                                   // on the top row
    // Case 11 ['o' won middle row 3,4,5]
    assertTrue(new BinaryBoard(0, 'o').move(3).move(0).move(4)        // true if there's a winning permutation for 'o'
        .move(1).move(5).win('o'));                                   // on the middle row
    // Case 12 ['o' won bottom row 6,7,8]
    assertTrue(new BinaryBoard(0, 'o').move(6).move(1).move(7)        // true if there's a winning permutation for 'o'
        .move(2).move(8).win('o'));                                   // on the bottom row
    // Case 13 ['o' won left column (0,3,6)]
    assertTrue(new BinaryBoard(0, 'o').move(0).move(1).move(3)        // true if there's a winning permutation for 'o'
        .move(2).move(6).win('o'));                                   // on the top column
    // Case 14 ['o' won middle column (1,4,7)]
    assertTrue(new BinaryBoard(0, 'o').move(1).move(2).move(4)        // true if there's a winning permutation for 'o'
        .move(3).move(7).win('o'));                                   // on the middle column
    // Case 15 ['o' won right column (2,5,8)]
    assertTrue(new BinaryBoard(0, 'o').move(2).move(0).move(5)        // true if there's a winning permutation for 'o'
        .move(1).move(8).win('o'));                                   // on the bottom column
    // Case 16 ['o' won left to right diagonal (0,4,8)]
    assertTrue(new BinaryBoard(0, 'o').move(0).move(1).move(4)        // true if there's a winning permutation for 'o'
        .move(2).move(8).win('o'));                                   // on the left to right diagonal
    // Case 17 ['o' won right to left diagonal (2,4,6)]
    assertTrue(new BinaryBoard(0, 'o').move(2).move(0).move(4)        // true if there's a winning permutation for 'o'
        .move(1).move(6).win('o'));                                   // on the right to left diagonal
  }

  @Test // 6
  public void testMinimaxmove() throws Exception {                    // minimaxmove expected to return:
    // Case 1 ['x' won (across first row = 7)]
    assertArrayEquals(new int[] { 100, -1 , 100, 100},                // array with max heuristic value and no move,
        new BinaryBoard(7, 'x').minimaxmove());                       // since game is over (represented by -1 flag)
    // Case 2 ['o' won (first row 7*512)]
    assertArrayEquals(new int[] { -100, -1, -100, -100 },             // array with min heuristic value and no move,
        new BinaryBoard(3584, 'o').minimaxmove());                    // since game is over (represented by -1 flag)
    // Case 3 [board full (2^0+2^2+2^4+2^7+512*(2^1+2^3+2^5+2^6+2^8))]
    assertArrayEquals(new int[] { 0, -1, 0, 0 }, new BinaryBoard(     // array with 0 heuristic value and no move since
        185493, 'o').minimaxmove());                                  // board is full (185,493 is one full permutation)
    // Case 4 ['x' one move away from winning (0,1,__)]
    assertArrayEquals(new int[] { 99, 2 , 100, 100},                  // array with max heuristic value - 1 and winning
        new BinaryBoard(3, 'x').minimaxmove());                       // move
    // Case 5 ['o' one move away from winning (0,1,__)]
    assertArrayEquals(new int[] { -99, 2, -100, -100 },               // array with min heuristic value + 1 and winning
        new BinaryBoard(1536, 'o').minimaxmove());                    // move
  }

  @Test // 7
  public void testBestMove() throws Exception {                       // bestMove expected to return index of best move
    // Case 1 ['x' one move away from winning (__,1,2)]
    assertEquals(0, new BinaryBoard(6, 'x').bestMove());
    // Case 2 ['x' one move away from winning (1,__,2)]
    assertEquals(1, new BinaryBoard(2560, 'o').bestMove());
  }

  @Test // 8
  public void testGameEnd() throws Exception {                        // gameEnd expected to return:
    assertFalse(new BinaryBoard().gameEnd());                         // false if game is not over
    assertTrue(new BinaryBoard(7, 'x').gameEnd());                    // true if a player has one
    assertTrue(new BinaryBoard(185493, 'x').gameEnd());               // true if the board is full
  }
}
