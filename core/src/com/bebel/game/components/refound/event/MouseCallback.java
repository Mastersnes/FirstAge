package com.bebel.game.components.refound.event;

/**
 * Represente un callback lié à un evenment de la souris
 */
public interface MouseCallback extends EventCallback {
    void run(final float x, final float y, final int pointer, final int button);
}
