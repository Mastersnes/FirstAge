package com.bebel.game.components.refound.event;

import java.util.List;

/**
 * Represente un callback lié à un evenement du clavier
 */
public interface KeyholdCallback extends EventCallback {
    void run(final List<Integer> keycode);
}
