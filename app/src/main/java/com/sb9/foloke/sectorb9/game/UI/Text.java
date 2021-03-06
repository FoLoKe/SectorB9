package com.sb9.foloke.sectorb9.game.UI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class Text {
    private String string;
    private float x,y;
    public Text(String string,float x,float y)
    {
        this.string=string;
        this.x=x;
        this.y=y;
    }
    public void tick()
    {

    }
    public void render(Canvas canvas)
    {
        Paint tPaint=new Paint();
        tPaint.setColor(Color.rgb(0,255,0));
        tPaint.setTextSize(25);
        canvas.drawText(string,x,y,tPaint);
    }

    public void setString(String string) {
        this.string=string;
    }
    public void setWorldLocation(PointF location)
    {
        this.x=location.x;
        this.y=location.y;
    }
}
