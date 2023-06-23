package com.grv.videoPlayer.handlers;

import static com.grv.videoPlayer.utils.AppConstants.CSS_FULLSCREEN;
import static com.grv.videoPlayer.utils.AppConstants.CSS_FULLSCREEN_EXIT;
import static com.grv.videoPlayer.utils.AppConstants.CSS_PAUSE;
import static com.grv.videoPlayer.utils.AppConstants.CSS_PLAY;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import com.grv.videoPlayer.StartApplication;
import com.grv.videoPlayer.controller.MasterController;
import com.grv.videoPlayer.utils.AppUtils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class VideoPlayerEventHandler {

	private static boolean isPlaying = true;
	private static boolean isMute = false;
	private static boolean isFullScreen = false;
	private static boolean isMaximized = false;

	private static DecimalFormat decimalFormat = new DecimalFormat("##.##");

	public static EventHandler<? super MouseEvent> playPauseToggle(MediaPlayer mediaPlayer, Label label) {
		return e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				System.out.println("Playing: " + isPlaying);
				if (isPlaying) {
					mediaPlayer.pause();
					AppUtils.setIcon(label, CSS_PLAY, CSS_PAUSE);
				} else {
					mediaPlayer.play();
					AppUtils.setIcon(label, CSS_PAUSE, CSS_PLAY);
				}
				isPlaying = !isPlaying;
			}
		};
	}

	public static EventHandler<? super MouseEvent> stopVideo(MediaPlayer mediaPlayer, Label label) {
		return e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				mediaPlayer.stop();
				AppUtils.setIcon(label, CSS_PLAY, CSS_PAUSE);
				isPlaying = false;
			}
		};
	}

	public static EventHandler<ActionEvent> closeVideoPlayer() {
		return e -> {
			StartApplication.getVideoPlayerStage().close();
			Platform.exit();
		};
	}

	public static EventHandler<MouseEvent> closeVideoPlayerApp() {
		return e -> {
			StartApplication.getVideoPlayerStage().close();
			Platform.exit();
		};
	}

	public static EventHandler<? super MouseEvent> minimizeApp() {
		return e -> {
			StartApplication.getVideoPlayerStage().setIconified(true);
			;
		};
	}

	public static EventHandler<? super MouseEvent> maximizeApp() {
		return e -> {
			StartApplication.getVideoPlayerStage().setMaximized(!isMaximized);
			isMaximized = !isMaximized;
		};
	}

	public static EventHandler<ActionEvent> loadMedia(MasterController masterController) {
		return e -> {

			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			File file = fileChooser.showOpenDialog(StartApplication.getVideoPlayerStage());
			if (null != file) {
				try {
					masterController.getMediaPlayer().stop();
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
				masterController.playVideo(file);
				isPlaying = true;
			}

		};
	}

	public static EventHandler<? super MouseEvent> seekTo(ProgressBar progressBar, MediaPlayer mediaPlayer) {
		return e -> {
			if (mediaPlayer == null)
				return;
			if (e.getButton().equals(MouseButton.PRIMARY) && (e.getEventType().equals(MouseEvent.MOUSE_DRAGGED)
					|| e.getEventType().equals(MouseEvent.MOUSE_CLICKED))) {
				mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(e.getX() / progressBar.getWidth()));
			}
		};
	}

	public static ChangeListener<? super Duration> showProgress(ProgressBar progressBar, MediaPlayer mediaPlayer,
			Label currentTime, Label totalTime, Label playBtn) {
		return (ov, oldVal, newVal) -> {
			progressBar.setProgress(
					1.0 * mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis());

			// TODO: check for time conversion
			Duration duration = mediaPlayer.getCurrentTime();
			currentTime.setText(AppUtils.getTimeFormat(duration));
			totalTime.setText(AppUtils.getTimeFormat(mediaPlayer.getTotalDuration()));

			if (currentTime.getText().equals(totalTime.getText())) {
				mediaPlayer.stop();
				AppUtils.setIcon(playBtn, CSS_PLAY, CSS_PAUSE);
				isPlaying = false;
			}
		};
	}

	public static EventHandler<? super MouseEvent> decreaseVolume(MediaPlayer mediaPlayer, Label volume) {
		return e -> {
			if (e.getButton().equals(MouseButton.PRIMARY) && mediaPlayer.getVolume() > 0.0) {
				isMute = false;
				mediaPlayer.setMute(isMute);
				mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.05);
				String volumeValue = decimalFormat.format(mediaPlayer.getVolume() * 100).replace("-", "");
				volume.setText(volumeValue + "%");
			}
		};
	}

	public static EventHandler<? super MouseEvent> increaseVolume(MediaPlayer mediaPlayer, Label volume) {
		return e -> {
			if (e.getButton().equals(MouseButton.PRIMARY) && mediaPlayer.getVolume() < 1.0) {
				isMute = false;
				mediaPlayer.setMute(isMute);
				mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.05);
				String volumeValue = decimalFormat.format(mediaPlayer.getVolume() * 100).replace("-", "");
				volume.setText(volumeValue + "%");
			}
		};
	}

	public static EventHandler<? super MouseEvent> muteVolume(MediaPlayer mediaPlayer) {
		return e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				mediaPlayer.setMute(!isMute);
				isMute = !isMute;
			}
		};
	}

	public static EventHandler<? super MouseEvent> toggleFullScreen(Label fullScreen, HBox header, int clickCount) {
		return e -> {
			if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == clickCount) {
				StartApplication.getVideoPlayerStage().setFullScreen(!isFullScreen);
				isFullScreen = !isFullScreen;
				if (isFullScreen) {
					AppUtils.setIcon(fullScreen, CSS_FULLSCREEN_EXIT, CSS_FULLSCREEN);
					header.setVisible(false);
					header.setMinHeight(0);
					header.setPrefHeight(0);
				} else {
					AppUtils.setIcon(fullScreen, CSS_FULLSCREEN, CSS_FULLSCREEN_EXIT);
					header.setVisible(true);
					header.setMinHeight(30);
					header.setPrefHeight(30);
				}
			}
		};
	}

	public static EventHandler<? super DragEvent> mediaDragOver(MasterController masterController) {
		return e -> {
			if (e.getDragboard().hasFiles()) {
				e.acceptTransferModes(TransferMode.LINK);
				List<File> files = e.getDragboard().getFiles();
				File file = files.get(0);
				if (null != file) {
					masterController.getMediaPlayer().stop();
					masterController.playVideo(file);
					isPlaying = true;
				}
			}
		};
	}

}
