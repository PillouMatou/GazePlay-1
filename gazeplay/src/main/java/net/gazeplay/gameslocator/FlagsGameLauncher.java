package net.gazeplay.gameslocator;

import javafx.scene.Scene;
import net.gazeplay.GameContext;
import net.gazeplay.GameLifeCycle;
import net.gazeplay.GameSpec;
import net.gazeplay.commons.utils.stats.Stats;
import net.gazeplay.games.whereisit.WhereIsIt;
import net.gazeplay.games.whereisit.WhereIsItStats;

public class FlagsGameLauncher implements GameSpec.GameLauncher {
	@Override
	public Stats createNewStats(Scene scene) {
		return new WhereIsItStats(scene, WhereIsIt.WhereIsItGameType.FLAGS.getGameName());
	}

	@Override
	public GameLifeCycle createNewGame(GameContext gameContext, GameSpec.GameVariant gameVariant,
									   Stats stats) {
		return new WhereIsIt(WhereIsIt.WhereIsItGameType.FLAGS, 2, 2, false, gameContext, stats);
	}
}
