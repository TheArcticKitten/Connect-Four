/***
 * @(#)BoardGUI.java
 *
 *
 * @author Wyatt Reeves and Jack Thielman
 * @version 0.02 2015/10/5
 */

//TODO: Make it interactable.
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoardGUI extends JFrame implements InputGetter{

	private static final long serialVersionUID = -1896935465535619089L;
	private Board board;
	private Slot[][] slots;
	private Object lock;
	private int lastClicked;
	
	public BoardGUI(Board b,int width,int height){
		lock = new Object();
		lastClicked = -1;
		board = b;
		slots = new Slot[Board.WIDTH][Board.HEIGHT];
		for(int col = 0; col<slots.length; col++){
			for(int row = 0; row<slots[0].length; row++){
				slots[col][row] = new Slot(col,row);
			}
		}
		
		getContentPane().setLayout(new GridLayout(Board.HEIGHT, Board.WIDTH));
		
		//Add them row by row, then column by column, as is standard
		for(int row = 0; row<slots[0].length; row++){
			for(int col = 0; col<slots.length; col++){
				getContentPane().add(slots[col][row]);
			}
		}
		
		this.setSize(width,height);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/*
	 * This method allows the gui to be given a completely new board to render.
	 */
	public void updateBoard(Board b){
		board = b;
		update();
	}
	
	/*
	 * This method updates all of the slots of this board so that they display pieces correctly.
	 * 
	 * TODO: Test
	 */
	public void update(){
		for(int col = 0; col<Board.WIDTH; col++){
			for(int row = 0; row<Board.HEIGHT; row++){
				slots[col][row].update();
			}
		}
	}
	
	/*
	 * This method gets input for a human player.
	 * 
	 * TODO: Test
	 */
	public int getInput(){
		synchronized(lock){
			try{
				lock.wait();
			}
			catch(InterruptedException e){
				
			}
		}
		return lastClicked;
	}
	
	/*
	 * This class is a graphical representation of one of the many slots of the connect-four grid.
	 */
	class Slot extends JPanel implements MouseListener{
		
		private static final long serialVersionUID = -9018467180850675062L;
		private int row;
		private int col;
		private int color;
		
		public Slot(int c, int r){
			row = r;
			col = c;
			color = board.board[col][row];
			setBackground(Color.BLUE);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			addMouseListener(this);
		}
		
		/*
		 * Draws the correctly colored piece in this Slot.
		 * 
		 * TODO: Test
		 */
		public void paintComponent(Graphics g){
			int height = getHeight();
			int width = getWidth();
			
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, width, height);
			
			switch(color){
			case Board.RED: g.setColor(Color.RED);break;
			case Board.YELLOW: g.setColor(Color.YELLOW);break;
			case Board.EMPTY: g.setColor(Color.WHITE);break;
			}
			
			g.fillOval(0, 0, width, height);
		}
		
		/*
		 * This method updates this Slot so that it contains the correct piece.
		 * 
		 * TODO: Test
		 */
		public void update(){
			color = board.board[col][row];
			repaint();
		}
		public void mouseClicked(MouseEvent arg){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e) {
			/*
			//System.out.println(this.col);
			if(board.isLegalMove(col))board.placePiece(col);
			/*for(int i =0;i<board.board[0].length;i++)
			{
				for(int j = 0;j<board.board.length;j++)
					System.out.print(board.board[j][i]+" ");
				System.out.println();
			}
			BoardGUI b = (BoardGUI)this.getTopLevelAncestor();
			b.update();
			*/
			synchronized(lock){
				lastClicked = this.col;
				lock.notify();
			}
		}
	}
}
