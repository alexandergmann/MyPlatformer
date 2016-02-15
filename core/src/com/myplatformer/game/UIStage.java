package com.myplatformer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.scene2d.CompositeActor;

/**
 * Created by Aaron Weiss on 2/13/2016.
 */

public class UIStage extends Stage {

    BitmapFont font = new BitmapFont();
    SpriteBatch batch = new SpriteBatch();
    CompositeActor buttonActor, playButtonActor;
    CompositeItemVO menuButtonData;
    ProjectInfoVO projectInfoVO;
    GlyphLayout layout, playLayout;
    boolean paused;

    public UIStage(IResourceRetriever ir) {
        paused = true;

        Gdx.input.setInputProcessor(this);
        projectInfoVO = ir.getProjectVO();
        layout  = new GlyphLayout(font,"Quit.");
        playLayout  = new GlyphLayout(font,"Continue.");
        menuButtonData = projectInfoVO.libraryItems.get("menuButton96x32");

        buttonActor = new CompositeActor(menuButtonData, ir);
        addActor(buttonActor);

        buttonActor.setX(getWidth()/2-buttonActor.getWidth()/2);
        buttonActor.setY(getHeight()/4);

        buttonActor.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        playButtonActor = new CompositeActor(menuButtonData, ir);
        addActor(playButtonActor);
        playButtonActor.setX(getWidth()/2-playButtonActor.getWidth()/2);
        playButtonActor.setY(getHeight()/4*3);

        playButtonActor.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                paused = false;
            }
        });

    }

    public void drawText() {
        batch.begin();
        font.draw(batch, layout, getWidth()/2-layout.width/2, getHeight()/4 + buttonActor.getHeight()-layout.height);
        font.draw(batch, playLayout, getWidth()/2-playLayout.width/2, getHeight()/4*3 + buttonActor.getHeight()-layout.height);
        batch.end();
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }
}
