package com.bebel.game.manager;

import com.bebel.game.components.refound.event.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager permettant de gerer les evenements
 */
public class EventManager {
    private Map<Events, List<EventCallback>> events = new HashMap<>();

    public void add(final Events type, final EventCallback callback) {
        events.computeIfAbsent(type, c -> new ArrayList<>())
            .add(callback);
    }

    public void clear(final Events type) {
        if (events.get(type) == null) return;
        events.get(type).clear();
    }
    public void clear() {
        events.clear();
    }

    public void fire(final Events type) {
        if (events.get(type) == null) return;
        for (final EventCallback callback : events.get(type)) {
            if (callback instanceof HoverCallback)
                ((HoverCallback) callback).run();
        }
    }

    public void fire(final Events type, final int keycode, char character) {
        if (events.get(type) == null) return;
        for (final EventCallback callback : events.get(type)) {
            if (callback instanceof KeyboardCallback)
                ((KeyboardCallback) callback).run(keycode, character);
        }
    }

    public void fire(Events type, float x, float y, int pointer, int button) {
        if (events.get(type) == null) return;
        for (final EventCallback callback : events.get(type)) {
            if (callback instanceof MouseCallback)
                ((MouseCallback) callback).run(x, y, pointer, button);
        }
    }

    public void fire(Events type, float amount) {
        if (events.get(type) == null) return;
        for (final EventCallback callback : events.get(type)) {
            if (callback instanceof ScrollCallback)
                ((ScrollCallback) callback).run(amount);
        }
    }

    public void fire(final Events type, final List<Integer> keyHolds) {
        if (events.get(type) == null) return;
        for (final EventCallback callback : events.get(type)) {
            if (callback instanceof KeyholdCallback)
                ((KeyholdCallback) callback).run(keyHolds);
        }
    }
}
