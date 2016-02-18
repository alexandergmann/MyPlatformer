package com.myplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public class CrouchState extends State {

    public static final int STATE_CROUCH = 3;

    public CrouchState(Player player) {
        this.enumState = STATE_CROUCH;
        this.player = player;
        this.world = player.world;
    }

    public void enter() {
        player.stateComponent.set(player.animationComponent.frameRangeMap.get("Crouch"), 24, Animation.PlayMode.NORMAL);
    }

    public void update(float delta) {

    }

    public void exit() {
        player.stateComponent.set(player.animationComponent.frameRangeMap.get("Crouch"), 24, Animation.PlayMode.REVERSED);
    }

    public void handleInput(float delta) {

    }

}