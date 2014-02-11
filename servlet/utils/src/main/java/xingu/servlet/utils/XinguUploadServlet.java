package xingu.servlet.utils;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

public class XinguUploadServlet
	extends HttpServlet
{
	File	repo	= new File("/tmp");

	int		max		= 10000000;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException 
	{
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, java.io.IOException 
	{
		System.out.println("CHARSET> " + request.getCharacterEncoding());
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if(!isMultipart)
		{
			ServletOutputStream os = response.getOutputStream();
			IOUtils.write("Not a file upload", os);
			os.close();
			return;
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(max);
		factory.setRepository(repo);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(max);

		try
		{
			List<FileItem> fileItems = upload.parseRequest(request);
			for(FileItem fi : fileItems)
			{
				boolean formField = fi.isFormField();
				if(formField)
				{
					System.out.println(fi.getFieldName() + "=" + fi.getString("utf-8"));
				}
				else
				{
					System.out.println(fi.getFieldName() + " (" + fi.getSize()+")");
				}
			}
		}
		catch(FileUploadException e)
		{
			e.printStackTrace();
		}
	}
}