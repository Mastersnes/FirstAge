package com.bebel.game.components.refound.action.time;

public class ScaleToAction extends TimeAction {
    private float startX, startY;
    private float endX, endY;

    protected void begin () {
        startX = target.getScaleX();
        startY = target.getScaleY();
    }

    protected void update (float percent) {
        target.setScale(startX + (endX - startX) * percent, startY + (endY - startY) * percent);
    }

    public void setScale (float x, float y) {
        endX = x;
        endY = y;
    }

    public void setScale (float scale) {
        endX = scale;
        endY = scale;
    }

    public float getX () {
        return endX;
    }

    public void setX (float x) {
        this.endX = x;
    }

    public float getY () {
        return endY;
    }

    public void setY (float y) {
        this.endY = y;
    }
}
