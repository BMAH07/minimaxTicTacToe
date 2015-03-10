/***********************************************************************************************************************
************************************************************************************************************************
******** LAST UPDATED: March 4, 2015                                                                                  **
********   WRITTEN BY: Bretton Auerbach                                                                               **
********  DESCRIPTION: BinaryGame.java contains the main method for the TicTacToe game. The main method handles all   **
********************** of the rendering concerns for the game. The game generates a new window and populates it with  **
********************** a 3x3 grid of buttons. Using a mouse listener, upon click the human player's move is recorded  **
********************** and the computer player makes a move (if the game is not over). Upon game completion, the      **
********************** final outcome is displayed in an alert message.                                                **
** SUMMARY OF CLASSES:                                                                                                **
** 1) Game                A single class represents the game, which handles generating the visual board,              **
                          appropriate alternation of moves and monitoring/messaging the gamestate.                    **
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

import java.awt.Color;                                                // Coloring for window object
import java.awt.Dimension;                                            // Sizing for window object
import java.awt.Font;                                                 // Font for window object
import java.awt.GridLayout;                                           // Grid for window object
import java.awt.event.MouseEvent;                                     // Mouse event actions for window object
import java.awt.event.MouseListener;                                  // Mouse listener for window object

import javax.swing.JButton;                                           // GUI for buttons
import javax.swing.JFrame;                                            // Frame inside window
import javax.swing.JOptionPane;                                       // Message pop-ups
import javax.swing.SwingUtilities;                                    // Encapsulates game in runnable
                                                                      // (executed once window exists)
public class Game {
  BinaryBoard board = new BinaryBoard();                              // Instantiate new board object

  public static void main(String[] args) {                            // Main method
    SwingUtilities.invokeLater(new Runnable() {                       // Passes runnable to execution after frame setup

      @Override
      public void run() {                                             // Construction of obj. and all game interaction
        JFrame frame = new JFrame("JavaTTT");                         // Instantiate new JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);         // End execution when JFrame is closed
        frame.setLayout(new GridLayout(3, 3));                        // Create grid in frame
        final Game game = new Game();                                 // Instantiate static constant instance of Game
        final JButton[] buttons = new JButton[9];                     // Create static constant array for 9 JButtons
        for (int i = 0; i < 9; i++) {
          final int idx = i;                                          // Initialize 9 index positions
          final JButton button = new JButton();                       // Create static constant instances of 9 JButtons
          buttons[i] = button;                                        // Add each button at appropriate index
          button.setPreferredSize(new Dimension(100, 100));           // Set button size
          button.setBackground(Color.BLACK);                          // Set background color black (see in btw buttons)
          button.setOpaque(true);                                     // Opaque buttons (visible black looks like grid)
          button.setFont(new Font(null, Font.PLAIN, 100));            // Appropriately large Xs and Os
          button.addMouseListener(new MouseListener() {               // Instantiate mouse listener
            public void mouseReleased(MouseEvent e) {}                // All member functions must remain even if no
            public void mousePressed(MouseEvent e) {}                 // code is executed for these actions
            public void mouseExited(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {                  // Upon click
              boolean validMove = button.getText() == "";             // Is button is blank?
              if (!validMove) {                                       // NO
                JOptionPane.showMessageDialog(null,                   // Prompt for valid move
                    "This position is taken. Try another.");
              } else {                                                // YES
                button.setText(Character.toString(game.board.turn));  // Place correct player's mark
                game.move(idx);                                       // Add move to state
                if (!game.board.gameEnd()) {                          // If game's not over, computer's turn
                  int best = game.board.bestMove();                   // Get move from minimaxmove
                  buttons[best].setText(Character                     // Add move to visual display
                      .toString(game.board.turn));
                  game.move(best);                                    // Add this move to game board
                }
                if (game.board.gameEnd()) {                           // If game's over
                  String message = "";                                // Initialize string
                  if (game.board.win('x')) {                          // If human won
                    message = "You will never see " + "this message.";// Create message (this should not happen)
                  } else if (game.board.win('o')) {                   // If computer won
                    message = "Why would you let me win. "            // Create computer won message
                        + "Not sure if I'm smart?";
                  } else {                                            // If it's tie
                    message = "It's a draw. What a surprise!";        // Create tie message
                  }
                  JOptionPane.showMessageDialog(null, message);       // Display message in pop-up dialog box
                }
              }
            }
          });
          frame.add(button);                                          // After creating buttons add them (note game
        }                                                             // logic as mouse event attached to each button)
        frame.pack();                                                 // Puts everything into window
        frame.setVisible(true);                                       // Display window upon successful pack
      }
    });
  }

  protected void move(int idx) {                                      // Move as part of game calls board member method
    board = board.move(idx);
  }
}
