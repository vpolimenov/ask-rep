package ask_rep.client;

import java.util.ArrayList;
import java.util.Collections;

import ask_rep.shared.Snippet;
import ask_rep.shared.SnippetContainer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MainEntryPoint implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	Label lblContentHeaderTitle = new Label();
	VerticalPanel vpCenterLayout;
	HorizontalPanel hpCenterLayout;
	Anchor lnkSignIn;
	LoginInfo objLoginInfo = null;
	String language = "Java";
	
	SectionStack objSectionStack;
	
	private int minLength = 100;
	private int maxLength = 500;
	
	/**  
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final UserServiceAsync objUserService = GWT
			.create(UserService.class);
	
	private final LoginServiceAsync objLoginService = GWT
			.create(LoginService.class);
	
	private final SessionServiceAsync objSessionService = GWT
			.create(SessionService.class);
	
	private final SearchServiceAsync linkSearcher = GWT
			.create(SearchService.class);
	
	private final CodeExtractionServiceAsync codeExtractor = GWT
			.create(CodeExtractionService.class);		

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		login();

		initializeComponents();	
		loadTrendingRepositoryPanel();
	}
	
	public void initializeComponents() {
		
		Anchor lnkTrendingRepositories = new Anchor();
		lnkTrendingRepositories.setText("Trending Repositories");
		// Add a handler to close the DialogBox
		lnkTrendingRepositories.addClickHandler(new ClickHandler()  {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				loadTrendingRepositoryPanel();
			}
			
		});
		
		Anchor lnkUpload = new Anchor();
		lnkUpload.setText("Upload Files");
		lnkUpload.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				objSessionService.validateSession("UserID", new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(Boolean result) {
						// TODO Auto-generated method stub
						
						lblContentHeaderTitle.setText("Upload Files");

						if(result) {
							loadUploadPanel();
						} else {
							RootPanel.get("mainContent").clear();
							
							Label objLabel = new Label("Please sign in to upload a file.");
							RootPanel.get("mainContent").add(objLabel);
						}
					}
				});
			}
		});
		
		Anchor lnkCreate = new Anchor();
		lnkCreate.setText("Create Files");
		lnkCreate.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				loadCreatePanel();
			}
		});
		
		RootPanel.get("contentHeaderTitle").add(lblContentHeaderTitle);
		RootPanel.get("lnkTrending").add(lnkTrendingRepositories);
		RootPanel.get("lnkUpload").add(lnkUpload);	
		RootPanel.get("lnkCreate").add(lnkCreate);
	}
	
	public void login() {
		 // Check login status using login service.
	    
	    objLoginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	    	
	      public void onFailure(Throwable error) {
	      }

	      public void onSuccess(LoginInfo loginResult) { 
	    	  
		        if(loginResult.isLoggedIn()) {
		        	
		    		final String strEmail = loginResult.getEmailAddress();
		    		final String strName = loginResult.getNickname();
		    		
		    		
		    		lnkSignIn = new Anchor(strEmail + ", Sign Out");	
		    		lnkSignIn.setHref(loginResult.getLogoutUrl());
		    	
		    		objUserService.checkUserExists(strEmail, new AsyncCallback<Integer>() {
		    			
		    			@Override
		    			public void onSuccess(Integer userExistsResult) {
		    				// TODO Auto-generated method stub
		    				if(userExistsResult == 0) {
		    					
		    					objUserService.insertUser(strName, strEmail, new AsyncCallback<Integer>() {
		    						
		    						@Override
		    						public void onSuccess(Integer insertUserResult) {
		    							// TODO Auto-generated method stub
		    							objSessionService.createSession("UserID", insertUserResult.toString(), new AsyncCallback<Void>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method stub
												
											}

											@Override
											public void onSuccess(Void result) {
												// TODO Auto-generated method stub
												
											}
										});  							
		    						}
		    						
		    						@Override
		    						public void onFailure(Throwable caught) {
		    							// TODO Auto-generated method stub
		    							Window.alert("An error has occurred");
		    						}
		    						
		    					});
		    					
		    				} else {
		    					objSessionService.createSession("UserID", userExistsResult.toString(), new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onSuccess(Void result) {
										// TODO Auto-generated method stub
										
									}
								});				
		    				}
		    				
		    			}
		    			
		    			@Override
		    			public void onFailure(Throwable caught) {
		    				// TODO Auto-generated method stub
		    				Window.alert(caught.getMessage());
		    			}
		    			
		    		});
		    		
		        } else {
		        	lnkSignIn = new Anchor("Sign In");
		        	lnkSignIn.setHref(loginResult.getLoginUrl());
		        	
		        	objSessionService.invalidateSession("UserID", new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(Void result) {
							// TODO Auto-generated method stub
							
						}
					});	
		        }

		        lnkSignIn.addStyleName("login_button");
	    	    RootPanel.get("login").add(lnkSignIn);
	      }
	    });
	}
	
	
	public void loadTrendingRepositoryPanel() {
		
		RootPanel.get("mainContent").clear();

		lblContentHeaderTitle.setText("Trending Repositories");
		
		Label myLabel = new Label("Hey");
		
		vpCenterLayout = new VerticalPanel();
		vpCenterLayout.add(myLabel);
		RootPanel.get("mainContent").add(vpCenterLayout);
	}
	
	public void loadUploadPanel() {
		
		RootPanel.get("mainContent").clear();
		
		final FormPanel objForm = new FormPanel();
		objForm.setAction(GWT.getModuleBaseURL() + "home");
		
		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		objForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		objForm.setMethod(FormPanel.METHOD_POST);
		
		vpCenterLayout = new VerticalPanel();
		
		objForm.setWidget(vpCenterLayout);
		
		// Create a FileUpload Widget
		final FileUpload objUpload = new FileUpload();
		
		objUpload.setName("upFiles");
		vpCenterLayout.add(objUpload);
		
		Button uploadButton = new Button("Upload");
		uploadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				objForm.submit();
			}
			
		});
		
		vpCenterLayout.add(uploadButton);
		
		objForm.addSubmitHandler(new FormPanel.SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				// TODO Auto-generated method stub
				if(objUpload.getFilename().length() == 0) {
			    	Window.alert("Please upload a file");
			    	event.cancel();
			    }
			}
		});

		objForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub

			}
		});
		
		RootPanel.get().add(objForm);
		RootPanel.get("mainContent").add(vpCenterLayout);
	}
	
	public void loadCreatePanel() {
		
		RootPanel.get("mainContent").clear();
		
		lblContentHeaderTitle.setText("Create Files");
		
		vpCenterLayout = new VerticalPanel();
		hpCenterLayout = new HorizontalPanel();

		final TextArea codePanel = new TextArea();
		codePanel.setCharacterWidth(63);
		codePanel.setVisibleLines(35);
		codePanel.setStyleName("codePanel");
		hpCenterLayout.add(codePanel);
		
		Label lblInstructions = new Label();
		lblInstructions.setStyleName("instructions");
		lblInstructions.setText("Press CTRL + L to search for code snippets ...");
	
		hpCenterLayout.add(lblInstructions);

		codePanel.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				
				if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
					if (codePanel.getSelectedText() != null && codePanel.getSelectionLength() > 3) {
						search(language + " " + getRecommendedSite(language) + codePanel.getSelectedText(), codePanel.getSelectedText());
					}
				}
			}
		});
		
		RootPanel.get("mainContent").add(hpCenterLayout);
	}
	
	protected void search(String searchText, final String keyWords) {
		// TODO Auto-generated method stub
//        answerPanel.addProgressBar();

				if(hpCenterLayout.getWidget(1) != null)
					hpCenterLayout.remove(1);
		
                linkSearcher.setQueryString(searchText, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("setQueryString failed MEP");
					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub
//						ArrayList<String> searchResults;

		                    linkSearcher.getSearchResults(new AsyncCallback<ArrayList<String>>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									System.out.println("getSearchResults failed MEP");
								}

								@Override
								public void onSuccess(ArrayList<String> result) {
									// TODO Auto-generated method stub
//									codeExtractor.resultLinks = result;
					                codeExtractor.extractCodeTags(result, new AsyncCallback<Void>() { //, answerPanel

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											System.out.println("extractCodeTags failed MEP");
											caught.printStackTrace();
										}

										@Override
										public void onSuccess(Void result) {
											// TODO Auto-generated method stub
											codeExtractor.extractSnippets(language, keyWords, minLength, maxLength, new AsyncCallback<Void>() {

												@Override
												public void onFailure(
														Throwable caught) {
													// TODO Auto-generated method stub
													System.out
															.println("extractSnippets failed MEP");
												}

												@Override
												public void onSuccess(
														Void result) {
													// TODO Auto-generated method stub

										                
										                codeExtractor.getSnippets(new AsyncCallback<ArrayList<Snippet>>() {

															@Override
															public void onFailure(
																	Throwable caught) {
																// TODO Auto-generated method stub
																System.out
																		.println("getSnippets failed MEP CE");
															}

															@Override
															public void onSuccess(
																	ArrayList<Snippet> result) {
																// TODO Auto-generated method stub
																
												                // TODO Do this when the server responds
												                if (result.isEmpty()) {
												                    Window.alert("No result can be found...");
												                } else {
												                	displaySnippets(result);
												                }
															}
														}); 
												}
											});
										}
									});					               
								}
							}); 
					}
				});
	}

	
	public void displaySnippets(ArrayList<Snippet> snippets) {
		Collections.sort(snippets);
		
	    objSectionStack = new SectionStack();
		objSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		objSectionStack.setWidth("550px");
		objSectionStack.setStyleName("snippetContainer");

		for(int i = 1; i <= 20; i++) {
			
			Snippet snippet = snippets.get(i);
			
			HTMLFlow objSnippet = new HTMLFlow();
			objSnippet.setStyleName("snippet");
			objSnippet.setContents(snippet.toString());
			
			SectionStackSection objSectionStackSection = new SectionStackSection("Snippet " + i);
			objSectionStackSection.addItem(objSnippet);
			objSectionStack.addSection(objSectionStackSection);
		   
			if(i == 1) 
				objSectionStackSection.setExpanded(true);
			else 
				objSectionStackSection.setExpanded(false);
		}
		
	    HLayout objHLayout = new HLayout();
	    objHLayout.setMembersMargin(50);
	    objHLayout.addMember(objSectionStack);

	    hpCenterLayout.add(objSectionStack);
	    RootPanel.get("mainContent").add(hpCenterLayout);
	}

	public String getRecommendedSite(String language)
    {
    	return "stackoverflow ";
    }
}

