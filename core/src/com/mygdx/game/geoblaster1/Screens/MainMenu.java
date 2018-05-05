package com.mygdx.game.geoblaster1.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.geoblaster1.MainActivity;

public class MainMenu implements Screen{
    private MainActivity game;
    private Camera camera;
    private Viewport viewport;
    private Stage stage;
    MyActor myActor;
    private Texture bg, title;
    private BitmapFont font;
    CharSequence str = "0";


    public class MyActor extends Actor {
        Texture texture = new Texture(Gdx.files.internal("playbutton.png"));


        public float actorX = viewport.getWorldWidth() / 2 - (texture.getWidth() / 2), actorY = viewport.getWorldHeight() / 2 - (texture.getHeight());
        public boolean started = false;


        public MyActor() {
            setBounds(actorX, actorY, texture.getWidth(), texture.getHeight());
            addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    ((MyActor) event.getTarget()).started = true;
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    started = false;

                }

            });

        }


        @Override
        public void draw(Batch batch, float alpha) {
            batch.draw(texture, actorX, actorY);
        }

        @Override
        public void act(float delta) {
            setBounds(actorX, actorY, texture.getWidth(), texture.getHeight());
            if (started) {
                startgame();
            }
        }

        public float getplayerX(){
            return actorX;
        }
    }

    private void startgame() {
        game.setScreen(new PlayScreen(game));
    }


    public MainMenu(MainActivity game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(1400, 2900, camera);
        viewport.setScreenPosition(0, 0);

        bg = new Texture(Gdx.files.internal("backgroundtest.jpg"));
        title = new Texture(Gdx.files.internal("title.png"));

        stage = new Stage(viewport);
        myActor = new MyActor();
        Gdx.input.setInputProcessor(stage);
        myActor.setTouchable(Touchable.enabled);
        stage.addActor(myActor);

        font = new BitmapFont(Gdx.files.internal("textfile.fnt"), false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        //game.batch.draw(bg, 0, 0, viewport.getWorldWidth() + 100, bg.getHeight());
        //game.batch.draw(bg, 0, viewport.getWorldHeight() / 2, viewport.getWorldWidth() + 100, bg.getHeight());
        game.batch.draw(title, (viewport.getWorldWidth() - viewport.getWorldWidth()) + viewport.getWorldWidth() / 200, viewport.getWorldHeight() - viewport.getWorldHeight() / 5, viewport.getWorldWidth(), title.getHeight());
        font.draw(game.batch, str, viewport.getWorldWidth() - (viewport.getWorldWidth() / 2) - (viewport.getWorldWidth() / 25), viewport.getWorldHeight() - viewport.getWorldHeight() / 3);
        game.batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
