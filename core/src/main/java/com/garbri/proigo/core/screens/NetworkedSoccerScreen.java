package com.garbri.proigo.core.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.garbri.proigo.core.AdventureBall;
import com.garbri.proigo.core.controls.ScreenDebug;
import com.garbri.proigo.core.objects.Ball;
import com.garbri.proigo.core.objects.Pitch;
import com.garbri.proigo.core.utilities.SpriteHelper;
import com.garbri.proigo.core.utilities.TextDisplayHelper;
import com.garbri.proigo.core.utilities.TimerHelper;
import com.garbri.proigo.core.vehicles.Car;
import com.garbri.proigo.core.vehicles.Vehicle;

public class NetworkedSoccerScreen extends ScreenDebug {


    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    private World world;

    private Box2DDebugRenderer debugRenderer;

    private int screenWidth;
    private int screenHeight;
    private float worldWidth;
    private float worldHeight;
    private static int PIXELS_PER_METER = 10;      //how many pixels in a meter

    private Ball ball;

    public float ballOffsetX;

    private int redTeamScore = 0;
    private int blueTeamScore = 0;

    private Boolean displayWinMessage;
    private String winMessage;

    private Vector2 center;

    public Pitch pitch;

    private SpriteHelper spriteHelper;

    private AdventureBall game;

    private TimerHelper timer;

    private TextDisplayHelper textDisplayer;

    private Sprite pitchSprite;

    private List<Car> vehicles;

    public Vector2 networkBallPosition;

    public NetworkedSoccerScreen(AdventureBall game) {
        this.game = game;

        this.screenWidth = 1400;
        this.screenHeight = 900;

        this.worldWidth = this.screenWidth / PIXELS_PER_METER;
        this.worldHeight = this.screenHeight / PIXELS_PER_METER;

        this.center = new Vector2(worldWidth / 2, worldHeight / 2);

        this.spriteHelper = new SpriteHelper();

        this.textDisplayer = new TextDisplayHelper();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.screenWidth, this.screenHeight);

        this.debugRenderer = new Box2DDebugRenderer();

        this.pitchSprite = spriteHelper.getPitchSprite(screenWidth, screenHeight);

        this.vehicles = new ArrayList<Car>();
    }

    @Override
    public void render(float delta) {
        this.checkDebugInput(this.game);
        Gdx.gl.glClearColor(0, 0.5f, 0.05f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        this.timer.progressTime();

        this.checkDebugInput(game);

        spriteBatch.setProjectionMatrix(camera.combined);

        if (this.timer.countDownTimer == 0) {
            for (Vehicle vehicle : this.vehicles) {
                vehicle.controlVehicle();
            }
        }

        this.ball.networkUpdate(null, this.networkBallPosition);
        this.ball.update();

        Vector2 ballLocation = this.ball.getLocation();

        if (!displayWinMessage) {
            if (this.pitch.leftGoal.checkForGoal(ballLocation, 0f)) {
                this.displayWinMessage = true;
                this.winMessage = "RED TEAM SCORED!!";
                this.redTeamScore++;
                this.timer.startCountDown(3);
            } else if (this.pitch.rightGoal.checkForGoal(ballLocation, 0f)) {
                this.displayWinMessage = true;
                this.winMessage = "BLUE TEAM SCORED!!";
                this.blueTeamScore++;
                this.timer.startCountDown(3);
            }
        }


        /**
         * Have box2d update the positions and velocities (and etc) of all
         * tracked objects. The second and third argument specify the number of
         * iterations of velocity and position tests to perform -- higher is
         * more accurate but is also slower.
         */
        world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);

        world.clearForces();

        this.spriteBatch.begin();
        //Update Player/Car 1

        this.pitchSprite.setPosition(0, 0);
        this.pitchSprite.draw(spriteBatch);

        for (Vehicle vehicle : this.vehicles) {
            vehicle.updateSprite(spriteBatch, PIXELS_PER_METER);
        }

        String blueTeamString = String.valueOf(this.blueTeamScore);
        String redTeamString = String.valueOf(this.redTeamScore);

        textDisplayer.font.draw(spriteBatch, blueTeamString, (center.x / 2 * PIXELS_PER_METER) - (blueTeamString.length() * 3), (worldHeight - 5f) * PIXELS_PER_METER);
        textDisplayer.font.draw(spriteBatch, redTeamString, ((3 * center.x / 2) * PIXELS_PER_METER) - (redTeamString.length() * 3), (worldHeight - 5f) * PIXELS_PER_METER);

        //Update Ball
        SpriteHelper.updateSprite(ball.sprite, spriteBatch, PIXELS_PER_METER, ball.body);

        if (this.displayWinMessage) {
            textDisplayer.font.draw(spriteBatch, this.winMessage, (center.x * PIXELS_PER_METER) - (this.winMessage.length() * 3), center.y * PIXELS_PER_METER);

            if (this.timer.countDownTimer == 0) {
                this.game.setScreen(this.game.raceScreen);
            }
        }

        this.spriteBatch.end();

        /**
         * Draw this last, so we can see the collision boundaries on top of the
         * sprites and map.
         */
        //debugRenderer.render(world, camera.combined.scale(PIXELS_PER_METER,PIXELS_PER_METER,PIXELS_PER_METER));

    }

    @Override
    public void show() {

        spriteBatch = new SpriteBatch();

        world = new World(new Vector2(0.0f, 0.0f), true);
        this.pitch = new Pitch(world, worldWidth, worldHeight, center);
        this.ball = new Ball(world, center.x + this.ballOffsetX, center.y, spriteHelper.getBallSprite(), true);

        createAllCars();

        this.displayWinMessage = false;

        this.timer = new TimerHelper();

    }

    @Override
    protected void resetGame() {
        dispose();
        spriteBatch = new SpriteBatch();

        for (Vehicle vehicle : this.vehicles) {
            vehicle.destroyVehicle();
        }

        createAllCars();

        this.displayWinMessage = false;

        this.blueTeamScore = 0;
        this.redTeamScore = 0;

        ballOffsetX = 0f;
    }

    private void createAllCars() {
        Car tempCar;
        this.vehicles.clear();
        spriteHelper.resetAvailableSprites();

        for (int i = 0; i < this.game.players.size(); i++) {
            tempCar = new Car(this.game.players.get(i).controls,
            		this.game.players.get(i),
                    this.world,
                    this.pitch.getTeamStartPoint(this.game.players.get(i).playerTeam, i),
                    this.pitch.getTeamStartAngle(this.game.players.get(i).playerTeam),
                    spriteHelper.getTeamCarSprite(this.game.players.get(i).playerTeam),
                    spriteHelper.getWheelSprite());

            this.vehicles.add(tempCar);
        }
    }
}
