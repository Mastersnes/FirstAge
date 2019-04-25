package com.bebel.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.bebel.game.LaunchGame;
import com.bebel.game.components.refound.abstrait.AbstractScreen;
import com.bebel.game.components.refound.element.Animate;
import com.bebel.game.components.refound.element.Image;

import java.util.Arrays;
import java.util.List;

import static com.bebel.game.utils.ElementFactory.animate;

public class Menu extends AbstractScreen {
    private Animate animation;
    private Image img;

    public Menu(final LaunchGame game) {
        super(game);
        renew = false;
        back = Color.WHITE.cpy();
    }

    @Override
    public void create() {
        add(animation = animate("test"));
        animation.setCenter(getWidth() / 2, getHeight() / 2);
        animation.hideOnFinish();

        setFocus(true);
    }

    @Override
    public void makeComponentEvents() {
        onKeyhold((mouse, keyboard) -> {
            if (keyboard.hold(Input.Keys.ESCAPE)) {
                Gdx.app.exit();
                return;
            }
        });
        onTouchdown((mouse, keyboard) -> {
            if (!mouse.left()) return;
            animation.restart();
        });
    }

    @Override
    public void resetComponent() {
    }

    @Override
    protected String context() {
        return "menu";
    }

    @Override
    protected List<String> nextScreens() {
        return Arrays.asList();
    }
}
