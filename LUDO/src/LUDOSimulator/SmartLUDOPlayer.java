package LUDOSimulator;

import java.util.Random;

public class SmartLUDOPlayer implements LUDOPlayer {

	LUDOBoard board;
	private int depthMin, minCounter;
	private int depthMax, maxCounter;
	private int[][] tempBoard;

	public SmartLUDOPlayer(LUDOBoard board) {
		this.board = board;
	}

	@Override
	public void play() {
		board.print("SmartLUDOPlayer playing");
		board.rollDice();

		depthMin = 3;
		depthMax = 3;
		minCounter = 0;
		maxCounter = 0;

		MiniMax(board);

	}

	private void MiniMax(LUDOBoard board) {
		int bestMove = 0;
		float bestScore = -9999;
		float minscore;
		int counter = 0;		
		int[] moveAbleBricks = null;
		
		//find bricks you can use
		for(int i=0;i<4;i++) // find a random moveable brick
		{
			if(board.moveable(i)) {
				if(moveAbleBricks == null){
					moveAbleBricks = new int[1];
					moveAbleBricks[counter] = i;
					counter++;
				}else
				{
					int[] tmp = new int[counter + 1];
					for(int a = 0; a<moveAbleBricks.length;a++){
						tmp[a] = moveAbleBricks[a];
					}
					tmp[counter] = i;
					moveAbleBricks = tmp;
					counter++;
				}
			}
		}
		
		if(moveAbleBricks != null)
		for (int b = 0; b < moveAbleBricks.length; b++) {
			tempBoard = board.getNewBoardState(board.getBoardState(), moveAbleBricks[b], board.getMyColor(), board.getDice());
			minscore = MIN();
			if (minscore > bestScore) {
				bestMove = moveAbleBricks[b];
				bestScore = minscore;
			}
			tempBoard = board.getNewBoardState(tempBoard, moveAbleBricks[b], board.getMyColor(), -board.getDice());
		}
		minCounter = 0;
		maxCounter = 0;
		board.moveBrick(bestMove);

		// make best move
	}

	private float MIN() {
		if (isGameOverComputer())
			return 999;
		else if (maxDepth(1)) {
			return EVAL();
		} else {
			float bestScore = 9999;
			for (int a = 0; a < 4; a++) {
					tempBoard = board.getNewBoardState(tempBoard, a, 0, board.getDice());
					float score = MAX();
					if (score < bestScore) {
						bestScore = score;
					}
					tempBoard = board.getNewBoardState(tempBoard, a, 0, -board.getDice());
			}
			return bestScore;
		}
	}

	private float MAX() {
		if (isGameOverHuman())
			return -999;
		else if (maxDepth(2))
			return EVAL();
		else {
			float bestScore = -9999;
			for (int color = 1; color < 4; color++) {
				for (int i = 0; i < 4; i++) {
						tempBoard = board.getNewBoardState(tempBoard, i, color, board.getDice());
						float score = MIN();
						if (score > bestScore) {
							bestScore = score;
						}
						tempBoard = board.getNewBoardState(tempBoard, i, color, -board.getDice());
				}
			}
			return bestScore;
		}
	}

	private float EVAL() {
		Random rng = new Random();
		return rng.nextInt(3);
	}

	private boolean maxDepth(int minormax) {
		if (minormax == 1) {
			minCounter++;
			if (minCounter >= depthMin)
				return true;
		} else if (minormax == 2) {
			maxCounter++;
			if (maxCounter >= depthMax)
				return true;
		}
		return false;
	}

	private boolean isGameOverHuman() {
		boolean result = true;
		for (int i = 0; i < 4; i++) {
			if (tempBoard[0][i] != ((1) * 100 + 9))
				result = false;
		}
		return result;
	}

	private boolean isGameOverComputer() {
		boolean result = true;
		for (int color = 1; color < 4; color++) {
			for (int i = 0; i < 4; i++) {
				if (tempBoard[color][i] != ((color + 1) * 100 + 9)) {
					result = false;
				}
			}
		}

		return result;
	}
	/*
	 * private float analyzeBrickSituation(int nrBrick, int color, int dice) {
	 * if (board.moveable(nrBrick)) { int[][] current_board =
	 * board.getBoardState(); int[][] new_board =
	 * board.getNewBoardState(nrBrick, color, dice);
	 * 
	 * if (hitOpponentHome(current_board, new_board)) { return 6; } else if
	 * (hitMySelfHome(current_board, new_board)) { return 0; } else if
	 * (board.isStar(new_board[board.getMyColor()][nrBrick])) { return 5; } else
	 * if (goToGlobe(current_board, new_board)) { return 4; } else if
	 * (moveOut(current_board, new_board)) { return 3; } else if
	 * (board.atHome(new_board[board.getMyColor()][nrBrick],
	 * board.getMyColor())) { return 2; } else { return 1; } } else { return 0;
	 * } }
	 * 
	 * private boolean goToGlobe(int[][] current_board, int[][] new_board) { for
	 * (int i = 0; i < 4; i++) { if
	 * (board.isGlobe(new_board[board.getMyColor()][i])) { return true; } }
	 * return false; }
	 * 
	 * private boolean moveOut(int[][] current_board, int[][] new_board) { for
	 * (int i = 0; i < 4; i++) { if
	 * (board.inStartArea(current_board[board.getMyColor()][i],
	 * board.getMyColor()) &&
	 * !board.inStartArea(new_board[board.getMyColor()][i], board.getMyColor()))
	 * { return true; } } return false; }
	 * 
	 * private boolean hitOpponentHome(int[][] current_board, int[][] new_board)
	 * { for (int i = 0; i < 4; i++) { for (int j = 0; j < 4; j++) { if
	 * (board.getMyColor() != i) { if (board.atField(current_board[i][j]) &&
	 * !board.atField(new_board[i][j])) { return true; } } } } return false; }
	 * 
	 * private boolean hitMySelfHome(int[][] current_board, int[][] new_board) {
	 * for (int i = 0; i < 4; i++) { if
	 * (!board.inStartArea(current_board[board.getMyColor()][i],
	 * board.getMyColor()) &&
	 * board.inStartArea(new_board[board.getMyColor()][i], board.getMyColor()))
	 * { return true; } } return false; }
	 */

}
