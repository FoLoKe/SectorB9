package com.sb9.foloke.sectorb9.game.Funtions;
import com.sb9.foloke.sectorb9.game.Managers.GameManager;
import com.sb9.foloke.sectorb9.game.Entities.*;
import java.util.List;
import android.graphics.*;
import java.util.*;

public class ProjectilesPool
{
	private ArrayList<Projectile> projectiles;

	public ProjectilesPool(Bitmap image, float speed, int maxSize,float damage,Entity parent, GameManager gameManager) {

		projectiles=new ArrayList<>();
		for(int i=0;i<maxSize;i++)
		{
			Projectile p=new Projectile(0,0,4,speed,0,damage,parent, gameManager);
			p.setImage(image);
			p.recreateCollision();
			
			projectiles.add(p);
			
		}
	}
	
	public List<Projectile> getArray()
	{
		return projectiles;
	}
	
	public void tick()
	{
		for(Projectile p:projectiles)
		{	
			p.tick();
		}
	}
	
	public void render(Canvas canvas)
	{
		for(Projectile p:projectiles)
		{
			p.render(canvas);
		}
	}
	
	public void shoot(PointF point,float rotation)
	{
		for(Projectile p:projectiles)
		{
			if(!p.getActive())
			{
				p.shoot(point,rotation);
				return;
			}
		}
	}
}
