package com.bebel.game.components.refound.abstrait;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.utils.Pool;
import com.bebel.game.components.refound.event.*;
import com.bebel.game.components.refound.hitbox.IHitbox;
import com.bebel.game.components.refound.hitbox.PolygonHitbox;
import com.bebel.game.manager.EventManager;
import com.bebel.game.manager.resources.AssetsManager;
import org.w3c.dom.events.MouseEvent;

import java.util.ArrayList;

import static com.badlogic.gdx.utils.Align.*;
import static com.bebel.game.components.refound.event.Events.*;
import static com.bebel.game.utils.Constantes.GAME_HEIGHT;
import static com.bebel.game.utils.Constantes.GAME_WIDTH;

public abstract class AbstractComponent extends Sprite implements Pool.Poolable {
    private Vector2 tmp = new Vector2();
    private EventManager events = new EventManager();
    protected String name;
    protected AbstractGroup parent;
    protected AssetsManager manager;
    protected IHitbox hitbox;
    protected Color debugColor;

    protected boolean touchable, visible, debug, focus, hover;
    private final ArrayList<Integer> keyHolds = new ArrayList<>();

    public AbstractComponent() {
        super();
        manager = AssetsManager.getInstance();
        initComponent();
    }

    public void initComponent() {
        hitbox = new PolygonHitbox();
        setName("");
        setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
        touchable = true;
        visible = true;
        debug = false;
        focus = false;
        keyHolds.clear();
        events.clear();
        debugColor = Color.GREEN.cpy();
        setColor(Color.WHITE.cpy());
        parent = null;
    }

    /**
     * Permet de (re)demarrer l'element
     */
    public void act(final float delta) {
        if (!keyHolds.isEmpty()) events.fire(KEY_HOLD, keyHolds);
        actComponent(delta);
    }

    protected abstract void actComponent(float delta);

    public void makeEvents() {
        onTouchdown((x, y, pointer, button) -> {
            if (button == Input.Buttons.RIGHT) {
                debug = !debug;
            }
        });

        onEnter(()-> setFocus(true));
        onExit(()-> setFocus(false));

        onKeyhold(keycodes -> {
            if (!debug && !focus) return;
            if (keycodes.contains(Input.Keys.LEFT)) rotate(-0.5f);
            if (keycodes.contains(Input.Keys.RIGHT)) rotate(0.5f);
            if (keycodes.contains(Input.Keys.UP)) scale(0.1f);
            if (keycodes.contains(Input.Keys.DOWN)) scale(-0.1f);
        });
        onScroll(amount -> {
            if (!debug && !focus) return;
            if (amount > 0) scale(0.05f);
            else scale(-0.05f);
        });
        onDrag((x, y, pointer, button) -> {
            if (!debug && !focus) return;
            if (button == Input.Buttons.LEFT) {
                final float w = getWidth();
                final float h = getHeight();
                setPosition(x - (w / 2), y - (h / 2));
            }
        });

        makeComponentEvents();
    }

    public abstract void makeComponentEvents();
    public abstract void resetComponent();

    public boolean hit(final float x, final float y, final boolean checkTouchable) {
        if (checkTouchable && !this.touchable) return false;
        if (!visible) return false;
        return hitbox.contains(x, y);
    }

    public void remove() {
        if (parent != null) {
            parent.remove(this);
            parent = null;
        }
    }

    @Override
    public void draw(final Batch batch, final float alphaModulation) {
        if (getTexture() != null) super.draw(batch, alphaModulation);
    }

    protected void drawDebug(final ShapeRenderer shapes) {
        if (this.debug) {
            hitbox.draw(shapes, debugColor);
        }
    }

    //--- Getter/Setter
    protected void setParent(final AbstractGroup parent) {
        this.parent = parent;
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public boolean isTouchable() {
        return touchable;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void debug(final Color color) {
        this.debugColor.set(color);
        debug = true;
        setFocus(true);
    }
    public void debug() {
        debug(Color.GREEN.cpy());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void front() {
        setZ(1000000);
    }

    public void back() {
        setZ(0);
    }

    public int getZ() {
        return parent != null ? parent.children.indexOf(this, true) : 0;
    }

    public void setZ(final int _z) {
        if (parent == null) return;
        int z = _z;
        z = z < 0 ? 0 : z;
        z = z >= parent.children.size ? parent.children.size - 1 : z;

        parent.children.insert(z, this);
    }

    //---- Changement d'etat
    public void sizeChanged() {
        hitbox.setSize(getWidth(), getHeight());
        setOriginCenter();
    }

    public void positionChanged() {
        hitbox.setPosition(getX(), getY());
    }

    public void scaleChanged() {
        hitbox.setScale(getScaleX(), getScaleY());
    }

    public void rotationChanged() {
        hitbox.setRotation(getRotation());
    }

    public void originChanged() {
        hitbox.setOrigin(getOriginX(), getOriginY());
    }

    @Override
    public void reset() {
        resetComponent();
        initComponent();
    }

    //---- Events
    public void onEnter(final HoverCallback event) {
        onHover(ENTER, event);
    }

    public void onExit(final HoverCallback event) {
        onHover(EXIT, event);
    }

    private void onHover(final Events type, final HoverCallback event) {
        events.add(type, event);
    }

    public void onKeyup(final KeyboardCallback event) {
        onKeyboard(KEY_UP, event);
    }

    public void onKeydown(final KeyboardCallback event) {
        onKeyboard(KEY_DOWN, event);
    }

    public void onKeytype(final KeyboardCallback event) {
        onKeyboard(KEY_TYPE, event);
    }

    private void onKeyboard(final Events type, final KeyboardCallback event) {
        events.add(type, event);
    }

    public void onKeyhold(final KeyholdCallback event) {
        events.add(KEY_HOLD, event);
    }

    public void onTouchdown(final MouseCallback event) {
        onMouse(TOUCH_DOWN, event);
    }

    public void onTouchup(final MouseCallback event) {
        onMouse(TOUCH_UP, event);
    }

    public void onDrag(final MouseCallback event) {
        onMouse(TOUCH_DRAG, event);
    }

    public void onMouseMove(final MouseCallback event) {
        onMouse(MOVE, event);
    }

    private void onMouse(final Events type, final MouseCallback event) {
        events.add(type, event);
    }

    public void onScroll(final ScrollCallback event) {
        events.add(SCROLL, event);
    }

    protected void fire(final Events type) {
        events.fire(type);
    }

    protected void fire(final Events type, final int keycode, final char character) {
        if (type == KEY_DOWN) keyHolds.add(Integer.valueOf(keycode));
        else if (type == KEY_UP) keyHolds.remove(Integer.valueOf(keycode));

        events.fire(type, keycode, character);
    }

    protected void fire(final Events type, final float x, final float y, final int pointer, final int button) {
        events.fire(type, x, y, pointer, button);
    }

    protected void fire(final Events type, final float amount) {
        events.fire(type, amount);
    }

    private Vector2 realign(final float xAmount, final float yAmount, final int align) {
        float x = xAmount;
        float y = yAmount;
        float pW = GAME_WIDTH, pH = GAME_HEIGHT;
        if (parent != null) {
            pW = parent.getWidth() * parent.getScaleX();
            pH = parent.getHeight() * parent.getScaleY();
        }
        float aW = getWidth() * getScaleX();
        float aH = getHeight() * getScaleY();

        if ((align & right) != 0) x = pW - aW - xAmount;
        if ((align & top) != 0) y = pH - aH - yAmount;
        return tmp.set(x, y);
    }

    // --- Gestion align
    public void setPosition(final float xAmount, final float yAmount, final int align) {
        realign(xAmount, yAmount, align);
        super.setPosition(tmp.x, tmp.y);
        positionChanged();
    }

    public void translate(float xAmount, float yAmount, int align) {
        tmp.set(xAmount, yAmount);
        if ((align & right) != 0) tmp.x = -tmp.x;
        if ((align & bottom) != 0) tmp.y = -tmp.y;
        super.translate(tmp.x, tmp.y);
        positionChanged();
    }

    // --- Override
    public AssetsManager getManager() {
        return manager;
    }

    @Override
    public void translateX(float xAmount) {
        super.translateX(xAmount);
        positionChanged();
    }

    @Override
    public void translateY(float yAmount) {
        super.translateY(yAmount);
        positionChanged();
    }

    @Override
    public void translate(float xAmount, float yAmount) {
        super.translate(xAmount, yAmount);
        positionChanged();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        positionChanged();
        sizeChanged();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        sizeChanged();
    }

    @Override
    public void setRotation(float degrees) {
        super.setRotation(degrees);
        rotationChanged();
    }

    @Override
    public void rotate(float degrees) {
        super.rotate(degrees);
        rotationChanged();
    }

    @Override
    public void rotate90(boolean clockwise) {
        super.rotate90(clockwise);
        rotationChanged();
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        scaleChanged();
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);
        scaleChanged();
    }

    @Override
    public void scale(float amount) {
        super.scale(amount);
        scaleChanged();
    }

    @Override
    public void setOrigin(float originX, float originY) {
        super.setOrigin(originX, originY);
        originChanged();
    }

    @Override
    public void setOriginBasedPosition(float x, float y) {
        super.setOriginBasedPosition(x, y);
        originChanged();
    }

    @Override
    public void setOriginCenter() {
        super.setOriginCenter();
        originChanged();
    }
}