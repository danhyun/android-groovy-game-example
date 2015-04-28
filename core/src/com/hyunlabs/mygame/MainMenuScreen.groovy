package com.hyunlabs.mygame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import groovy.transform.CompileStatic

/**
 * Created by danny on 2015-04-26.
 */
@CompileStatic
class MainMenuScreen implements Screen {

    final Drop game
    OrthographicCamera camera

    MainMenuScreen(Drop game) {
        this.game = game

        camera = new OrthographicCamera()
        camera.setToOrtho false, 800, 480
    }

    @Override
    void show() {

    }

    @Override
    void render(float delta) {
        Gdx.gl.with {
            glClearColor 0, 0, 0.2f, 1
            glClear GL20.GL_COLOR_BUFFER_BIT
        }
        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        game.font.with {
            draw game.batch, "Welcome to Drop!!!", 100, 150
            draw game.batch, "Tap anywhere to begin", 100, 100
        }
        game.batch.end()

        if (Gdx.input.isTouched()) {
            game.screen = new GameScreen(game)
            dispose()
        }
    }

    @Override
    void resize(int width, int height) {

    }

    @Override
    void pause() {

    }

    @Override
    void resume() {

    }

    @Override
    void hide() {

    }

    @Override
    void dispose() {

    }
}
