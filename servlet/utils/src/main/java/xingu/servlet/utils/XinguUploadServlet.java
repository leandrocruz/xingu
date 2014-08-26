package xingu.servlet.utils;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

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
		try
		{
			String result = process(request, response);
			response.addHeader("Content-Type:", "text/html; charset=utf8");
			ServletOutputStream os = response.getOutputStream();
			IOUtils.write(result, os);
			os.close();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}

	private String process(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		InputStream  is   = Thread.currentThread().getContextClassLoader().getResourceAsStream("form.html");
		String       form = IOUtils.toString(is);
		StringBuffer sb   = new StringBuffer(form);
		
		sb.append("<h4>Headers</h4>");
		Enumeration<String> names = request.getHeaderNames();
		while(names.hasMoreElements())
		{
			String name = names.nextElement();
			Enumeration<String> headers = request.getHeaders(name);
			while(headers.hasMoreElements())
			{
				String header = (String) headers.nextElement();
				sb.append(name).append(" = [").append(header).append("]<br/>\n");
			}
		}

		sb.append("<h4>Info</h4>\n");
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		String  charset     = request.getCharacterEncoding();
		sb.append("Charset: ").append(charset).append("<br/>\n");
		sb.append("isMultipart: ").append(isMultipart).append("<br/>\n");

		Map<String, String[]> params = request.getParameterMap();
		sb.append("<h4>Params</h4>\n");
		for(String name : params.keySet())
		{
			String[] values = params.get(name);
			for(String value : values)
			{
				sb.append(name).append(" = [").append(value).append("]<br/>\n");
			}
		}
		
		if(isMultipart)
		{
			sb.append("<h4>Upload</h4>\n");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(max);
			factory.setRepository(repo);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(max);

			List<FileItem> fileItems = upload.parseRequest(request);
			for(FileItem fi : fileItems)
			{
				boolean formField = fi.isFormField();
				sb.append(fi.getFieldName()).append(" = [");
				String value;
				if(formField)
				{
					value = fi.getString(charset != null ? charset : "ISO-8859-1");
				}
				else
				{
					value = fi.getFieldName() + " (" + fi.getSize() + ")";
				}
				sb.append(value).append("]<br/>\n");
			}
		}
		
		return sb.toString();
	}
}