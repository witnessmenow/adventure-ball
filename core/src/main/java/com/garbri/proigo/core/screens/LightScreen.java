package com.garbri.proigo.core.screens;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.controls.ScreenDebug;
import com.garbri.proigo.core.objects.Ball;
import com.garbri.proigo.core.utilities.SpriteHelper;
import com.garbri.proigo.core.vehicles.Car;

public class LightScreen extends ScreenDebug {

    OrthographicCamera camera;
    RayHandler rayHandler;
    World world;

    protected Box2DDebugRenderer debugRenderer;
    protected float accumulator;
    private static int PIXELS_PER_METER = 10;      //how many pixels in a meter

    float width = 1400;
    float height = 900;

    protected static final float BOX_STEP = 1 / 30f;
    protected static final int BOX_VELOCITY_ITERATIONS = 6;
    protected static final int BOX_POSITION_ITERATIONS = 2;

    private SpriteHelper spriteHelper;

    private Sprite finishLine;
    private SpriteBatch spriteBatch;

    private Ball ball;
    private Car car;

    private AdventureBall game;

    public LightScreen(AdventureBall game) {
        this.game = game;
    }

    @Override
    public void show() {

        this.spriteHelper = new SpriteHelper();

        this.finishLine = this.spriteHelper.getFinishLineSprite(20, 20);

        spriteBatch = new SpriteBatch();

        debugRenderer = new Box2DDebugRenderer();

        accumulator = 0.0f;


        camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 70, 45);
        camera.update();
        world = new World(new Vector2(0, 0), true);

        rayHandler = new RayHandler(world);
        rayHandler.setCombinedMatrix(camera.combined);

        new PointLight(rayHandler, 50, Color.CYAN, 20, 30, 15);
        new PointLight(rayHandler, 50, Color.GREEN, 20, 40, 25);
        new ConeLight(rayHandler, 50, Color.PINK, 100, 30, 35, 270, 35);
        new ConeLight(rayHandler, 500, Color.PINK, 700, width / 4 + 50, height / 4 + 15, 270, 35);

        BodyDef circleDef = new BodyDef();
        circleDef.type = BodyType.DynamicBody;
        circleDef.position.set(width / 4, height / 4);

        Body circleBody = world.createBody(circleDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(6f);

        FixtureDef circleFixture = new FixtureDef();
        circleFixture.shape = circleShape;
        circleFixture.density = 0.4f;
        circleFixture.friction = 0.2f;
        circleFixture.restitution = 0.6f;

        circleBody.createFixture(circleFixture);

        // ground
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, 3);

        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth * 2, 3.0f);

        groundBody.createFixture(groundBox, 0.0f);

        this.ball = new Ball(world, width / 2, height / 2, this.spriteHelper.getBallSprite());

        this.car = new Car(this.game.players.get(0).controls,
        		this.game.players.get(0), world, new Vector2(40, 20), 180, this.spriteHelper.getCarSprite(1), this.spriteHelper.getWheelSprite());


    }

    @Override
    public void render(float delta) {
        this.resetGame();

        Gdx.gl.glClearColor(0, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();
        this.car.controlVehicle();
        this.spriteBatch.begin();
        this.finishLine.setPosition((600), (600));
        this.finishLine.draw(spriteBatch);
        car.updateSprite(spriteBatch, 10);
        this.spriteBatch.end();

        rayHandler.render();
        debugRenderer.render(world, camera.combined);
        world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);

        accumulator += delta;
        rayHandler.update();

        this.ball.update();

    }

    @Override
    protected void resetGame() {
    }

}
