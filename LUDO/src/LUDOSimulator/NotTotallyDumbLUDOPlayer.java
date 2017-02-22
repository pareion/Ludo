package LUDOSimulator;

import java.util.Random;

public class NotTotallyDumbLUDOPlayer implements LUDOPlayer {

	LUDOBoard board;
	private int counter, maxcounter;
	private int[][] tempBoard;

	public NotTotallyDumbLUDOPlayer(LUDOBoard board) {
		this.board = board;
	}

	@Override
	public void play() {
		board.print("SmartLUDOPlayer playing");
		board.rollDice();

		counter = 0;
		maxcounter = 30;

		MiniMax(board);

	}

	private void MiniMax(LUDOBoard board) {
		int bestMove = 0;
		float bestScore = -9999;
		float minscore;
		int counter = 0;
		int[] moveAbleBricks = null;

		// find bricks you can use
		for (int i = 0; i < 4; i++) {
			if (board.moveable(i)) {
				if (moveAbleBricks == null) {
					moveAbleBricks = new int[1];
					moveAbleBricks[counter] = i;
					counter++;
				} else {
					int[] tmp = new int[counter + 1];
					for (int a = 0; a < moveAbleBricks.length; a++) {
						tmp[a] = moveAbleBricks[a];
					}
					tmp[counter] = i;
					moveAbleBricks = tmp;
					counter++;
				}
			}
		}

		if (moveAbleBricks != null)
			for (int b = 0; b < moveAbleBricks.length; b++) {
				tempBoard = board.getNewBoardState(board.getBoardState(), moveAbleBricks[b], board.getMyColor(),
						board.getDice());
				minscore = MIN();
				if (minscore > bestScore) {
					bestMove = moveAbleBricks[b];
					bestScore = minscore;
				}
				// TODO se om man står på en stjerne og ryk +x felter tilbage
				// tempBoard = board.getNewBoardState(tempBoard,
				// moveAbleBricks[b], board.getMyColor(), -board.getDice());
			}
		this.counter = 0;
		maxcounter = 0;
		board.moveBrick(bestMove);
	}

	private float MAX() {
		if (isGameOverComputer())
			return -999;
		else if (maxDepth()) {
			return EVAL();
		} else {
			float bestScore = -9999;
			for (int brick = 0; brick < 4; brick++)
				for (int color = 1; color < 4; color++)
					for (int dice = 1; dice < 7; dice++) {

						tempBoard = board.getNewBoardState(tempBoard, brick, color, dice);

						float score = MIN();
						if (score > bestScore) {
							bestScore = score;
						}
						tempBoard = board.getNewBoardState(tempBoard, brick, color, -dice);
					}
			return bestScore;
		}
	}

	private float MIN() {
		if (isGameOverHuman())
			return 999;
		else if (maxDepth())
			return EVAL();
		else {
			float bestScore = 9999;
			for (int i = 0; i < 4; i++) {
				for (int dice = 1; dice < 7; dice++) {
					tempBoard = board.getNewBoardState(tempBoard, i, 0, dice);
					float score = MAX();
					if (score < bestScore) {
						bestScore = score;
					}
					tempBoard = board.getNewBoardState(tempBoard, i, 0, -dice);
				}
			}

			return bestScore;
		}
	}

	private float EVAL() {
		int points = 0;
		for (int color = 0; color < 4; color++)
			for (int i = 0; i < 4; i++) {
				int index = tempBoard[color][i];
				if (color == 0) {
					if(board.atHome(index, color))
						points += 90;
					if(board.isGlobe(index))
						points -= 80;
					if(board.isStar(index))
						points -= 50;
					if (index >= 37 && index < 51)
						points -= 70;
					else if (index >= 24 && index < 51)
						points -= 60;
					else if (index >= 11 && index < 51)
						points -= 40;
					else if (index >= 0 && index < 51)
						points -= 30;
					if(index < 103)
						points -= 100;
				}else{
					if(color == 1 && index < 13)
						index -= 13;
					if(color == 2 && index < 26)
						index -= 16;
					if(color == 3 && index < 39)
						index -= 39;
					if(board.atHome(index, color))
						points -= 90;
					if(board.isGlobe(index))
						points += 80;
					if(board.isStar(index))
						points += 50;
					if (index >= 37 && index < 51)
						points += 70;
					else if (index >= 24 && index < 51)
						points += 60;
					else if (index >= 11 && index < 51)
						points += 40;
					else if (index >= 0 && index < 51)
						points += 30;
					if(index < 203 && index < 210 && color == 1)
						points += 100;
					if(index < 303 && index < 310 && color == 2)
						points += 100;
					if(index < 403 && index < 410 && color == 3)
						points += 100;
				}

				
			}

		return points;

	}

	private boolean maxDepth() {
		if (counter >= maxcounter)
			return true;
		else
			counter++;
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

}
