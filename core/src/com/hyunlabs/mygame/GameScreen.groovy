package com.hyunlabs.mygame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import groovy.transform.CompileStatic

import static com.badlogic.gdx.Gdx.*
import static com.badlogic.gdx.Input.Keys.LEFT
import static com.badlogic.gdx.Input.Keys.RIGHT

/**
 * Created by danny on 2015-04-26.
 */
@CompileStatic
class GameScreen implements Screen {

    final Drop game

    Texture dropImage
    Texture bucketImage
    Sound dropSound
    Music rainMusic
    OrthographicCamera camera
    Rectangle bucket
    Array<Rectangle> raindrops
    long lastDropTime
    int dropsGathered

    GameScreen(Drop game) {
        this.game = game

        dropImage = new Texture(files.internal("droplet.png"))
        bucketImage = new Texture(files.internal("bucket.png"))

        dropSound = audio.newSound(files.internal("drop.mp3"))
        rainMusic = audio.newMusic(files.internal("rain.mp3"))
        rainMusic.looping = true

        camera = new OrthographicCamera()
        camera.setToOrtho false, 800, 480

        bucket = new Rectangle(x: 800/2 - 64/2, y: 20, width: 64, height: 64)

        raindrops = new Array<Rectangle>()
        spawnRaindrop()
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle(
                x: MathUtils.random(0, 800 -64),
                y: 480,
                width: 64,
                height: 64)
        raindrops.add raindrop
        lastDropTime = TimeUtils.nanoTime()
    }

    @Override
    void show() {
        rainMusic.play()
    }

    @Override
    void render(float delta) {
        Gdx.gl.with {
            glClearColor 0, 0, 0.2f, 1
            glClear GL_COLOR_BUFFER_BIT
        }

        camera.update()

        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        game.font.draw game.batch, "Drops Collected: $dropsGathered", 0, 480
        game.batch.draw bucketImage, bucket.x, bucket.y
        raindrops.each { raindrop ->
            game.batch.draw dropImage, raindrop.x, raindrop.y
        }
        game.batch.end()

        if (input.isTouched()) {
            Vector3 touchPosition = new Vector3(input.x, input.y, 0)
            camera.unproject touchPosition
            bucket.x = touchPosition.x - 64/2
        }

        if (input.isKeyPressed(LEFT)) {
            bucket.x -= 200 * Gdx.graphics.deltaTime
        }
        if (input.isKeyPressed(RIGHT)) {
            bucket.x += 200 * Gdx.graphics.deltaTime
        }

        if (bucket.x < 0) bucket.x = 0
        if (bucket.x > 800 - 64) bucket.x = 800 - 64

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop()

        Iterator<Rectangle> iterator = raindrops.iterator()
        while (iterator.hasNext()) {
            Rectangle raindrop = iterator.next()
            raindrop.y -= 200 * graphics.deltaTime
            if (raindrop.y + 64 < 0) iterator.remove()
            if (raindrop.overlaps(bucket)) {
                dropsGathered ++
                dropSound.play()
                iterator.remove()
            }
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
        dropImage.dispose()
        bucketImage.dispose()
        dropSound.dispose()
        rainMusic.dispose()
    }
}
