package com.myplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public class MoveState extends State {

    Player player;
    StateMachine stateMachine;
    World world;

    public MoveState(Player player) {
        this.player = player;
        this.world = player.world;
        this.stateMachine = player.movementStateMachine;
    }

    public void enter() {
        //if(stateMachine.previousState != stateMachine.currentState)
            player.stateComponent.set(player.animationComponent.frameRangeMap.get("Walk"), 24, Animation.PlayMode.LOOP);
    }

    public void update(float delta) {

    }

    public void exit() {

    }

    public void handleInput(float delta) {
        //MOVE
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.transformComponent.x -= player.speed.x * player.speedModifier * delta;
            player.transformComponent.scaleX = -1f;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.transformComponent.x += player.speed.x * player.speedModifier * delta;
            player.transformComponent.scaleX = 1f;
        }
        else {
            stateMachine.change("still");
        }
    }
}