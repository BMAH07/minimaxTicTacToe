/***********************************************************************************************************************
************************************************************************************************************************
******** LAST UPDATED: March 4, 2015                                                                                  **
********   WRITTEN BY: Bretton Auerbach                                                                               **
********  DESCRIPTION: BinaryBoard.java contains the game logic for a TicTacToe game using 18 bits to represent the   **
********************** game states. Specifically, the board state is stored as an int. Each position on the board is  **
********************** represented by a power of 2. With the first 9 digits (0th - 8th powers) X's positions and the  **
********************** 10th - 18th digits (9th - 17th powers) O's positions. Thus, each move is recorded by adding the**
********************** appropriate power of 2. When the value is viewed in base 2 this becomes immediately apparent.  **
********************** For clarity, an empty board can be thought of as: 000 000 000 000 000 000, and a board full of **
********************** Xs would be:                000 000 000 111 111 111 (511 in base 10),                          **
********************** and a board filled with Os: 111 111 111 000 000 000 (130,560 in base 10).                      **
********************** Each winning permutation has a unique value:                                                   **
**********************                                                                                                **
**********************      273                 84         ** 273*2^9                 84*2^9                          **
**********************         \               /           **        \               /                                **
**********************           1 |   2 |   4  == 7       **        2^9 | 2^10| 2^11 ==  7*2^9                       **
**********************        -----+-----+-----            **       -----+-----+-----                                 **
**********************           8 |  16 |  32  == 56      **        2^12| 2^13| 2^14 == 56*2^9                       **
**********************        -----+-----+-----            **       -----+-----+-----                                 **
**********************          64 | 128 | 256  == 448     **        2^15| 2^16| 2^17 == 448*2^9                      **
**********************        =================            **       =================                                 **
**********************          73   146   292             **     73*2^9 146*2^9 292*2^9                              **
** SUMMARY OF CLASSES:                                                                                                **
** 1) BinaryBoard         A single class represents the board, which holds the gamestate in an int called state and   **
**                        the current player in a char called turn. The class contains methods for updating the board **
**                        with moves, for turn switching, for the computer to make moves, and for assessing whether   **
**                        there has been a win or whether the game is over.                                           **
** SUMMARY OF METHODS:                                                                                                **
** 1) BinaryBoard()       takes no parameters and constructs a new board with position set to 0 and player set to 'x'.**
** 2) BinaryBoard()       takes an int representing the board configuration and constructs a new board with these     **
**                        parameters.                                                                                 **
** 3) BinaryBoard move()  takes an int representing the index where the move should go and returns a new board with   **
**                        the correct value of the move added.                                                        **
** 4) int possibleMoves() takes no parameters and returns an integer representing the sum of all possible moves       **
**                        () using the first 9 bits minus the moves that have been made.                              **
** 5) boolean win()       takes a char representing the current and returns a boolean representing whether that       **
**                        player has won.                                                                             **
** 6) int[] minimaxmove() takes no parameters and returns an array representing the heuristic score of the best move  **
**                        as well as the index of the best available move.                                            **
** 7) int bestMove()      takes no parameters and returns an int representing the index of the best available move.   **
** 8) boolean gameEnd()   takes no parameters and returns a boolean representing whether the game is over or not.     **
************************************************************************************************************************
***********************************************************************************************************************/

package ttt;

public class BinaryBoard {

  public int state;                                                   // Board state represented by unique integer value
  public char turn;                                                   // Active player represented by char 'x' or 'o'
  final int AI_SHIFT = 512;                                           // AI moves are binary digits 10-18 (shift 2^9)
  final int[] XWINS = { 7, 56, 73, 84, 146, 273, 292, 448 };          // Values of winning permutations for 'x'
  final int[] OWINS = { 3584, 28672, 37376, 43008, 74752, 139776,     // Values of winning permutations for 'o'
      149504, 229376 };


  public BinaryBoard() {                                              // Default constructor
    this.state = 0;                                                   // Value set to 0 represents empty board
    this.turn = 'x';                                                  // Turn defaults to human player first (x)
  }


  public BinaryBoard(int newState, char turn) {                       // Constructs new board from existing state
    this.state = newState;                                            // state set to state passed in
    this.turn = turn;                                                 // turn set to turn passed in
  }


  /* Value passed to move is an unchecked preconditions because values
  ** can only be passed in from the game module. (User clicks buttons
  0 - 8 to place a move.) */
  public BinaryBoard move(int e) {                                    // Makes move by adding move as power of 2
    int newState = state;                                             // don't not mutate state (move called by minimax)
    if (turn == 'x') {                                                // If human player is taking a turn
      newState = state + (int) (Math.pow(2, e));                      // moves are stored in binary digits 0 - 8
    } else {                                                          // otherwise computer player is taking a turn
      newState = state + AI_SHIFT * (int) (Math.pow(2, e));           // moves are stored in binary digits 9 - 17
    }
    return new BinaryBoard(newState, turn == 'x' ? 'o' : 'x');        // New board is returned with move added
  }                                                                   // and player toggled


  public int possibleMoves() {                                        // Calculates value of open positions
    int moves = 511;                                                  // Sum of powers of 2 0 -> 8 = (2^(n+1)-1)
    for (int i = 0; i < 9; i++) {                                     // Iterate over all positions on the board
      if (((int) (Math.pow(2, i)) | state) == state                   // If BINARY OR of either move or moved shifted
          || (AI_SHIFT * (int) (Math.pow(2, i)) | state) == state) {  // with state == state, then move is in state
        moves -= (int) (Math.pow(2, i));                              // Subtract that move from possible moves
      }
    }
    return moves;                                                     // Return value representing remaining moves
  }


  public boolean win(char turn) {                                     // Tests board for win values
    if (turn == 'x') {                                                // If it's x's turn
      for (int i = 0; i < XWINS.length; i++) {
        if ((XWINS[i] | state) == state) {                            // Check if x win values are on the board with
          return true;                                                // binary or and return true if value found
        }
      }
    } else {                                                          // If it's o's turn
      for (int i = 0; i < OWINS.length; i++) {
        if ((OWINS[i] | state) == state) {                            // Check if o win values are on the board with
          return true;                                                // binary or and return true if value found
        }
      }
    }
    return false;                                                     // If no wins found, no one has won, return false
  }

/*
 * Using recursion, minimax returns the best available move and the
 * corresponding heuristic score in an array called minOrmax (since
 * depending on the last player to evaluate it can be either the
 * lowest or highest score). The first element of minOrMax
 * represents the heuristic score of a move and second element
 * represents the index of a move, the third element is an alpha value
 * the fourth element is a beta value. In summary:
 *            [heuristicNodeValue, moveIndex, α, β]
 * When first initialized, minOrMax has flags that are out of bounds
 * to indicate values not yet set. Score has a flag value of 1,000,
 * and index has a flag value of -1. α and β are set at the worst
 * possible values -100 for α and 100 for β.
 * minimaxmove() resets scores only when a new best case has been
 * reached either min or max depending. α and β are updated likewise,
 * recursion is stopped when αβ pruning is possible, i.e. once the
 * algorithm has reached a point where α >= β (meaning some previously
 * evaluated branch will definitely be chosen).
 */

  public int[] minimaxmove() {
    if (win('x')) {                                                   // x is maximizing, if x wins return 100 as max
      return new int[] { 100, -1, 100, 100 };                         // heuristic value, flag move as unset
    }
    if (win('o')) {                                                   // o is minimizing, if o wins return -100 as min
      return new int[] { -100, -1 , -100, -100};                      // heuristic value, flag move as unset
    }
    if (Integer.bitCount(state) == 9) {                               // If state has 9 bits containing 1s, game is draw
      return new int[] { 0, -1, 0, 0 };                               // return neutral heur' value, flag move as unset
    }
    int[] minOrMax = { 1000, -1 , -100, 100};                         // Initialize array to hold score/move, α, β
    for (int i = 0; i < 9; i++) {                                     // Iterate over every board position
      if ((possibleMoves() | (int) (Math.pow(2, i)))                  // If position at i is empty known because that
          == possibleMoves()) {                                       // power of 2 hasn't been removed by possiblemoves
        int[] value = move(i).minimaxmove();                          // Make move at i and call minimaxmove recursively
        if (minOrMax[0] == 1000 || turn == 'x'                        // If value hasn't been set, or if x is evaluating
            && minOrMax[0] < value[0] || turn == 'o'                  // and returned value > existing value, or if o is
            && minOrMax[0] > value[0]) {                              // evaluating and value < existing value
          minOrMax[0] = value[0];                                     // set node value to preferable value
          minOrMax[1] = i;                                            // set move index to corresponding i
        }
        if (turn == 'x' && minOrMax[2] < value[0]){                   // If x is evaluating and value > α
          minOrMax[2] = value[0];                                     // update α
        }
        else if (turn == 'o' && minOrMax[3] > value[0]) {             // If o is evaluating and value < β
          minOrMax[3] = value[0];                                     // update β
        }
      }
      if (minOrMax[2] >= minOrMax[3]){                                // If α >= β found
        break;                                                        // cease exploring that branch
      }
    }
    minOrMax[0] += (turn == 'x' ? -1 : 1);                            // Since winning sooner is better than later,
    return minOrMax;                                                  // taking more moves for the same end state is
  }                                                                   // penalized


  public int bestMove() {                                             // Returns move index from minimaxmove's returned
    return minimaxmove()[1];                                          // array
  }


  public boolean gameEnd() {                                          // Tests whether a player has won or a tie has
    return (win('x') || win('o') || (Integer.bitCount(state) == 9));  // occurred
  }

}
