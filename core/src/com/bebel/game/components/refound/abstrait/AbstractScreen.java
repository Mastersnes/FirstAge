package com.bebel.game.components.refound.abstrait;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bebel.game.LaunchGame;
import com.bebel.game.manager.resources.AssetsManager;

import java.util.Iterator;
import java.util.List;

import static com.bebel.game.components.refound.event.Events.*;
import static com.bebel.game.utils.Constantes.GAME_HEIGHT;
import static com.bebel.game.utils.Constantes.GAME_WIDTH;

public abstract class AbstractScreen extends AbstractGroup implements Screen, InputProcessor {
    protected final LaunchGame game;
    public static Viewport viewport;
    protected final AssetsManager manager;
    protected ShapeRenderer debugShape = new ShapeRenderer();
    protected Vector2 mouse = new Vector2();
    protected boolean renew = true, firstTime = true;


    public AbstractScreen(final LaunchGame game) {
        this.game = game;
        final OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
        viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
        manager = AssetsManager.getInstance();
        debugShape.setAutoShapeType(true);

        // Chargement des prochains ecrans
        for (final String next : nextScreens()) {
            manager.loadContext(next);
        }
    }

    protected abstract void create();
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        manager.finishLoading(context());
        if (renew || firstTime) {
            firstTime = false;
            create();
            makeGroupEvents();
        }
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.getCamera().update();
        if (visible) {
            game.batch.setProjectionMatrix(viewport.getCamera().combined);
            game.batch.begin();
            draw(game.batch, 1);
            game.batch.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            debugShape.setProjectionMatrix(viewport.getCamera().combined);
            debugShape.begin();
            drawDebug(debugShape);
            debugShape.end();
        }

        actGroup(delta);
    }

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, true);
    }

    @Override
    protected void actComponent(float delta) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        if (renew) {
            manager.unloadContext(context());
            reset();
        }
    }

    @Override
    public void dispose() {
        hide();
        Gdx.app.log(context(), "DISPOSE");
    }

    @Override
    public void makeEvents() {
        makeComponentEvents();
    }

    @Override
    public boolean keyDown(final int keycode) {
        if (!isVisible() || !isTouchable() || !isFocus()) return false;
        for (final Iterator<AbstractComponent> iterator = children.iterator(); iterator.hasNext();) {
            final AbstractComponent child = iterator.next();
            if (child.isVisible() && child.isTouchable() && child.isFocus()) {
                child.fire(KEY_DOWN, keycode, '\u0000');
            }
        }
        fire(KEY_DOWN, keycode, '\u0000');
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!isVisible() || !isTouchable() || !isFocus()) return false;
        for (final Iterator<AbstractComponent> iterator = children.iterator(); iterator.hasNext();) {
            final AbstractComponent child = iterator.next();
            if (child.isVisible() && child.isTouchable() && child.isFocus()) {
                child.fire(KEY_UP, keycode, '\u0000');
            }
        }
        fire(KEY_UP, keycode, '\u0000');
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if (!isVisible() || !isTouchable() || !isFocus()) return false;
        for (final Iterator<AbstractComponent> iterator = children.iterator(); iterator.hasNext();) {
            final AbstractComponent child = iterator.next();
            if (child.isVisible() && child.isTouchable() && child.isFocus()) {
                child.fire(KEY_TYPE, -1, character);
            }
        }
        fire(KEY_TYPE, -1, character);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!isVisible() || !isTouchable()) return false;
        if (!isInsideViewport(screenX, screenY)) return false;

        for (final Iterator<AbstractComponent> iterator = children.iterator(); iterator.hasNext();) {
            final AbstractComponent child = iterator.next();
            if (child.isHover()) {
                child.fire(TOUCH_DOWN, mouse.x, mouse.y, pointer, button);
                return true;
            }
        }
        fire(TOUCH_DOWN, mouse.x, mouse.y, pointer, button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!isVisible() || !isTouchable()) return false;
        if (!isInsideViewport(screenX, screenY)) return false;
        for (final Iterator<AbstractComponent> iterator = children.iterator(); iterator.hasNext();) {
            final AbstractComponent child = iterator.next();
            if (child.isHover()) {
                child.fire(TOUCH_UP, mouse.x, mouse.y, pointer, button);
                return true;
            }
        }
        fire(TOUCH_UP, mouse.x, mouse.y, pointer, button);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!isVisible() || !isTouchable()) return false;
        if (!isInsideViewport(screenX, screenY)) return false;

        mouse.set(screenX, screenY);
        mouse.set(viewport.unproject(mouse));

        for (final Iterator<AbstractComponent> iterator = children.iterator(); iterator.hasNext();) {
            final AbstractComponent child = iterator.next();
            if (child.hit(mouse.x, mouse.y, true)) {
                child.fire(TOUCH_DRAG, mouse.x, mouse.y, -1, -1);
                if (!child.isHover()) {
                    child.setHover(true);
                    child.fire(ENTER);
                }
                // A voir si on garde
                return true;
            }else if (child.isHover()) {
                child.setHover(false);
                child.fire(EXIT);
            }
        }
        fire(TOUCH_DRAG, mouse.x, mouse.y, pointer, -1);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!isVisible() || !isTouchable()) return false;
        if (!isInsideViewport(screenX, screenY)) return false;
        boolean trigger = false;

        mouse.set(screenX, screenY);
        mouse.set(viewport.unproject(mouse));

        for (final Iterator<AbstractComponent> iterator = children.iterator(); iterator.hasNext();) {
            final AbstractComponent child = iterator.next();
            if (child.hit(mouse.x, mouse.y, true)) {
                trigger = true;
                child.fire(MOVE, mouse.x, mouse.y, -1, -1);
                if (!child.isHover()) {
                    child.setHover(true);
                    child.fire(ENTER);
                }
            }else if (child.isHover()) {
                child.setHover(false);
                child.fire(EXIT);
            }
        }
        fire(MOVE, mouse.x, mouse.y, -1, -1);
        return trigger;
    }

    @Override
    public boolean scrolled(int amount) {
        if (!isVisible() || !isTouchable()) return false;
        if (!isInsideViewport(mouse.x, mouse.y)) return false;
        for (final Iterator<AbstractComponent> iterator = children.iterator(); iterator.hasNext();) {
            final AbstractComponent child = iterator.next();
            if (child.isHover()) {
                child.fire(SCROLL, amount);
                return true;
            }
        }
        fire(SCROLL, amount);
        return true;
    }

    protected boolean isInsideViewport (final float screenX, float screenY) {
        int x0 = viewport.getScreenX();
        int x1 = x0 + viewport.getScreenWidth();
        int y0 = viewport.getScreenY();
        int y1 = y0 + viewport.getScreenHeight();
        screenY = Gdx.graphics.getHeight() - 1 - screenY;
        return screenX >= x0 && screenX < x1 && screenY >= y0 && screenY < y1;
    }

    protected abstract String context();
    protected abstract List<String> nextScreens();
}
