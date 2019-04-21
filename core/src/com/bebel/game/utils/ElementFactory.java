package com.bebel.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Pools;
import com.bebel.game.components.refound.element.Image;
import com.bebel.game.components.refound.element.Text;

public class ElementFactory {
    public static Image image(final String key) {
        final Image image = Pools.get(Image.class).obtain();
        image.init(key);
        return image;
    }
    public static Text text(final String key) {
        final Text text = Pools.get(Text.class).obtain();
        text.init(key);
        return text;
    }
    public static Text text(final String key, final BitmapFont font) {
        final Text text = Pools.get(Text.class).obtain();
        text.init(key, font);
        return text;
    }
    public static Text text(final String key, final BitmapFont font, final Color color) {
        final Text text = Pools.get(Text.class).obtain();
        text.init(key, font, color);
        return text;
    }
}
