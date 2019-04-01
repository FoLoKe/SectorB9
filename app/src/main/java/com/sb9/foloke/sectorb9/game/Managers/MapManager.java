package com.sb9.foloke.sectorb9.game.Managers;

import java.util.*;

public class MapManager
{
	public enum sectorType{ASTEROID_BELT,ASTEROIDS_CLUSTER,STATION,EMPTY,RANDOM,EVENT;
		public static sectorType getRandomType() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];}
	}
	
	public enum sectorAgr{PEACEFUL,HOSTILE,NEUTRAL;
		public static sectorAgr getRandomAgr() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];}
	}
	

	private static final int mapWidth=32;
	private static final int mapHeight=32;
	private ArrayList<Sector> sectors=new ArrayList<Sector>();
	
	public class Sector
	{
		public int x=0,y=0;
		sectorType type;
		sectorAgr agr;
		public boolean discovered=false;
		public boolean explored=false;
		
		Sector(int x,int y,sectorType type,sectorAgr agr)
		{
			this.x=x;
			this.y=y;
			this.agr=agr;
			this.type=type;
		}
	}
	
	MapManager()
	{

		for(int i=0;i<mapWidth;i++)
			for(int j=0;j<mapHeight;j++)
				sectors.add(new Sector(i,j,sectorType.getRandomType(),sectorAgr.getRandomAgr()));
	}
	
	
	
	public void save()
	{
		
	}
	
	public void load()
	{
		
	}
	
	public int getMapWidth()
	{
		return mapWidth;
	};
	
	public int getMapHeight()
	{
		return mapHeight;
	};
	
	public Sector getSector(int x,int y)
	{
		for(Sector s:sectors)
		if(s.x==x&&s.y==y)
			return s;
		return null;
	}
	
	ArrayList<Sector> getSectors()
	{
		return sectors;
	}
}
