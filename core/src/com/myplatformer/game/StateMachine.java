package com.myplatformer.game;


import java.util.HashMap;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public class StateMachine {
    private HashMap<String, State> map;
    private State currentState;
    private State previousState;

    public StateMachine() {
        this.map = new HashMap<String, State>();
    }

    public void add(String name, State state) {
        this.map.put(name, state);
    }

    public void change(String name) {
        if(currentState != null) {
            currentState.exit();
            previousState = currentState;
        }
        currentState = map.get(name);
        currentState.enter();
    }

    public void update(float delta) {
        currentState.update(delta);
    }

    public void handleInput(float delta) {
        currentState.handleInput(delta);
    }

    public State getState() {
        return this.currentState;
    }

    public void getPreviousState() {
        return this.previousState;
    }
}