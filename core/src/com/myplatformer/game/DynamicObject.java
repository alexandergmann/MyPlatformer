package com.myplatformer.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Aaron Weiss on 1/23/2016.
 */
public abstract class DynamicObject {
    TransformComponent transformComponent;
    World world;
    Vector2 speed;
    float gravity = -500f;
    float jumpSpeed = 100f;
    float speedModifier;
    SpriteAnimationComponent animationComponent;
    SpriteAnimationStateComponent stateComponent;
    DimensionsComponent dimensionsComponent;
    private DecimalFormat df;
    DynamicObjectStateMachine stateMachine;

    private abstract void rayCastDown();
    public abstract float getX();
    public abstract float getY();
    public abstract String getMomentum();
}
