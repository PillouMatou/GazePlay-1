package net.gazeplay.gameslocator;

import javafx.scene.Scene;
import net.gazeplay.GameContext;
import net.gazeplay.GameLifeCycle;
import net.gazeplay.GameSpec;
import net.gazeplay.commons.utils.stats.Stats;
import net.gazeplay.games.colors.ColorsGame;
import net.gazeplay.games.colors.ColorsGamesStats;

public class ColorsGameLauncher implements GameSpec.GameLauncher {

	private ColorsGamesStats gameStat;

	@Override
	public Stats createNewStats(Scene scene) {

		gameStat = new ColorsGamesStats(scene);
		return gameStat;
	}

	@Override
	public GameLifeCycle createNewGame(GameContext gameContext, GameSpec.GameVariant gameVariant,
									   Stats stats) {
		return new ColorsGame(gameContext, gameStat);
	}
}
