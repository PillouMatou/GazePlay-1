package net.gazeplay.gameslocator;

import javafx.scene.Scene;
import net.gazeplay.GameContext;
import net.gazeplay.GameLifeCycle;
import net.gazeplay.GameSpec;
import net.gazeplay.commons.utils.stats.Stats;
import net.gazeplay.games.shooter.Shooter;
import net.gazeplay.games.shooter.ShooterGamesStats;

public class RobotsGameLauncher implements GameSpec.GameLauncher {
	@Override
	public Stats createNewStats(Scene scene) {
		return new ShooterGamesStats(scene, "robot");
	}

	@Override
	public GameLifeCycle createNewGame(GameContext gameContext, GameSpec.GameVariant gameVariant,
									   Stats stats) {
		return new Shooter(gameContext, stats, "robot");
	}
}
