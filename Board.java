/***
 * @(#)Board.java 
 *
 * @author Mr. Kiesel and Paschal CS 2 & CS 3 Students
 * @version 0.85 2015/10/20
 */
public class Board implements Cloneable{

    /**
     * An int representing an empty square.
     */
    public static final int EMPTY = 0;

    /**
     * An int representing a square with a Red piece.
     */
    public static final int RED = 1;

    /**
     * An int representing a square with a Yellow piece.
     */
    public static final int YELLOW = 2;

    /**
     * The width of the game board, in columns.
     */
    public static final int WIDTH = 7;

    /**
     * The height of the game board, in rows.
     */
    public static final int HEIGHT = 6;

    /**
     * A 2D array of the board spaces; (x,y) indexed. Row 0 is the top row.
     */
    int[][] board;

    /**
     * A boolean that is true if it is currently Red's turn.
     */
    boolean turnRed;

    /**
     * A boolean that is true if this board is in a game over state.
     */
    boolean gameOver;

    /**
     * An int that reveals this game's winner, if there was one; see EMPTY, RED and YELLOW.
     */
    int winner;

    /**
     *  Creates a blank board ready for a new game.
     */
    public Board()
    {
        board = new int[WIDTH][HEIGHT];
        turnRed = true;
        gameOver = false;
        winner = EMPTY;
    }

    /**
     *  Produce an exact clone of this Board.
     */
    public Board clone()
    {
        Board clone = new Board();
        clone.turnRed = this.turnRed;
        clone.gameOver = this.gameOver;
        clone.winner = this.winner;
        for ( int i = 0; i < WIDTH; i++ )
        {
            System.arraycopy(this.board[i], 0, clone.board[i], 0, HEIGHT);
        }
        return clone;
    }

    /**
     *  Check legality of move in the notated column. (0 to WIDTH-1 incl.)
     *
     *  Returns true if the piece was placed successfully,
     *  and false if the game is over already or invalid input.
     *  Invalid input is out of range OR column already full.
     */
    public boolean isLegalMove(int col)
    {
        if ( gameOver ) return false; //Game already over
        if(col<0||col>=WIDTH)return false; //Out of bounds
        if(board[col][0]!=EMPTY)return false; //Row is filled up
        return true;
    }

    /**
     *  Place a piece in the notated column. (0 to WIDTH-1 incl.)
     *
     *  Returns true if the piece was placed successfully,
     *  and false if move was illegal or some other error occured.
     *
     */
    public boolean placePiece(int col)
    {
        if ( !isLegalMove(col) ) return false;

        //Start from the bottom and go up
        for(int row = HEIGHT-1; row>=0; row--){ //stupid brackets are stupid
            if(board[col][row]==EMPTY){
                board[col][row] = turnRed?RED:YELLOW;
                turnRed = !turnRed;
                checkGameState();
                return true;
            }
        }

        System.out.println("SERIOUS BOARD ERROR!  AHHHH!!!!");
        return false;
    }

    /**
     *  This version of placePiece lets you place any color piece: 
     *  Be careful it may violate game rules, so 
     *  only use on hypothetical clone boards, etc.
     */
    public boolean placePiece(int col, int color)
    {
        if ( !isLegalMove(col) ) return false;

        //Start from the bottom and go up
        for(int row = HEIGHT-1; row>=0; row--){ //stupid brackets are stupid
            if(board[col][row]==EMPTY){
                board[col][row] = color;
                turnRed = !turnRed;
                checkGameState();
                return true;
            }
        }

        //checkGameState();
        System.out.println("SERIOUS BOARD ERROR!  AHHHH!!!!");
        return false;
    }

    /**
     *  Returns an array of all four-in-a-row positions on the board.
     *
     *  Each inner int[] is length 4 and shows the value of four spots on the board.
     *
     *  Ex. quartets()[5][0] through quartets()[5][3]  represent the 6th four-in-a-row 
     *  spaces on the board.
     *
     *  Useful for Scoring Algorithms!
     */
    public int[][] quartets()
    {
        int size = HEIGHT*(WIDTH-3) + (HEIGHT-3)*WIDTH + 2*(HEIGHT-3)*(WIDTH-3);
        int[][] answer = new int[size][4];
        int iA = 0;

        // vertical
        for ( int j = 0; j < WIDTH; j++ )
        {
            for ( int i = 0; i <= HEIGHT - 4; i++  )
            {
                answer[iA][0] = board[j][i];
                answer[iA][1] = board[j][i+1];
                answer[iA][2] = board[j][i+2];
                answer[iA][3] = board[j][i+3];
                iA++;
            }
        }

        // horizontal
        for ( int i = 0; i < HEIGHT; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                answer[iA][0] = board[j][i];
                answer[iA][1] = board[j+1][i];
                answer[iA][2] = board[j+2][i];
                answer[iA][3] = board[j+3][i];
                iA++;
            }

        }

        // down-right
        for ( int i = 0; i <= HEIGHT-4; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                answer[iA][0] = board[j][i];
                answer[iA][1] = board[j+1][i+1];
                answer[iA][2] = board[j+2][i+2];
                answer[iA][3] = board[j+3][i+3];
                iA++;
            }
        }

        // up right
        for ( int i = 3; i < HEIGHT; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                answer[iA][0] = board[j][i];
                answer[iA][1] = board[j+1][i-1];
                answer[iA][2] = board[j+2][i-2];
                answer[iA][3] = board[j+3][i-3];
                iA++;
            }
        }

        return answer;
    }

    /**
     *  Returns an array of all four-in-a-row positions on the board WITH positional data!
     *
     *  Each inner int[][] is length 4 and shows the value,x,y of four spots on the board.
     *
     * i.e. spatialQuartets()[9][3][0] - 10th quartet, 4th entry, RED/YELLOW/EMPTY
     * i.e. spatialQuartets()[8][3][1] - 9th quartet, 4th entry, x-coord
     * i.e. spatialQuartets()[7][3][2] - 8th quartet, 4th entry, y-ccord
     *
     *                                                                C==0: value
     * spatialQuartets()[X1][X2][X3] - Ath quartet, Bth entry (of 4), C==1: x-coordinate
     *                                                                C==2: y-coordinate
     *
     *  Useful for Scoring Algos!
     */
    public int[][][] spatialQuartets()
    {
        int size = HEIGHT*(WIDTH-3) + (HEIGHT-3)*WIDTH + 2*(HEIGHT-3)*(WIDTH-3);
        int[][][] answer = new int[size][4][3];
        int iA = 0;

        // vertical
        for ( int j = 0; j < WIDTH; j++ )
        {
            for ( int i = 0; i <= HEIGHT - 4; i++  )
            {
                answer[iA][0][0] = board[j][i];
                answer[iA][0][1] = j;
                answer[iA][0][2] = i;
                answer[iA][1][0] = board[j][i+1];
                answer[iA][1][1] = j;
                answer[iA][1][2] = i+1;
                answer[iA][2][0] = board[j][i+2];
                answer[iA][2][1] = j;
                answer[iA][2][2] = i+2;
                answer[iA][3][0] = board[j][i+3];
                answer[iA][3][1] = j;
                answer[iA][3][2] = i+3;
                iA++;
            }
        }

        // horizontal
        for ( int i = 0; i < HEIGHT; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                answer[iA][0][0] = board[j][i];
                answer[iA][0][1] = j;
                answer[iA][0][2] = i;
                answer[iA][1][0] = board[j+1][i];
                answer[iA][1][1] = j+1;
                answer[iA][1][2] = i;
                answer[iA][2][0] = board[j+2][i];
                answer[iA][2][1] = j+2;
                answer[iA][2][2] = i;
                answer[iA][3][0] = board[j+3][i];
                answer[iA][3][1] = j+3;
                answer[iA][3][2] = i;
                iA++;
            }

        }

        // down-right
        for ( int i = 0; i <= HEIGHT-4; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                answer[iA][0][0] = board[j][i];
                answer[iA][0][1] = j;
                answer[iA][0][2] = i;
                answer[iA][1][0] = board[j+1][i+1];
                answer[iA][1][1] = j+1;
                answer[iA][1][2] = i+1;
                answer[iA][2][0] = board[j+2][i+2];
                answer[iA][2][1] = j+2;
                answer[iA][2][2] = i+2;
                answer[iA][3][0] = board[j+3][i+3];
                answer[iA][3][1] = j+3;
                answer[iA][3][2] = i+3;
                iA++;
            }
        }

        // up right
        for ( int i = 3; i < HEIGHT; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                answer[iA][0][0] = board[j][i];
                answer[iA][0][1] = j;
                answer[iA][0][2] = i;
                answer[iA][1][0] = board[j+1][i-1];
                answer[iA][1][1] = j+1;
                answer[iA][1][2] = i-1;
                answer[iA][2][0] = board[j+2][i-2];
                answer[iA][2][1] = j+2;
                answer[iA][2][2] = i-2;
                answer[iA][3][0] = board[j+3][i-3];
                answer[iA][3][1] = j+3;
                answer[iA][3][2] = i-3;
                iA++;
            }
        }

        return answer;
    }

    /**
     *  Check the state of the game after a piece is placed.
     *
     *  Checks if the board is full, and if there has been a winner, and
     *  sets the appropriate variables.
     */
    private void checkGameState()
    {
        if ( isFull() ) gameOver = true;

        // vertical
        for ( int j = 0; j < WIDTH; j++ )
        {
            for ( int i = 0; i <= HEIGHT - 4; i++  )
            {
                if ( board[j][i] != EMPTY )
                {
                    if ( board[j][i] == board[j][i+1] && 
                    board[j][i] == board[j][i+2] &&
                    board[j][i] == board[j][i+3] )
                    {
                        winner = board[j][i];
                        gameOver = true;
                        return;
                    }
                }
            }
        }

        // horizontal
        for ( int i = 0; i < HEIGHT; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                if ( board[j][i] != EMPTY )
                {
                    if ( board[j][i] == board[j+1][i] && 
                    board[j][i] == board[j+2][i] &&
                    board[j][i] == board[j+3][i] )
                    {
                        winner = board[j][i];
                        gameOver = true;
                        return;
                    }
                }
            }
        }

        // down-right
        for ( int i = 0; i <= HEIGHT-4; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                if ( board[j][i] != EMPTY )
                {
                    if ( board[j][i] == board[j+1][i+1] && 
                    board[j][i] == board[j+2][i+2] &&
                    board[j][i] == board[j+3][i+3] )
                    {
                        winner = board[j][i];
                        gameOver = true;
                        return;
                    }
                }
            }
        }

        // up right
        for ( int i = 3; i < HEIGHT; i++  )
        {
            for ( int j = 0; j <= WIDTH-4; j++ )
            {
                if ( board[j][i] != EMPTY )
                {
                    if ( board[j][i] == board[j+1][i-1] && 
                    board[j][i] == board[j+2][i-2] &&
                    board[j][i] == board[j+3][i-3] )
                    {
                        winner = board[j][i];
                        gameOver = true;
                        return;
                    }
                }
            }
        }

    }

    /**
     *  Returns true if a given board int array is entirely full.
     */
    public static boolean isFull(int[][] board)
    {
        for ( int i = 0; i < WIDTH; i++ )
            if ( board[i][0] == EMPTY )
                return false;
        return true;
    }

    /**
     *  Returns true if a given Board is entirely full.
     */
    public static boolean isFull(Board b)
    {
        return isFull(b.board);
    }

    /**
     *  Returns true if this Board is entirely full.
     */
    public boolean isFull()
    {
        return isFull(this.board);
    }

    /***
     * Returns how the board will look like after a hypothetical move is made: 
     * returns null if the hypothetical move cannot be made.
     */
    public Board ifMoveMade(int col)
    {
        Board copy = this.clone();
        if ( copy.placePiece(col) ) return copy;
        return null;
    }
    /***
     * This version of ifMoveMade returns how the board will look like after a hypothetical move of a color is made: 
     * returns null if the hypothetical move cannot be made.
     */
    public Board ifMoveMade(int col, int color)
    {
        Board copy = this.clone();
        if ( copy.placePiece(col, color) ) return copy;
        return null;
    }

    /**
     * This method returns true if this Board has identical square contents as a given Board.
     */
    public boolean equals(Board other){
        for(int col = 0; col < WIDTH; col++){
            for(int row = 0; row < HEIGHT; row++){
                if(this.board[col][row]!=other.board[col][row])return false;
            }
        }
        return true;
    }
}
