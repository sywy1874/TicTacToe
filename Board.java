import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** A simple TicTacToe game that allows you to play as many times as you'd like.
 * @author syamang
 */

public class Board extends ComponentAdapter implements ActionListener{	
	/**an inner class made such that each button knows its array indices.
	 * i might remove this in the future and add all button functionality
	 * into the board class. */
	private class TTTSquare extends MouseAdapter {
		JButton button;
		byte r, c; //coordinates of square (0 based indexing)

		private TTTSquare(int x, int y) {
			button = new JButton("-");

			r = (byte) ((x - 315) / 90);
			c = (byte) ((y - 315) / 90);

			button.setBounds(x, y, 90, 90);				
			button.setBackground(Color.BLACK);
			button.setForeground(Color.WHITE);				
			button.setFont(new Font(null, Font.BOLD, 20));

			button.addMouseListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(values[r][c] == EMPTY) {
				turns++;
				values[r][c] = (currPlayer == X) ? X : O;

				button.setForeground((currPlayer == X) ? Color.RED : Color.YELLOW);
				button.setText((currPlayer == X) ? "X" : "O");
				button.setFont(new Font(null, Font.PLAIN, 40));

				winner = getWinner(currPlayer, r, c);

				if(winner == -1 && turns < 9) {
					currPlayer = (currPlayer == X) ? O : X;
					xo.setForeground((currPlayer == X) ? Color.RED : Color.YELLOW);
					xo.setText((currPlayer == X) ? "X" : "O");

				} else turn.setVisible(false);		//triggers a ComponentEvent indicating the end of the game								
			}										//either a tie or a win
		}
	}
	
	//constants representing X and O
	private static final byte X = 1, O = 0, EMPTY = -1;

	//internal calculations of tic tac toe board
	private byte currPlayer = (Math.random() < 0.5) ? X : O, winner = -1, turns = 0;
	private byte[][] values = new byte[3][3];
	private int xWins = 0, oWins = 0;

	//some text labels
	JLabel turn = new JLabel("Turn:"),
			xo = new JLabel((currPlayer == X) ? "X" : "O"),	
			win = new JLabel("The winner is Player"),
			again = new JLabel("Play Again?"),			
			x = new JLabel("X - 0"),
			o = new JLabel("O - 0");
	
	//yes and no buttons for if you want to play again
	JButton yes = new JButton("Yes"), 
			no = new JButton("No");

	//the frame (duh)
	JFrame f;
	
	//3x3 grid of buttons
	TTTSquare[][] board = new TTTSquare[3][3];

	/**create and design all of the text labels. button design is handled in the 
	 * TTTSquare constructor.*/
	private Board() {
		//fill board with -1 (indicating there is no X or O in that space)
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				values[i][j] = EMPTY;

		JFrame f = new JFrame("Tic-Tac-Toe");

		//design the frame
		f.setBounds(0, 0, 900, 900);		
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		f.getContentPane().setBackground(Color.BLACK);

		//arrange buttons in a 3x3 grid
		for(int i = 0; i < 3; i++) {
			int xPos = 315 + (90 * i);

			for(int j = 0; j < 3; j++) {
				int yPos = 315 + (90 * j);

				board[i][j] = new TTTSquare(xPos, yPos);
				f.add(board[i][j].button);
			}
		}		

		//design the "Turn:" label
		turn.setBounds(315, 135, 270, 54);
		turn.setForeground(Color.WHITE);
		turn.setFont(new Font("Sansation", Font.PLAIN, 40));			
		turn.addComponentListener(this);
		f.add(turn);

		//design the colored X or O after the "Turn:" label
		xo.setBounds(430, 135, 54, 54);
		xo.setForeground((currPlayer == X) ? Color.RED : Color.YELLOW);
		xo.setFont(new Font("Sansation", Font.PLAIN, 40));
		f.add(xo);			

		//design win label			
		win.setBounds(315, 135, 270, 54);
		win.setForeground(Color.WHITE);
		win.setFont(new Font("Sansation", Font.PLAIN, 30));			
		win.setVisible(false);
		f.add(win);	
		
		//design Play Again? label
		again.setBounds(350, 230, 200, 60);
		again.setForeground(Color.WHITE);
		again.setFont(new Font("Sansation", Font.PLAIN, 30));	
		again.setVisible(false);
		f.add(again);
		
		//design X score label
		x.setBounds(265, 650, 70, 30);
		x.setForeground(Color.RED);
		x.setBackground(Color.BLACK);
		x.setFont(new Font("Sansation", Font.PLAIN, 30));	
		f.add(x);
		
		//design O score label
		o.setBounds(565, 650, 70, 30);
		o.setForeground(Color.YELLOW);
		o.setBackground(Color.BLACK);
		o.setFont(new Font("Sansation", Font.PLAIN, 30));	
		f.add(o);
		
		//design yes button
		yes.setBounds(315, 405, 150, 60);
		yes.setForeground(Color.GREEN);
		yes.setBackground(Color.BLACK);
		yes.setFont(new Font("Sansation", Font.PLAIN, 30));			
		yes.setVisible(false);
		
		yes.setActionCommand("yes");
		yes.addActionListener(this);		
		f.add(yes);
		
		//design no button
		no.setBounds(495, 405, 150, 60);
		no.setForeground(Color.RED);
		no.setBackground(Color.BLACK);
		no.setFont(new Font("Sansation", Font.PLAIN, 30));			
		no.setVisible(false);
		
		no.setActionCommand("no");
		no.addActionListener(this);		
		f.add(no);

		f.setLayout(null);
		f.setVisible(true);

		this.f = f;
	}

	/**code for what to do after a win or a tie. this is triggered
	 * by the turn label becoming invisible*/
	@Override
	public void componentHidden(ComponentEvent e) {		
		//hide the board			
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 3; j++)
				board[i][j].button.setVisible(false);

		//shift position of colored letter
		xo.setBounds(600, 135, 54, 54);

		//checks for tie
		if(turns == 9) {
			xo.setVisible(false);
			win.setText("It was a tie!");
		} else {
			//update score counting labels
			if(currPlayer == X) {
				xWins++;	
				x.setText("X - " + xWins);
			} else {
				oWins++;
				o.setText("O - " + oWins);
			}
		}

		win.setVisible(true);
		again.setVisible(true);
		
		yes.setVisible(true);
		no.setVisible(true);
	}
	
	/**code to handle the play again yes/no buttons. if the user selected yes, 
	 * this does NOT create a new board object, it just resets the state 
	 * of the current board to it's starting state.*/
	@Override
	public void actionPerformed(ActionEvent e) {
		yes.setVisible(false);
		no.setVisible(false);
		
		if("yes".equals(e.getActionCommand())) {
			for(int i = 0; i < 3; i++)
				for(int j = 0; j < 3; j++) {
					values[i][j] = EMPTY;
					
					//set buttons to default appearance 
					board[i][j].button.setText("-");
					board[i][j].button.setBackground(Color.BLACK);
					board[i][j].button.setForeground(Color.WHITE);				
					board[i][j].button.setFont(new Font(null, Font.BOLD, 20));
					board[i][j].button.setVisible(true);
				}					
			
			//reset fields and choose a new first player by random
			turns = 0;
			currPlayer = (Math.random() < 0.5) ? X : O; 
			winner = -1;
			
			xo.setForeground((currPlayer == X) ? Color.RED : Color.YELLOW);
			xo.setText((currPlayer == X) ? "X" : "O");
			xo.setBounds(430, 135, 54, 54);
			
			turn.setVisible(true);
			
			win.setVisible(false);
			again.setVisible(false);
			
		} else System.exit(0);
	}

	/**method that returns -1 if there is no winner for the square that was just updated
	 * otherwise the player is returned*/
	private byte getWinner(byte player, byte r, byte c) {	
		boolean row = true, col = true;

		for(int i = 0; i < 3; i++) {
			if(values[r][i] != player) 
				row = false;

			if(values[i][c] != player) 
				col = false;			
		}

		if(row || col) 
			return player;		

		//check diags if necessary 
		byte square = (byte) ((3 * r) + (c + 1));

		if(square % 2 == 1) {			
			if(square == 1 || square == 5 || square == 9) {
				boolean leftDiag = true;
				int i = 0, j = 0;

				while(i < 3) 
					if(values[i++][j++] != player) {
						leftDiag = false;	
						break;
					}

				if(leftDiag) return player;
			}

			if(square == 3 || square == 5 || square == 7) {
				boolean rightDiag = true;
				int i = 0, j = 2;

				while(i < 3)
					if(values[i++][j--] != player) {
						rightDiag = false;
						break;
					}

				if(rightDiag) return player;
			}
		}

		return -1;
	}
	
	public static void main(String[] args) {
		new Board();
	}

	
}
