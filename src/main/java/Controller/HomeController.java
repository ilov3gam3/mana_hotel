package Controller;

import Dao.RoomTypeDao;
import Dao.UtilityDao;
import Model.RoomType;
import Model.Utility;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

public class HomeController {
    @WebServlet("/home")
    public static class Index extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/public/index.jsp").forward(req, resp);
        }
    }

    @WebServlet("/admin")
    public static class Admin extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/admin/panel.jsp").forward(req, resp);
        }
    }

    @WebServlet("/hotel")
    public static class Hotel extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.getRequestDispatcher("/views/hotel/panel.jsp").forward(req, resp);
        }
    }
    @WebServlet("/search")
    public static class Search extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String query = req.getParameter("query");
            String[] utility_ids = req.getParameterValues("utility_ids");
            String price_from = req.getParameter("price_from");
            String price_to = req.getParameter("price_to");
            String sort = req.getParameter("sort");
            ArrayList<Utility> utilities = UtilityDao.getAllUtilities();
            req.setAttribute("utilities", utilities);
            ArrayList<RoomType> roomTypes;
            if (query == null && utility_ids == null && price_from == null && price_to == null && sort == null) {
                roomTypes = new ArrayList<>();
            } else {
                roomTypes = RoomTypeDao.search(query, utility_ids, price_from, price_to, sort);
            }
            req.setAttribute("roomTypes", roomTypes);
            req.getRequestDispatcher("/views/public/search.jsp").forward(req, resp);
        }
    }
}
