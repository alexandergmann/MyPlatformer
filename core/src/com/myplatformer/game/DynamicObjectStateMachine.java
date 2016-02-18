package com.myplatformer.game;


import java.util.HashMap;

/**
 * Created by Aaron Weiss on 2/17/2016.
 */

public class DynamicObjectStateMachine extends StateMachine {    

    public StateMachine() {
        // call constructor for statemachine class
        super();
        super.add("stand", new StandState(this));
        super.add("falling", new FallingState(this));
        super.add("move", new MoveState(this));
        super.change("stand");
    }
}