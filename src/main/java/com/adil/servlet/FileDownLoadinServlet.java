package com.adil.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

import java.io.OutputStream;
import java.io.InputStream;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/downloadurl")
public class FileDownLoadinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String GET_PHOTO_FILE_NAME_QUERY = "select photo_fileName from student_details where id=?";
	private static final String GET_RESUME_FILE_NAME_QUERY = "select resume_fileName from student_details where id=?";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String query = null;

		int sId = 0;
		// check which download hyperlink is clicked (photo or resume)
		if (request.getParameter("resumeId") != null) {
			// Resume download
			query = GET_RESUME_FILE_NAME_QUERY;
			sId = Integer.parseInt(request.getParameter("resumeId"));

		} else if (request.getParameter("photoId") != null) {
			// Photo download
			query = GET_PHOTO_FILE_NAME_QUERY;
			sId = Integer.parseInt(request.getParameter("photoId"));
		}

		// Load JDBC driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		}
		String fileName = null;
		// Write JDBC code to get file name from database table based on id
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "password");
				PreparedStatement ps = con.prepareStatement(query);) {
			// set value to query parameter
			ps.setInt(1, sId);
			// execute the query
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					fileName = rs.getString(1);
				}
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get Length of the file and make it response content length
		File file = new File(fileName);
		response.setContentLengthLong(file.length());
		// get file content type(MIME T) and make it as response content type
		ServletContext context = getServletContext();
		String mimeType = context.getMimeType(fileName);
		mimeType = mimeType != null ? "application/octet-stream" : mimeType;
		response.setContentType(mimeType);
		// Give instruction to browser to download the file
		response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
		// create input stream pointing to file and write file content to response
		InputStream is = new FileInputStream(fileName);
		// create output stream pointing to response object
		OutputStream os = response.getOutputStream();// byte stream taken for both text and binary file
		// copy file content to response object
		IOUtils.copy(is, os);
		// close stream
		os.close();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
