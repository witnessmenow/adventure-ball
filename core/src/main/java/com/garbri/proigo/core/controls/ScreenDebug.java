package com.garbri.proigo.core.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.garbri.proigo.core.AdventureBall;

/**
 * All the debug controls are defined here for the screens
 * User: Conor
 * Date: 5/27/13
 * Time: 11:28 AM
 */
public abstract class ScreenDebug implements Screen {
    public void checkDebugInput(AdventureBall game){
        checkReset();
        changePlayers(game);
        serverControl(game);
        changeLevel(game);
    }

    private void checkReset() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        {
            resetGame();
        }
    }

    private void changeLevel(AdventureBall game) {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0))
        {
            game.setScreen(game.soccerScreen);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9))
        {
            game.setScreen(game.raceScreen);
        }
    }

    private void serverControl(AdventureBall game) {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5))
        {
            game.startServer();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_6))
        {
            game.connectToServer();
        }
    }

    private void changePlayers(AdventureBall game) {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2))
        {
            game.changeNumberPlayers(2, this);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4))
        {
            game.changeNumberPlayers(4, this);
        }
    }

    protected abstract void resetGame();

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }



}
