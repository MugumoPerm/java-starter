//====================== student name ==========================
//======================MINESWEEPER===========================
//======================student number=========================
/*
The game has 4 difficulty levels 
starts on easy
if win, automatically gets harder
if loose, resets and try same difficulty again
can reset the if you get lost in all the flags

everything is well thought out. 
can right click to flag and then rightclick again to unflag
checks for win after every click
good incentive to finish the final level as there is a hidden.
*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.Timer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


public class Minesweeper extends JFrame implements ActionListener, MouseListener
{
		// Define constants for the board sizes and mine counts
		private static final int EASY_ROWS = 6;
		private static final int EASY_COLS = 9;
		private static final int EASY_MINES = 11;

		private static final int MEDIUM_ROWS = 12;
		private static final int MEDIUM_COLS = 18;
		private static final int MEDIUM_MINES = 36;

		private static final int HARD_ROWS = 21;
		private static final int HARD_COLS = 26;
		private static final int HARD_MINES = 92;

		String diffic;
		int size=10;
		int numMines;
		boolean won=false;
		boolean lost=false;
		boolean[][] mines= new boolean[size][size];
		boolean[][] clicked= new boolean[size][size];
		int[][] cells= new int[size][size];
		JFrame window;
		JPanel panel= new JPanel();
		JButton[][] buttons= new JButton[size][size];
		boolean[][] flagged= new boolean[size][size];
		int totFlagged= 0;
		Timer timer;

			//========================================================================main method
		
		public static void main(String[] args){
			 
			
			new Minesweeper(11, "EASY");
			
		}
		//============================================================================constructor---------
		
		public Minesweeper(int noOfMines, String diffic){
		
		
			
			numMines=noOfMines;
			this.diffic=diffic;
			 // Determine board size and number of mines based on difficulty
        if (diffic.equals("EASY")) {
            size = 6;
            numMines = 11;
        } else if (diffic.equals("MEDIUM")) {
            size = 12;
            numMines = 36;
        } else if (diffic.equals("HARD")) {
            size = 21;
            numMines = 92;
        } 			
			//----window frame---
			window= new JFrame();
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setSize(50*size,50*size);
			window.setTitle("MINESWEEPER");
			window.setLocationRelativeTo(null);
		//----window frame---
			window= new JFrame();
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setSize(50*size,50*size);
			window.setTitle("MINESWEEPER");
			window.setLocationRelativeTo(null);
			
		//--MENU--
			
			JMenuBar bar= new JMenuBar();
			JMenu diff= new JMenu("Difficulty");
			JMenu curDifficulty= new JMenu("current: "+diffic);
			JMenu restart= new JMenu("........RESET GAME........");
			JLabel timerLabel = new JLabel("Time left: 0 seconds");
			
			JMenuItem confirm=new JMenuItem("CONFIRM");
			confirm.addActionListener(this);
			JMenuItem cancel=new JMenuItem("CANCEL");			
			
			JMenuItem hard= new JMenuItem("HARD");
			hard.addActionListener(this);
			JMenuItem easy= new JMenuItem("EASY");
			easy.addActionListener(this);
			JMenuItem med= new JMenuItem("MEDIUM");
			med.addActionListener(this);
			JMenuItem cust= new JMenuItem("RUSSIAN ROULETTE");
			cust.addActionListener(this);
			
			
			restart.add(confirm);
			restart.add(cancel);
			
			diff.add(easy);
			diff.add(med);
			diff.add(hard);
			diff.add(cust);
			
			
			bar.add(diff);
			bar.add(curDifficulty);
			bar.add(Box.createHorizontalGlue());
			bar.add(timerLabel);
			bar.add(restart);
			
			// Update the board size
        buttons = new JButton[size][size];
        mines = new boolean[size][size];
        clicked = new boolean[size][size];
        cells = new int[size][size];
        flagged = new boolean[size][size];

			panel.setLayout(new GridLayout(size,size));
			initialise();
			populateMines();
        		populateCells();
			// window.setVisible(true);
					
			
		
// 			add buttons
			
			
			for (int i=0; i<size ; i++){
				for (int j=0 ; j<size ; j++){
					panel.add( buttons[i][j] );
					
				}
			}
			window.setJMenuBar(bar);
			window.add(panel, BorderLayout.CENTER);
			populateMines();
			populateCells();
					/////add buttons--------------------
			// ... existing code ...

			// Set the timer duration based on the difficulty level and board size
			int duration;
			if(diffic.equals("EASY")) {
				duration = 11 * 60; // 11 minutes in seconds
			} else if(diffic.equals("MEDIUM")) {
				duration = 36 * 60; // 36 minutes in seconds
			} else if(diffic.equals("HARD")) {
				duration = 92 * 60;
			} else if(diffic.equals("RUSSIAN ROULETTE")) {
				duration = 92 * 60;
			} else{
				duration = 0;
			}
			// Start the timer when the game starts
			timer = new Timer(1000, new ActionListener() {

				int timeLeft = duration;

				@Override
				public void actionPerformed(ActionEvent e) {
					timeLeft--;
					// Update the timer display
					timerLabel.setText("Time left: " + timeLeft + " seconds");

					// When the timer reaches zero, end the game
					if (timeLeft <= 0) {
						((Timer)e.getSource()).stop();
						// End the game
						explode();
					}
				}
			});
			timer.start();

			window.setVisible(true);

			// ... existing code ...
			
		//	window.setVisible(true);
		
		}
		//----------------------------------
		
		
		//=======================================================================Mouse listener
		
			public void mouseEntered ( MouseEvent e ) {}
			public void mouseExited ( MouseEvent e ) {}
			public void mousePressed ( MouseEvent e ) {}
			
			public void mouseReleased ( MouseEvent e ) {} 
			public void mouseClicked ( MouseEvent e ) {
				
				if (SwingUtilities.isRightMouseButton(e)){
					
					//find adress
					JButton b=(JButton) e.getSource();//share an address
					
						String index=(String) b.getClientProperty("index");
			
						int row= Integer.parseInt("" + index.charAt(2));// get row and column of button pressed
						int column= Integer.parseInt("" + index.charAt(6));
						
						
					
						if (b.isEnabled() && !clicked[row][column]) {
									// Set a text "F" for flagged
									b.setText("F");
									// If you still want to set a background color or style, you can do that here
							}
					
					
						
		
						
						if (flagged[row][column] && (!clicked[row][column] ))
						
						{flagged[row][column]=false;totFlagged--;b.setIcon(null);check();return;}
						
						if (!flagged[row][column]&& (!clicked[row][column]))
						
						{flagged[row][column]=true;totFlagged++;}
						check();
						
						
					}
				}
		
		//-------------------	
		//=======================================================================ACTION LISTENER=======
		public void actionPerformed(ActionEvent e){
			//===========check for menu buttons using getActionCommand()---
			String tempS= e.getActionCommand();
			
			
			if(tempS.equals("HARD")){refresh(17,"HARD");return;}
			if(tempS.equals("EASY")){refresh(5,"EASY");return;}
			if(tempS.equals("MEDIUM")){refresh(13,"MEDIUM");return;}
			if(tempS.equals("CONFIRM")){refresh();return;}
			if(tempS.equals("RUSSIAN ROULETTE")){refresh(20,"RUSSIAN ROULETTE");return;}
			
			//------------------------------------------------------------
			check();
			JButton b=(JButton)e.getSource();  //share an address
			String index=(String) b.getClientProperty("index");
			int rowStart = index.indexOf("r:") + 2;
			int rowEnd = index.indexOf(" ", rowStart);
			
			int row= Integer.parseInt(index.substring(rowStart, rowEnd));// get row and column of button pressed


			int columnStart = index.indexOf("c:") + 2;
			int column = Integer.parseInt(index.substring(columnStart));
			
			
			if (flagged[row][column]){flagged[row][column]=false;totFlagged--;check();buttons[row][column].setText("");buttons[row][column].setIcon(null);}
			
			if (mines[row][column]){    //if a mine
			  	explode();
			}  
			else if (cells[row][column]>0){   // if a number
				b.setText("" + cells[row][column]);
				// b.setBackground(Color.GRAY);	
				b.setForeground(Color.BLACK);	
				clicked[row][column]=true;
			}
			
			else if (cells[row][column]==0){  //if zero
				
				zeroClear(row,column);
			}
			
		check();	
		}
	// =========================================================================Zero Clear
	
		public void zeroClear(int i, int j){
						clicked[i][j]=true;
						buttons[i][j].setBackground(Color.BLACK);
						buttons[i][j].setEnabled(false);
						
						
						boolean left= (j-1 >= 0);    //checks if in bounds
						boolean right= (j+1 < size);
						boolean up= (i-1 >= 0);
						boolean down= (i+1 < size);
						
						if (left){                  //checks the left etc..using recursion
									if (cells[i][j-1]==0 && (!clicked[i][j-1]) && (!flagged[i][j-1]) ){
											
											buttons[i][j-1].setText("");
											buttons[i][j-1].setBackground(Color.BLACK);
											buttons[i][j-1].setForeground(Color.BLACK);
											clicked[i][j-1]=true;
											zeroClear(i,j-1);}
									//below else if line makes sure no zeros are showing
									else if (cells[i][j-1]==0 && (!flagged[i][j-1])) {buttons[i][j-1].setText("");}	
									else if ((!flagged[i][j-1])){buttons[i][j-1].setText("" + cells[i][j-1]);clicked[i][j-1]=true;}
													
						}
						if (right){	
										
									if (cells[i][j+1]==0 && (!clicked[i][j+1])&& (!flagged[i][j+1])){
											
											buttons[i][j+1].setText("");
											buttons[i][j+1].setBackground(Color.BLACK);
											buttons[i][j+1].setForeground(Color.BLACK);
											clicked[i][j+1]=true;
											zeroClear(i,j+1);
											}
									
									else if (cells[i][j+1]==0 && (!flagged[i][j+1])) {buttons[i][j+1].setText("");}		
									else if ((!flagged[i][j+1])) {buttons[i][j+1].setText(""+cells[i][j+1]);clicked[i][j+1]=true;}
						}
						if (up){	
										
									if (cells[i-1][j]==0 && (!clicked[i-1][j]) && (!flagged[i-1][j])){
									
											buttons[i-1][j].setText("");
											buttons[i-1][j].setBackground(Color.BLACK);
											buttons[i-1][j].setForeground(Color.BLACK);
											clicked[i-1][j]=true;
											zeroClear(i-1,j);}
											
											
									else if (cells[i-1][j]==0 && (!flagged[i-1][j])) {buttons[i-1][j].setText("");}
									else if ((!flagged[i-1][j])){buttons[i-1][j].setText(""+cells[i-1][j]);clicked[i-1][j]=true;}
						}
						if (down){	
										
									if (cells[i+1][j]==0 && (!clicked[i+1][j]) && (!flagged[i+1][j])){
											
											buttons[i+1][j].setText("");
											buttons[i+1][j].setBackground(Color.BLACK);
											buttons[i+1][j].setForeground(Color.BLACK);
											clicked[i+1][j]=true;
											zeroClear(i+1,j);}
											
									else if (cells[i+1][j]==0 && (!flagged[i+1][j])) {buttons[i+1][j].setText("");}		
									else if ((!flagged[i+1][j])){buttons[i+1][j].setText(""+cells[i+1][j]);clicked[i+1][j]=true;}
						}
		}
	
	
		//======================================================================  initialise
		
		public void initialise(){
		
		for (int i=0; i<size ; i++){
				for (int j=0 ; j<size ; j++){
					mines[i][j]=false;
					clicked[i][j]=false;
					flagged[i][j]=false;
					cells[i][j]=0;
					buttons[i][j]= new JButton();
					buttons[i][j].addActionListener(this);
					buttons[i][j].addMouseListener(this);
					buttons[i][j].putClientProperty("index","r:"+i+" "+"c:"+j);
				}} 
		
		}
		
	

		//======================================================================POPULATE MINES
		public void populateMines(){
		
		int needed = numMines ;
		while ( needed > 0 ) {
			int x = ( int ) Math.floor ( Math.random()* size ) ;
			int y = ( int ) Math.floor ( Math.random()* size ) ;
			if ( !mines[x][y] ) {
				mines [x][y] = true ;
				needed -- ;
			}
		}
		}
		//======================================================================check
	public void check() {
    int count = 0;
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if (clicked[i][j] || flagged[i][j]) {
                count++;
                if (totFlagged == numMines && count == (size * size)) {

                    if (diffic.equals("RUSSIAN ROULETTE")) {
                        JOptionPane.showMessageDialog(null,
                                "Congratulations! You've completed the Russian Roulette level.",
                                "Well Done!", JOptionPane.INFORMATION_MESSAGE);
                        refresh();
                        break;
                    }

                    JOptionPane.showMessageDialog(null, "Congratulations! You've won!", "Well Done!",
                            JOptionPane.INFORMATION_MESSAGE);

                    if (diffic.equals("EASY")) {
                        refresh(13, "MEDIUM");
                        break;
                    }
                    if (diffic.equals("MEDIUM")) {
                        refresh(17, "HARD");
                        break;
                    }
                    if (diffic.equals("HARD")) {
                        refresh(20, "RUSSIAN ROULETTE");
                        break;
                    }
                } else {
                    continue;
                }
            } else {
                break;
            }
        }
    }
}

		
		private void playExplosionSound() {
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource("./explosion.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | IllegalArgumentException | NullPointerException e) {
        e.printStackTrace();
    }
}
		//=====================================================================Explode 
		public void explode(){
			playExplosionSound();
			for (int i=0; i<size ; i++ ){     //i is rows j is columns
				for (int j=0; j<size ; j++){
						
						if (mines[i][j]){
							
							buttons[i][j].setText("💣");
							buttons[i][j].setBackground(Color.RED);
							
						}
				}
			//showMessageDialog(Component parent, Object message, String title, int messageType, Icon icon)	
		}
		
		
		
		JOptionPane.showMessageDialog(null, "Oh no! You hit a mine. Game over!", "Explosion", JOptionPane.INFORMATION_MESSAGE);
		refresh();
		}
		//=====================================================================  refresh 
		
		public void refresh(){
				window.setVisible(false);
				new Minesweeper(numMines,diffic);
		}
		
		public void refresh(int numMin, String difficul){
				window.setVisible(false);
				new Minesweeper(numMin,difficul);
		}
		
		//=====================================================================custom
		
		public void custom(){
			
			
		}
		
     //======================================================================POPULATE cells

		public void populateCells(){
			int count=0;
			for (int i=0; i<size ; i++ ){     //i is rows j is columns
				for (int j=0; j<size ; j++) {
						
						boolean left= (j-1 >= 0);    //checks if in bounds
						boolean right= (j+1 < size);
						boolean up= (i-1 >= 0);
						boolean down= (i+1 < size);
						
						//count the mines surrounding the JButton cell				
						
						if (left){
								if (mines[i][j-1]){count++;}
									if (up){
										if (mines[i-1][j-1]){count++;}}
									if (down){
										if (mines[i+1][j-1]){count++;}}
						}
						if (right){
								if (mines[i][j+1]){count++;}
									if (up){
										if (mines[i-1][j+1]){count++;}}
									if (down){
										if (mines[i+1][j+1]){count++;}}
						}
						if (up){
								if (mines[i-1][j]){count++;}}
						if (down){
								if (mines[i+1][j]){count++;}}
						
						//give value of count to cell
						cells[i][j]=count;
						count=0;					
						
				}
			}
		}

} 
