package com.myplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Owner on 2/17/2016.
 */

public class StillState extends State {

    Player player;
    StateMachine stateMachine;
    World world;

    public StillState(Player player) {
        this.player = player;
        this.world = player.world;
        this.stateMachine = player.movementStateMachine;
    }

    public void enter() {
        player.stateComponent.set(player.animationComponent.frameRangeMap.get("Stand"), 24, Animation.PlayMode.NORMAL);
    }

    public void update(float delta) {


    }

    public void exit() {

    }

    public void handleInput(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            stateMachine.change("move");
        }
    }
}