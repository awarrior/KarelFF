package com.jyu.KarelFF.tool;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jyu.KarelFF.map.Block;
import com.jyu.KarelFF.map.Dimension;
import com.jyu.KarelFF.map.Direction;
import com.jyu.KarelFF.map.Location;
import com.jyu.KarelFF.map.Wall;




public class BlockPanel extends JPanel
{
	public BlockPanel()
	{
		super();
	}
	
	/**
	 * paint boxes
	 */
	public void paintBlock(int numX, int numY)
	{
		this.numX = numX;
		this.numY = numY;
		calcSide();
		calcInitLoc();
		repaint();
	}
	
	/**
	 * paint boxes - numX change
	 */
	public void paintBlockX(int numX)
	{
		this.numX = numX;
		calcSide();
		calcInitLoc();
		repaint();
	}

	/**
	 * paint boxes - numY change
	 */
	public void paintBlockY(int numY)
	{
		this.numY = numY;
		calcSide();
		calcInitLoc();
		repaint();
	}
	
	/**
	 * paint walls
	 */
	public void paintWall(int x, int y, Direction drt)
	{
		aWallList.add(new Wall(new Block(x, y), drt));
		
		int blockX = x - 1;
		int blockY = numY - y;
		int locX = initLocX + blockX * side;
		int locY = initLocY + blockY * side;
		switch (drt)
		{
		/*case NORTH:
			aLineList.add(new Line(locX, locY, locX+side, locY));
			break;
		case EAST:
			aLineList.add(new Line(locX+side, locY, locX+side, locY+side));
			break;*/
		case SOUTH:
			aLineList.add(new Line(locX, locY+side, locX+side, locY+side));
			break;
		case WEST:
			aLineList.add(new Line(locX, locY, locX, locY+side));
			break;
		default:
			break;
		}
		repaint();
	}
	
	/**
	 * judge the direction
	 */
	private double judgeDirection(Direction drt)
	{
		switch(drt)
		{
		case NORTH:
			return -90;
		case EAST:
			return 0;
		case SOUTH:
			return 90;
		case WEST:
			return 180;
		default:
			return -1;
		}
	}
	
	/**
	 * locate Karel
	 */
	public void locateKarel(int x, int y, Direction drt)
	{
		nowLocX = x;
		nowLocY = y;
		loc = new Location(new Block(x, y), drt);
		
		int blockX = x - 1;
		int blockY = numY - y;
		locXKarel = initLocX + blockX * side + 1;
		locYKarel = initLocY + blockY * side + 1;
		try
		{
			String tmpFileName = "KarelTmp.png";
			InputStream ist = getClass().getResourceAsStream(tmpFileName);
			Image tmpImg;
			if (ist == null)
				tmpImg = ImageIO.read(new File(tmpFileName));
			else
				tmpImg = ImageIO.read(ist);
			tempImg = tmpImg.getScaledInstance(side-2, side-2, Image.SCALE_AREA_AVERAGING);
			
			String fileName = "Karel.png";
			InputStream is = getClass().getResourceAsStream(fileName);
			Image img;
			if (is == null)
				img = ImageIO.read(new File(fileName));
			else
				img = ImageIO.read(is);
			scaledImg = img.getScaledInstance(side-2, side-2, Image.SCALE_AREA_AVERAGING);
			angle = judgeDirection(drt);
			repaint();
		} 
		catch (IOException e)
		{
			System.out.println("load Karel.jpg error");
			e.printStackTrace();
		}
	}
	
	/**
	 * repaint the component
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		repaintBlocks(g);
		repaintWalls(g);
		repaintKarel(g);
		g.drawLine(tempLine.getbX(), tempLine.getbY(), tempLine.geteX(), tempLine.geteY());
		if (inKarel)
		{
			tempKarel(g);
		}
	}
	
	/**
	 * repaint blocks
	 */
	private void repaintBlocks(Graphics g)
	{
		g.setColor(new Color(240,240,240));
		int locY = initLocY;
		for (int i=0; i<numY; i++)
		{
			int locX = initLocX;
			for (int j=0; j<numX; j++)
			{
				g.drawRect(locX, locY, side, side);
				locX += side;
			}
			locY += side;
		}
	}
	
	/**
	 * repaint walls
	 */
	private void repaintWalls(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawRect(initLocX, initLocY, numX*side, numY*side);
		for (int k = 0; k < aLineList.size(); k++)
		{
			Line l = aLineList.get(k);
			g.drawLine(l.getbX(), l.getbY(), l.geteX(), l.geteY());
		}
	}
	
	/**
	 * repaint Karel
	 */
	private void repaintKarel(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform origXform = g2.getTransform();
	    AffineTransform newXform = (AffineTransform)(origXform.clone());

	    int xRot = locXKarel + side / 2;
	    int yRot = locYKarel + side / 2;
	    newXform.rotate(Math.toRadians(angle), xRot, yRot);
	    g2.setTransform(newXform);
	    
	    g2.drawImage(scaledImg, locXKarel, locYKarel, this);
	    g2.setTransform(origXform);
	}
	
	/**
	 * calculate the side
	 */
	private void calcSide()
	{
		boolean horizon = (numX >= numY ? true : false);
		if (horizon)
		{
			side = maxlen / numX;
		}
		else 
		{
			side = maxlen / numY;
		}
	}
	
	/**
	 * calculate the start painting location
	 */
	private void calcInitLoc()
	{
		initLocX = (widthLen - numX * side) / 2;
		initLocY = (heightLen - numY * side) / 2;
	}
	
	/**
	 * Karel run
	 */
	public void runKarel(int time, String direction, String filename)
	{
		try
		{
			File action = new File(direction, filename);
			br = new BufferedReader(new FileReader(action));
			
			aTimer = new Timer(true); 
			aTimer.schedule(new TimerTask() {
				@Override
				public void run()
				{
					try
					{
						String line;
						do
						{
							line = br.readLine();
							if (line == null)
							{
								cancel();
								return;
							}
						} while (line == "" || !isCmdLine(line));
						if (!actionFilter(line))
						{
							cancel();
							return;
						}
					} catch (Exception e)
					{
						System.out.println("read action line error");
						e.printStackTrace();
					}
				}
				}, 0, time);
		}
		catch (IOException e) 
		{
			System.out.println("read action error");
			e.printStackTrace();
		}
	}
	
	/**
	 * judge whether line is available
	 */
	private boolean isCmdLine(String line)
	{
		if (line.equals("move") || line.equals("turnLeft") || line.equals("turnRight"))
			return true;
		else
			return false;
	}
	
	/**
	 * Karel actions filter
	 */
	private boolean actionFilter(String line)
	{
		boolean success = true;
		if (line.equals("move"))
			success = move();
		if (line.equals("turnLeft"))
			success = turnLeft();
		if (line.equals("turnRight"))
			success = turnRight();
		return success;
	}
	
	/**
	 * judge whether Karel can move
	 */
	private boolean allowMove()
	{
		Block tempLine;
		switch (loc.getDrt())
		{
		case NORTH:
			if (loc.getBlk().getY() >= numY)
				return false;
			tempLine = new Block(loc.getBlk().getX(), loc.getBlk().getY()+1);
			if (checkWall(tempLine, Direction.SOUTH))
				return false;
			break;
		case EAST:
			if (loc.getBlk().getX() >= numX)
				return false;
			tempLine = new Block(loc.getBlk().getX()+1, loc.getBlk().getY());
			if (checkWall(tempLine, Direction.WEST))
				return false;
			break;
		case SOUTH:
			if (loc.getBlk().getY() <= 1)
				return false;
			if (checkWall(loc.getBlk(), Direction.SOUTH))
				return false;
			break;
		case WEST:
			if (loc.getBlk().getX() <= 1)
				return false;
			if (checkWall(loc.getBlk(), Direction.WEST))
				return false;
			break;
		default:
			break;
		}
		return true;
	}
	
	/**
	 * check whether there is a wall
	 */
	private boolean checkWall(Block blk, Direction dir)
	{
		for (int i = 0; i < aWallList.size(); i++)
		{
			Wall aWall = aWallList.get(i);
			if (aWall.getBlk().equals(blk))
			{
				if (aWall.getDrt() == dir)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Karel move
	 */
	private boolean move()
	{
		if (allowMove())
		{
			int x,y;
			switch (loc.getDrt())
			{
			case NORTH:
				locYKarel -= side;
				y = loc.getBlk().getY();
				loc.getBlk().setY(++y);
				break;
			case EAST:
				locXKarel += side;
				x = loc.getBlk().getX();
				loc.getBlk().setX(++x);
				break;
			case SOUTH:
				locYKarel += side;
				y = loc.getBlk().getY();
				loc.getBlk().setY(--y);
				break;
			case WEST:
				locXKarel -= side;
				x = loc.getBlk().getX();
				loc.getBlk().setX(--x);
				break;
			default:
				break;
			}
			repaint();
			return true;
		}
		else 
		{
			JOptionPane.showMessageDialog(null, "There is a wall！", "KarelFF", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	 * Karel turnLeft
	 */
	private boolean turnLeft()
	{
		angle = judgeDirection(loc.getDrt()) - 90;
		if (angle == -180)
			angle = 180;
		changeDrt(angle);
		repaint();
		return true;
	}
	
	/**
	 * Karel turnRight
	 */
	private boolean turnRight()
	{
		angle = judgeDirection(loc.getDrt()) + 90;
		if (angle == 270)
			angle = -90;
		changeDrt(angle);
		repaint();
		return true;
	}
	
	/**
	 * direction change
	 */
	private void changeDrt(double angle)
	{
		switch ((int)angle)
		{
		case -90:
			loc.setDrt(Direction.NORTH);
			break;
		case 0:
			loc.setDrt(Direction.EAST);
			break;
		case 90:
			loc.setDrt(Direction.SOUTH);
			break;
		case 180:
			loc.setDrt(Direction.WEST);
			break;
		default:
			break;
		}
	}
	
	/**
	 * delete painted walls
	 */
	public void delWalls()
	{
		aLineList.clear();
		aWallList.clear();
	}
	
	/**
	 * over the map to set a wall
	 */
	public void overMap(Point point)
	{
		judgePointToArea(point);
		repaint();
	}
	
	/**
	 * judge the point belongs to which area
	 *  __________
	 * |\	     /|
	 * |  \    /  |
	 * |    \/	  |
	 * |   /  \   |
	 * | /      \ |
	 * |__________|
	 */
	private void judgePointToArea(Point point)
	{
		int x = (int)point.getX() - initLocX;
		int y = (int)point.getY() - initLocY;
		
		if (x >= numX*side || y >= numY*side)
		{
			return;
		}
		
		int bNumX = x / side + 1;
		int bNumY = numY - y / side;
		int bX = (x / side) * side + initLocX;
		int bY = (y / side) * side + initLocY;
		
		x = x % side;
		y = y % side;
		
		if (0<=x && x<side && 0<=y && y<0.5*side && y<=x && y<side-x)
		{
			tempLine = new Line(bX, bY, bX+side, bY);
			tempWall = new Wall(new Block(bNumX, bNumY+1), Direction.SOUTH);
		}
		if (0.5*side<x && x<=side && 0<=y && y<side && y<x && y>=side-x)
		{
			tempLine = new Line(bX+side, bY, bX+side, bY+side);
			tempWall = new Wall(new Block(bNumX+1, bNumY), Direction.WEST);
		}
		if (0<x && x<=side && 0.5*side<y && y<=side && y>=x && y>side-x)
		{
			tempLine = new Line(bX, bY+side, bX+side, bY+side);
			tempWall = new Wall(new Block(bNumX, bNumY), Direction.SOUTH);
		}
		if (0<=x && x<0.5*side && 0<y && y<=side && y>x && y<=side-x)
		{
			tempLine = new Line(bX, bY, bX, bY+side);
			tempWall = new Wall(new Block(bNumX, bNumY), Direction.WEST);
		}
	}
	
	/**
	 * mark a wall
	 */
	public void markWall()
	{
		for (int i = 0; i < aLineList.size(); i++)
		{
			Line a = aLineList.get(i);
			if (a.equals(tempLine))
			{
				aLineList.remove(i);
				aWallList.remove(i);
				return;
			}
		}
		aLineList.add(tempLine);
		aWallList.add(tempWall);
	}
	
	/**
	 * clear the temp wall
	 */
	public void clearTempWall()
	{
		tempLine = new Line(0, 0, 0, 0);
		tempWall = new Wall();
	}
	
	/**
	 * over the map to locate Karel
	 */
	public void locateKarel(Point point)
	{
		judgeLocation(point);
		repaint();
	}
	
	/**
	 * judge the point belongs to which area
	 */
	private void judgeLocation(Point point)
	{
		int x = (int)point.getX() - initLocX;
		int y = (int)point.getY() - initLocY;
		
		if (x >= numX*side || y >= numY*side)
		{
			inKarel = false;
			return;
		}
		else 
		{
			inKarel = true;
			tempLocX = x - x % side + 2 + initLocX;
			tempLocY = y - y % side + 2 + initLocY;
			
			nowLocX = x / side + 1;
			nowLocY = numY - y / side;
		}
	}
	
	/**
	 * draw a temp Karel
	 */
	private void tempKarel(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform origXform = g2.getTransform();
	    AffineTransform newXform = (AffineTransform)(origXform.clone());

	    int xRot = tempLocX + side / 2;
	    int yRot = tempLocY + side / 2;
	    newXform.rotate(Math.toRadians(angle), xRot, yRot);
	    g2.setTransform(newXform);
	    
	    g2.drawImage(tempImg, tempLocX, tempLocY, this);
	    g2.setTransform(origXform);
	}
	
	/**
	 * mark Karel
	 */
	public void markKarel()
	{
		if (locXKarel == tempLocX && locYKarel == tempLocY)
		{
			turnRight();
		}
		else 
		{
			locXKarel = tempLocX;
			locYKarel = tempLocY;
		}
		
		loc.setBlk(new Block(nowLocX, nowLocY));
	}
	
	/**
	 * random walls
	 */
	public void random(int x, int y)
	{
		delWalls();
		// sum of random walls
		int sum = x * y;
		Random generator = new Random();
		for (int i = 0; i < sum; i++)
		{
			int nextX = generator.nextInt(x+1)+1;
			int nextY = generator.nextInt(y+1)+1;
			int nextDrt = generator.nextInt(2);
			Direction d = null;
			switch (nextDrt)
			{
			case 0:
				d = Direction.WEST;
				break;
			case 1:
				d = Direction.SOUTH;
			}
			
			boolean flag = false;
			for (int j = 0; j < aWallList.size(); j++)
			{
				Wall w = aWallList.get(j);
				if (w.getBlk().getX() == nextX && w.getBlk().getY() == nextY && w.getDrt() == d)
				{
					flag = true;
					break;
				}
			}
			if (flag)
			{
				continue;
			}
			
			paintWall(nextX, nextY, d);
		}
	}
	
	public int getLocXKarel()
	{
		return locXKarel;
	}
	
	public int getLocYKarel()
	{
		return locYKarel;
	}
	
	public Timer getaTimer()
	{
		return aTimer;
	}

	public Dimension getDimension()
	{
		Dimension d = new Dimension(numX, numY);
		return d;
	}
	
	public ArrayList<Wall> getaWallList()
	{
		return aWallList;
	}
	
	public Location getLoc()
	{
		return loc;
	}
	
	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public void setInKarel(boolean inKarel)
	{
		this.inKarel = inKarel;
	}

	public void setMaxlen(int maxlen) {
		this.maxlen = maxlen;
	}

	public int getNumX() {
		return numX;
	}

	public int getNumY() {
		return numY;
	}

	public void setWidthLen(int widthLen) {
		this.widthLen = widthLen;
	}

	public void setHeightLen(int heightLen) {
		this.heightLen = heightLen;
	}

	private int widthLen;		// 背景宽度
	private int heightLen;		// 背景高度
	private int maxlen = 427;	// 最大边长
	private int side;			// 边长
	private int initLocX;		// 最左上block左上角 - 水平起点
	private int initLocY;		// 最左上block左上角 - 垂直起点
	private int numX;			// 水平block数目
	private int numY;			// 垂直block数目
	private ArrayList<Line> aLineList = new ArrayList<BlockPanel.Line>();	// 线段
	private ArrayList<Wall> aWallList = new ArrayList<Wall>();				// 墙体
	private Image scaledImg;	// Karel图像
	private int locXKarel;		// Karel左上角x坐标
	private int locYKarel;		// Karel左上角y坐标
	private double angle;		// Karel转向角度
	private Location loc;		// Karel位置
	private BufferedReader br;	// 读取action文件
	private Timer aTimer = null;		// 定时器
	private Line tempLine = new Line(0, 0, 0, 0); // 临时边，用于设定墙体
	private Wall tempWall = new Wall();	// 临时墙体
	private Image tempImg;		// 临时Karel
	private int tempLocX;		// 临时x坐标
	private int tempLocY;		// 临时y坐标
	private boolean inKarel = false;	// 是否在map中
	private int nowLocX;		// 当前所在block - 水平
	private int nowLocY;		// 当前所在block - 垂直
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * a line as a wall
	 */
	private class Line
	{
		public Line(int bX, int bY, int eX, int eY)
		{
			this.bX = bX;
			this.bY = bY;
			this.eX = eX;
			this.eY = eY;
		}
		
		public int getbX()
		{
			return bX;
		}
		
		public int getbY()
		{
			return bY;
		}
		
		public int geteX()
		{
			return eX;
		}
		
		public int geteY()
		{
			return eY;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			
			Line l = (Line)obj;
			if (bX == l.getbX() && bY == l.getbY() && eX == l.geteX() && eY == l.geteY())
			{
				return true;
			}
			else 
			{
				return false;
			}
		}
		
		private int bX;	// 起点x轴坐标
		private int bY;	// 起点y轴坐标
		private int eX;	// 终点x轴坐标
		private int eY;	// 终点y轴坐标
	}
}
