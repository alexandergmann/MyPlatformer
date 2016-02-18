package com.myplatformer.game;


import java.util.HashMap;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public class StateMachine {
    HashMap<String, State> map;
    State currentState;
    State previousState;

    public StateMachine() {
        map = new HashMap<String, State>();
    }

    public void add(String name, State state) {
        map.put(name, state);
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
}