package com.grv.videoPlayer.controller;

import static com.grv.videoPlayer.utils.AppConstants.CSS_CLOSE_APP;
import static com.grv.videoPlayer.utils.AppConstants.CSS_FULLSCREEN;
import static com.grv.videoPlayer.utils.AppConstants.CSS_MAXIMIZE_APP;
import static com.grv.videoPlayer.utils.AppConstants.CSS_MINIMIZE_APP;
import static com.grv.videoPlayer.utils.AppConstants.CSS_PAUSE;
import static com.grv.videoPlayer.utils.AppConstants.CSS_STOP;
import static com.grv.videoPlayer.utils.AppConstants.CSS_VOLUME_MINUS;
import static com.grv.videoPlayer.utils.AppConstants.CSS_VOLUME_MUTE;
import static com.grv.videoPlayer.utils.AppConstants.CSS_VOLUME_PLUS;
import static com.grv.videoPlayer.utils.AppConstants.NULL_STRING;

import java.io.File;

import com.grv.videoPlayer.StartApplication;
import com.grv.videoPlayer.handlers.VideoPlayerEventHandler;
import com.grv.videoPlayer.utils.AppUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MasterController {

	private String MEDIA_FILE = "C:\\Users\\grvsg\\Downloads\\torrent_completed\\Murder Mystery (2019) [WEBRip] [720p] [YTS.LT]\\Murder.Mystery.2019.720p.WEBRip.x264-[YTS.LT].mp4";

	@FXML
	private Label playBtn;

	@FXML
	private Label stopBtn;

	@FXML
	private Label incVolumeBtn;

	@FXML
	private Label decVolumeBtn;

	@FXML
	private Label muteBtn;

	@FXML
	private Label volume;

	@FXML
	private Label fullScreenBtn;

	@FXML
	private Label minimizeBtn;

	@FXML
	private Label maximizeBtn;

	@FXML
	private Label closeBtn;

	@FXML
	private Label totalTime;

	@FXML
	private Label currentTime;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private HBox header;

	@FXML
	private MenuItem closeMenuItem;

	@FXML
	private MenuItem loadMenuItem;

	@FXML
	private MediaView mediaView;

	private Media media;
	private MediaPlayer mediaPlayer;

	@FXML
	private void initialize() {
//		File mediaFile = new File(MEDIA_FILE);
//		playVideo(mediaFile);
		bindProperties();
		addListeners();
		setInitialValues();
		addIcons();
	}

	public void playVideo(File mediaFile) {
		if (mediaFile.exists()) {
			media = new Media(mediaFile.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setAutoPlay(true);
			mediaView.setMediaPlayer(mediaPlayer);
			listenersToReload();
		} else {
			System.out.println("File Not Found");
		}
	}

	private void addListeners() {
		loadMenuItem.setOnAction(VideoPlayerEventHandler.loadMedia(this));
		closeMenuItem.setOnAction(VideoPlayerEventHandler.closeVideoPlayer());
		decVolumeBtn.setOnMouseClicked(VideoPlayerEventHandler.decreaseVolume(mediaPlayer, volume));
		incVolumeBtn.setOnMouseClicked(VideoPlayerEventHandler.increaseVolume(mediaPlayer, volume));
		muteBtn.setOnMouseClicked(VideoPlayerEventHandler.muteVolume(mediaPlayer));
		fullScreenBtn.setOnMouseClicked(VideoPlayerEventHandler.toggleFullScreen(fullScreenBtn, header, 1));
		mediaView.setOnMouseClicked(VideoPlayerEventHandler.toggleFullScreen(fullScreenBtn, header, 2));
		// TODO: need to check for maximize app
//		closeBtn.setOnMouseClicked(VideoPlayerEventHandler.closeVideoPlayerApp());
//		minimizeBtn.setOnMouseClicked(VideoPlayerEventHandler.minimizeApp());
//		maximizeBtn.setOnMouseClicked(VideoPlayerEventHandler.maximizeApp());
		closeBtn.setVisible(false);
		minimizeBtn.setVisible(false);
		maximizeBtn.setVisible(false);
	}

	private void listenersToReload() {
		playBtn.setOnMouseClicked(VideoPlayerEventHandler.playPauseToggle(mediaPlayer, playBtn));
		stopBtn.setOnMouseClicked(VideoPlayerEventHandler.stopVideo(mediaPlayer, playBtn));
		progressBar.setOnMouseDragged(VideoPlayerEventHandler.seekTo(progressBar, mediaPlayer));
		progressBar.setOnMouseClicked(VideoPlayerEventHandler.seekTo(progressBar, mediaPlayer));
		mediaPlayer.currentTimeProperty().addListener(
				VideoPlayerEventHandler.showProgress(progressBar, mediaPlayer, currentTime, totalTime, playBtn));
		mediaView.setOnDragOver(VideoPlayerEventHandler.mediaDragOver(this));
	}

	private void bindProperties() {
		mediaView.fitWidthProperty().bind(StartApplication.getVideoPlayerStage().widthProperty());
		mediaView.fitHeightProperty().bind(StartApplication.getVideoPlayerStage().heightProperty().subtract(140));
		progressBar.prefWidthProperty().bind(StartApplication.getVideoPlayerStage().widthProperty());
	}

	private void setInitialValues() {
		try {
			volume.setText(String.valueOf((int) mediaPlayer.getVolume() * 100) + "%");
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	private void addIcons() {
		AppUtils.setIcon(playBtn, CSS_PAUSE, NULL_STRING);
		AppUtils.setIcon(stopBtn, CSS_STOP, NULL_STRING);
		AppUtils.setIcon(incVolumeBtn, CSS_VOLUME_PLUS, NULL_STRING);
		AppUtils.setIcon(decVolumeBtn, CSS_VOLUME_MINUS, NULL_STRING);
		AppUtils.setIcon(muteBtn, CSS_VOLUME_MUTE, NULL_STRING);
		AppUtils.setIcon(fullScreenBtn, CSS_FULLSCREEN, NULL_STRING);
		AppUtils.setIcon(minimizeBtn, CSS_MINIMIZE_APP, NULL_STRING);
		AppUtils.setIcon(maximizeBtn, CSS_MAXIMIZE_APP, NULL_STRING);
		AppUtils.setIcon(closeBtn, CSS_CLOSE_APP, NULL_STRING);
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
}
