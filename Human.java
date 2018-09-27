//import java.util.Scanner;

/**
 * @(#)Human.java
 *
 *
 * @author Mr. Kiesel and Paschal CS 2 & CS 3 Students
 * @version 0.02 2015/10/01
 */

public class Human implements Player 
{

	InputGetter input;
	
    public Human(InputGetter i) 
    {
    	input = i;
    }
    
    /*
	 *  Print a message to a human about the current
	 *  board state, which side they are playing as,
	 *  and ask the human for a move.
	 *
	 *  Returns the move choice made by the human only
	 *  if that choice is an int in the range
	 *  0 to Board.WIDTH-1 inclusive.
	 *
	 *  TODO: Write!
	 *  TODO: Test.
	 */
    public int nextTurn(Board b)
    {
    	return input.getInput();
    }
    
}