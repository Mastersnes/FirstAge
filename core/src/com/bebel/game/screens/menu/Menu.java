package com.bebel.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.bebel.game.LaunchGame;
import com.bebel.game.components.refound.abstrait.AbstractScreen;
import com.bebel.game.components.refound.element.Image;
import com.bebel.game.components.refound.element.Text;

import java.util.Arrays;
import java.util.List;

import static com.bebel.game.utils.ElementFactory.image;
import static com.bebel.game.utils.ElementFactory.text;

public class Menu extends AbstractScreen {
    private Image img;
    private Text elmt;

    public Menu(final LaunchGame game) {
        super(game);
        renew = false;
    }

    @Override
    public void create() {
        add(elmt = text("Ceci est un test"));
        elmt.setCenter(getWidth() / 2, getHeight() / 2);

        add(img = image("general/quitter.png"));
        img.setPosition(10, 10);

        setFocus(true);
    }

    @Override
    public void makeComponentEvents() {
        onKeyhold((keycodes) -> {
            if (keycodes.contains(Input.Keys.ESCAPE)) {
                Gdx.app.exit();
                return;
            }
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
