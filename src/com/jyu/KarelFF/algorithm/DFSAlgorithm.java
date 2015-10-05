package com.jyu.KarelFF.algorithm;

import java.util.ArrayList;
import java.util.Stack;

import com.jyu.KarelFF.map.Block;

/**
 * 深度优先搜索方法
 */
public class DFSAlgorithm extends LabyrinthAlgorithm implements Algorithm {

	@Override
	public void load(String[] args) {
		super.load(args);
		findWays();
	}

	/**
	 * 查找路径
	 */
	private void findWays() {
		int aDir;
		stk.push(new Pack(getStartLoc().getBlk(), -1));
		while (!stk.isEmpty()) {
			// 取栈顶元素并判断是否为终点
			Pack pack = stk.peek();
			if (pack.getBlk().equals(getEndLoc())) {
				ArrayList<String> ways = getPath();
				for (String s : ways) {
					System.out.println(s);
				}
				return;
			}

			// 查找无墙路口
			boolean find = false;
			aDir = pack.getDir();
			int x = 0;
			int y = 0;
			while (!find && aDir < 4) {
				aDir++;
				x = pack.getBlk().getX();
				y = pack.getBlk().getY();
				Block blk;
				switch (aDir) {
				case 0:
					y++;
					blk = new Block(x, y);
					find = !getWallSet().contains(new Wall(blk, 2));
					break;
				case 1:
					x++;
					blk = new Block(x, y);
					find = !getWallSet().contains(new Wall(blk, 3));
					break;
				case 2:
					y--;
					blk = new Block(x, y);
					find = !getWallSet().contains(new Wall(pack.getBlk(), 2));
					break;
				case 3:
					x--;
					blk = new Block(x, y);
					find = !getWallSet().contains(new Wall(pack.getBlk(), 3));
				}
			}

			// 进退栈
			if (find) {
				pack.setDir(aDir);
				stk.push(new Pack(new Block(x, y), -1));
				getWallSet().add(switchToRightWallObject(x, y, aDir));
			} else {
				Pack now = stk.pop();
				Pack before = stk.peek();
				getWallSet().remove(getTempWall(before, now));
			}
		}
	}

	/**
	 * 转换为正确的墙对象
	 * 
	 * @param x
	 * @param y
	 * @param dir
	 * @return
	 */
	private Wall switchToRightWallObject(int x, int y, int dir) {
		switch (dir) {
		case 0:
			return new Wall(new Block(x, y), 2);
		case 1:
			return new Wall(new Block(x, y), 3);
		case 2:
			return new Wall(new Block(x, y + 1), 2);
		case 3:
			return new Wall(new Block(x + 1, y), 3);
		default:
			return null;
		}
	}

	/**
	 * 获取路径 路径 样例: (MoveEast Blk0101 Blk0201)
	 * 
	 * @return
	 */
	private ArrayList<String> getPath() {
		ArrayList<String> strs = new ArrayList<String>();
		stk.pop();
		while (stk.size() != 0) {
			Pack blk = stk.peek();
			int x = blk.getBlk().getX();
			int y = blk.getBlk().getY();
			strs.add(0, super.switchWay(blk.getDir(), x, y));
			stk.pop();
		}
		return strs;
	}

	/**
	 * 获取相邻临时墙对象
	 * 
	 * @param before
	 * @param now
	 * @return
	 */
	private Wall getTempWall(Pack before, Pack now) {
		int bx = before.getBlk().getX();
		int by = before.getBlk().getY();
		int nx = now.getBlk().getX();
		int ny = now.getBlk().getY();
		if (bx != nx) {
			if (bx - nx > 0) {
				return new Wall(before.getBlk(), 3);
			} else {
				return new Wall(now.getBlk(), 3);
			}
		} else {
			if (by - ny > 0) {
				return new Wall(before.getBlk(), 2);
			} else {
				return new Wall(now.getBlk(), 2);
			}
		}
	}

	// 路径栈
	private Stack<Pack> stk = new Stack<Pack>();
}
