package com.jyu.KarelFF.map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.jyu.KarelFF.tool.Directory;



public class Map
{
	/**
	 * update dimension in the map
	 */
	public void setDimension(int numX, int numY)
	{
		try
		{
			String targetX = Integer.toString(numX);
			String targetY = Integer.toString(numY);
			modifyDi(targetX, targetY);
		} 
		catch (IOException e)
		{
			System.out.println("update Dimension in the map file fail");
			e.printStackTrace();
		}
	}
	
	/**
	 * modify Dimension
	 */
	private void modifyDi(String targetX, String targetY) throws IOException
	{
		String key = "Dimension";
		File map = new File(direction, filename);
		BufferedReader br = new BufferedReader(new FileReader(map));
		int len = key.length();
		ArrayList<String> lines = new ArrayList<String>();
		while(true)
		{
			String line = br.readLine();
			if (line == null)
			{
				break;
			}
			String temp = line.substring(0, len);
			if (temp.equals(key))
			{
				line = "Dimension: (" + targetX + ", " + targetY + ")";
			}
			lines.add(line);
		}
		br.close();
		PrintWriter out = new PrintWriter(new FileOutputStream(map));
		for(int i=0; i<lines.size(); i++)
		{
			out.println(lines.get(i));
		}
		out.flush();
		out.close();
	}
	
	/**
	 * update speed
	 */
	public void setSpeed(int speed)
	{
		try
		{
			String target = Integer.toString(speed);
			if (target.length() == 1)
			{
				target = "0" + target;
			}
			modifySp("Speed", target);
			this.speed = Double.parseDouble("0."+target);
		} 
		catch (IOException e)
		{
			System.out.println("update Speed fail");
			e.printStackTrace();
		}
	}
	
	/**
	 * modify speed
	 */
	private void modifySp(String key, String target) throws IOException
	{
		File map = new File(direction, filename);
		BufferedReader br = new BufferedReader(new FileReader(map));
		int len = key.length();
		ArrayList<String> lines = new ArrayList<String>();
		while(true)
		{
			String line = br.readLine();
			if (line == null)
			{
				break;
			}
			String temp = line.substring(0, len);
			if (temp.equals(key))
			{
				int dot = line.indexOf(".");
				line = line.substring(0, dot+1) + target;
			}
			lines.add(line);
		}
		br.close();
		PrintWriter out = new PrintWriter(new FileOutputStream(map));
		for(int i=0; i<lines.size(); i++)
		{
			out.println(lines.get(i));
		}
		out.flush();
		out.close();
	}
	
	/**
	 * read map
	 * @throws IOException 
	 */
	public void readMap()
	{
		try
		{
			File map = new File(direction, filename);
			BufferedReader br = new BufferedReader(new FileReader(map));
			while(true)
			{
				String line = br.readLine();
				if (line == null)
				{
					break;
				}
				filter(line);
			}
		}
		catch (IOException e) {
			System.out.println("read map error");
			e.printStackTrace();
		}
	}
	
	/**
	 * load information
	 */
	private void filter(String line)
	{
		loadDimension(line);
		loadWalls(line);
		loadLocation(line);
		loadSpeed(line);
	}
	
	/**
	 * load Dimension
	 */
	private void loadDimension(String line)
	{
		String target = "Dimension";
		int len = target.length();
		if (line.substring(0, len).equals(target))
		{
			target = line.substring(len+2, line.length());
			Pack aPack = unpack(target);
			dms = new Dimension(aPack.getX(), aPack.getY());
		}
	}
	
	/**
	 * load Walls
	 */
	private void loadWalls(String line)
	{
		String target = "Wall";
		int len = target.length();
		if (line.substring(0, len).equals(target))
		{
			target = line.substring(len+2, line.length());
			Pack aPack = unpack(target);
			int begin = target.indexOf(")") + 2;
			target = target.substring(begin, target.length());
			someWalls.add(new Wall(new Block(aPack.getX(), aPack.getY()), drtFilter(target)));
		}
	}
	
	/**
	 * load Location
	 */
	private void loadLocation(String line)
	{
		String target = "Karel";
		int len = target.length();
		if (line.substring(0, len).equals(target))
		{
			target = line.substring(len+2, line.length());
			Pack aPack = unpack(target);
			int begin = target.indexOf(")") + 2;
			target = target.substring(begin, target.length());
			loc = new Location(new Block(aPack.getX(), aPack.getY()), drtFilter(target));
		}
	}
	
	/**
	 * load speed
	 */
	private void loadSpeed(String line)
	{
		String target = "Speed";
		int len = target.length();
		if (line.substring(0, len).equals(target))
		{
			target = line.substring(len+2, line.length());
			speed = Double.parseDouble(target);
		}
	}
	
	/**
	 * unpack the (x, y)
	 */
	private Pack unpack(String target)
	{
		int comma = target.indexOf(",");
		int right = target.indexOf(")");
		String strX = target.substring(1, comma);
		String strY = target.substring(comma+2, right);
		int x = Integer.parseInt(strX);
		int y = Integer.parseInt(strY);
		return new Pack(x, y);
	}
	
	/**
	 * pack two numbers
	 */
	private class Pack
	{
		public Pack(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		
		public int getX()
		{
			return x;
		}
		
		public int getY()
		{
			return y;
		}
		
		private int x;
		private int y;
	}
	
	/**
	 * direction filter
	 */
	private Direction drtFilter(String drt)
	{
		if (drt.equals("north"))
			return Direction.NORTH;
		if (drt.equals("east"))
			return Direction.EAST;
		if (drt.equals("south"))
			return Direction.SOUTH;
		if (drt.equals("west"))
			return Direction.WEST;
		return null;
	}
	
	/**
	 * set walls in the map
	 */
	public void setWalls(ArrayList<Wall> walls)
	{
		try
		{
			someWalls = walls;
			modifyWa(walls);
		} 
		catch (IOException e)
		{
			System.out.println("update Wall in the map file fail");
			e.printStackTrace();
		}
	}
	
	/**
	 * modify Wall
	 */
	private void modifyWa(ArrayList<Wall> walls) throws IOException
	{
		String key = "Wall";
		File map = new File(direction, filename);
		BufferedReader br = new BufferedReader(new FileReader(map));
		int len = key.length();
		ArrayList<String> lines = new ArrayList<String>();
		String preLine = "";
		while(true)
		{
			String line = br.readLine();
			if (line == null)
			{
				break;
			}
			String temp = line.substring(0, len);
			if (temp.equals(key))
			{
				continue;
			}
			else if (preLine.contains("Dimension"))
			{
				for (int i = 0; i < walls.size(); i++)
				{
					String x = Integer.toString(walls.get(i).getBlk().getX());
					String y = Integer.toString(walls.get(i).getBlk().getY());
					String dir = "";
					switch (walls.get(i).getDrt())
					{
					case NORTH:
						dir = "north";
						break;
					case EAST:
						dir = "east";
						break;
					case SOUTH:
						dir = "south";
						break;
					case WEST:
						dir = "west";
						break;
					default:
						break;
					}
					String tempLine = "Wall: (" + x + ", " + y + ") " + dir;
					lines.add(tempLine);
				}
			}
			lines.add(line);
			preLine = line;
		}
		br.close();
		PrintWriter out = new PrintWriter(new FileOutputStream(map));
		for(int i=0; i<lines.size(); i++)
		{
			out.println(lines.get(i));
		}
		out.flush();
		out.close();
	}
	
	/**
	 * set Karel in the map
	 */
	public void setLocation(Location loc)
	{
		try
		{
			this.loc = loc;
			modifyKa(loc);
		} 
		catch (IOException e)
		{
			System.out.println("update Karel in the map file fail");
			e.printStackTrace();
		}
	}
	
	/**
	 * modify Karel location
	 */
	public void modifyKa(Location loc) throws IOException
	{
		String key = "Karel";
		File map = new File(direction, filename);
		BufferedReader br = new BufferedReader(new FileReader(map));
		int len = key.length();
		ArrayList<String> lines = new ArrayList<String>();
		while(true)
		{
			String line = br.readLine();
			if (line == null)
			{
				break;
			}
			String temp = line.substring(0, len);
			if (temp.equals(key))
			{
				String x = Integer.toString(loc.getBlk().getX());
				String y = Integer.toString(loc.getBlk().getY());
				String dir = "";
				switch (loc.getDrt())
				{
				case NORTH:
					dir = "north";
					break;
				case EAST:
					dir = "east";
					break;
				case SOUTH:
					dir = "south";
					break;
				case WEST:
					dir = "west";
					break;
				default:
					break;
				}
				line = "Karel: (" + x + ", " + y + ") " + dir;
			}
			lines.add(line);
		}
		br.close();
		PrintWriter out = new PrintWriter(new FileOutputStream(map));
		for(int i=0; i<lines.size(); i++)
		{
			out.println(lines.get(i));
		}
		out.flush();
		out.close();
	}
	
	public Dimension getDms()
	{
		return dms;
	}
	
	public void setLoc(Location loc)
	{
		this.loc = loc;
	}
	
	public Location getLoc()
	{
		return loc;
	}
	
	public ArrayList<Wall> getSomeWalls()
	{
		return someWalls;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
	
	public void setFilename(String filename)
	{
		this.filename = filename;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public String getDirection()
	{
		return direction;
	}
	
	private Dimension dms;
	private ArrayList<Wall> someWalls = new ArrayList<Wall>();
	private Location loc;
//	private int beeperBag = -1;	// ÎÞÇî
	private double speed;
	
	private String direction = Directory.getDefMaps();
	private String filename = Directory.getDefMAP();
}
