package com.adil.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Vector;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/uploadurl")
@MultipartConfig
public class StudentRegistrationServlet extends HttpServlet {
	private static final String INSER_QUERY = "Insert into student_details values(?,?,?,?,?)";

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		// set Response content type
		response.setContentType("text/html");

		// read special request object
		try {
             
			int id = Integer.parseInt(request.getParameter("sid"));
			
			String sname = request.getParameter("sname");

			String address = request.getParameter("saddr");

			Part sresume = request.getPart("resume");

			String resume = sresume.getSubmittedFileName();

			Part sphoto = request.getPart("photo");

			String photo = sphoto.getSubmittedFileName();

			String uploadPath = "C:/adil/";
			// Create full path
			String resumeFullPath =
			        uploadPath + resume;
			System.out.println(resumeFullPath);

			String photoFullPath =
			        uploadPath + photo;
			System.out.println(resumeFullPath);

			// Photo upload
			InputStream photoInputStream = sphoto.getInputStream();

			Path photoPath = Paths.get(uploadPath, photo);

			Files.copy(photoInputStream, photoPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

			// Resume upload
			InputStream resumeInputStream = sresume.getInputStream();

			Path resumePath = Paths.get(uploadPath, resume);

			Files.copy(resumeInputStream, resumePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

			out.println("<h1>File uploaded successfully</h1>");
			
			// get history
		
			// write jdbc code to save form data and file data in table.

			// Load jdbc driver class.
			Class.forName("com.mysql.cj.jdbc.Driver");

			// stablish the connection
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "password");
			// create Prepared statament object
			  PreparedStatement ps = con.prepareStatement(INSER_QUERY);
			  // set values in ps
			   ps.setInt(1, id);
			   ps.setString(2, sname);
			   ps.setString(3, address);
			   ps.setString(4,resumeFullPath);
			   ps.setString(5, photoFullPath);
			  
			   int count = ps.executeUpdate();
			  
			   if(count>0) {
				   
				   out.println("<h1>Student details insert successfully</h1>");
			   }else {
				   
				   out.println("<h1>Student details not inserted</h1>");
			   }
			   //close resources
			   ps.close();
			   con.close();

		} catch (Exception e) {

			e.printStackTrace();
			out.println("<h1>Folder not search");
		}

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}
}
