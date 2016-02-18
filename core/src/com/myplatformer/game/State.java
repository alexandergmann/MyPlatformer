package com.myplatformer.game;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public abstract class State {
    public abstract void update(float delta);
    public abstract void enter();
    public abstract void exit();
    public abstract void handleInput(float delta);
}