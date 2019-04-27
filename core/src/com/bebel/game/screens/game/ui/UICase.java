package com.bebel.game.screens.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.bebel.game.components.refound.abstrait.AbstractGroup;

public class UICase extends AbstractGroup {
    public static final Vector2 DECALAGE = new Vector2(10, 10);
    public static final Vector2 TAILLE = new Vector2(80, 80);

    @Override
    public UICase create() {
        setSize(TAILLE.x, TAILLE.y);
        debug(Color.RED.cpy());
        return this;
    }

    @Override
    protected void actComponent(float delta) {
    }

    @Override
    public void makeComponentEvents() {
    }

    @Override
    public void resetComponent() {
    }
}
