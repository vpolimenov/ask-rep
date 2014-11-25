package ask_rep.client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ask_rep.shared.Snippet;
import ask_rep.shared.SnippetContainer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
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
	VerticalPanel vpCenterLayout;
	HorizontalPanel hpCenterLayout;
	Anchor lnkSignIn;
	LoginInfo objLoginInfo = null;
	String language = "Java";
	DateTimeFormat objDateFormat = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss");

	SectionStack objSectionStack;
	
	private int minLength = 100;
	private int maxLength = 500;
	private int UserID = 0;
	
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
	
	private final RepositoryServiceAsync objRepService = GWT
			.create(RepositoryService.class);
	
	private final FolderServiceAsync objFoldService = GWT
			.create(FolderService.class);
	
	private final FileServiceAsync objFileService = GWT
			.create(FileService.class);

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

						if(result) {
							loadRepositoryPanel(1);
						} else {
							RootPanel.get("mainContent").clear();
							
							Label objLabel = new Label("Please sign in to continue.");
							RootPanel.get("mainContent").add(objLabel);
						}
						
						/* if(result) {
							loadUploadPanel();
						} else {
							RootPanel.get("mainContent").clear();
							
							Label objLabel = new Label("Please sign in to upload a file.");
							RootPanel.get("mainContent").add(objLabel);
						}*/
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
		    			public void onSuccess(final Integer userExistsResult) {
		    				// TODO Auto-generated method stub
		    				if(userExistsResult == 0) {
		    					
		    					objUserService.insertUser(strName, strEmail, new AsyncCallback<Integer>() {
		    						
		    						@Override
		    						public void onSuccess(final Integer insertUserResult) {
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
												UserID = insertUserResult;
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
										UserID = userExistsResult;
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
	
	public void loadRepositoryPanel(int option) {
		
		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("contentTitle");
		RootPanel.get("contentHeadWrapper").clear();
		
		HTMLPanel contentTitle = new HTMLPanel("<div id='contentHead'><div id='contentHeaderTitle'>Create / Update Repository</div></div>");
		
		HTML lblSubTitle = new HTML();
		lblSubTitle.setHTML("Create your personal repository and customize it do your liking by adding a folder and sub-folder structure." +
	    					"<p><b>Please specify your repository name in the textfield below and click on 'Create Repository'.</b></p><br/>");
		
		final TextBox txtRepositoryName = new TextBox();
		txtRepositoryName.setText("");
		txtRepositoryName.setStyleName("textbox");
		
		Button btnCreate = new Button("Create Repository");
		btnCreate.setStylePrimaryName("button");
		btnCreate.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(UserID != 0) {
					final String strRepository = txtRepositoryName.getText();
					
					if(strRepository == null || strRepository.equals("")) {
						Window.alert("Repository Name empty!");
					}
					else {
					
						/*objRepService.insertRepository(strRepository, UserID, new AsyncCallback<Integer>() {
		
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert(caught.getMessage());
							}
		
							@Override
							public void onSuccess(Integer repositoryID) {
								// TODO Auto-generated method stub
								
								objRepService.getRepository(repositoryID, new AsyncCallback<RepositoryInfo>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onSuccess(RepositoryInfo result) {
										// TODO Auto-generated method stub							
										loadCreateRepPanel(result);		
									}
	
								});	
												
							}
						});	*/
						
						objRepService.getRepository(1, new AsyncCallback<RepositoryInfo>() {
							
							@Override
							public void onSuccess(RepositoryInfo result) {
								// TODO Auto-generated method stub
								loadCreateRepPanel(result);
							}
							
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}
						});
					}				
				}
			}
		});
		
		HTML existRep = new HTML();
		existRep.setHTML("<hr class='hrStyle'>" + 
						 "<p><b>Select a repository below to " + (option == 1 ? "upload" : "create")+ " your files.</b></p>");
		
		//add existing repositories, listview ..
		
		
		vpCenterLayout = new VerticalPanel();
		vpCenterLayout.add(lblSubTitle);
		vpCenterLayout.add(txtRepositoryName);
		vpCenterLayout.add(btnCreate);
		vpCenterLayout.add(existRep);
	
		RootPanel.get("mainContent").add(vpCenterLayout);
		RootPanel.get("contentHeadWrapper").add(contentTitle);
		
	}
	
	public void loadCreateRepPanel(RepositoryInfo repository) {
		
		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("repTitleWrapper");
		RootPanel.get("contentHeadWrapper").clear();

		Anchor lnkRepository = new Anchor();
		lnkRepository.setText(repository.getName());

		lnkRepository.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
			
		});
	
		
		Label lblCreatedDate = new Label();
		lblCreatedDate.setText("created on " + objDateFormat.format(repository.getCreatedDate()));
		
		String htmlContentTitle = "";
		htmlContentTitle =  "<div id='contentHead'>";
		htmlContentTitle += "<div id='contentHeaderTitle' class='repTitle'>";
		htmlContentTitle += "<span>" + repository.getUser().getName() + "</span> / <span class='lnkRepTitle'>" + lnkRepository.getHTML() + "</span>";
		htmlContentTitle += "</div>";
		htmlContentTitle += "<div class='created_date'>";
		htmlContentTitle += lblCreatedDate.getText();
		htmlContentTitle += "</div>";
		htmlContentTitle += "</div>";
			
		HTMLPanel contentTitle = new HTMLPanel(htmlContentTitle);
		HTMLPanel contentSubTitle = new HTMLPanel("<div class='repNavigate'>navigate / navigate / etc..</div>");
		
		String htmlRepGrid = "";
		htmlRepGrid =  "<div id='repositoryGrid' class='repGridWrapper'>";
		htmlRepGrid += "<table>";
		htmlRepGrid += printFolders(repository.getRepositoryID(), 0).getHTML();
		htmlRepGrid += printFiles(repository.getRepositoryID(), 0).getHTML();
		htmlRepGrid += "</table>";
		htmlRepGrid += "</div>";

		HTMLPanel contentRepository = new HTMLPanel(htmlRepGrid);
		
		RootPanel.get("contentHeadWrapper").add(contentTitle);
		RootPanel.get("mainContent").add(contentSubTitle);
		RootPanel.get("mainContent").add(contentRepository);
	}
	
	public HTML printFolders(int repositoryID, int parentFolderID) {
		
		if(RootPanel.get("repositoryGrid table") != null)
			RootPanel.get("repositoryGrid table").clear();
		
	    final HTML hPanel = new HTML();
		
		objFoldService.getFolders(repositoryID, parentFolderID, new AsyncCallback<List<FolderInfo>>() {
			
			@Override
			public void onSuccess(List<FolderInfo> result) {
				// TODO Auto-generated method stub
				String structure = "";
				
				for(int i =0; i < result.size(); i++) {
					final FolderInfo objFoldInfo = result.get(i);
					
					structure += "<tr>";
					
					Anchor lnkName = new Anchor();
					lnkName.setText(objFoldInfo.getName());
					lnkName.setStyleName("lnkItemTitle");

					lnkName.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							printFolders(objFoldInfo.getRepository().getRepositoryID(), objFoldInfo.getFolderID());
							printFiles(objFoldInfo.getRepository().getRepositoryID(), objFoldInfo.getFolderID());
						}
						
					});
					
					structure += "<td>" + lnkName + "</td>";
					structure += "<td>" + objDateFormat.format(objFoldInfo.getDatecreated()) + "</td>";
					structure += "</tr>";
						
				}
				
				hPanel.setHTML(structure);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
		
		return hPanel;
	}
	
	public HTML printFiles(int repositoryID, int parentFolderID) {
		
	    final HTML hPanel = new HTML();
		
		objFileService.getFiles(repositoryID, parentFolderID, new AsyncCallback<List<FileInfo>>() {
			
			@Override
			public void onSuccess(List<FileInfo> result) {
				// TODO Auto-generated method stub
				String structure = "";
				
				for(int i =0; i < result.size(); i++) {
					final FileInfo objFileInfo = result.get(i);
					
					structure += "<tr>";
					
					Anchor lnkName = new Anchor();
					lnkName.setText(objFileInfo.getName() + objFileInfo.getExtension());
					lnkName.setStyleName("lnkItemTitle");

					lnkName.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub
							
						}
						
					});
					
					structure += "<td>" + lnkName + "</td>";
					structure += "<td>" + objDateFormat.format(objFileInfo.getDatecreated()) + "</td>";
					structure += "</tr>";
						
				}
				
				hPanel.setHTML(structure);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return hPanel;
	}
	
	
	
	public void loadTrendingRepositoryPanel() {
		
		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("contentTitle");
		
		if(RootPanel.get("contentHeadWrapper").getWidgetCount() > 0)
			RootPanel.get("contentHeadWrapper").clear();
		
	    HTMLPanel contentTitle = new HTMLPanel("<div id='contentHead'><div id='contentHeaderTitle'>Trending Repositories</div></div>");
 
		Label myLabel = new Label("Trending....");
		
		vpCenterLayout = new VerticalPanel();
		vpCenterLayout.add(myLabel);
		
		RootPanel.get("mainContent").add(vpCenterLayout);
		RootPanel.get("contentHeadWrapper").add(contentTitle);	
	}
	
	public void loadUploadPanel() {
		
		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("contentTitle");
		RootPanel.get("contentHeadWrapper").clear();
		
		HTMLPanel contentTitle = new HTMLPanel("<div id='contentHead'><div id='contentHeaderTitle'>Upload Files</div></div>");
		
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
		RootPanel.get("contentHeadWrapper").add(contentTitle);
		
	}
	
	public void loadCreatePanel() {
		
		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("contentTitle");
		RootPanel.get("contentHeadWrapper").clear();
		
		HTMLPanel contentTitle = new HTMLPanel("<div id='contentHead'><div id='contentHeaderTitle'>Create Files</div></div>");
		
		vpCenterLayout = new VerticalPanel();
		hpCenterLayout = new HorizontalPanel();

		final TextArea codePanel = new TextArea();
		codePanel.setCharacterWidth(63);
		codePanel.setVisibleLines(35);
		codePanel.setStyleName("codePanel");
		hpCenterLayout.add(codePanel);
		
		final Label lblInstructions = new Label();
		lblInstructions.setStyleName("instructions");
		lblInstructions.setText("Press CTRL to search for code snippets ...");
		hpCenterLayout.add(lblInstructions);

		codePanel.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				
				if (event.getNativeKeyCode() == KeyCodes.KEY_CTRL && codePanel.getSelectedText() != null && codePanel.getSelectionLength() > 3) {
					
					if(hpCenterLayout.getWidget(1) != null) {
						
						hpCenterLayout.remove(1);
						
						final HTML lblLoading = new HTML();
						lblLoading.setHTML("<div class='instructions' style='padding-top:230px; height:300px'><img src='/images/loader.GIF' alt='loading ...' /></div>");
						hpCenterLayout.add(lblLoading);
						
					}	
									
					search(language + " " + getRecommendedSite(language) + codePanel.getSelectedText(), codePanel.getSelectedText());
				}
				else if(event.getNativeKeyCode() == KeyCodes.KEY_TAB)
				{
					      event.preventDefault();
					      event.stopPropagation();
					      
					      if(event.getSource() instanceof TextArea) {
						        TextArea txtCodePanel = (TextArea) event.getSource();
						        int index = txtCodePanel.getCursorPos();
						        String text = txtCodePanel.getText();
						        txtCodePanel.setText(text.substring(0, index) + "\t" + text.substring(index));
						        txtCodePanel.setCursorPos(index + 1);
					      }
				}
			}
		});
		
		RootPanel.get("mainContent").add(hpCenterLayout);
		RootPanel.get("contentHeadWrapper").add(contentTitle);
		
	}
	
	protected void search(String searchText, final String keyWords) {
		// TODO Auto-generated method stub
//        answerPanel.addProgressBar();
		
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
		
		if(hpCenterLayout.getWidget(1) != null) {
			hpCenterLayout.remove(1);
		}
		
	    objSectionStack = new SectionStack();
		objSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		objSectionStack.setWidth("550px");
		objSectionStack.setStyleName("snippetContainer");
		
		if(!snippets.isEmpty()) {
			for(int i = 1; i <= 20; i++) {	
						Snippet snippet = snippets.get(i);
						
						HTMLFlow objSnippet = new HTMLFlow();
						objSnippet.setStyleName("snippet");
						objSnippet.setContents("<pre><code style='font-size:12px' class='" + snippet.getLanguage() + "'>" + snippet.toString() + "</code></pre>");
						
						SectionStackSection objSectionStackSection = new SectionStackSection("Snippet " + i);
						objSectionStackSection.addItem(objSnippet);
						objSectionStack.addSection(objSectionStackSection);
					   
						if(i == 1) 
							objSectionStackSection.setExpanded(true);
						else 
							objSectionStackSection.setExpanded(false);

			}
		} else {
			
			HTMLFlow objSnippet = new HTMLFlow();
			objSnippet.setStyleName("snippet");
			objSnippet.setContents("No snippets can be found ...");
			
			SectionStackSection objSectionStackSection = new SectionStackSection("Results");
			objSectionStackSection.addItem(objSnippet);
			objSectionStack.addSection(objSectionStackSection);
		   
			objSectionStackSection.setExpanded(true);
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

