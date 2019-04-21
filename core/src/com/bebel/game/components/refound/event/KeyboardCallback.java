package com.bebel.game.components.refound.event;

/**
 * Represente un callback lié à un evenement du clavier
 */
public interface KeyboardCallback extends EventCallback {
    void run(final int keycode, final char character);
}
