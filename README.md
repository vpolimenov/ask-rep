ask-rep
=======

The main objective for this project is to create an on-line repository where users would be able to create, upload and share their files amongst other users. Users would be able to create public repositories in order to upload or create new files and store them in folders/sub-folders. The main reason for creating custom files is to allow the user to write pieces of code and if needs be whilst typing the application will recommend on-line code snippets to him/her. This will permit the user to search for code through the application instead of typing their query in Google.

These files are saved on the cloud and placed in new or existing repositories according to the users’ preference. All repositories are available to the public through trending repositories and the files can be viewed straight from the browser. An authentication system is implemented where guests can only access repositories created by users as well as the repository structure and files created in it. Alternatively, users can login into “ask-rep” and this would automatically connect to their Google account or request the necessary permissions needed. Moreover, the user would then be able to create personal repositories and customize them how they wish.

Implementation

The project is coded in Java using Google's App Engine API. This uses the client-server framework where the back-end code is created on the server and interfaces are used to access the server code. The client interfaces are then used through an “entry point” which translates Java/object-oriented code into JavaScript onto the front-end. This ensures efficient responses and minimum delays for users.

We use two toolkits provided by Google (GWT [1] and SmartGWT [2]) which are open-source software tools that help developers better manage the client-server framework. Google Web Tools (GWT) provided us with GUI components to create a structured application design through our “entry point”. This includes text boxes, HTML placeholders, panels, buttons etc…

Front-end design

We started off by creating the front-end design including HTML and CSS for our cloud application. This includes the website theme, menu structure, logo and custom buttons. A database was needed to manage and store data such as user information.

Database and instance configuration

Initially we created the database locally using MySQL and created the appropriate table structure. Our database consists of 4 tables; users, repositories, folders and files. This enabled us to create a repository-like structure to manage folders/sub-folders and files. We ensure relationships between tables are defined to keep the database secure. In order to deploy it onto the cloud we use Google Cloud SQL service.

We signed up with Google Cloud platform and automatically got assigned a personal console management. Through this console we created a project and added the Cloud SQL service. We created an instance which Google provided us with an IP address linking to the SQL database. This enabled us to connect to the Cloud SQL and import the local database through command line.

We chose Cloud SQL rather than Cloud Data-store or Storage because it allowed us to store user information linked with repositories/folders and files. Therefore, the application can display the repositories of the current user logged in. The other two alternatives only stored folders and files, and it’s not ideal of creating a repository-like application.

Login

In order to access specific application functions such as uploading and creating files, users need to sign in using their Google account. The user information is stored in the database which allows us to keep track of the files/folders/repositories they create as well as a time-stamp for each record created. Also, this permits users in having their own personal repositories in a self-managed manner.  

Users who are not linked with a Google account or don't wish to sign in can browse through Trending Repositories created by other users.

Create/Edit Repository

Once a user logs into the application he/she can create/edit personal repositories. In the menu there are two links; “Create Files” and “Upload Files”. The application will show the user a section where he/she could create or select an existing repository. This allows the user to create fresh repositories or edit an existing repository they created previously. This data is stored in the repository table along with created and updated timestamps.

Customize Repository

In the repository section users can add folders and files to organize their data properly. They can easily navigate through existing folders in the repository by clicking on a respectful folder name. This provides the user with a repository experience where he/she could create infinite folders and easily traverse back to the root.

Alternatively they can add custom folders by clicking on “Create Folder” located next to the directory listing. The user can see the path of which folders they've accessed and the application will indicate the user the one he/she is currently accessing. The created folder will then be placed in the current directory.

The database structure permits the application to store folders in a single table where each folder links to a parent folder unless it's created in the root. Once a folder is created, the timestamps are updated for its parents and its linked repository. This provides the user with continuous updated data where he/she can keep track of the folders/repositories that were last accessed.

Create/Upload/View Files

Whilst accessing a specific repository the user can be able to create a file and write their own piece of code in the application. Instead of using an external application such as “eclipse” they can create code snippets or files and store them in their own repository on the cloud. The user is provided with a code panel (text area) which includes customized tabbing so he/she can indent their code like “eclipse”.


Furthermore, if the user wishes to upload external files, he may do so by uploading single or multiple files in specific repositories. The applications restricts the user to 5 extensions (code languages) which are “.py, .js, .cs, .cpp and .java”. We used a third party library called “GWT Uploader” [3] which allows the user to upload multiple files. It enables you to handle and use specific events in turn improving the user experience.

The files in each repository are listed accordingly to where the user has created them. The user can view the file's contents by clicking on the respectful file name. They won't be able to download the file to their PC but only view it through the browser. This also applies to guest users who don't sign into the application because they can view files created by other users. 

Trending Repositories

As you enter the application, immediately it will list you all the repositories that have recently been created.  They are sorted in descending order by repository date. Guests will be able to browse through recently created repositories and view their files and folders. This provides users/guests with constantly updated information depending on how much the application is being is used.

Alternatively an authenticated user can view all repositories that are created only by other authenticated users. Since they can already view their own repositories whilst creating/uploading files we decided to remove the duplications and suggest better repositories to them.

## Search functionality

We implemented a functionality where users can be able to search for code snippets through the Internet using Google's Custom Search Engine. Whilst users are creating files, if they get stuck and don't know how to write a specific function, they can send a request to Google and retrieve the code snippet.

Hot-Button for quick search

We provided the user with the choice of 5 languages – JavaScript, Java, Python, C++, and C#. When creating files the user needs to select the language from a drop down list and insert a filename. The user can select the world/phrase and by pressing the button SHIFT a query will be sent to Google.

A website was needed in order for Google to crawl through it an extract code snippets; we chose “http://www.stackoverflow.com”. The end of the query consists of the user's highlighted phrase and chosen language followed by the name of the website.

Custom Search Engine

This is an API provided by Google, so that developers can integrate customizable search engines in each and every website as well as in on-line applications. To integrate the API we used the search engine' URL (placed at “googleapis.com/customsearch/”), the cx_key, which serves as an authenticator for the engine, the API_KEY provided by Google which acts as credentials to the cloud application, and a parameter that enforces JSON output format.

Jsoup Library

When the user's query is sent to Google, our application gathers all the results as links to different snippet web pages and stores them in an Array List. Once this process is done, the same data structure is sent to an iterator, which opens each link in order to access the html code for every web page. The iterator seeks for “<code>” tags in the HTML and when found extracts the text within and inserts it into a HashMap. Therefore, for this functionality we used an external library for HTML stripping called Jsoup [4].

Code Snippet Rating

In order to achieve plausible results for the ultimate user experience, we implemented a rating method which rates the extracted code snippets before they actually get displayed for the user. Firstly, we have predefined language code tags that serve as identifiers for each language. If the language of the extracted snippet does not match the language chosen by the user, snippet gets penalized. However, if the searchable keyword occurs more than once the rating increases by 5 points.

Displaying Code Snippets

For displaying the snippets we decided to use an “accordion” type of container that manages a list of sections of widgets, each with a header provided by SmartGWT [5]. We gather a minimum of twenty code snippets and display them next to the text area assigned for coding. Also, for each code snippet we display the URL from where its’ found.


Deployment & Results

After implementing and testing the application locally, we uploaded it onto Google Cloud platform. We deployed the application through “eclipse” by linking the application id – “future-spot-815” with the source code. Currently Google provides us with 60 days free trial so we didn’t need to choose a pricing plan for hosting. Once the trial expires, we would need to think about choosing the right plan to host the application depending if our intention is to keep it live or not.  

The end product resulted into a fully pledged cloud application. Users are able to create repositories with a proper repository structure, create or upload code files and also seek for code using Google Custom Search Engine. Google provides the code snippets and our rate algorithm chooses the best ones.  
