
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package application.gui;

import java.util.HashMap;

import application.TimeScreen;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class ScreensController
{

	private HashMap<TimeScreen, Node> screens = new HashMap<TimeScreen, Node>();
	private ObjectProperty<TimeScreen> actScreen;
	private final StackPane sp;


	public ScreensController(TimeScreen... screens)
	{
		sp = new StackPane();
		for (TimeScreen screen : screens)
			addScreen(screen);
	}


	public Node getScreenNode(TimeScreen screen)
	{
		for (TimeScreen key : screens.keySet())
		{
			if (key.equals(screen))
				return key.getNode();
		}
		throw new IllegalArgumentException("screen not found");
	}


	public void addScreen(TimeScreen screenId)
	{
		screens.put(screenId, screenId.getNode());
		actScreen = new SimpleObjectProperty<TimeScreen>();
	}


	public void setNextScreen()
	{
		setScreen(actScreen.get().nextScreen());
	}


	public boolean setScreen(final TimeScreen id)
	{
		if (screens.get(id) != null)
		{ // screen loaded
			final DoubleProperty opacity = sp.opacityProperty();

			if (!sp.getChildren().isEmpty())
			{
				Timeline fade = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
						new KeyFrame(new Duration(500),

								new EventHandler<ActionEvent>()
								{
									@Override
									public void handle(ActionEvent event)
									{
										sp.getChildren().remove(0);
										sp.getChildren().add(0, screens.get(id));
										Timeline fadeIn = new Timeline(
												new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
												new KeyFrame(new Duration(200), new KeyValue(opacity, 1.0)));
										fadeIn.play();

									}
								}, new KeyValue(opacity, 0.0)));
				fade.play();
			}
			else
			{
				// no one else been displayed, then just show
				sp.setOpacity(0.0);
				sp.getChildren().add(screens.get(id));
				Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
						new KeyFrame(new Duration(2500), new KeyValue(opacity, 1.0)));
				fadeIn.play();
			}
			this.actScreen.set(id);
			return true;
		}
		else
		{
			System.out.println("screen hasn't been loaded!\n");
			return false;
		}
	}


	public TimeScreen getScreen()
	{
		return actScreen.get();
	}


	public void registerScreenChangedListener(ChangeListener<TimeScreen> listener)
	{
		actScreen.addListener(listener);
	}


	public double getPrefHeights()
	{
		return screens.get(getScreen()).prefHeight(0);
	}


	public double getPrefWidth()
	{
		return screens.get(getScreen()).prefWidth(0);
	}


	public Node getPane()
	{
		return sp;
	}

}
