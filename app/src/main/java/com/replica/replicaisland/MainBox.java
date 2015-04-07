package com.replica.replicaisland;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.jimulabs.mirrorsandbox.MirrorSandboxBase;


/**
 * Created by lintonye on 15-01-15.
 */
public class MainBox extends MirrorSandboxBase {
    private Game mGame;

    public MainBox(View root) {
        super(root);
    }

    @Override
    public void $onLayoutDone(View rootView) {
        mGame = new Game();
        GLSurfaceView surfaceView = (GLSurfaceView) rootView.findViewById(R.id.glsurfaceview);
        mGame.setSurfaceView(surfaceView);
        Context context = surfaceView.getContext();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int defaultWidth = 480;
        int defaultHeight = 320;
        if (dm.widthPixels != defaultWidth) {
            float ratio =((float)dm.widthPixels) / dm.heightPixels;
            defaultWidth = (int)(defaultHeight * ratio);
        }

        mGame.bootstrap(context, dm.widthPixels, dm.heightPixels, defaultWidth, defaultHeight, 1);
        surfaceView.setRenderer(mGame.getRenderer());

        LevelTree.loadLevelTree(R.xml.level_tree, context);
        LevelTree.loadAllDialog(context);
        mGame.setPendingLevel(LevelTree.get(3, 0));

        mGame.onStartCallback = new Runnable() {
            @Override
            public void run() {
                GameObject player = BaseObject.sSystemRegistry.gameObjectManager.getPlayer();
                Log.d("MainBox", "player="+player);
                player.setVelocity(new Vector2(0, 1500));
                player.setCurrentAction(GameObject.ActionType.DEATH);
            }
        };
    }

    @Override
    public void $onDestroy() {
        mGame.stop();
    }
}

