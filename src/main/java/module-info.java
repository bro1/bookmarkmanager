module plaintextbookmarksfx { 
	
	  requires javafx.controls;
	  requires javafx.fxml;
	  requires org.apache.commons.text;	 
	  requires java.net.http;
	  requires java.desktop;
	  
	  
	  opens com.bro1.bookmarks to javafx.fxml;

	  exports com.bro1.bookmarks;
}