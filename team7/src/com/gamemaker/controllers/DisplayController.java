package com.gamemaker.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gamemaker.action.DownHopAction;
import com.gamemaker.action.DownMovementAction;
import com.gamemaker.action.FireAction;
import com.gamemaker.action.LeftHopAction;
import com.gamemaker.action.LeftMovementAction;
import com.gamemaker.action.RightHopAction;
import com.gamemaker.action.RightMovementAction;
import com.gamemaker.action.UpHopAction;
import com.gamemaker.action.UpMovementAction;
import com.gamemaker.event.Hop;
import com.gamemaker.event.KeyboardPressEvent;
import com.gamemaker.helper.EventActionHandler;
import com.gamemaker.helper.TimerTask;
import com.gamemaker.models.Sprite;
import com.gamemaker.views.GamePlayView;

public class DisplayController extends Controller {

	private final GamePlayView gamePlayView;
	private final ArrayList<Sprite> dynamicSprites;
	private TimerTask timerTask;
	private EventActionHandler eventActionHandler;
	
	
	@Override
	public EventActionHandler getEventActionHandler() {
		return eventActionHandler;
	}

	public void setEventActionHandler(EventActionHandler eventActionHandler) {
		this.eventActionHandler = eventActionHandler;
	}

	public void setTimerTask(TimerTask timerTask) {
		this.timerTask = timerTask;
	}

	public DisplayController() {
		super();

		//Initialize the view
		gamePlayView = new GamePlayView(this);
		gamePlayView.setVisible(true);
		dynamicSprites = new ArrayList<Sprite>();
	
		timerTask = new TimerTask(this);
		
		eventActionHandler = new EventActionHandler(getSpritesData(), dynamicSprites);
	}

	@Override
	public GamePlayView getView() {
		return gamePlayView;
	}

	/*
	 * Helper class to catch keypress events and delegate to event action
	 * handler
	 * 
	 * View should call this function. This will check whether we are in play
	 * state or not. If we are in play state then all actions are executed
	 * 
	 */
	public void keyIsPressed(KeyEvent ke) {
		if (isGameRunning()) {
			int key = ke.getKeyCode();
			if (key == KeyEvent.VK_LEFT) {
				eventActionHandler.executeAction(new KeyboardPressEvent(), new LeftMovementAction());
				eventActionHandler.executeAction(new Hop(), new LeftHopAction());
			} else if (key == KeyEvent.VK_RIGHT) {
				eventActionHandler.executeAction(new KeyboardPressEvent(), new RightMovementAction());
				eventActionHandler.executeAction(new Hop(), new RightHopAction());
			} else if (key == KeyEvent.VK_UP) {
				eventActionHandler.executeAction(new KeyboardPressEvent(), new UpMovementAction());
				eventActionHandler.executeAction(new Hop(), new UpHopAction());
			} else if (key == KeyEvent.VK_DOWN) {
				eventActionHandler.executeAction(new KeyboardPressEvent(), new DownMovementAction());
				eventActionHandler.executeAction(new Hop(), new DownHopAction());
			} else if (key == KeyEvent.VK_SPACE) {
				eventActionHandler.executeAction(new KeyboardPressEvent(), new FireAction(getSpritesData()));
			}
		}
	}


	@Override
	public List<Sprite> getEventActionSpriteData() {
		return getSpritesData();

	}

	private boolean isGameRunning() {
		return timerTask.isRunning();
	}

	public void playIsRequested() {
		
		timerTask.run();
		timerTask.setGametimer(getGameModel().isTimer());
		gamePlayView.draw(getSpritesData());
	}

	public void stopIsRequested() {
		if (isGameRunning()) {
			timerTask.stop();
			gamePlayView.draw(getSpritesData());
		}
	}

	public void pauseIsRequested() {
		if (isGameRunning()) {
			timerTask.stop();
		}
	}

	public void setGameRequest(String gameName) {

		//The current game is first stored into the memory and then gameplay is started
		
		loadGame(gameName);
		gamePlayView.draw(getSpritesData());
	}

	
}
