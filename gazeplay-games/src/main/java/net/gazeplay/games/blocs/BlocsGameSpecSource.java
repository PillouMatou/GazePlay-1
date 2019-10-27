package net.gazeplay.games.blocs;

import net.gazeplay.GameCategories;
import net.gazeplay.GameSpec;
import net.gazeplay.GameSpecSource;
import net.gazeplay.GameSummary;

public class BlocsGameSpecSource implements GameSpecSource {
    @Override
    public GameSpec getGameSpec() {
        return new GameSpec(
            GameSummary.builder().nameCode("Blocks").gameThumbnail("data/Thumbnails/block.png").category(GameCategories.Category.ACTION_REACTION).build(),
            new BlocsGameVariantGenerator(), new BlocsGameLauncher());
    }
}