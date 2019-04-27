package com.bebel.game.screens.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.bebel.game.components.refound.abstrait.AbstractGroup;
import com.bebel.game.manager.save.Player;
import com.bebel.game.manager.save.SaveInstance;
import com.bebel.game.manager.save.SaveManager;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.utils.Align.bottomLeft;
import static com.bebel.game.utils.Constantes.GAME_WIDTH;
import static com.bebel.game.utils.ElementFactory.group;

public class UI extends AbstractGroup {
    final List<UICase> cases = new ArrayList<>();

    @Override
    public UI create() {
        setName("UI");
        setBounds(0, 0, GAME_WIDTH, 100, bottomLeft);
        debug(Color.GREEN.cpy());

        addCases();

        showInventory();
        return this;
    }

    private void addCases() {
        boolean ok = false;
        while (!ok) {
            final UICase uiCase = group(UICase.class);
        }
    }

    private void showInventory() {
        final SaveInstance save = SaveManager.getInstance().getCurrent();
        final Player player = save.getPlayer();
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
