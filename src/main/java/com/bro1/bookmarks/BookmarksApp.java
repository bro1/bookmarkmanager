package com.bro1.bookmarks;

import java.io.File;
import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookmarksApp extends Application {

	public static Stage myStage = null;

	 
	static File file;			
			
	public static void main(String[] args) throws Exception {					
		Application.launch(BookmarksApp.class, args);
	
		
	}

	@Override
	public void start(Stage stage) throws Exception {

		List<String> args = getParameters().getUnnamed();
		
		String fileName;
		if (!args.isEmpty()) {
			fileName = args.get(0);
		} else {
			fileName = System.getProperty("user.home") + File.separator + ".mybookmarks";
		}
		 
		file = new File(fileName).getCanonicalFile();
		System.out.println("Using file " + file.getAbsolutePath());

		
		
		myStage = stage;

		URL res = getClass().getResource("BookmarksHomePage.fxml");
		FXMLLoader loader = new FXMLLoader();

		loader.setLocation(res);
		loader.setBuilderFactory(new JavaFXBuilderFactory());

		BookmarksHomePageController controller = new BookmarksHomePageController();
		controller.myStage = stage;
		loader.setController(controller);

		Parent root = (Parent) loader.load(res.openStream());
		
		Scene scene = new Scene(root);
		
		stage.setScene(scene);

		stage.setTitle("Plaintext bookmark manager");
		stage.show();
		
	}
}
