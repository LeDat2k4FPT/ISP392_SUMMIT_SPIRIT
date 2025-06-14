package controllers;

import dao.ProductDAO;
import dto.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ViewProductController", urlPatterns = {"/ViewProductController"})
public class ViewProductController extends HttpServlet {

    
    private static final Map<String, Integer> CATEGORY_MAP = new HashMap<>();
    static {
        CATEGORY_MAP.put("ao", 2);      
        CATEGORY_MAP.put("quan", 1);     
        CATEGORY_MAP.put("balo", 3);     
        CATEGORY_MAP.put("dungcu", 4);   
        CATEGORY_MAP.put("trai", 5);     
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "homepage.jsp"; 
        try {
            String category = request.getParameter("category");
            String sort = request.getParameter("sort");
            String sortOrder = sort != null ? sort : "asc";

            ProductDAO dao = new ProductDAO();

            if (category == null || !CATEGORY_MAP.containsKey(category.toLowerCase())) {
                
                int[] defaultIDs = {1, 2, 3};
                List<ProductDTO> products = dao.getTopNProductsByIDs(defaultIDs);
                request.setAttribute("PRODUCT_LIST", products);
            } else {
                
                int cateID = CATEGORY_MAP.get(category.toLowerCase());
                List<ProductDTO> products = dao.getProductsByCategorySorted(cateID, sortOrder);
                request.setAttribute("PRODUCT_LIST", products);
                request.setAttribute("CURRENT_CATEGORY", category);
                request.setAttribute("SORT_ORDER", sortOrder);
            }

        } catch (Exception e) {
            log("Error at ViewProductController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Load product list by category and sort, or default 3 items if no category provided";
    }
}
