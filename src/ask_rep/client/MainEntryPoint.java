package ask_rep.client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.Highlighter;

import org.apache.commons.el.Literal;

import ask_rep.shared.Snippet;
import ask_rep.shared.SnippetContainer;

import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.HTMLPane;
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
	DialogBox dialogbox;
	Anchor lnkSignIn;
	LoginInfo objLoginInfo = null;
	String language;
	DateTimeFormat objDateFormat = DateTimeFormat
			.getFormat("dd/MM/yyyy HH:mm:ss");
	SectionStack objSectionStack;

	TextBox fileName = null;
	String fileToDatabase = " ";
	String[] languages = new String[] { "Java", "C++", "Python", "C#" };
	ListBox languageChoose;

	private int minLength = 100;
	private int maxLength = 500;
	private int UserID = 0;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
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

	private final FileServiceAsync objFileService = GWT
			.create(FileService.class);
	
	private final FolderServiceAsync objFoldService = GWT
			.create(FolderService.class);

//	private final FileServiceAsync objFileService = GWT
//			.create(FileService.class);

	private final UserServiceAsync usrService = GWT.create(UserService.class);

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
		lnkTrendingRepositories.addClickHandler(new ClickHandler() {

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

				objSessionService.validateSession("UserID",
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(Boolean result) {
								// TODO Auto-generated method stub

								if (result) {
									loadRepositoryPanel(1);
								} else {

									final HTML wrapper = new HTML(
											"<div class='overlay' style='overflow: visible; position: absolute; left: 0px; top: 0px;'><div class='popupContent'></div></div>");

									dialogbox = new DialogBox(false);
									dialogbox.setStyleName("dialog");

									VerticalPanel vpDialogContent = new VerticalPanel();

									HTML htmlMessage = new HTML(
											"Please sign in to continue");
									Button btnClose = new Button("Close");
									btnClose.setStylePrimaryName("dialog-button");
									btnClose.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											// TODO Auto-generated method stub
											RootPanel.get("mainContent")
													.remove(wrapper);
											dialogbox.hide();
										}

									});

									SimplePanel holder = new SimplePanel();
									holder.add(btnClose);

									vpDialogContent.add(htmlMessage);
									vpDialogContent.add(holder);
									dialogbox.setWidget(vpDialogContent);

									RootPanel.get("mainContent").add(wrapper);
									dialogbox.center();
								}

								/*
								 * if(result) { loadUploadPanel(); } else {
								 * RootPanel.get("mainContent").clear();
								 * 
								 * Label objLabel = new
								 * Label("Please sign in to upload a file.");
								 * RootPanel.get("mainContent").add(objLabel); }
								 */
							}
						});
			}
		});

		Anchor lnkCreate = new Anchor();
		lnkCreate.setText("Create Files");
		lnkCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				objSessionService.validateSession("UserID",
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									loadRepositoryPanel(2);
								} else {
									final HTML wrapper = new HTML(
											"<div class='overlay' style='overflow: visible; position: absolute; left: 0px; top: 0px;'><div class='popupContent'></div></div>");

									dialogbox = new DialogBox(false);
									dialogbox.setStyleName("dialog");

									VerticalPanel vpDialogContent = new VerticalPanel();

									HTML htmlMessage = new HTML(
											"Please sign in to continue");
									Button btnClose = new Button("Close");
									btnClose.setStylePrimaryName("dialog-button");
									btnClose.addClickHandler(new ClickHandler() {

										@Override
										public void onClick(ClickEvent event) {
											// TODO Auto-generated method stub
											RootPanel.get("mainContent")
													.remove(wrapper);
											dialogbox.hide();
										}

									});

									SimplePanel holder = new SimplePanel();
									holder.add(btnClose);

									vpDialogContent.add(htmlMessage);
									vpDialogContent.add(holder);
									dialogbox.setWidget(vpDialogContent);

									RootPanel.get("mainContent").add(wrapper);
									dialogbox.center();
								}
							}
						});
			}
		});

		RootPanel.get("lnkTrending").add(lnkTrendingRepositories);
		RootPanel.get("lnkUpload").add(lnkUpload);
		RootPanel.get("lnkCreate").add(lnkCreate);
	}

	public void login() {
		// Check login status using login service.

		objLoginService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {

					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo loginResult) {

						if (loginResult.isLoggedIn()) {

							final String strEmail = loginResult
									.getEmailAddress();
							final String strName = loginResult.getNickname();

							lnkSignIn = new Anchor(strEmail + ", Sign Out");
							lnkSignIn.setHref(loginResult.getLogoutUrl());

							objUserService.checkUserExists(strEmail,
									new AsyncCallback<Integer>() {

										@Override
										public void onSuccess(
												final Integer userExistsResult) {
											// TODO Auto-generated method stub
											if (userExistsResult == 0) {

												objUserService
														.insertUser(
																strName,
																strEmail,
																new AsyncCallback<Integer>() {

																	@Override
																	public void onSuccess(
																			final Integer insertUserResult) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		objSessionService
																				.createSession(
																						"UserID",
																						insertUserResult
																								.toString(),
																						new AsyncCallback<Void>() {

																							@Override
																							public void onFailure(
																									Throwable caught) {
																								// TODO
																								// Auto-generated
																								// method
																								// stub

																							}

																							@Override
																							public void onSuccess(
																									Void result) {
																								// TODO
																								// Auto-generated
																								// method
																								// stub
																								UserID = insertUserResult;
																							}
																						});
																	}

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		Window.alert("An error has occurred");
																	}

																});

											} else {
												objSessionService
														.createSession(
																"UserID",
																userExistsResult
																		.toString(),
																new AsyncCallback<Void>() {

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub

																	}

																	@Override
																	public void onSuccess(
																			Void result) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
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

							objSessionService.invalidateSession("UserID",
									new AsyncCallback<Void>() {

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

	public void loadRepositoryPanel(final int option) {

		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("contentTitle");
		RootPanel.get("contentHeadWrapper").clear();

		HTMLPanel contentTitle = new HTMLPanel(
				"<div id='contentHead'><div id='contentHeaderTitle'>Create / Update Repository</div></div>");

		HTML lblSubTitle = new HTML();
		lblSubTitle
				.setHTML("Create your personal repository and customize it do your liking by adding a folder and sub-folder structure."
						+ "<p><b>Please specify your repository name in the textfield below and click on 'Create Repository'.</b></p><br/>");

		final TextBox txtRepositoryName = new TextBox();
		txtRepositoryName.setText("");
		txtRepositoryName.setStyleName("textbox");

		Button btnCreate = new Button("Create Repository");
		btnCreate.setStylePrimaryName("button");
		btnCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (UserID != 0) {
					final String strRepository = txtRepositoryName.getText();

					if (strRepository == null || strRepository.equals("")) {
						Window.alert("Repository Name empty!");
					} else {

						
						  objRepService.insertRepository(strRepository, UserID,
						  new AsyncCallback<Integer>() {
						  
						  @Override public void onFailure(Throwable caught) {
						  // TODO Auto-generated method stub
						  Window.alert(caught.getMessage()); }
						  
						  @Override public void onSuccess(Integer repositoryID)
						  { // TODO Auto-generated method stub
						  
						  objRepService.getRepository(repositoryID, new
						  AsyncCallback<RepositoryInfo>() {
						  
						  @Override public void onFailure(Throwable caught) {
						  // TODO Auto-generated method stub
						  
						  }
						  
						  @Override public void onSuccess(RepositoryInfo
						  result) { // TODO Auto-generated method stub
						  loadCreateRepPanel(result, 1); }
						  
						  });
						  
						  } });
						 

						objRepService.getRepository(1,
								new AsyncCallback<RepositoryInfo>() {

									@Override
									public void onSuccess(RepositoryInfo result) {
										// TODO Auto-generated method stub
										loadCreateRepPanel(result, option);
									}

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										Window.alert(caught.getMessage());
									}
								});
					}
				}
			}
		});

		HTML existRep = new HTML();
		existRep.setHTML("<hr class='hrStyle'>"
				+ "<p><b>Select a repository below to "
				+ (option == 1 ? "upload" : "create") + " your files.</b></p>");

		// add existing repositories, listview ..

		vpCenterLayout = new VerticalPanel();
		vpCenterLayout.add(lblSubTitle);
		vpCenterLayout.add(txtRepositoryName);
		vpCenterLayout.add(btnCreate);
		vpCenterLayout.add(existRep);

		if (UserID != 0) {

			objRepService.getRepositoryByUserID(UserID,
					new AsyncCallback<List<RepositoryInfo>>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(List<RepositoryInfo> result) {
							// TODO Auto-generated method stub

							String strRepositories = "<table>";

							List<Anchor> lstRepositoryAnchors = new ArrayList<Anchor>();

							for (int i = 0; i < result.size(); i++) {

								final RepositoryInfo objRepository = result
										.get(i);

								if ((i + 1) % 4 == 1) {
									strRepositories += "<tr>";
								}

								Anchor lnkRepository = new Anchor();

								if ((i + 1) % 4 == 1) {
									lnkRepository
											.setHTML("<div class='repositoryitem' style='margin-left:0px'>"
													+ objRepository.getName()
													+ "</div>");
								} else {
									lnkRepository
											.setHTML("<div class='repositoryitem'>"
													+ objRepository.getName()
													+ "</div>");

								}

								lnkRepository
										.addClickHandler(new ClickHandler() {

											@Override
											public void onClick(ClickEvent event) {
												// TODO Auto-generated method
												// stub
												loadCreateRepPanel(
														objRepository, option);
											}
										});

								strRepositories += "<td><div id='lnkRepository"
										+ i + "'></div></td>";
								lstRepositoryAnchors.add(lnkRepository);

								if ((i + 1) % 4 == 0) {
									strRepositories += "</tr>";
								}
							}

							strRepositories += "</table>";

							HTMLPanel hRepositories = new HTMLPanel(
									strRepositories);
							if (lstRepositoryAnchors.size() > 0) {
								for (int i = 0; i < lstRepositoryAnchors.size(); i++) {
									hRepositories.add(
											lstRepositoryAnchors.get(i),
											"lnkRepository" + i);
								}
							}

							vpCenterLayout.add(hRepositories);
						}
					});
		}

		RootPanel.get("mainContent").add(vpCenterLayout);
		RootPanel.get("contentHeadWrapper").add(contentTitle);

	}

	public void loadCreateRepPanel(final RepositoryInfo repository,
			final int option) {

		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("repTitleWrapper");
		RootPanel.get("contentHeadWrapper").clear();

		final String strNav = "<b style='color:black'>" + repository.getName()
				+ "<b>";

		Label lblCreatedDate = new Label();
		lblCreatedDate.setText("created on "
				+ objDateFormat.format(repository.getCreatedDate()));

		String htmlContentTitle = "";
		htmlContentTitle = "<div id='contentHead'>";
		htmlContentTitle += "<div id='contentHeaderTitle' class='repTitle'>";
		htmlContentTitle += "<span>"
				+ repository.getUser().getName()
				+ "</span> / <span id='lnkRepository' class='lnkRepTitle'></span>";
		htmlContentTitle += "</div>";
		htmlContentTitle += "<div class='created_date'>";
		htmlContentTitle += lblCreatedDate.getText();
		htmlContentTitle += "</div>";
		htmlContentTitle += "</div>";

		HTMLPanel Navigation = new HTMLPanel(strNav);
		RootPanel.get("mainContent").add(Navigation);

		Anchor lnkRepository = new Anchor();
		lnkRepository.setText(repository.getName());

		lnkRepository.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				RootPanel.get("mainContent").remove(1);
				RootPanel.get("mainContent").getWidget(0).getElement()
						.setInnerHTML(strNav);

				printRepository(repository.getRepositoryID(), 0, strNav, option);
			}

		});

		HTMLPanel contentTitle = new HTMLPanel(htmlContentTitle);
		contentTitle.add(lnkRepository, "lnkRepository");
		RootPanel.get("contentHeadWrapper").add(contentTitle);

		printRepository(repository.getRepositoryID(), 0, strNav, option);
	}

	public void printRepository(final int repositoryID,
			final int parentFolderID, final String Navigation, final int option) {

		hpCenterLayout = new HorizontalPanel();

		objFoldService.getFolders(repositoryID, parentFolderID,
				new AsyncCallback<List<FolderInfo>>() {

					String htmlRepository = "<div id='repositoryGrid' class='repGridWrapper'>";
					String NavigateText = Navigation;

					Anchor lnkPrevious;
					List<Anchor> lstFolderAnchors = new ArrayList<Anchor>();
					List<Anchor> lstFileAnchors = new ArrayList<Anchor>();
					HTMLPanel contentRepository;

					@Override
					public void onSuccess(List<FolderInfo> result) {

						// TODO Auto-generated method stub
						htmlRepository += "<table>";

						if (parentFolderID != 0) {
							lnkPrevious = new Anchor();
							lnkPrevious.setText("...");
							lnkPrevious.setStyleName("lnkItemTitle");
							lnkPrevious.addClickHandler(new ClickHandler() {

								@Override
								public void onClick(ClickEvent event) {
									// TODO Auto-generated method stub
									RootPanel.get("mainContent").remove(1);

									objFoldService.getFolder(parentFolderID,
											new AsyncCallback<FolderInfo>() {

												@Override
												public void onSuccess(
														FolderInfo result) {
													// TODO Auto-generated
													// method stub

													NavigateText = NavigateText
															.substring(
																	0,
																	NavigateText
																			.lastIndexOf('/'));

													if (!NavigateText
															.contains("/")) {
														NavigateText = NavigateText
																.replaceAll(
																		"rgb\\(153\\,201\\,198\\)",
																		"black");
													} else {
														String start = NavigateText
																.substring(
																		0,
																		NavigateText
																				.lastIndexOf('/'));
														String end = NavigateText.substring(
																NavigateText
																		.lastIndexOf('/'),
																NavigateText
																		.length());

														end = end
																.replaceAll(
																		"rgb\\(153\\,201\\,198\\)",
																		"black");
														NavigateText = start
																+ end;
													}

													RootPanel
															.get("mainContent")
															.getWidget(0)
															.getElement()
															.setInnerHTML(
																	NavigateText);

													printRepository(
															repositoryID,
															result.getParentFolderID(),
															NavigateText,
															option);
												}

												@Override
												public void onFailure(
														Throwable caught) {
													// TODO Auto-generated
													// method stub

												}
											});
								}

							});

							htmlRepository += "<tr>";
							htmlRepository += "<td><div id='repRoot'></div></td>";
							htmlRepository += "</tr>";
						}

						if (result.size() > 0) {
							for (int i = 0; i < result.size(); i++) {

								final FolderInfo objFoldInfo = result.get(i);
								htmlRepository += "<tr>";

								Anchor lnkName = new Anchor();
								lnkName.setText(objFoldInfo.getName());
								lnkName.setStyleName("lnkItemTitle");

								lnkName.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										// TODO Auto-generated method stub

										RootPanel.get("mainContent").remove(1);

										NavigateText = NavigateText.replaceAll(
												"black", "rgb(153,201,198)");
										NavigateText += " / "
												+ "<b style='color:black'>"
												+ objFoldInfo.getName() + "<b>";
										RootPanel.get("mainContent")
												.getWidget(0).getElement()
												.setInnerHTML(NavigateText);

										printRepository(objFoldInfo
												.getRepository()
												.getRepositoryID(), objFoldInfo
												.getFolderID(), NavigateText,
												option);
									}

								});

								htmlRepository += "<td><div id='lnkFolder" + i
										+ "'></div></td>";
								htmlRepository += "<td style='text-align:right'>"
										+ objDateFormat.format(objFoldInfo
												.getDatecreated()) + "</td>";
								htmlRepository += "</tr>";

								lstFolderAnchors.add(lnkName);
							}
						}

						objFileService.getFiles(repositoryID, parentFolderID,
								new AsyncCallback<List<FileInfo>>() {

									@Override
									public void onSuccess(List<FileInfo> result) {
										// TODO Auto-generated method stub

										if (result.size() > 0) {
											for (int i = 0; i < result.size(); i++) {
												final FileInfo objFileInfo = result
														.get(i);

												htmlRepository += "<tr>";

												Anchor lnkName = new Anchor();
												lnkName.setText(objFileInfo
														.getName()
														+ objFileInfo
																.getExtension());
												lnkName.setStyleName("lnkItemTitle");

												lnkName.addClickHandler(new ClickHandler() {

													@Override
													public void onClick(
															ClickEvent event) {
														// TODO Auto-generated
														// method stub

													}

												});

												htmlRepository += "<td><div id='lnkFile"
														+ i + "'></div></td>";
												htmlRepository += "<td style='text-align:right'>"
														+ objDateFormat
																.format(objFileInfo
																		.getDatecreated())
														+ "</td>";
												htmlRepository += "</tr>";

												lstFileAnchors.add(lnkName);
											}
										}

										htmlRepository += "</table>";

										contentRepository = new HTMLPanel(
												htmlRepository);

										if (lstFolderAnchors.size() > 0) {
											for (int i = 0; i < lstFolderAnchors
													.size(); i++) {
												contentRepository.add(
														lstFolderAnchors.get(i),
														"lnkFolder" + i);
											}
										}
										if (lstFileAnchors.size() > 0) {
											for (int i = 0; i < lstFileAnchors
													.size(); i++) {
												contentRepository.add(
														lstFileAnchors.get(i),
														"lnkFile" + i);
											}
										}
										if (parentFolderID != 0) {
											contentRepository.add(lnkPrevious,
													"repRoot");
										}

										// Add Repository Grid to Horizontal
										// Panel
										hpCenterLayout.add(contentRepository);

										Anchor createFolder = new Anchor();
										createFolder.setText("Create Folder");
										createFolder
												.setStyleName("lnkItemTitle");

										createFolder
												.addClickHandler(new ClickHandler() {

													@Override
													public void onClick(
															ClickEvent event) {
														// TODO Auto-generated
														// method stub
														loadCreateFolderPanel(
																parentFolderID,
																repositoryID,
																Navigation,
																option);
													}

												});

										// Add Create File link to Horizontal
										// Panel
										Anchor createFile = new Anchor();
										createFile
												.setText(option == 1 ? "Upload"
														: "Create" + " Files");
										createFile.setStyleName("lnkItemTitle");

										createFile
												.addClickHandler(new ClickHandler() {

													@Override
													public void onClick(
															ClickEvent event) {
														// TODO Auto-generated//
														// method stub
														if (option == 1) {
															loadUploadPanel();
														} else {
															loadCreateFilePanel();
														}
													}

												});

										vpCenterLayout = new VerticalPanel();
										vpCenterLayout
												.setStyleName("sideLinkWrapper");
										vpCenterLayout.add(createFolder);
										vpCenterLayout.add(createFile);

										hpCenterLayout.add(vpCenterLayout);

										RootPanel.get("mainContent").add(
												hpCenterLayout);

									}

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										Window.alert(caught.getMessage());
									}
								});
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	public void loadTrendingRepositoryPanel() {

		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("contentTitle");

		if (RootPanel.get("contentHeadWrapper").getWidgetCount() > 0)
			RootPanel.get("contentHeadWrapper").clear();

		HTMLPanel contentTitle = new HTMLPanel(
				"<div id='contentHead'><div id='contentHeaderTitle'>Trending Repositories</div></div>");

		// objSessionService.validateSession("UserID", new
		// AsyncCallback<Boolean>() {
		//
		// @Override
		// public void onSuccess(Boolean result) {
		// // TODO Auto-generated method stub
		// if(result){
		// objRepService.getRepository(UserID, new
		// AsyncCallback<RepositoryInfo>() {
		//
		// @Override
		// public void onSuccess(RepositoryInfo result) {
		// // TODO Auto-generated method stub
		// System.out.println(result.getName());
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// }else{
		//
		// }
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		Label myLabel = new Label("Trending ...");

		vpCenterLayout = new VerticalPanel();
		vpCenterLayout.add(myLabel);

		RootPanel.get("mainContent").add(vpCenterLayout);
		RootPanel.get("contentHeadWrapper").add(contentTitle);
	}

	public void loadUploadPanel() {

		final HTML wrapper = new HTML(
				"<div class='overlay' style='overflow: visible; position: absolute; left: 0px; top: 0px;'><div class='popupContent'></div></div>");

		dialogbox = new DialogBox(false);
		dialogbox.setStyleName("dialog");

		VerticalPanel vpDialogContent = new VerticalPanel();

		FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");

		Button btnClose = new Button("Upload");
		btnClose.setStylePrimaryName("dialog-button");
		btnClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				RootPanel.get("mainContent").remove(wrapper);
				dialogbox.hide();
			}

		});

		SimplePanel holder = new SimplePanel();
		holder.add(btnClose);

		vpDialogContent.add(upload);
		vpDialogContent.add(holder);
		dialogbox.setWidget(vpDialogContent);

		RootPanel.get("mainContent").add(wrapper);
		dialogbox.center();

	}

	public void loadCreateFilePanel() {

		final HTML wrapper = new HTML(
				"<div class='overlay' style='overflow: visible; position: absolute; left: 0px; top: 0px;'><div class='popupContent'></div></div>");

		dialogbox = new DialogBox(false);
		dialogbox.setStyleName("dialog");

		VerticalPanel vpDialogContent = new VerticalPanel();

		fileName = new TextBox();
		fileName.setStylePrimaryName("dialog-textbox");

		languageChoose = new ListBox();

		for (int i = 0; i < languages.length; i++) {
			languageChoose.addItem(languages[i]);
		}
		
		Button btnCreate = new Button("Create");
		btnCreate.setStylePrimaryName("dialog-button");
		btnCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (fileName.getText().length() == 0) {
					Window.alert("Could you please mention the name of the file");
				} else {

					language = " ";

					final LoginInfo logInfo = new LoginInfo();

					loadCreatePanel();
					// set the file extension
					switch (languageChoose.getItemText(languageChoose
							.getSelectedIndex())) {
					case ("Java"):
						fileToDatabase = fileName.getText() + ".java";
						break;
					case ("C++"):
						fileToDatabase = fileName.getText() + ".cpp";
						break;
					case ("Python"):
						fileToDatabase = fileName.getText() + ".py";
						break;
					case ("C#"):
						fileToDatabase = fileName.getText() + ".cs";
						break;
					}
					// should get user's nick name
					// add
					// add to database query fileToDatabase
					System.out.println("User: '" + logInfo.getNickname()
							+ "' created file: '" + fileToDatabase + "'");
					
					objFileService.insertFile(fileToDatabase, UserID,
							  new AsyncCallback<Integer>() {
							  
							  @Override public void onFailure(Throwable caught) {
							  // TODO Auto-generated method stub
							  Window.alert(caught.getMessage()); }
							  
							  @Override public void onSuccess(Integer fileID)
							  { // TODO Auto-generated method stub
							  
							  objFileService.getFiles(fileID, UserID, new
							  AsyncCallback<List<FileInfo>>() {
							  
							  @Override public void onFailure(Throwable caught) {
							  // TODO Auto-generated method stub
							  
							  }
							  
							  @Override public void onSuccess(List<FileInfo>
							  result) { // TODO Auto-generated method stub
//							  loadCreateRepPanel(result, 1); 
								  System.out.println(result);
								  }
							  
							  });
							  
							  } });

					dialogbox.hide();
				}
			}
		});

		SimplePanel holder = new SimplePanel();
		holder.add(btnCreate);

		vpDialogContent.add(fileName);
		vpDialogContent.add(languageChoose);
		vpDialogContent.add(holder);
		dialogbox.setWidget(vpDialogContent);

		RootPanel.get("mainContent").add(wrapper);
		dialogbox.center();

	}

	public void loadCreateFolderPanel(final int parentFolderID,
			final int repositoryID, final String Navigation, final int option) {

		final HTML wrapper = new HTML(
				"<div class='overlay' style='overflow: visible; position: absolute; left: 0px; top: 0px;'><div class='popupContent'></div></div>");

		dialogbox = new DialogBox(false);
		dialogbox.setStyleName("dialog");

		VerticalPanel vpDialogContent = new VerticalPanel();
		fileName = new TextBox();
		fileName.setStylePrimaryName("dialog-textbox");

		Button btnCreate = new Button("Create");
		btnCreate.setStylePrimaryName("dialog-button");
		btnCreate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				objFoldService.insertFolder(fileName.getText(), parentFolderID,
						repositoryID, new AsyncCallback<Integer>() {

							@Override
							public void onSuccess(Integer result) {
								// TODO Auto-generated method stub
								RootPanel.get("mainContent").clear();
								printRepository(repositoryID, result,
										Navigation, option);
								dialogbox.hide();
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});

			}

		});

		SimplePanel holder = new SimplePanel();
		holder.add(btnCreate);

		vpDialogContent.add(fileName);
		vpDialogContent.add(holder);
		dialogbox.setWidget(vpDialogContent);

		RootPanel.get("mainContent").add(wrapper);
		dialogbox.center();

	}

	public void loadCreatePanel() {

		RootPanel.get("mainContent").clear();
		RootPanel.get("contentHeadWrapper").setStyleName("contentTitle");
		RootPanel.get("contentHeadWrapper").clear();

		HTMLPanel contentTitle = new HTMLPanel(
				"<div id='contentHead'><div id='contentHeaderTitle'>Create Files</div></div>");

		vpCenterLayout = new VerticalPanel();
		hpCenterLayout = new HorizontalPanel();

		final TextArea codePanel = new TextArea();

		// AceEditor codePanel = new AceEditor();
		//
		// codePanel.setWidth("600px");
		// codePanel.setHeight("400px");

		// codePanel.startEditor();
		// codePanel.setMode(AceEditorMode.PERL);
		// codePanel.setTheme(AceEditorTheme.TWILIGHT);

		codePanel.setCharacterWidth(63);
		codePanel.setVisibleLines(35);

		codePanel.setStyleName("codePanel");
		hpCenterLayout.add(codePanel);

		final Label lblInstructions = new Label();
		lblInstructions.setStyleName("instructions");
		lblInstructions.setText("Press -> to search for code snippets ...");
		hpCenterLayout.add(lblInstructions);

		codePanel.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {

				if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT
						&& codePanel.getSelectedText() != null
						&& codePanel.getSelectionLength() > 3) {

					if (hpCenterLayout.getWidget(1) != null) {

						hpCenterLayout.remove(1);

						final HTML lblLoading = new HTML();
						lblLoading
								.setHTML("<div class='instructions' style='padding-top:230px; height:300px'><img src='/images/loader.GIF' alt='loading ...' /></div>");
						hpCenterLayout.add(lblLoading);

					}

					search(languageChoose.getItemText(languageChoose
							.getSelectedIndex())
							+ " "
							+ getRecommendedSite(languageChoose
									.getItemText(languageChoose
											.getSelectedIndex()))
							+ codePanel.getSelectedText(), codePanel
							.getSelectedText());
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					event.preventDefault();
					event.stopPropagation();

					if (event.getSource() instanceof TextArea) {
						TextArea txtCodePanel = (TextArea) event.getSource();
						int index = txtCodePanel.getCursorPos();
						String text = txtCodePanel.getText();
						txtCodePanel.setText(text.substring(0, index) + "\t"
								+ text.substring(index));
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
		// answerPanel.addProgressBar();

		linkSearcher.setQueryString(searchText, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("setQueryString failed MEP");
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				// ArrayList<String> searchResults;

				linkSearcher
						.getSearchResults(new AsyncCallback<ArrayList<String>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								System.out
										.println("getSearchResults failed MEP");
							}

							@Override
							public void onSuccess(ArrayList<String> result) {
								// TODO Auto-generated method stub
								// codeExtractor.resultLinks = result;
								codeExtractor.extractCodeTags(result,
										new AsyncCallback<Void>() { // ,
											// answerPanel

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub
												System.out
														.println("extractCodeTags failed MEP");
												caught.printStackTrace();
											}

											@Override
											public void onSuccess(Void result) {
												// TODO Auto-generated method
												// stub
												codeExtractor
														.extractSnippets(
																language,
																keyWords,
																minLength,
																maxLength,
																new AsyncCallback<Void>() {

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		System.out
																				.println("extractSnippets failed MEP");
																	}

																	@Override
																	public void onSuccess(
																			Void result) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub

																		codeExtractor
																				.getSnippets(new AsyncCallback<ArrayList<Snippet>>() {

																					@Override
																					public void onFailure(
																							Throwable caught) {
																						// TODO
																						// Auto-generated
																						// method
																						// stub
																						System.out
																								.println("getSnippets failed MEP CE");
																					}

																					@Override
																					public void onSuccess(
																							ArrayList<Snippet> result) {
																						// TODO
																						// Auto-generated
																						// method
																						// stub

																						// TODO
																						// Do
																						// this
																						// when
																						// the
																						// server
																						// responds
																						if (result
																								.isEmpty()) {
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

		if (hpCenterLayout.getWidget(1) != null) {
			hpCenterLayout.remove(1);
		}

		objSectionStack = new SectionStack();
		objSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		objSectionStack.setWidth("550px");
		objSectionStack.setStyleName("snippetContainer");

		if (!snippets.isEmpty()) {
			for (int i = 1; i <= 20; i++) {
				Snippet snippet = snippets.get(i);

				HTMLFlow objSnippet = new HTMLFlow();
				objSnippet.setStyleName("snippet");
				objSnippet
						.setContents("<pre><code style='font-size:12px' class='"
								+ snippet.getLanguage()
								+ "'>"
								+ snippet.toString() + "</code></pre>");

				SectionStackSection objSectionStackSection = new SectionStackSection(
						"Snippet " + i);
				objSectionStackSection.addItem(objSnippet);
				objSectionStack.addSection(objSectionStackSection);

				if (i == 1)
					objSectionStackSection.setExpanded(true);
				else
					objSectionStackSection.setExpanded(false);

			}
		} else {

			HTMLFlow objSnippet = new HTMLFlow();
			objSnippet.setStyleName("snippet");
			objSnippet.setContents("No snippets can be found ...");

			SectionStackSection objSectionStackSection = new SectionStackSection(
					"Results");
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

	public String getRecommendedSite(String language) {
		return "stackoverflow ";
	}
}
