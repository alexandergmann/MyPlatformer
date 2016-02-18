package com.myplatformer.game;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public abstract class DynamicObjectState  extends State {
    Player player;
    World world;
    DynamicObjectStateMachine stateMachine;
    public abstract void handleInput(float delta); 
}