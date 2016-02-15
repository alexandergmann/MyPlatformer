package com.myplatformer.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Owner on 1/30/2016.
 */
public class End implements IScript {

    private TransformComponent transformComponent;
    private Entity end;
    private DimensionsComponent dimensionsComponent;
    private World world;

    public End(World world) {
        this.world = world;

    }

    @Override
    public void init(Entity entity) {
        end = entity;
        transformComponent = ComponentRetriever.get(end, TransformComponent.class);
        dimensionsComponent = ComponentRetriever.get(end, DimensionsComponent.class);
    }

    @Override
    public void act(float delta) {
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
}
