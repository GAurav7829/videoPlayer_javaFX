package com.grv.videoPlayer.utils;

import static com.grv.videoPlayer.utils.AppConstants.NULL_STRING;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Label;
import javafx.util.Duration;

public class AppUtils {

	public static void setIcon(Label label, String cssToAdd, String cssToRemove) {
		label.setText(NULL_STRING);
		FontIcon icon = new FontIcon();
		label.setGraphic(icon);
		label.getStyleClass().remove(cssToRemove);
		label.getStyleClass().add(cssToAdd);
	}

	public static String getTimeFormat(Duration duration) {
		int timeInSeconds = (int) duration.toSeconds();
		int seconds = (int) (timeInSeconds) % 60;
		int minutes = (int) ((timeInSeconds / (60)) % 60);
		int hours = (int) ((timeInSeconds / (60 * 60)) % 24);

		String time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
				+ String.format("%02d", seconds);
		return time;
	}

}
