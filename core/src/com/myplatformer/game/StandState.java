package com.myplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Owner on 2/17/2016.
 */

public class StandState extends DynamicObjectState {
    public static final int STATE_STAND = 2;

    public StandState (Player player) {
        this.enumState = STATE_STAND;
        this.player = player;
        this.world = player.world;
        this.stateMachine = player.positionStateMachine;
    }

    public void enter() {

    }

    public void update(float delta) {
        if(player.momentumTime == 0) {
            player.speedModifier = 1;
        }
    }

    public void exit() {

    }

   public void handleInput(float delta) {

        //JUMP
        if(player.autoHop) {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
                jump();
        }
        else {
          if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                jump();
        }

        //CROUCH
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            stateMachine.change("crouch");
    }

    private void jump() {
        stateMachine.change("falling");
    }
}