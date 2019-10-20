package net.gazeplay.gameslocator;

import javafx.scene.Scene;
import net.gazeplay.GameContext;
import net.gazeplay.GameLifeCycle;
import net.gazeplay.GameSpec;
import net.gazeplay.commons.utils.stats.Stats;
import net.gazeplay.games.divisor.Divisor;
import net.gazeplay.games.divisor.DivisorStats;

public class DivisorGameLauncher implements GameSpec.GameLauncher {
	@Override
	public Stats createNewStats(Scene scene) {
		return new DivisorStats(scene);
	}

	@Override
	public GameLifeCycle createNewGame(GameContext gameContext, GameSpec.GameVariant gameVariant,
									   Stats stats) {
		return new Divisor(gameContext, stats, false);
	}
}
