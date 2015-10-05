package com.jyu.KarelFF.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.jyu.KarelFF.map.Block;
import com.jyu.KarelFF.map.Direction;
import com.jyu.KarelFF.map.Location;
import com.jyu.KarelFF.tool.Directory;

public class LabyrinthAlgorithm implements Algorithm {

	@Override
	public void load(String[] args) {
		readMap();
		setStartLoc(args[2], args[3], args[4]);
		setEndLoc(args[5], args[6]);
	}

	/**
	 * 设置出口位置
	 * 
	 * @param a
	 * @param b
	 */
	private void setEndLoc(String a, String b) {
		endLoc = new Block(Integer.parseInt(a), Integer.parseInt(b));
	}

	/**
	 * 地图读取
	 */
	private void readMap() {
		try {
			File file = new File(Directory.getDefMaps(), Directory.getDefMAP());
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.contains("Dimension")) {
					addOutsideWalls(line);
				} else if (line.contains("Wall")) {
					addWalls(line);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置开始位置
	 * 
	 * @param a
	 * @param b
	 * @param dir
	 */
	private void setStartLoc(String a, String b, String dir) {
		int x = Integer.parseInt(a);
		int y = Integer.parseInt(b);
		Direction d = null;
		switch (dir.charAt(0)) {
		case 'N':
			d = Direction.NORTH;
			break;
		case 'E':
			d = Direction.EAST;
			break;
		case 'S':
			d = Direction.SOUTH;
			break;
		case 'W':
			d = Direction.WEST;
		}
		startLoc = new Location(new Block(x, y), d);
	}

	/**
	 * 添加外围墙
	 * 
	 * @param line
	 */
	private void addOutsideWalls(String line) {
		int blockBegin = line.indexOf('(');
		int blockEnd = line.indexOf(')');
		String blockStr = line.substring(blockBegin + 1, blockEnd);
		int comma = blockStr.indexOf(',');
		// 水平垂直的块数
		int numX = Integer.parseInt(blockStr.substring(0, comma));
		int numY = Integer.parseInt(blockStr.substring(comma + 2));

		int count = 1;
		while (count <= numX) {
			wallSet.add(new Wall(new Block(count, 1), 2));
			wallSet.add(new Wall(new Block(count, numY + 1), 2));
			count++;
		}
		count = 1;
		while (count <= numY) {
			wallSet.add(new Wall(new Block(1, count), 3));
			wallSet.add(new Wall(new Block(numX + 1, count), 3));
			count++;
		}
	}

	/**
	 * 添加墙
	 * 
	 * @param line
	 */
	private void addWalls(String line) {
		int blockBegin = line.indexOf('(');
		int blockEnd = line.indexOf(')');
		String blockStr = line.substring(blockBegin + 1, blockEnd);
		String dirStr = line.substring(blockEnd + 2);
		// 放置转换的墙对象到集合
		wallSet.add(switchToWall(blockStr, dirStr));
	}

	/**
	 * 转换数值为墙对象
	 * 
	 * @param blockStr
	 * @param dirStr
	 * @return
	 */
	private Wall switchToWall(String blockStr, String dirStr) {
		int comma = blockStr.indexOf(','); // 逗号位置
		int x = Integer.parseInt(blockStr.substring(0, comma));
		int y = Integer.parseInt(blockStr.substring(comma + 2));
		Block blk = new Block(x, y);
		int dir = -1;
		switch (dirStr.charAt(0)) {
		case 's':
			dir = 2;
			break;
		case 'w':
			dir = 3;
			break;
		}

		return new Wall(blk, dir);
	}

	/**
	 * 路径转换
	 * 
	 * @param dir
	 * @param x
	 * @param y
	 * @return
	 */
	protected String switchWay(int dir, int x, int y) {
		String move = "";
		switch (dir) {
		case 0:
			move = "(MoveNorth ";
			break;
		case 1:
			move = "(MoveEast ";
			break;
		case 2:
			move = "(MoveSouth ";
			break;
		case 3:
			move = "(MoveWest ";
		}
		String xStr = (x / 10 == 0) ? ("0" + x) : ("" + x);
		String yStr = (y / 10 == 0) ? ("0" + y) : ("" + y);
		return move + "Blk" + xStr + " Blk" + yStr + ")";
	}

	public Set<Wall> getWallSet() {
		return wallSet;
	}

	public void setWallSet(Set<Wall> wallSet) {
		this.wallSet = wallSet;
	}

	public Location getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(Location startLoc) {
		this.startLoc = startLoc;
	}

	public Block getEndLoc() {
		return endLoc;
	}

	public void setEndLoc(Block endLoc) {
		this.endLoc = endLoc;
	}

	// 障碍墙体集合
	private Set<Wall> wallSet = new HashSet<Wall>();
	// 入口
	private Location startLoc;
	// 出口
	private Block endLoc;
}

/**
 * 墙类
 */
class Wall {

	public Wall() {
		super();
	}

	public Wall(Block blk, int drt) {
		super();
		this.blk = blk;
		this.drt = drt;
	}

	public int getDrt() {
		return drt;
	}

	public void setDrt(int drt) {
		this.drt = drt;
	}

	public Block getBlk() {
		return blk;
	}

	public void setBlk(Block blk) {
		this.blk = blk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blk == null) ? 0 : blk.hashCode());
		result = prime * result + drt;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wall other = (Wall) obj;
		if (blk == null) {
			if (other.blk != null)
				return false;
		} else if (!blk.equals(other.blk))
			return false;
		if (drt != other.drt)
			return false;
		return true;
	}

	private Block blk;
	// drt = -1 : no direction
	// drt = 0 : north
	// drt = 1 : east
	// drt = 2 : south
	// drt = 3 : west
	private int drt;
}

/**
 * 临时打包类
 */
class Pack {

	public Pack() {
		super();
	}

	public Pack(Block blk, int dir) {
		super();
		this.blk = blk;
		this.dir = dir;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public Block getBlk() {
		return blk;
	}

	public void setBlk(Block blk) {
		this.blk = blk;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Pack pack = (Pack) obj;
		if (pack.getBlk().equals(blk) && dir == pack.getDir())
			return true;
		else
			return false;
	}

	private Block blk;
	// dir = -1 : no direction
	// dir = 0 : north
	// dir = 1 : east
	// dir = 2 : south
	// dir = 3 : west
	private int dir;
}
