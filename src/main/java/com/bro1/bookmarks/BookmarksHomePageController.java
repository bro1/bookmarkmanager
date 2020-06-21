package com.bro1.bookmarks;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;


public class BookmarksHomePageController implements Initializable {
	
	
	public String currentBrowser = "default";
	
	
	
	public Map<String, List<String>> browsers = new HashMap<>();
	{
		browsers.put("chrome anonymous", List.of("google-chrome", "--incognito"));
		browsers.put("chrome", List.of("google-chrome"));
		browsers.put("firefox", List.of("firefox"));
		browsers.put("firefox private", List.of("firefox", "-private-window"));
	}
	
	public Map<String, List<String>> winBrowsers = new HashMap<>();
	{
		winBrowsers.put("chrome anonymous", List.of("C:/Program Files (x86)/Google/Chrome/Application/chrome.exe", "--incognito"));
		winBrowsers.put("chrome", List.of("C:/Program Files (x86)/Google/Chrome/Application/chrome.exe"));		
		winBrowsers.put("firefox", List.of("C:/Program Files/Mozilla Firefox/firefox.exe"));
		winBrowsers.put("firefox private", List.of("c:/Program Files/Mozilla Firefox/firefox.exe", "-private-window"));
	}
	
	public ObservableList<NameAndURL> data = FXCollections.observableArrayList();
	FilteredList<NameAndURL> filteredData = new FilteredList<>(data, s -> true);
	
	public Stage myStage;
        
  @FXML
  private TextField title;

  @FXML
  private RadioMenuItem menuChrome;

  @FXML
  private ToggleGroup browser;

  @FXML
  private RadioMenuItem menuChromeA;  
  

  @FXML
  private URL location;
  
  
  @FXML
  private ScrollPane scroll;
  
  @FXML
  private TextField url;

  @FXML
  private TextField filter;

  @FXML
  private ListView<NameAndURL> list;
  
        
	@FXML
	private void onMenuExit(ActionEvent e) {
		saveFile();
		Platform.exit();
	}
	
	@FXML
	private void onMenuQuitNoSave(ActionEvent e) {
		Platform.exit();
	}	
	

	@FXML
	private void onMenuSave(ActionEvent e) {
		saveFile();
	}
	

	@FXML
	private void onMenuBrowserChrome(ActionEvent e) {
		currentBrowser = "chrome";
	}
	
	@FXML
	private void onMenuBrowserDefault(ActionEvent e) {
		currentBrowser = "default";
	}
	
	@FXML
	private void onMenuBrowserFirefox(ActionEvent e) {
		currentBrowser = "firefox";
	}
	

	@FXML
	private void onMenuBrowserFirefoxA(ActionEvent e) {
		currentBrowser = "firefox private";
	}

	

	@FXML
	private void onMenuBrowserChromeA(ActionEvent e) {
		currentBrowser = "chrome anonymous";
	}
	
    

    @FXML
    void onFilterKeyReleased(KeyEvent event) {
    	if (event.getCode().getCode() == 27) {
    		filter.setText("");    		
    		filteredData.setPredicate(s -> true);
    		list.requestFocus();
    	}
    	
    }
    
    
    @FXML
    void onFilterKeyTyped(KeyEvent event) {
    	    	
		filteredData.setPredicate(s -> {
			return s.name.toLowerCase().contains(filter.getText().toLowerCase())
					|| s.url.toLowerCase().contains(filter.getText().toLowerCase());
		});
    }    
	
    @FXML
    void onFilterAction(ActionEvent event) {
    	list.requestFocus();
    }
	

    @FXML
    void onTitleAction(ActionEvent event) {
    	var item = list.getSelectionModel().getSelectedItem();
    	if (item != null) {
    		item.name = title.getText();
    		item.url = url.getText();
    	}
    	
    	title.setDisable(true);
    	url.setDisable(true);
    	
    	list.refresh();
    	list.requestFocus();
    }
	
	
	 class MyCell extends ListCell<NameAndURL> {
	        @Override
	        public void updateItem(NameAndURL item, boolean empty) {
	        		        	
	            super.updateItem(item, empty);
	            if (!empty) {
	            	this.setText(item.toString());	            	
	            	
	            } else {
	            	this.setText("");
	            }
	            
	        }

	    }	

  @Override
  public void initialize(URL url1, ResourceBundle rb) {
	  
	  list.setItems(filteredData);	  
	  	  	  
	  
	  list.setCellFactory(new Callback<ListView<NameAndURL>, ListCell<NameAndURL>>() {
	                @Override 
	                public ListCell<NameAndURL> call(ListView<NameAndURL> list) {
	                    var c = new MyCell();	                    	                   
	                    //c.setText(value);
	                    return c;
	                    
	                }
	            }
	        );	  
	  
	  
	  list.setOnKeyPressed(event -> {
		  KeyCode code = event.getCode();
		  			
			// Delete
			if (code.getCode() == 127) {
				var item = list.getSelectionModel().getSelectedItem();			
				if (item != null) {
					data.remove(item);					
					list.refresh();
				}

			}
	  });
	  
		list.setOnKeyTyped(event -> {
						
		
			var c = event.getCharacter();
			
			
			if (event.isAltDown()) {
				return;
			}
			
			if (c.equalsIgnoreCase("x")) {
				onMenuExit(null);
				return;
			}
			
			
			if (c.equalsIgnoreCase("f")) {
				filter.requestFocus();
//				//list.setVisible(false);
//				list.autosize();
			}
			
			
			if (c.equalsIgnoreCase("r")) {
				// random
				launchRandom();
				
			}	
			
			
			
			if (c.equalsIgnoreCase("a")) {
				currentBrowser = "chrome anonymous";
				menuChromeA.setSelected(true);
			}
			
			
			if (c.equalsIgnoreCase("s")) {
				saveFile();
			}
			
			
			var item = list.getSelectionModel().getSelectedItem();			
			if (item == null) return;
			
			if (c.equalsIgnoreCase("u")) {				
				new UpdateTitleThread(item).start();
			}
			
			if (c.equalsIgnoreCase("l")) {
				launchBrowser(item.url);
			}
			
			if (c.equalsIgnoreCase("e")) {
				title.setText(item.name);				
				url.setText(item.url);
				
				title.setDisable(false);
				url.setDisable(false);
				title.requestFocus();
			}

			
			

			
			
		});	  
	  
	  
    Timeline fiveSecondsWonder = new Timeline(new KeyFrame(
        Duration.millis(150), new EventHandler<ActionEvent>() {

          String p;

          @Override
          public void handle(ActionEvent event) {

            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            String z = systemClipboard.getString();
            if (p == null || !p.equals(z)) {
              p = z;

              String r = z;

              if (r != null && !r.equals(p)) {
                p = r;

                Map<DataFormat, Object> m = new HashMap<>();
                m.put(DataFormat.PLAIN_TEXT, r);
                systemClipboard.setContent(m);
              }

              url.setText(r);
              if (r != null && (r.startsWith("http://") || r.startsWith("https://"))) {
            	  
            	  var addedItem = add("", r);
            	  if (addedItem != null) {
            		  new UpdateTitleThread(addedItem).start();
            	  }
            	  
              }               
              
            }

          }



        }));

    fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    fiveSecondsWonder.play();
    
   	loadFile();
   	
   	
   	//list.getFocusModel().focus(0);
   	//list.requestFocus();
   	//filter.setFocusTraversable(true);

  }

	private void launchRandom() {

		var size = data.size();
		if (size > 0) {
			var r = new Random(new Date().getTime());
			var i = r.nextInt(size);
			var d = data.get(i);
			launchBrowser(d.url);
			
			list.getSelectionModel().select(d);
			list.scrollTo(d);
			
		}
	
	}

	private NameAndURL add(String name, String url) {
		
		// check for duplicates
		for (var d : data) {
			if (d.url.equals(url)) {
				
				//var index = data.indexOf(d);
				list.getSelectionModel().select(d);
				list.scrollTo(d);
				
				return null;
			}
		}
		
		var item = new NameAndURL(name, url);
		data.add(item);
		list.getSelectionModel().select(item);
		list.scrollTo(item);
		
		return item;
	}

  
	public void launchBrowser(String targeturl) {
		List<String> command = new LinkedList<String>();
		
		String os = System.getProperty("os.name").toLowerCase();				
		var isWindows = os.indexOf("win") >= 0;
		var isMac = os.indexOf("mac") >= 0;
		var isLinuxOrUnix = os.indexOf("nix") >=0 || os.indexOf("nux") >=0;
		
		if (currentBrowser.equals("default")) {
			
			if (isLinuxOrUnix) {
				command.add("xdg-open");				
				command.add(targeturl);
				
				try {					
					new ProcessBuilder(command).start();                  
				} catch (Throwable t) {
					t.printStackTrace(System.err);
				}			
				
			} else if (isWindows || isMac) {
				
				try {
					Desktop.getDesktop().browse(new URI(targeturl));
				} catch (Throwable t) {
					t.printStackTrace(System.err);				
				}
			}
		} else {		
			List<String> l = null; 
			if (isLinuxOrUnix) {
				l = browsers.get(currentBrowser);		
			} else if (isWindows) {
				l = winBrowsers.get(currentBrowser);
			}

			command.addAll(l);					
			command.add(targeturl);

			try {
				  System.out.println(targeturl);
				  new ProcessBuilder(command).start();                  
			} catch (Throwable th) {
				th.printStackTrace(System.err);
			}

		}
	}
  
  
  
  
  
  
  public void saveFile() {
	  
	  	  
	  var ff = BookmarksApp.file;
	  	  
	   try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ff)))) {

		   for (var d : data) {
			   String[] nameLines = d.name.split("\\n");
			   for (var l : nameLines) {
				   if (!l.isBlank()) {
					   out.write("# " + l + "\n");
				   }
			   }
			   out.write(d.url + "\n");			   
		   }
		   
	   } catch (IOException e) {
		e.printStackTrace();
	} 
	  
  }
  
  
  public void loadFile() {
	  
	  var ff = BookmarksApp.file;	  
	  
	  try (BufferedReader in = new BufferedReader(new FileReader(ff))) {
		  				 
		    // read line by line
		  
		  	String name = "";
		 
		    String line = "";
		    String linelc;
		    while ((line = in.readLine()) != null) {
		    	linelc = line.toLowerCase();
		    	boolean isurl = linelc.startsWith("http://") || linelc.startsWith("https://");		    	
		    	
		    	if (!isurl) {
		    		if (line.startsWith("#")) {
		    			line = line.substring(1);
		    		}
	    		
	    			if (name.isEmpty()) {
	    				name = line.trim();
	    			} else {
	    				name += "\n" + line.trim(); 
	    			}
		    				    		
		    	}
		    	
		    	if (isurl) {
		    		add(name, line.trim());		    		
		    		//data.add(new NameAndURL(name, line.trim()));
		    		
		    		// reset variables
		    		name="";
		    		line="";
		    	}

		    	
		    }
		  
		 		  		
		  
	  } catch (IOException e) {
		  System.out.println("Warning, cannot process file " + ff.getAbsolutePath());
	  }	  
	  
	  
	  
  }
  
  private NameAndURL retrieveTitle(NameAndURL item) {
	  
	  NameAndURL result = new NameAndURL("","");
	  	 
	  
	  try {
		  java.time.Duration a = java.time.Duration.ofMillis(5000);
		HttpClient client = HttpClient.newBuilder()
				.followRedirects(Redirect.ALWAYS)
				.connectTimeout(a)
				.build();
	
		  HttpRequest request = HttpRequest.newBuilder()
		      .uri(new URI(item.url))
		      .build();
	
		  java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
		  		  
		  
			  
		  System.out.println(response.statusCode());
		  System.out.println(response.body());
		  System.out.println(response.uri().toASCIIString());		  
		
		  String title = getTitle(response.body());
          title = reworkTitle(title);
          
          result.name = title;
          result.url = response.uri().toASCIIString();
          
          return result;

		  
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	  
	  
	  return result;
  }
  

	private String reworkTitle(String title) {
		
		return StringEscapeUtils.unescapeHtml4(title);
	}

	
	private String getTitle(String responseBody) {				
		Pattern pattern = Pattern.compile("<title>(.*)</title>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(responseBody);
		if (matcher.find())	{
			return matcher.group(1);
		}

		return "";				 
		
	}
	
	class UpdateTitleThread extends Thread {

		NameAndURL data;

		UpdateTitleThread(NameAndURL data) {
			this.data = data;
		}

		public void run() {

			var newName = retrieveTitle(data);

			if (!newName.name.isBlank()) {
				data.name = newName.name;
				data.url = newName.url;
				list.refresh();
			}

		}
	}
  
}
