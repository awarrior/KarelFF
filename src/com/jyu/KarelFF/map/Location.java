package com.jyu.KarelFF.map;

public class Location
{
	public Location()
	{
		blk = new Block();
		drt = null;
	}
	
	public Location(Block blk, Direction drt)
	{
		this.blk = blk;
		this.drt = drt;
	}
	
	public Block getBlk()
	{
		return blk;
	}
	
	public Direction getDrt()
	{
		return drt;
	}
	
	public void setBlk(Block blk)
	{
		this.blk = blk;
	}
	
	public void setDrt(Direction drt)
	{
		this.drt = drt;
	}
	
	private Block blk;
	private Direction drt;
}
