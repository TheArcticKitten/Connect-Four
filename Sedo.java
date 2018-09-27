import java.util.Random;
public class Sedo implements Player 
{
    
    int startDepth = 4;
    
    public Sedo(int d)
    {
    	startDepth = d;
    }
    
    public Sedo()
    {
    	this(6);
    }
    
    public int nextTurn(Board b)
    {
    	
    	for(int i = 0; i<Board.WIDTH; i++)
        {
       	    Board oBoard  = b.ifMoveMade(i);
       	    if( oBoard != null  && oBoard.gameOver )return i;
        }
    	
    	MoveScore bestMove = search(b,startDepth);
    	System.out.println("Sedo: Score: "+bestMove.score + " Move: " + bestMove.move);
    	return bestMove.move;	
    }
    
    private MoveScore search(Board b, int depth)
    {
    	if ( depth <= 0 || b.gameOver ) {
    		return new MoveScore(0, score(b) );
    	}
    	
    	int bestMove = 0;
    	int bestScore = b.turnRed?Integer.MIN_VALUE:Integer.MAX_VALUE;
    	
    	for(int i = 0; i<Board.WIDTH; i++)
        {
        	Board oBoard  = b.ifMoveMade(i);
       	    if( oBoard != null )
       	    {
       	    	MoveScore hypMS = search(oBoard,depth-1);
       	    	if ( b.turnRed )
       	    	{
       	    		if ( hypMS.score > bestScore )
       	    		{
       	    			bestScore = hypMS.score;
       	    			bestMove = i;
       	    		}
       	    	}
       	    	else
       	    	{
       	    		if ( hypMS.score < bestScore )
       	    		{
       	    			bestScore = hypMS.score;
       	    			bestMove = i;
       	    		}
       	    	}
       	    }
        }
        
        return new MoveScore(bestMove, bestScore);
    	
    }
    
    public static int score(Board p)
    	{
    		
    		if ( p.gameOver )
    		{
    			if ( p.winner == Board.RED ) return 400000;
    			else if ( p.winner == Board.RED ) return -400000;
    			return 0;
    		}
    		
    	 int score = 0;
    	 int bQ[][] = p.quartets();
    	 	for(int i = 0; i < bQ.length;i++){
    	 		    int reds  = 0;
                	int yellows = 0;
                	for(int j =0;j < 4;j++)
                	{
                		if(bQ[i][j] == Board.RED)reds++;
                		else if(bQ[i][j] == Board.YELLOW)yellows++;
                	}
                 	if(reds == 4)score += 40000;
                 	else if(yellows == 4)score -= 40000;
                	else if(reds == 3 && yellows == 0)score += 10000;
                	else if(reds == 2 && yellows == 0)score += 100;
                	else if(reds == 1 && yellows == 0)score += 1;
                	else if(reds == 0 && yellows == 3)score -= 10000;
                	else if(reds == 0 && yellows == 2)score -= 100;
                	else if(reds == 0 && yellows == 1)score -= 1;
                	
    	 	}
    	 	return score; 
    	}
    	
    	private class MoveScore
    	{
    		int move;
    		int score;
    		
    		public MoveScore(int m, int s)
    		{
    			move = m;
    			score = s;
    		}
    	}
    	
}