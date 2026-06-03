package com.adil.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reporturl")
public class StudentReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String GET_ALL_STUDENT = "select * from student_details";

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get PrintWriter
		PrintWriter out = response.getWriter();
		// set Response Content type
		response.setContentType("text/html");
		// load jdbc driver class
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException cnf) {
			cnf.printStackTrace();
		}
		// Write jdbc code for get Data from data table
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "password");
				PreparedStatement ps = con.prepareStatement(GET_ALL_STUDENT);
				ResultSet rs = ps.executeQuery();) {
			// process the result and display the context as html table
			out.println("<table border='1' align='center' bgcolor='cyan'>");

			out.println("<tr>" + "<th>s_id</th>" + "<th>sname</th>" + "<th>saddress</th>" + "<th>resume</th>"
					+ "<th>photo</th>" + "<th>Resume Download</th>" + "<th>Photo Download</th>" + "</tr>");

			while (rs.next()) {

				out.println("<tr>");

				out.println("<td>" + rs.getInt(1) + "</td>");

				out.println("<td>" + rs.getString(2) + "</td>");

				out.println("<td>" + rs.getString(3) + "</td>");

				out.println("<td>" + rs.getString(4) + "</td>");

				out.println("<td>" + rs.getString(5) + "</td>");

				out.println(
						"<td>" + "<a href='downloadurl?resumeId=" + rs.getInt(1) + "'>Download Resume</a>" + "</td>");

				out.println("<td>" + "<a href='downloadurl?photoId=" + rs.getInt(1) + "'>Download Photo</a>" + "</td>");

				out.println("</tr>");
			}

			out.println("</table>");

			// Home hyperlink
			out.println("<br><a href='index.html'>Home</a>");
			// close stream
			out.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}
}
