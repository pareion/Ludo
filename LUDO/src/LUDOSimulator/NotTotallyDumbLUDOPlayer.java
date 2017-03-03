package LUDOSimulator;

public class NotTotallyDumbLUDOPlayer implements LUDOPlayer {

	LUDOBoard board;
	private int counter, maxcounter;
	private int[][] tempBoard, previousBoard;
	private float alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
	boolean done = false;
	float bestScoreMin;
	float bestScoreMax;

	public NotTotallyDumbLUDOPlayer(LUDOBoard board) {
		this.board = board;
	}

	@Override
	public void play() {
		board.print("SmartLUDOPlayer playing");
		board.rollDice();

		counter = 0;
		maxcounter = 3;

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
				if (minscore >= bestScore) {
					bestMove = moveAbleBricks[b];
					bestScore = minscore;
				}
				this.counter = 0;
				maxcounter = 0;
			}

		board.moveBrick(bestMove);
	}

	private float MAX() {
		if (isGameOverComputer())
			return 9999;
		else if (maxDepth()) {
			return EVAL();
		} else {

			bestScoreMax = -9999;
			done = false;
			done: if (!done)
				for (int brick = 0; brick < 4; brick++)
					for (int color = 1; color < 4; color++) {
						for (int dice = 1; dice < 7; dice++) {
							previousBoard = tempBoard;
							tempBoard = board.getNewBoardState(tempBoard, brick, color, dice);

							float score = MIN();
							alpha = score;

							if (beta <= alpha) {
								done = true;
								break done;
							}
							
							if (score > bestScoreMax) {
								bestScoreMax = score;
							}

							tempBoard = previousBoard;
						}

					}
			return bestScoreMax;
		}
	}

	private float MIN() {
		if (isGameOverHuman())
			return -9999;
		else if (maxDepth())
			return EVAL();
		else {
			bestScoreMin = 9999;
			done = false;
			done: if (!done)
				for (int i = 0; i < 4; i++) {
					for (int dice = 1; dice < 7; dice++) {
						previousBoard = tempBoard;
						tempBoard = board.getNewBoardState(tempBoard, i, 0, dice);
						float score = MAX();
						beta = score;
						
						if (beta <= alpha) {
							done = true;
							break done;
						}
						if (score < bestScoreMin) {
							bestScoreMin = score;
						}

						tempBoard = previousBoard;
					}

				}
			return bestScoreMin;
		}
	}

	private float EVAL() {
		int points = 0;
		for (int color = 0; color < 4; color++) {
			for (int brick = 0; brick < 4; brick++) {
				points += OpponentsOuts(color, brick);
				points += StandInFrontOfOpponent(color, brick);
				points += HitOpponent(color, brick);
			}

		}
		return points;

	}

	private int HitOpponent(int color, int brick) {
		int index = tempBoard[color][brick];
		int points = 0;
		if (index < 51)
			switch (color) {
			case 0:
				for (int color2 = 1; color2 < 4; color2++) {
					for (int brick2 = 0; brick2 < 4; brick2++) {
						if (tempBoard[color2][brick2] == index) {
							points -= 5;
						}
					}
				}
				break;
			case 1:
				if (index >= 13)
					index -= 13;
				for (int color2 = 0; color2 < 4; color2++) {
					for (int brick2 = 0; brick2 < 4; brick2++) {
						if (tempBoard[color2][brick2] == index) {
							points += 5;
						}
					}
				}
				break;
			case 2:
				if (index >= 26)
					index -= 26;
				for (int color2 = 0; color2 < 4; color2++) {
					for (int brick2 = 0; brick2 < 4; brick2++) {
						if (tempBoard[color2][brick2] == index) {
							points += 5;
						}
					}
				}
				break;
			case 3:
				if (index >= 39)
					index -= 39;
				for (int color2 = 0; color2 < 4; color2++) {
					for (int brick2 = 0; brick2 < 4; brick2++) {
						if (tempBoard[color2][brick2] == index) {
							points += 5;
						}
					}
				}
				break;
			}
		return points;
	}

	private int StandInFrontOfOpponent(int color, int brick) {
		int index = tempBoard[color][brick];
		int points = 0;
		if (index < 51)
			switch (color) {
			case 0:
				for (int color2 = 1; color2 < 4; color2++) {
					for (int brick2 = 0; brick2 < 4; brick2++) {
						if ((tempBoard[color2][brick2] - index) <= 6) {
							points -= (tempBoard[color2][brick2] - index);
						}
					}
				}
				break;
			case 1:
				if (index >= 13)
					index -= 13;
				for (int color2 = 0; color2 < 4; color2++) {
					for (int brick2 = 0; brick2 < 4; brick2++) {
						if ((tempBoard[color2][brick2] - index) <= 6 && color2 != 1) {
							points += (tempBoard[color2][brick2] - index);
						}
					}
				}
				break;
			case 2:
				if (index >= 26)
					index -= 26;
				for (int color2 = 0; color2 < 4; color2++) {
					for (int brick2 = 0; brick2 < 4; brick2++) {
						if ((tempBoard[color2][brick2] - index) <= 6 && color2 != 2) {
							points += (tempBoard[color2][brick2] - index);
						}
					}
				}
				break;
			case 3:
				if (index >= 39)
					index -= 39;
				for (int color2 = 0; color2 < 4; color2++) {
					for (int brick2 = 0; brick2 < 4; brick2++) {
						if ((tempBoard[color2][brick2] - index) <= 6 && color2 != 3) {
							points += (tempBoard[color2][brick2] - index);
						}
					}
				}
				break;
			}
		return points;
	}

	private int OpponentsOuts(int color, int brick) {
		int index = tempBoard[color][brick];
		int points = 0;
		switch (color) {
		case 0:
			if (index > 99 && index < 104)
				points -= 3;
			break;
		case 1:
			if (index > 199 && index < 204)
				points += 3;
			break;
		case 2:
			if (index > 299 && index < 304)
				points += 3;
			break;
		case 3:
			if (index > 399 && index < 404)
				points += 3;
			break;
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

	private boolean isGameOverComputer() {
		boolean result = true;
		for (int i = 0; i < 4; i++) {
			if (tempBoard[0][i] != ((1) * 100 + 9))
				result = false;
		}
		return result;
	}

	private boolean isGameOverHuman() {
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
