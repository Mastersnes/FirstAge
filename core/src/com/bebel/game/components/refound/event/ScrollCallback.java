package com.bebel.game.components.refound.event;

/**
 * Represente un callback lié à un evenement de scroll
 */
public interface ScrollCallback extends EventCallback {
    void run(final float amount);
}
