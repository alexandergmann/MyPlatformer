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
public class Player implements IScript {

    private Entity player;
    TransformComponent transformComponent;
    World world;
    Vector2 speed;
    float gravity = -500f;
    float jumpSpeed = 100f;
    float speedModifier;
    //private boolean isFalling, isWalking, wasWalking, wasFalling, isCrouching;
    int jumpCounter, momentumTime;
    SpriteAnimationComponent animationComponent;
    SpriteAnimationStateComponent stateComponent;
    DimensionsComponent dimensionsComponent;
    private DecimalFormat df;
    int bunnyhopGap;
    boolean autoHop;
    StateMachine positionStateMachine, movementStateMachine;

    public Player(World world) {
        this.world = world;
    }

    @Override
    public void init(Entity entity) {
        player = entity;

        transformComponent = ComponentRetriever.get(player, TransformComponent.class);
        animationComponent = ComponentRetriever.get(player, SpriteAnimationComponent.class);
        stateComponent = ComponentRetriever.get(player, SpriteAnimationStateComponent.class);
        dimensionsComponent = ComponentRetriever.get(player, DimensionsComponent.class);

        speed = new Vector2(80,0);
        momentumTime = 0;
        speedModifier = 1;
        bunnyhopGap = 6; //4 for extreme, 6 for standard, 10 for easy

        autoHop = false;

        df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);

        positionStateMachine = new StateMachine();
        positionStateMachine.add("crouch", new CrouchState(this));
        positionStateMachine.add("stand", new StandState(this));
        positionStateMachine.add("falling", new FallingState(this));
        positionStateMachine.change("stand");

        movementStateMachine = new StateMachine();
        movementStateMachine.add("move", new MoveState(this));
        movementStateMachine.add("still", new StillState(this));
        movementStateMachine.change("still");

    }

    @Override
    public void act(float delta) {
        if(delta == 0)
            return;

        //GRAVITY
        speed.y += gravity*delta;
        transformComponent.y += speed.y*delta;

        rayCastDown();
        positionStateMachine.update(delta);
        positionStateMachine.handleInput(delta);

        movementStateMachine.update(delta);
        movementStateMachine.handleInput(delta);


        //MOMENTUM
        if(momentumTime > 0)
            momentumTime -= 1;


        /*
        //MOVE LEFT
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            if(!isCrouching) {
                transformComponent.x -= speed.x * speedModifier * delta;
                transformComponent.scaleX = -1f;

                if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    isWalking = true;
                }
                if (!wasFalling && momentumTime <= 0) {
                    speedModifier = 1;
                }
            }
            else {
                transformComponent.x -= speed.x/2 * speedModifier * delta;
                transformComponent.scaleX = -1f;

                    isWalking = true;
                    isCrouching = true;
                    //stateComponent.set(animationComponent.frameRangeMap.get("Crawl"), 24, Animation.PlayMode.LOOP);

                if (!wasFalling && momentumTime <= 0) {
                    speedModifier = 1;
                }
            }


        }
        //MOVE RIGHT
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            if(!isCrouching) {
                transformComponent.x += speed.x * speedModifier * delta;
                transformComponent.scaleX = 1f;

                if (!Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    isWalking = true;
                    isCrouching = false;
                }
                if (!wasFalling && momentumTime <= 0) {
                    speedModifier = 1;
                }
            }
            else {
                transformComponent.x += speed.x/2 * speedModifier * delta;
                transformComponent.scaleX = 1f;

                    isWalking = true;
                    isCrouching = true;
                    //stateComponent.set(animationComponent.frameRangeMap.get("Crawl"), 24, Animation.PlayMode.LOOP);

                if (!wasFalling && momentumTime <= 0) {
                    speedModifier = 1;
                }
            }

        }*/

        /*
        //CROUCH
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && !isWalking && !isCrouching && !isFalling) {
            isCrouching = true;
            stateComponent.set(animationComponent.frameRangeMap.get("Crouch"), 24, Animation.PlayMode.NORMAL);
        }

        //UNCROUCH
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && !isWalking && isCrouching && !isFalling) {
            isCrouching = false;
            stateComponent.set(animationComponent.frameRangeMap.get("Crouch"), 24, Animation.PlayMode.REVERSED);
        }*/




    }




    private void rayCastDown() {
        if(speed.y > 0)
            return;

        if(speed.y < 0) {

            Vector2 rayFrom, rayToDown;
            float rayGap = dimensionsComponent.height/2;
            float raySize = -(speed.y+Gdx.graphics.getDeltaTime())*Gdx.graphics.getDeltaTime();
            rayFrom = new Vector2((transformComponent.x+dimensionsComponent.width/2)* PhysicsBodyLoader.getScale(), (transformComponent.y+rayGap)*PhysicsBodyLoader.getScale());
            rayToDown = new Vector2((transformComponent.x+dimensionsComponent.width/2)* PhysicsBodyLoader.getScale(), (transformComponent.y-raySize)*PhysicsBodyLoader.getScale());

            world.rayCast(new RayCastCallback() {
                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

                    speed.y = 0;
                    transformComponent.y = (point.y / PhysicsBodyLoader.getScale()+ 0.01f);

                    //MOVE

                    positionStateMachine.change("stand");

                    //momentumTime = bunnyhopGap;


                    return 0;
                }
            }, rayFrom, rayToDown);
        }
    }


    @Override
    public void dispose() {

    }

    public float getX() {
        return transformComponent.x;
    }

    public float getY() {
        return transformComponent.y;
    }

    public String getMomentum() {
        return df.format(speedModifier);
    }
}
