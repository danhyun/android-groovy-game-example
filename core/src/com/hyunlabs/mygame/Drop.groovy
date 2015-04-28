package com.hyunlabs.mygame

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import groovy.transform.CompileStatic

/**
 * Created by danny on 2015-04-26.
 */
@CompileStatic
class Drop extends Game {

    SpriteBatch batch
    BitmapFont font

    @Override
    void create() {
        batch = new SpriteBatch()
        font = new BitmapFont()
        this.screen = new MainMenuScreen(this)
    }

    @Override
    void render() {
        super.render()
    }

    @Override
    void dispose() {
        batch.dispose()
        font.dispose()
    }
}
