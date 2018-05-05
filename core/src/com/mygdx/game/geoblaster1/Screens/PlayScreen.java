package com.mygdx.game.geoblaster1.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.geoblaster1.MainActivity;

import java.util.Iterator;
import java.util.Random;

public class PlayScreen implements Screen{

    private MainActivity game;
    private Stage stage;
    private Camera camera;
    private Viewport viewport;
    MyActor myActor;
    private Texture circle;
    private double timescale = 1;

    private int testgithubrepository = 1;
    private Rectangle playerRECT;
    private Texture bulletimg, bg, enemysquare, enemyRectangle;
    private Array<Rectangle> bullets;
    private Array<Rectangle> enemySQUARES;
    private Array<Rectangle> enemyRECTANGLES;
    private Array<Circle> enemyCIRCLES;
    private Long lastDropTime, lastSpawnTime, lastcirclespawn, spawnrate, shotduration, lastrectanglespawn;
    private int bullet1 = 1;
    private Random rand;
    private Sound shotSound;


    public class MyActor extends Actor {
        Texture texture = new Texture(Gdx.files.internal("tankreworked.png"));

        public float actorX = viewport.getWorldWidth() / 2 - (texture.getWidth() / 2), actorY = viewport.getWorldHeight() / 8;
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
                Vector3 touchPos = new Vector3(Gdx.input.getX(), 0, 0);
                camera.unproject(touchPos);
                //float deltaX = touchPos.x - actorX;
                actorX = touchPos.x - texture.getWidth() / 2;
            }
        }

        public float getplayerX(){
            return actorX;
        }
    }






    public PlayScreen(MainActivity game){
        //setting up the camera/viewport/stage
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(1200, 2320, camera);
        viewport.setScreenPosition(0, 0);
        stage = new Stage(viewport);

        myActor = new MyActor();
        Gdx.input.setInputProcessor(stage);
        myActor.setTouchable(Touchable.enabled);
        stage.addActor(myActor);


        bulletimg = new Texture("bulletimg.png");
        bg = new Texture(Gdx.files.internal("backgroundtest.jpg"));
        enemysquare = new Texture(Gdx.files.internal("enemySQUARE.png"));
        circle = new Texture(Gdx.files.internal("enemyCIRCLE.png"));
        enemyRectangle = new Texture(Gdx.files.internal("enemyRectangle.png"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("popv2.mp3"));

        bullets = new Array<Rectangle>();
        enemySQUARES = new Array<Rectangle>();
        lastDropTime = TimeUtils.nanoTime();
        lastSpawnTime = TimeUtils.nanoTime();
        lastcirclespawn = TimeUtils.nanoTime();
        shotduration = TimeUtils.nanoTime();
        spawnrate = TimeUtils.nanoTime();
        lastrectanglespawn = TimeUtils.nanoTime();
        rand = new Random();
        playerRECT = new Rectangle();

        enemyCIRCLES = new Array<Circle>();
        enemyRECTANGLES = new Array<Rectangle>();
    }

    public void spawnbullets(){
        Rectangle bullet = new Rectangle();
        //Random rand = new Random();

        float bulletx1 = (myActor.getX() + myActor.texture.getWidth() / 2) + myActor.texture.getWidth() / 30;
        float bulletx2 = (myActor.getX() + myActor.texture.getWidth() / 2) - myActor.texture.getWidth() / 22;
        if(bullet1 == 1){
            bullet.x = bulletx1;
            bullet1 = 0;
        }else{
            bullet.x = bulletx2;
            bullet1 = 1;
        }

        bullet.y = myActor.getY() + myActor.texture.getHeight() - (myActor.texture.getHeight() / 5);
        bullet.width = 35;
        bullet.height = 50;
        bullets.add(bullet);
        lastDropTime = TimeUtils.nanoTime();
    }

    public void spawnenemyCIRCLE(){
        Circle circleenemy = new Circle();

        circleenemy.x = rand.nextFloat() * ((viewport.getWorldWidth() - viewport.getWorldWidth() / 8) - (viewport.getWorldWidth() / 100)) + viewport.getWorldWidth() / 100;
        circleenemy.y = viewport.getWorldHeight();
        circleenemy.radius = circle.getWidth() / 2;
        enemyCIRCLES.add(circleenemy);
        lastcirclespawn = TimeUtils.nanoTime();
    }

    public void spawnenemyRECTANGLE(){
        Rectangle rectangleenemy = new Rectangle();

        rectangleenemy.x = rand.nextFloat() * ((viewport.getWorldWidth() - viewport.getWorldWidth() / 8) - (viewport.getWorldWidth() / 100)) + viewport.getWorldWidth() / 100;
        rectangleenemy.y = viewport.getWorldHeight();
        rectangleenemy.width = enemyRectangle.getWidth();
        rectangleenemy.height = enemyRectangle.getHeight();
        enemyRECTANGLES.add(rectangleenemy);
        lastrectanglespawn = TimeUtils.nanoTime();
    }

    public void spawnenemySQUARE(){
        Rectangle square = new Rectangle();

        square.x = rand.nextFloat() * ((viewport.getWorldWidth() - viewport.getWorldWidth() / 8) - (viewport.getWorldWidth() / 100)) + viewport.getWorldWidth() / 100;
        square.y = viewport.getWorldHeight();
        square.width = enemysquare.getWidth();
        square.height = enemysquare.getHeight();
        enemySQUARES.add(square);
        lastSpawnTime = TimeUtils.nanoTime();
    }

    public void update(float dt){

        if (TimeUtils.timeSinceNanos(spawnrate) > 1000000000) {
            timescale = timescale - .005;
            spawnrate = TimeUtils.nanoTime();
        }


        if (TimeUtils.timeSinceNanos(shotduration) > 480000000) {
            shotSound.play();
            shotduration = TimeUtils.nanoTime();
        }



        playerRECT.set(myActor.actorX, myActor.actorY, myActor.texture.getWidth(), myActor.texture.getHeight());
        if(TimeUtils.nanoTime() - lastDropTime > 500000000) spawnbullets();

        Iterator<Rectangle> bulletIter = bullets.iterator();
        while(bulletIter.hasNext()) {

            Rectangle bullet = bulletIter.next();
            for(int j = enemySQUARES.size - 1; j >= 0; j--){
                Rectangle square = enemySQUARES.get(j);
                if (bullet.overlaps(square)) {


                }
            }




            if(bullet.getX() == viewport.getWorldWidth() / 2){
                bulletIter.remove();
            }

            if(bullet.getY() >= viewport.getWorldHeight()){
                bullet.set(viewport.getWorldWidth() + 200, viewport.getWorldHeight() / 10, 20, 20);
            }

            bullet.y += 38;
        }


        if(TimeUtils.nanoTime() - lastSpawnTime > 900000000 * timescale) spawnenemySQUARE();
        Iterator<Rectangle> enemySquareIter = enemySQUARES.iterator();
        while(enemySquareIter.hasNext()){
            Rectangle square = enemySquareIter.next();
            for(int j = bullets.size - 1; j >= 0; j--){
                Rectangle bullet = bullets.get(j);
                if (bullet.overlaps(square)) {
                    enemySquareIter.remove();
                    bullet.set(viewport.getWorldWidth() + 200, viewport.getWorldHeight() / 2 -100, 20, 20);

                }

            }

            if(square.overlaps(playerRECT)){
                Gdx.app.exit();
            }

            if(square.getY() < viewport.getWorldHeight() - viewport.getWorldHeight()){
                enemySquareIter.remove();
            }
            square.y -= 10;
        }

        if(TimeUtils.nanoTime() - lastcirclespawn > 900000000 * timescale) spawnenemyCIRCLE();
        Iterator<Circle> enemyCirclesIter = enemyCIRCLES.iterator();
        while(enemyCirclesIter.hasNext()){
            Circle circleenemy = enemyCirclesIter.next();
            for(int j = bullets.size - 1; j >= 0; j--) {
                Rectangle bullet = bullets.get(j);
                if(Intersector.overlaps(circleenemy, bullet)){
                    enemyCirclesIter.remove();
                    bullet.set(viewport.getWorldWidth() + 200, viewport.getWorldHeight() / 2 -100, 20, 20);
                }
            }
            if(Intersector.overlaps(circleenemy, playerRECT)){
                Gdx.app.exit();
            }
            if(circleenemy.y == viewport.getWorldHeight() - viewport.getWorldHeight()){
                enemyCirclesIter.remove();
            }
            circleenemy.y -= 10;
        }


        if(TimeUtils.nanoTime() - lastrectanglespawn > 900000000 * timescale) spawnenemyRECTANGLE();
        Iterator<Rectangle> enemyRectangleIter = enemyRECTANGLES.iterator();
        while(enemyRectangleIter.hasNext()){
            Rectangle rectangleenemy = enemyRectangleIter.next();
            for(int j = bullets.size - 1; j >= 0; j--) {
                Rectangle bullet = bullets.get(j);
                if(Intersector.overlaps(rectangleenemy, bullet)){
                    enemyRectangleIter.remove();
                    bullet.set(viewport.getWorldWidth() + 200, viewport.getWorldHeight() / 2 -100, 20, 20);
                }
            }
            if(rectangleenemy.overlaps(playerRECT)){
                Gdx.app.exit();
            }
            if(rectangleenemy.getY() == viewport.getWorldHeight() - viewport.getWorldHeight()){
                enemyRectangleIter.remove();
            }
            rectangleenemy.y -= 10;
        }
    }


    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor( 255, 255, 255, 1 );
        stage.act(Gdx.graphics.getDeltaTime());

        game.batch.begin();
        game.batch.draw(bg, 0, 0, viewport.getWorldWidth() + 100, bg.getHeight());
        game.batch.draw(bg, 0, viewport.getWorldHeight() / 2, viewport.getWorldWidth() + 100, bg.getHeight());

        //game.batch.draw(bg, 250, viewport.getWorldHeight() / 2);
        for(Rectangle bullet: bullets) {
            game.batch.draw(bulletimg, bullet.getX(), bullet.getY());
        }
        for(Rectangle square : enemySQUARES){
            game.batch.draw(enemysquare, square.getX(), square.getY());
        }
        for(Circle circleenemy : enemyCIRCLES){
            game.batch.draw(circle, circleenemy.x - (circle.getWidth() / 2), circleenemy.y - (circle.getHeight() / 2));
        }
        for(Rectangle rectangleenemy : enemyRECTANGLES){
            game.batch.draw(enemyRectangle, rectangleenemy.getX(), rectangleenemy.getY());
        }
        game.batch.end();
        game.batch.setProjectionMatrix(stage.getCamera().combined);
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

    @Override
    public void show() {

    }
}
