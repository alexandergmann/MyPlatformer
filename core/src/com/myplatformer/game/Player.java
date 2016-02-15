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
    private TransformComponent transformComponent;
    private World world;
    private Vector2 speed;
    private float gravity = -500f;
    private float jumpSpeed = 100f;
    private float speedModifier;
    private boolean isFalling, isWalking, wasWalking, wasFalling, isCrouching;
    private int jumpCounter, momentumTime;
    private SpriteAnimationComponent animationComponent;
    private SpriteAnimationStateComponent stateComponent;
    private DimensionsComponent dimensionsComponent;
    private DecimalFormat df;
    private int bunnyhopGap;
    boolean autoHop;

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

        autoHop = true;

        df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
    }

    @Override
    public void act(float delta) {

        if(delta == 0)
            return;

        wasWalking = isWalking;
        isWalking = false;
        wasFalling = isFalling;


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

        }

        //CROUCH
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && !isWalking && !isCrouching && !isFalling) {
            isCrouching = true;
            stateComponent.set(animationComponent.frameRangeMap.get("Crouch"), 24, Animation.PlayMode.NORMAL);
        }

        //UNCROUCH
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && !isWalking && isCrouching && !isFalling) {
            isCrouching = false;
            stateComponent.set(animationComponent.frameRangeMap.get("Crouch"), 24, Animation.PlayMode.REVERSED);
        }

        //JUMP
        if(autoHop) {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
                jump();
        }
        else {
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                jump();
        }

        //GRAVITY
        speed.y += gravity*delta;
        transformComponent.y += speed.y*delta;

        rayCastDown();

        if (wasWalking && !isWalking && !isFalling) {
            if(!isCrouching)
                stateComponent.set(animationComponent.frameRangeMap.get("Stand"), 24, Animation.PlayMode.NORMAL);
            else
                stateComponent.set(animationComponent.frameRangeMap.get("CrouchStay"), 24, Animation.PlayMode.LOOP);
        }
        if (!wasWalking && isWalking && !isFalling) {
            if(!isCrouching)
                stateComponent.set(animationComponent.frameRangeMap.get("Walk"), 24, Animation.PlayMode.LOOP);
            else
                stateComponent.set(animationComponent.frameRangeMap.get("Crawl"), 24, Animation.PlayMode.LOOP);
        }

        //MOMENTUM
        if(momentumTime > 0)
            momentumTime -= 1;

    }

    private void jump() {
        if(!isFalling) {
            speed.y = jumpSpeed*1.5f;
            isFalling = true;
            isCrouching = false;
            stateComponent.set(animationComponent.frameRangeMap.get("Jump"), 24, Animation.PlayMode.NORMAL);
            jumpCounter++;
            if((momentumTime > 0 && isWalking) || bunnyhopGap == 0) {
                speedModifier += 0.1;
            }
            if(momentumTime > 0 && !isWalking) {
                speedModifier = 1;
            }
        }
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
                    if(isFalling) {
                        stateComponent.set(animationComponent.frameRangeMap.get("Stand"), 24, Animation.PlayMode.NORMAL);
                        wasWalking = false;
                    }
                    isFalling = false;
                    jumpCounter = 0;
                    if(wasFalling)
                        momentumTime = bunnyhopGap;


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