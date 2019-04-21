package com.bebel.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bebel.game.LaunchGame;
import com.bebel.game.components.refound.abstrait.AbstractScreen;
import com.bebel.game.components.refound.element.Image;
import com.bebel.game.manager.resources.ScreensManager;

import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.utils.Align.bottomRight;
import static com.badlogic.gdx.utils.Align.topLeft;
import static com.bebel.game.utils.ElementFactory.image;

public class Game extends AbstractScreen {
    private Image image;
    private Image autre;

    public Game(final LaunchGame game) {
        super(game);
        renew = false;
    }

    @Override
    public void create() {
        add(image = image("general/quitter.png"))
            .setPosition(50, 50, topLeft);
        add(autre = image("general/quitter.png"))
            .setPosition(0, 0, bottomRight);

        setFocus(true);
    }

    @Override
    public void makeComponentEvents() {
        onKeyhold((keycodes) -> {
            if (keycodes.contains(Input.Keys.ESCAPE)) {
                Gdx.app.exit();
                return;
            }

            if (keycodes.contains(Input.Keys.LEFT)) image.translateX(-1);
            if (keycodes.contains(Input.Keys.RIGHT)) image.translateX(1);
            if (keycodes.contains(Input.Keys.UP)) image.translateY(1);
            if (keycodes.contains(Input.Keys.DOWN)) image.translateY(-1);
        });
        image.onDrag((x, y, button, pointer) -> {
            image.setPosition(x - image.getWidth() / 2, y - image.getHeight() / 2);
        });
        autre.onTouchdown((x, y, button, pointer) -> ScreensManager.getInstance().switchTo(Menu.class));
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
