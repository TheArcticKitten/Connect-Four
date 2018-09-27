/***
 * @(#)ConnectFour.java
 *
 *
 * @author Mr. Kiesel and Paschal CS 2 & CS 3 Students
 * @version 1.0 2015/9/28
 */

public class ConnectFour
{
	public static final int GUI_DELAY = 1000;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
    	Board b = new Board();

		BoardGUI gui = new BoardGUI(b,700,600);

		Player red = new Human(gui);
        Player yellow = new Sedo(7);

		while(true)
		{
			int col = -1;
			if(b.turnRed)
			{
				col = red.nextTurn(b);

				if(!(red instanceof Human))
				{
					try
					{
						Thread.sleep(GUI_DELAY);
					}
					catch(InterruptedException e)
					{

					}
				}
			}
			else{
				col = yellow.nextTurn(b);
				if(!(yellow instanceof Human))
				{
					try
					{
						Thread.sleep(GUI_DELAY);
					}
					catch(InterruptedException e)
					{

					}
				}
			}
			b.placePiece(col);
			gui.update();
			if(b.gameOver)
			    break;
		}
		switch(b.winner)
		{
		    case 0:
		        System.out.println("Tie");
		        break;
		    case 1:
		        System.out.println("Red Wins");
		        break;
		    case 2:
		        System.out.println("Yellow Wins");
		}
    }
}