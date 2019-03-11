package com.myteam.intermon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.intermon.states.GameStateManager;
import com.myteam.intermon.states.LoginState;

public class Intermon extends Game
{
	public static final int width = 480;
	public static final int height = 800;
	public static final String TITLE = "INTERMON";
	private GameStateManager gsm;
	private SpriteBatch batch;
	private ClientSocket cs = new ClientSocket();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		cs.connectSocket();
		cs.connectStreams();
		gsm.push(new LoginState(gsm, cs));
	}

	@Override
	public void render () {
		super.render();
		Gdx.gl.glClearColor(1, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
