package com.sb9.foloke.sectorb9.game.Funtions;
import android.graphics.*;
import com.sb9.foloke.sectorb9.game.Entities.*;
import com.sb9.foloke.sectorb9.game.Managers.*;
import com.sb9.foloke.sectorb9.game.UI.*;

public class PlayerController extends Controller
{
	ControlledShip controlledEntity;
	public static int interactionRadius=15000;
	
	public int team=1;
	public boolean shootFlag=false;
	
	public PlayerController()
	{
		controlledEntity=null;
	}
	
	public void setControlledEntity(ControlledShip cs)
	{
		if(controlledEntity!=null)
		controlledEntity.setController(null);
		controlledEntity=cs;
		if(controlledEntity!=null)
		cs.setController(this);
	}
	
	public ControlledShip getControlledEntity()
	{
		return controlledEntity;
	}
	
	public void tick()
	{
		if(controlledEntity!=null)
		{
			if(!controlledEntity.getActive())
			{
				
				controlledEntity=null;
				ActionUI.toInteracrion();
				return;
			}
			
		    GameManager.uIhp.tick(controlledEntity.getHp()/controlledEntity.getMaxHP()*100);
            GameManager.uIsh.tick(controlledEntity.getSH()/controlledEntity.getMaxSH()*100);
			controlledEntity.setMovable(GameManager.joystick.getTouched());

			if(shootFlag)
			{
				controlledEntity.shoot();
				
			}
			
			if(GameManager.joystick.getTouched())
			{

				controlledEntity.rotationToPoint(GameManager.joystickTouchPoint);
				float targetAcceleration=GameManager.joystick.getAcceleration();

				if(targetAcceleration>1)
					targetAcceleration=1;
				if(targetAcceleration<0)
					targetAcceleration=0;
     			controlledEntity.addMovement(targetAcceleration);
       		}
		}
	}
	
	public void shoot(boolean state)
	{
		shootFlag=state;
	}
	
	public void render(Canvas canvas)
	{
		if(controlledEntity.getGameManager().currentCommand== GameManager.command.INTERACTION)
			drawInteractionCircle(canvas);
	}
	
	private void drawInteractionCircle(Canvas canvas)
	{
		Paint tPaint = new Paint();
		tPaint.setColor(Color.YELLOW);
		tPaint.setStyle(Paint.Style.STROKE);
		tPaint.setStrokeWidth(2);
		tPaint.setPathEffect(new DashPathEffect(new float[] { 15, 16}, 0));
		canvas.drawCircle(controlledEntity.getCenterX(),controlledEntity.getCenterY(),interactionRadius,tPaint);
	}
	public int getRadius(){return interactionRadius;}
}
