/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DAO;
import model.DataSourceFactory;

/**
 *
 * @author bonne
 */
@WebServlet(name = "deleteCommande", urlPatterns = {"/deleteCommande"})
public class DeleteCommandeServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		// Créér le DAO avec sa source de données
		DAO dao = new DAO(DataSourceFactory.getDataSource());
		String ordernum = request.getParameter("ordernum");
		String message;
		try {
			int count = dao.supprimeCommande(Integer.parseInt(ordernum));
			// Générer du JSON
			if (count == 1) {
				message = String.format("Commande %s supprimé", ordernum);
			} else {
				message = String.format("Commande %s inconnue", ordernum);
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			message = String.format("Impossible de supprimer cette commande", ordernum);
		}
		catch (SQLException ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			message = ex.getMessage();
		}
		
		Properties resultat = new Properties();
		resultat.put("message", message);
		
		try (PrintWriter out = response.getWriter()) {
			// On spécifie que la servlet va générer du JSON
			response.setContentType("application/json;charset=UTF-8");
			// Générer du JSON
			Gson gson = new Gson();
			out.println(gson.toJson(resultat));
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
