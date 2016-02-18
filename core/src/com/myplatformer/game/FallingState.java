package com.myplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public class FallingState extends DynamicObjectState {
    public static final int STATE_FALL = 3;
    public static final int STATE_JUMP = 2;
    int yDirectionState;

    public FallingState(Player player) {
        this.enumState = STATE_JUMP;
        this.player = player;
        this.world = player.world;
    }

    public void enter() {
        player.stateComponent.set(player.animationComponent.frameRangeMap.get("Jump"), 24, Animation.PlayMode.NORMAL);
        player.speed.y = player.jumpSpeed*1.5f;
    }

    public void update(float delta) {

        if((player.momentumTime > 0) || player.bunnyhopGap == 0) {
            player.speedModifier += 0.1;
        } else if(player.momentumTime > 0 ) {
            player.speedModifier = 1;
        }

        if(player.speed.y > 0) {
            this.yDirectionState = STATE_JUMP;
        } else if (player.speed.y < 0 ) {
            this.yDirectionState = STATE_FALL;
        }
    }

    public void exit() {

    }

    public void handleInput(float delta) {
        //MOVE
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.transformComponent.x -= player.speed.x * player.speedModifier * delta;
            player.transformComponent.scaleX = -1f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.transformComponent.x += player.speed.x * player.speedModifier * delta;
            player.transformComponent.scaleX = 1f;
        }
    }

}