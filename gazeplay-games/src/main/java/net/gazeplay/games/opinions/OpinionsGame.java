package net.gazeplay.games.opinions;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import net.gazeplay.GameLifeCycle;
import net.gazeplay.IGameContext;
import net.gazeplay.commons.configuration.Configuration;
import net.gazeplay.commons.gaze.devicemanager.GazeEvent;
import net.gazeplay.commons.utils.multilinguism.Multilinguism;
import net.gazeplay.commons.utils.multilinguism.MultilinguismFactory;
import net.gazeplay.components.ProgressButton;

public class OpinionsGame extends AnimationTimer implements GameLifeCycle {

    private final OpinionsGameStats opinionGameStats;
    private final IGameContext gameContext;
    private final Dimension2D dimension2D;
    private final Configuration configuration;
    private final Group backgroundLayer;
    private final Group middleLayer;
    private final Rectangle interactionOverlay;
    private final Multilinguism translate;

    private final Rectangle shade;
    private final ProgressButton restartButton;
    private final Text finalScoreText;

    private Point2D gazeTarget;

    private long lastTickTime = 0;
    private long minFPS = 1000;

    public OpinionsGame(final IGameContext gameContext, final OpinionsGameStats stats) {
        this.opinionGameStats = stats;
        this.gameContext = gameContext;
        this.dimension2D = gameContext.getGamePanelDimensionProvider().getDimension2D();
        this.configuration = gameContext.getConfiguration();

        this.backgroundLayer = new Group();
        this.middleLayer = new Group();
        final Group foregroundLayer = new Group();
        final StackPane sp = new StackPane();
        gameContext.getChildren().addAll(sp, backgroundLayer, middleLayer, foregroundLayer);

        this.translate = MultilinguismFactory.getSingleton();

        final Rectangle backgroundImage = new Rectangle(0, 0, dimension2D.getWidth(), dimension2D.getHeight());
        backgroundImage.widthProperty().bind(gameContext.getRoot().widthProperty());
        backgroundImage.heightProperty().bind(gameContext.getRoot().heightProperty());
        backgroundImage.setFill(new ImagePattern(new Image("data/space/background/space_img.png")));

        backgroundImage.setOpacity(0.08);

        sp.getChildren().add(backgroundImage);
        backgroundImage.toFront();

        // Menu
        final int fixationLength = configuration.getFixationLength();

        shade = new Rectangle(0, 0, dimension2D.getWidth(), dimension2D.getHeight());
        shade.setFill(new Color(0, 0, 0, 0.75));

        restartButton = new ProgressButton();
        final String dataPath = "data/space";
        final ImageView restartImage = new ImageView(dataPath + "/menu/restart.png");
        restartImage.setFitHeight(dimension2D.getHeight() / 6);
        restartImage.setFitWidth(dimension2D.getHeight() / 6);
        restartButton.setImage(restartImage);
        restartButton.setLayoutX(dimension2D.getWidth() / 2 - dimension2D.getHeight() / 12);
        restartButton.setLayoutY(dimension2D.getHeight() / 2 - dimension2D.getHeight() / 12);
        restartButton.assignIndicator(event -> launch(), fixationLength);

        finalScoreText = new Text(0, dimension2D.getHeight() / 4, "");
        finalScoreText.setFill(Color.WHITE);
        finalScoreText.setTextAlignment(TextAlignment.CENTER);
        finalScoreText.setFont(new Font(50));
        finalScoreText.setWrappingWidth(dimension2D.getWidth());
        foregroundLayer.getChildren().addAll(shade, finalScoreText, restartButton);

        gameContext.getGazeDeviceManager().addEventFilter(restartButton);

        // Interaction
        gazeTarget = new Point2D(dimension2D.getWidth() / 2, dimension2D.getHeight() / 2);

        interactionOverlay = new Rectangle(0, 0, dimension2D.getWidth(), dimension2D.getHeight());

        final EventHandler<Event> movementEvent = (Event event) -> {
            if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
                gazeTarget = new Point2D(((MouseEvent) event).getX(), ((MouseEvent) event).getY());
            } else if (event.getEventType() == GazeEvent.GAZE_MOVED) {
                gazeTarget = new Point2D(((GazeEvent) event).getX(), ((GazeEvent) event).getY());
            }
        };

        interactionOverlay.addEventFilter(MouseEvent.MOUSE_MOVED, movementEvent);
        interactionOverlay.addEventFilter(GazeEvent.GAZE_MOVED, movementEvent);
        interactionOverlay.setFill(Color.TRANSPARENT);
        foregroundLayer.getChildren().add(interactionOverlay);

        gameContext.getGazeDeviceManager().addEventFilter(interactionOverlay);
    }

    @Override
    public void launch() {
        // hide end game menu
        shade.setOpacity(0);
        restartButton.disable();
        finalScoreText.setOpacity(0);

        interactionOverlay.setDisable(false);

        this.backgroundLayer.getChildren().clear();
        this.middleLayer.getChildren().clear();

        gazeTarget = new Point2D(dimension2D.getWidth() / 2, 0);

        this.start();

        opinionGameStats.notifyNewRoundReady();
    }

    @Override
    public void handle(final long now) {

        if (lastTickTime == 0) {
            lastTickTime = now;
        }
        double timeElapsed = ((double) now - (double) lastTickTime) / Math.pow(10, 6); // in ms
        lastTickTime = now;

        if (1000 / timeElapsed < minFPS) {
            minFPS = 1000 / (int) timeElapsed;
        }
        timeElapsed /= getGameSpeed();

    }

    @Override
    public void dispose() {

    }

    private double getGameSpeed() {
        final double speed = gameContext.getAnimationSpeedRatioSource().getDurationRatio();
        return Math.max(speed, 1.0);
    }
}