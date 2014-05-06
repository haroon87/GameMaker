package com.gamemaker.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gamemaker.action.Action;
import com.gamemaker.event.Auto;
import com.gamemaker.event.Collision;
import com.gamemaker.event.Event;
import com.gamemaker.event.GameInitEvent;
import com.gamemaker.event.Hop;
import com.gamemaker.event.KeyboardPressEvent;
import com.gamemaker.helper.DatabaseManager;
import com.gamemaker.helper.EventActionHandler;
import com.gamemaker.models.Sprite;
import com.gamemaker.views.GameMakerView;
import com.gamemaker.views.GamePlayView;
import com.gamemaker.views.SignUpLoginView;
import com.gamemaker.views.StartView;

public class MakerController extends Controller {

	private GamePlayView mainDisplay = null;
	private GameMakerView addSpriteView = null;
	private StartView startView = null;
	private SignUpLoginView signUpLoginView;

	private final ArrayList<Sprite> dynamicSprites;
	private EventActionHandler eventActionHandler;

	private final Auto autoObj;
	private final GameInitEvent gameInitEventObj;
	private final Collision collisionObj;
	private final Hop hopObj;
	private final KeyboardPressEvent keyboardPressObj;

	@Override
	public EventActionHandler getEventActionHandler() {
		return eventActionHandler;
	}

	public void setEventActionHandler(EventActionHandler eventActionHandler) {
		this.eventActionHandler = eventActionHandler;
	}

	public GamePlayView getMainDisplay() {
		return mainDisplay;
	}

	public void setMainDisplay(GamePlayView mainDisplay) {
		this.mainDisplay = mainDisplay;
	}

	public GameMakerView getAddSpriteView() {
		return addSpriteView;
	}

	public void setAddSpriteView(GameMakerView addSpriteView) {
		this.addSpriteView = addSpriteView;
	}

	public MakerController() {
		dynamicSprites = new ArrayList<Sprite>();
		eventActionHandler = new EventActionHandler(gameModel.getSpriteList(), dynamicSprites);

		//Initializing initial window. This window will allow login and signup
		signUpLoginView = new SignUpLoginView(this);
		
		autoObj = new Auto();
		gameInitEventObj = new GameInitEvent();
		collisionObj = new Collision();
		hopObj = new Hop();
		keyboardPressObj = new KeyboardPressEvent();
	}

	@Override
	public GamePlayView getView() {
		return this.mainDisplay;
	}

	/*
	 * View should call this function when game maker loading is requested
	 */
	
	public void gameMakerWindowIsRequested(String gameName, boolean resumeExitingGame) {
		if (resumeExitingGame) {
			loadGame(gameName);
		} else {
		}
		addSpriteView = new GameMakerView(this);
		addSpriteView.setLblGameName(gameName);
		addSpriteView.paintSprites(gameModel.getSpriteList(), true);
	}

	public void gamePlayWindowIsRequested(String gameName) {
		DisplayController displayController = new DisplayController();
		displayController.setGameRequest(gameName);
	}


	@Override
	public List<Sprite> getEventActionSpriteData() {
		return getSpritesData();
	}

	// will return a map containing the key as the event and the values as the
	// actions associated with it
	public HashMap<String, ArrayList<String>> getEventActionPairs() {

		HashMap<String, ArrayList<String>> eventActionList = new HashMap<String, ArrayList<String>>();

		eventActionList.put(gameInitEventObj.getName(), listToStringAssociatedActionsHelper(gameInitEventObj.getAssociatedActions()));

		eventActionList.put(autoObj.getName(), listToStringAssociatedActionsHelper(autoObj.getAssociatedActions()));

		eventActionList.put(collisionObj.getName(), listToStringAssociatedActionsHelper(collisionObj.getAssociatedActions()));

		eventActionList.put(hopObj.getName(), listToStringAssociatedActionsHelper(hopObj.getAssociatedActions()));

		eventActionList.put(keyboardPressObj.getName(), listToStringAssociatedActionsHelper(keyboardPressObj.getAssociatedActions()));
		return eventActionList;
	}

	private ArrayList<String> listToStringAssociatedActionsHelper(List<Action> actionList) {
		ArrayList<String> actionListStr = new ArrayList<String>();

		for (int i = 0; i < actionList.size(); i++) {
			actionListStr.add(actionList.get(i).getName());
		}
		return actionListStr;
	}

	// helper method to return gameState as string array
	public String[] gameStateStr() {
		String[] tempGameStateArray = { GameState.GAME_WIN.toString(), GameState.GAME_LOOSE.toString(), GameState.GAME_CONTINUE.toString() };
		return tempGameStateArray;
	}

	public void addAll(List<Sprite> sprites, String gameName) {
		removeExitingSprites(sprites);

		getSpritesData().addAll(sprites);
		saveGame(gameName);
	}

	private void removeExitingSprites(List<Sprite> sprites) {
		getSpritesData().removeAll(sprites);
	}

	public void add(Sprite sprite) {
		if (getSpritesData().contains(sprite)) {
			getSpritesData().remove(sprite);
		}
		getSpritesData().add(sprite);
	}

	public List<String> getAllSprites() {
		List<String> spriteNames = new ArrayList<String>();
		for (Sprite sprite : getSpritesData()) {
			spriteNames.add(sprite.getName());
		}
		return spriteNames;
	}

	public Sprite loadSavedSprites(String selectedSpriteName) {
		Sprite sprite = null;
		List<Sprite> temp = new ArrayList<Sprite>();
		temp = getSpritesData();
		for (Sprite s : temp) {
			if ((s.getName().toString()).equals(selectedSpriteName)) {
				sprite = s;
				this.getAddSpriteView().setSpriteName(s.getName());
				HashMap<String, ArrayList<Action>> tempSpecialActions = new HashMap<String, ArrayList<Action>>();
				tempSpecialActions = s.getSpecialActions();
				HashMap<Event, ArrayList<Action>> tempMap = new HashMap<Event, ArrayList<Action>>();
				tempMap = s.getNewEventActionPairs();
				String summaryText = new String();
				// Iterating over an event
				for (Event k : tempMap.keySet()) {
					for (Action a : tempMap.get(k)) {
						summaryText = k.getName() + "-" + a.getName() + "-" + a.getState() + "-";
						if (tempSpecialActions.isEmpty()) {
							summaryText = summaryText + "";
						} else {
							for (String str : tempSpecialActions.keySet()) {
								summaryText = summaryText + str + ":";
							}
						}
						this.getAddSpriteView().setSummaryTextArea(summaryText);
						summaryText = "";
					}
				}
			}
		}
		return sprite;
	}

	public String[] getGamesList() {

		String[] list = dataBaseManager.savedGames();

		return list;
	}
	
	public void registerUser()
	{
		
	}

}
