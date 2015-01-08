package ask_rep.server;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class FileUploadServlet extends HttpServlet {

  private static final Logger log =
		      Logger.getLogger(FileUploadServlet.class.getName());
  
  private FileServiceImpl fileService = new FileServiceImpl();

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

	  try {
	      ServletFileUpload upload = new ServletFileUpload();
	      res.setContentType("text/plain");

	      FileItemIterator iterator = upload.getItemIterator(req);
	      while (iterator.hasNext()) {
	        FileItemStream item = iterator.next();
	        InputStream stream = item.openStream();

	        if (item.isFormField()) {
	          log.warning("Got a form field: " + item.getFieldName());
	        } else {
	          log.warning("Got an uploaded file: " + item.getFieldName() +
	                      ", name = " + item.getName());

	          int len;
	          byte[] buffer = new byte[8192];
	          while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
	            res.getOutputStream().write(buffer, 0, len);
	          }
	          
	          String extension = "." + FilenameUtils.getExtension(item.getName());
	          String filename = item.getName().replace(extension, "");
	          
	          int folderID = Integer.parseInt(req.getParameter("folderID"));
	          int repositoryID = Integer.parseInt(req.getParameter("repositoryID"));

		  	  try {
		  			String decodedDataUsingUTF8 = new String(buffer, "UTF-8");  // Best way to decode using "UTF-8" 
		  			fileService.insertFile(filename, extension, decodedDataUsingUTF8, folderID, repositoryID);
	          
		  	  } catch (UnsupportedEncodingException e) {
		  			e.printStackTrace();
		  	  }
	        }
	      }
	    } catch (Exception ex) {
	      throw new ServletException(ex);
	    }
  }

}