package application.gui.info;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class DatePane extends UpdateablePane
{
	protected final DoubleProperty fontSize = new SimpleDoubleProperty(10);
	protected final ObjectProperty<Font> font = new SimpleObjectProperty<Font>();
	private final StringProperty infoText = new SimpleStringProperty();


	public DatePane(Duration update)
	{
		super(update);
		setId("datepane");
		build();
	}


	private void build()
	{
		fontSize.bind(widthProperty().add(heightProperty()).divide(25));

		HBox hb = new HBox(0);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().add(createText(infoText));
		addRow(0, hb);
		HBox.setMargin(getChildren().get(0), new Insets(5, 0, 5, 0));
		GridPane.setFillWidth(hb, true);
		GridPane.setHalignment(hb, HPos.CENTER);

		RowConstraints rc = new RowConstraints();
		rc.setVgrow(Priority.NEVER);
		getRowConstraints().add(rc);
	}


	private void setFont()
	{
		double size = fontSize.get() > 22 ? 22 : fontSize.get();
		font.set(Font.font("Tahoma", FontWeight.THIN, FontPosture.ITALIC, size));
	}


	@Override
	public void update(Duration update)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d.M. (w)");
		String date = LocalDate.now().format(formatter);
		infoText.set(date);
	}


	public Text createText(StringProperty toBind)
	{
		Text control = createText("");
		control.textProperty().bind(toBind);
		return control;
	}


	public Text createText(String toDisplay)
	{
		Text control = new Text(toDisplay);
		control.setTextAlignment(TextAlignment.CENTER);
		control.fontProperty().bind(font);
		control.setFontSmoothingType(FontSmoothingType.LCD);
		control.setFill(Color.GRAY);
		return control;
	}


	@Override
	protected void layoutChildren()
	{
		super.layoutChildren();
		setFont();
	}
}
