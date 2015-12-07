package application.gui.ledbutton;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LedButton extends Control
{
	private ObjectProperty<Color> ledColor;
	private BooleanProperty selected;
	private StringProperty text;


	public LedButton(int min, int sec)
	{
		this();
	}


	public LedButton()
	{
		getStyleClass().add("tbutton");
		ledColor = new SimpleObjectProperty<>(Color.RED);
		text = new SimpleStringProperty(this, "text", "");
		selected = new SimpleBooleanProperty(this, "selected", false);
	}


	public LedButton(String toolTipText)
	{
		this();
		Tooltip tooltip = new Tooltip();
		tooltip.getStyleClass().add("tooltip");
		tooltip.setFont(new Font("Tahoma", 12));
		tooltip.setText(toolTipText);
		setTooltip(tooltip);
	}


	public String getText()
	{
		return text.get();
	}


	public void setText(String value)
	{
		text.set(value);
	}


	public final boolean isSelected()
	{
		return selected.get();
	}


	public final void setSelected(final boolean SELECTED)
	{
		selected.set(SELECTED);
	}


	public final BooleanProperty selectedProperty()
	{
		return selected;
	}


	public void addSelectedListener(ChangeListener<Boolean> listener)
	{
		selectedProperty().addListener(listener);
	}


	public final StringProperty textProperty()
	{
		return text;
	}


	public final Color getLedColor()
	{
		return ledColor.get();
	}


	public final void setLedColor(final Color LED_COLOR)
	{
		ledColor.set(LED_COLOR);
	}


	public final ObjectProperty<Color> ledColorProperty()
	{
		return ledColor;
	}


	public void fireSelectEvent(final SelectEvent EVENT)
	{
		final EventHandler<SelectEvent> HANDLER;
		@SuppressWarnings("unchecked")
		final EventType<SelectEvent> TYPE = (EventType<SelectEvent>) EVENT.getEventType();
		if (SelectEvent.SELECT == TYPE)
		{
			HANDLER = getOnSelect();
		}
		else if (SelectEvent.DESELECT == TYPE)
		{
			HANDLER = getOnDeselect();
		}
		else
		{
			HANDLER = null;
		}

		if (HANDLER != null)
		{
			HANDLER.handle(EVENT);
		}
	}


	public final ObjectProperty<EventHandler<SelectEvent>> onSelectProperty()
	{
		return onSelect;
	}


	public final void setOnSelect(EventHandler<SelectEvent> value)
	{
		onSelectProperty().set(value);
	}


	public final EventHandler<SelectEvent> getOnSelect()
	{
		return onSelectProperty().get();
	}

	private ObjectProperty<EventHandler<SelectEvent>> onSelect = new ObjectPropertyBase<EventHandler<SelectEvent>>()
	{
		@Override
		public Object getBean()
		{
			return this;
		}


		@Override
		public String getName()
		{
			return "onSelect";
		}
	};


	public final ObjectProperty<EventHandler<SelectEvent>> onDeselectProperty()
	{
		return onDeselect;
	}


	public final void setOnDeselect(EventHandler<SelectEvent> value)
	{
		onDeselectProperty().set(value);
	}


	public final EventHandler<SelectEvent> getOnDeselect()
	{
		return onDeselectProperty().get();
	}

	private ObjectProperty<EventHandler<SelectEvent>> onDeselect = new ObjectPropertyBase<EventHandler<SelectEvent>>()
	{
		@Override
		public Object getBean()
		{
			return this;
		}


		@Override
		public String getName()
		{
			return "onDeselect";
		}
	};

	public static class SelectEvent extends Event
	{
		private static final long serialVersionUID = 1L;
		public static final EventType<SelectEvent> SELECT = new EventType<SelectEvent>(ANY, "select");
		public static final EventType<SelectEvent> DESELECT = new EventType<SelectEvent>(ANY, "deselect");


		public SelectEvent(final Object SOURCE, final EventTarget TARGET, EventType<SelectEvent> TYPE)
		{
			super(SOURCE, TARGET, TYPE);
		}
	}
}
