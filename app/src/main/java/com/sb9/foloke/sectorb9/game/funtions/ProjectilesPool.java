package com.sb9.foloke.sectorb9.game.funtions;
import com.sb9.foloke.sectorb9.game.funtions.Pool;
import com.sb9.foloke.sectorb9.game.entities.*;
import java.util.List;
import android.graphics.*;
import java.util.*;
import com.sb9.foloke.sectorb9.game.display.*;
public class ProjectilesPool
{
	static Bitmap image;
	ArrayList<Projectile> projectiles;
	
	public ProjectilesPool(Bitmap image,float speed, int maxSize,Game game) {
		projectiles=new ArrayList<Projectile>();
		for(int i=0;i<maxSize;i++)
		{
			projectiles.add(new Projectile(0,0,image,"p"+i,(int)1,speed,0,game));
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
					//p.getGame().debugText.setString(p.getName());
					return;
				}
			}
		}
}
