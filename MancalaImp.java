import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

public class MancalaImp implements MancalaAgent{
        public static void main(String args[]){
    }
        	final static int highestDepth = 8;
        	static int[] mainboard;

    public int move(int[] board) {
    	mainboard = board;
        int minmaxvalue = minMax(Integer.MIN_VALUE,Integer.MAX_VALUE,highestDepth,true);
        return minmaxvalue;
    }


    public String name(){return "Bob the bad bot";}


    public void reset() {
    	mainboard = null;
        
    }

    protected static int evalFunc(){
        int biggestStore = 0;
        for(int i = 0;i<5;i++){
        	if(mainboard[i]!=0){
         int currentSeeds = mainboard[i];
         int evalSeeds = 0;
         	for(int count = 1; count <= currentSeeds;count++){
         		if( i + count < 6  && count == currentSeeds &&mainboard[i+count] == 0 && mainboard[i+count+7] != 0) //If last seeds end up on empty slot, loot opponents store
         		{
         			evalSeeds = mainboard[6] + mainboard[i+count+7] + 1;
         		}
         		else{evalSeeds = mainboard[6];}
                if(evalSeeds>biggestStore) biggestStore = evalSeeds;
         		}	
        	}
        }
        return  biggestStore;
    }
    
     private static ArrayList<Integer> generateLegalMoves(boolean maximiser){
         if(!maximiser)
         {
          invertBoard();
         }//Flip the board if it is the opponent and minimiser
        ArrayList<Integer> moves = new ArrayList<Integer>();
        for(int i = 0;i<6;i++){
         if(mainboard[i] != 0){
            moves.add(i);
            }
        }
        return moves;
    }
    
    static void undoLastMove(int pastmove, int seeds){
             if(seeds > 100)
             {
            	 int opponentseed = seeds%100;
              seeds = (seeds-opponentseed) / 100; //convert to normal seed amount  
              mainboard[pastmove+seeds+7] = opponentseed;
              mainboard[pastmove+seeds] = 1;
              mainboard[6] -= (opponentseed + 1);
             }
             mainboard[pastmove] = seeds;
        for(int count = seeds; count > 0;count--){
            mainboard[(++pastmove)%14]--;
        }
       
    }
    
    static int applyMove(int move){
        int seeds = mainboard[move];
        mainboard[move] = 0;
        for(int count = seeds; count > 0;count--){
            if(count == 1 && move < 5 && mainboard[move+1] == 0 && mainboard[move+1+7] != 0) //If last seeds end up on empty slot, loot opponents store
             {
              mainboard[6] += mainboard[(move+8)] + 1;
              seeds=seeds*100+mainboard[move+8]; //A large seed number signifies that opponent is looted 
              mainboard[(move+8)%14] = 0;
              break;
             }
            mainboard[(++move)%14]++;
        }
        return seeds;
    }
    
      private static void invertBoard(){
    int[] bd = new int[14];
    for(int i = 0; i<7;i++){
      bd[i] = mainboard[7+i];
      bd[7+i] = mainboard[i];
    }	
    mainboard = Arrays.copyOf(bd,bd.length);
   }
    
    protected static int minMax(double alpha, double beta, int maxDepth, boolean isMaximizer) {       
        if (maxDepth == 0){         
        	return evalFunc();
        }
     ArrayList<Integer> moves = generateLegalMoves(isMaximizer);   //If it is the minimizer (opponent) then flip board as well
     Iterator<Integer> movesIterator = moves.iterator();
     int returnMove;
     int bestMoveValue = -1;
     int bestMove = 0;
      if (isMaximizer) {   
        while (movesIterator.hasNext()) {
            int currentMove = movesIterator.next();
            int seeds = applyMove(currentMove);
            returnMove = minMax(alpha, beta, maxDepth - 1, !isMaximizer); //The opponent is a minimizer (set as false)
            undoLastMove(currentMove, seeds);
            if ((bestMoveValue == -1) || (bestMoveValue < returnMove)) {
                bestMoveValue = returnMove;
                bestMove = currentMove;
            }
            if (returnMove > alpha) {
                alpha = returnMove;
                bestMoveValue = returnMove;
                bestMove = currentMove;
            }
            if (beta <= alpha) {
                bestMoveValue = (int) beta;
                return bestMoveValue; // pruning
            }
        }
        if(maxDepth == highestDepth){
        	return bestMove;
        }
        return bestMoveValue;
    } else {
        while (movesIterator.hasNext()) {
            int currentMove = movesIterator.next();
            int seeds = applyMove(currentMove);
            returnMove = minMax(alpha, beta, maxDepth - 1, !isMaximizer); //The opponent is a minimizer (set as false)
            undoLastMove(currentMove, seeds);
            if ((bestMoveValue == -1) || (bestMoveValue > returnMove)) {
                bestMoveValue = returnMove;
                bestMove = currentMove;
            }
            if (returnMove < alpha) {
                alpha = returnMove;
                bestMoveValue = returnMove;
                bestMove = currentMove;
            }
            if (beta <= alpha) {
                bestMoveValue = (int) alpha;
                return bestMoveValue; // pruning
            }
        }
        invertBoard();  //Reflect back
        if(maxDepth == highestDepth){
        	return bestMove;
        }
        return bestMoveValue;
    }    

 }
}
