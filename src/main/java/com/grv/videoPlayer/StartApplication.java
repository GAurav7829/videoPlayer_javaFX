package com.grv.videoPlayer;

import java.io.IOException;

import com.grv.videoPlayer.controller.MasterController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApplication extends Application {

	private static Stage videoPlayerStage;
	private static Scene scene;

	@Override
	public void start(Stage stage) throws IOException {
		videoPlayerStage = stage;
		Parent loadFXML = loadFXML("master");
		scene = new Scene(loadFXML, 600, 400);
		scene.getStylesheets().add("/static/css/bootstrap3.css");
		scene.getStylesheets().add("/static/css/style.css");
		stage.setScene(scene);
		stage.setMinWidth(600);
		stage.setMinHeight(500);
		stage.setFullScreenExitHint("");
//		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Video Player");
		stage.show();
	}

	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/fxml/" + fxml + ".fxml"));
		fxmlLoader.setController(new MasterController());
		return fxmlLoader.load();
	}

	public static void launchApp(String[] args) {
		launch(args);
	}

	public static Stage getVideoPlayerStage() {
		return videoPlayerStage;
	}

}