package staff.management.product;

import dao.ProductDAO;
import dao.ProductVariantDAO;
import dto.ProductDTO;
import dto.ProductVariantDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductListController", urlPatterns = {"/ProductListController"})
public class ProductListController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            System.out.println("[DEBUG] ProductListController START");

            ProductDAO productDAO = new ProductDAO();
            ProductVariantDAO variantDAO = new ProductVariantDAO();

            String keyword = request.getParameter("keyword");
            List<ProductDTO> productList;
            if (keyword != null && !keyword.trim().isEmpty()) {
                System.out.println("[DEBUG] Searching products by keyword: " + keyword);
                productList = productDAO.getProductsByName(keyword.trim());
            } else {
                productList = productDAO.getAllProducts();
            }

            Map<Integer, List<ProductVariantDTO>> variantMap = variantDAO.getAllVariantsGroupedByProduct();

            System.out.println("[DEBUG] Loaded " + productList.size() + " products");
            System.out.println("[DEBUG] Loaded variant map for products: " + variantMap.keySet());

            request.setAttribute("productList", productList);
            request.setAttribute("variantMap", variantMap);

            request.getRequestDispatcher("/staff/productlist.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("[ERROR] ProductListController: " + e.getMessage());
            request.setAttribute("error", "Error loading product list: " + e.getMessage());
            request.getRequestDispatcher("/staff/productlist.jsp").forward(request, response);
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
        return "Loads the product list with variants for staff management";
    }
}
