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
        this.stateMachine = player.stateMachine;
    }

    public void enter() {
        //if(stateMachine.previousState != stateMachine.currentState)
            player.stateComponent.set(player.animationComponent.frameRangeMap.get("Walk"), 24, Animation.PlayMode.NORMAL);
    }

    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.transformComponent.x -= player.speed.x * player.speedModifier * delta;
            player.transformComponent.scaleX = -1f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.transformComponent.x += player.speed.x * player.speedModifier * delta;
            player.transformComponent.scaleX = 1f;
        }
    }

    public void exit() {

    }

    public void handleInput(float delta) {
        //JUMP
        if(player.autoHop) {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
                stateMachine.change("falling");
        }
        else {
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                stateMachine.change("falling");
        }
    }
}