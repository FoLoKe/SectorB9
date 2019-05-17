package com.sb9.foloke.sectorb9.game.Funtions;
import com.sb9.foloke.sectorb9.game.Entities.*;


public class Damage
{
	public float amount;
	public Entity instignator;
	public type damageType;
	public enum type{LASER,PHYSICAL,PROJECTILE,EXPLOSION};
	
	public Damage(float amount,Entity instignator,type damageType)
	{
		this.amount=amount;
		this.instignator=instignator;
		this.damageType=damageType;
	}
}
