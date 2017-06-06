package application.gui.info;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class RotateablePane extends StackPane
{
	private Timeline animation;
	private static final Double HALF_PIE = Math.PI / 2;
	private static final double ANIMATION_DURATION = 5000;
	private static final double ANIMATION_RATE = 10;
	private SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PIE);
	private PerspectiveTransform transform = new PerspectiveTransform();
	private SimpleBooleanProperty flippedProperty = new SimpleBooleanProperty(true);


	public RotateablePane()
	{
		angle = createAngleProperty();
		addListener();
		Timeline infoLine = new Timeline(new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				flip();
			}
		}), new KeyFrame(Duration.seconds(7)));
		infoLine.setCycleCount(Animation.INDEFINITE);
		infoLine.play();
	}


	private RotateablePane(Node... children)
	{
		super(children);
	}


	private void addListener()
	{
		widthProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(final ObservableValue<? extends Number> arg0, final Number arg1, final Number arg2)
			{
				recalculateTransformation(angle.doubleValue());
			}
		});

		heightProperty().addListener(new ChangeListener<Number>()
		{

			@Override
			public void changed(final ObservableValue<? extends Number> arg0, final Number arg1, final Number arg2)
			{
				recalculateTransformation(angle.doubleValue());
			}
		});
	}


	private SimpleDoubleProperty createAngleProperty()
	{
		final SimpleDoubleProperty angle = new SimpleDoubleProperty(HALF_PIE);
		angle.addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(final ObservableValue<? extends Number> obsValue, final Number oldValue,
					final Number newValue)
			{
				recalculateTransformation(newValue.doubleValue());
			}
		});
		return angle;
	}


	private Timeline createAnimation()
	{
		return new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(angle, HALF_PIE)),
				new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, 0, Interpolator.EASE_IN)),
				new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(final ActionEvent arg0)
					{
						flippedProperty.set(flippedProperty.not().get());
					}
				}), new KeyFrame(Duration.millis(ANIMATION_DURATION / 2), new KeyValue(angle, Math.PI)), new KeyFrame(
						Duration.millis(ANIMATION_DURATION), new KeyValue(angle, HALF_PIE, Interpolator.EASE_OUT)));
	}


	private void flip()
	{
		if (animation == null)
			animation = createAnimation();
		animation.setRate(flippedProperty.get() ? ANIMATION_RATE : -ANIMATION_RATE);
		animation.play();
	}


	private void recalculateTransformation(final double angle)
	{
		final double insetsTop = getInsets().getTop() * 2;
		final double insetsLeft = getInsets().getLeft() * 2;

		final double radius = widthProperty().subtract(insetsLeft).divide(2).doubleValue();
		final double height = heightProperty().subtract(insetsTop).doubleValue();
		final double back = height / 10;

		/*
		 * You may Google "Affine Transformation - Rotation"
		 */
		transform.setUlx(radius - Math.sin(angle) * radius);
		transform.setUly(0 - Math.cos(angle) * back);
		transform.setUrx(radius + Math.sin(angle) * radius);
		transform.setUry(0 + Math.cos(angle) * back);
		transform.setLrx(radius + Math.sin(angle) * radius);
		transform.setLry(height - Math.cos(angle) * back);
		transform.setLlx(radius - Math.sin(angle) * radius);
		transform.setLly(height + Math.cos(angle) * back);
	}


	public void addChild(Pane child1, Pane child2)
	{
		child1.setEffect(transform);
		child1.visibleProperty().bind(flippedProperty.not());
		child2.setEffect(transform);
		child2.visibleProperty().bind(flippedProperty);

		getChildren().setAll(child1, child2);
	}
}
