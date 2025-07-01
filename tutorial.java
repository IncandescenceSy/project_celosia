package io.github.celosia;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SpriteBatch spriteBatch;
    FitViewport viewport;

    Vector2 touchPos;

    Texture texBg;
    Texture texBucket;
    Texture texDrop;
    Sound sfxDrop;
    Music music;

    Sprite sprBucket;

    float worldWidth;
    float worldHeight;
    float bucketWidth;
    float bucketHeight;

    Array<Sprite> dropSprites;
    float dropTimer = 0;

    Rectangle recBucket;
    Rectangle recDrop;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        touchPos = new Vector2();

        texBg = new Texture("tutorial/bg.png");
        texBucket = new Texture("tutorial/bucket.png");
        texDrop = new Texture("tutorial/drop.png");
        sfxDrop = Gdx.audio.newSound(Gdx.files.internal("tutorial/drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("tutorial/music.mp3"));

        sprBucket = new Sprite(texBucket);
        sprBucket.setSize(1, 1);

        worldWidth = viewport.getWorldWidth();
        worldHeight = viewport.getWorldHeight();
        bucketWidth = sprBucket.getWidth();
        bucketHeight = sprBucket.getHeight();

        dropSprites = new Array<>();

        recBucket = new Rectangle();
        recDrop = new Rectangle();

        music.setLooping(true);
        music.setVolume(.5f);
        music.play();
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            sprBucket.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sprBucket.translateX(-speed * delta);
        }

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            viewport.unproject(touchPos); // Convert the units to the world units of the viewport
            sprBucket.setCenterX(touchPos.x); // Change the horizontally centered position of the bucket
        }
    }

    private void logic() {
        // Subtract the bucket width
        sprBucket.setX(MathUtils.clamp(sprBucket.getX(), 0, worldWidth - bucketWidth));

        float delta = Gdx.graphics.getDeltaTime();

        recBucket.set(sprBucket.getX(), sprBucket.getY(), bucketWidth, bucketHeight);

        // Loop through the sprites backwards to prevent out of bounds errors
        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite sprDrop = dropSprites.get(i); // Get the sprite from the list
            float dropHeight = sprDrop.getHeight();
            float dropWidth = sprDrop.getWidth();

            sprDrop.translateY(-2f * delta);
            recDrop.set(sprDrop.getX(), sprDrop.getY(), dropWidth, dropHeight);

            // if the top of the drop goes below the bottom of the view, remove it
            if (sprDrop.getY() < -dropHeight) dropSprites.removeIndex(i);
            else if (recBucket.overlaps(recDrop)) { // Check if the bucket overlaps the drop
                dropSprites.removeIndex(i); // Remove the drop
                sfxDrop.play(); // Play the sound
            }
        }

        dropTimer += delta; // Adds the current delta to the timer
        if (dropTimer > 1f) { // Check if it has been more than a second
            dropTimer = 0; // Reset the timer
            createDroplet(); // Create the droplet
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        spriteBatch.draw(texBg, 0, 0, worldWidth, worldHeight);
        sprBucket.draw(spriteBatch);

        // draw each sprite
        for (Sprite sprDrop : dropSprites) {
            sprDrop.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    private void createDroplet() {
        // create local variables for convenience
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // create the drop sprite
        Sprite sprDrop = new Sprite(texDrop);
        sprDrop.setSize(dropWidth, dropHeight);
        sprDrop.setX(MathUtils.random(0f, worldWidth - dropWidth)); // Randomize the drop's x position
        sprDrop.setY(worldHeight);
        dropSprites.add(sprDrop); // Add it to the list
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }


    @Override
    public void dispose() {
    }
}
