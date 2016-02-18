package com.myplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;

/**
 * Created by Owner on 2/17/2016.
 */

public class StandState extends State {

    Player player;
    StateMachine stateMachine;
    World world;

    public StandState (Player player) {
        this.player = player;
        this.world = player.world;
        this.stateMachine = player.stateMachine;
    }

    public void enter() {
        player.stateComponent.set(player.animationComponent.frameRangeMap.get("Stand"), 24, Animation.PlayMode.NORMAL);
    }

    public void update(float delta) {


    }

    public void exit() {

    }

    public void handleInput(float delta) {

        //MOVE
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            stateMachine.change("move");
        }

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