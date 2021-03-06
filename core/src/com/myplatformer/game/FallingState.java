package com.myplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public class FallingState extends DynamicObjectState {
    public static final int STATE_FALL = 5;
    public static final int STATE_JUMP = 4;
    int yDirectionState;

    public FallingState(Player player) {
        this.enumState = STATE_JUMP;
        this.player = player;
        this.world = player.world;
        this.stateMachine = player.positionStateMachine;
    }

    public void enter() {
        player.stateComponent.set(player.animationComponent.frameRangeMap.get("Jump"), 24, Animation.PlayMode.NORMAL);
        player.speed.y = player.jumpSpeed*1.5f;

        if((player.momentumTime > 0)) {
            player.speedModifier += 0.1;
        }
        if(player.momentumTime == 0 ) {
            player.speedModifier = 1;
        }
        if(player.speed.y > 0) {
            this.yDirectionState = STATE_JUMP;
        } else if (player.speed.y < 0 ) {
            this.yDirectionState = STATE_FALL;
        }
    }

    public void update(float delta) {
        
    }

    public void exit() {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.stateComponent.set(player.animationComponent.frameRangeMap.get("Walk"), 24, Animation.PlayMode.LOOP);
        } else {
            player.stateComponent.set(player.animationComponent.frameRangeMap.get("Stand"), 24, Animation.PlayMode.NORMAL);
        }

        player.momentumTime = player.bunnyhopGap;
    }

    public void handleInput(float delta) {

    }

}