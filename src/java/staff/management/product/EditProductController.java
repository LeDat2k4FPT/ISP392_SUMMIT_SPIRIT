package staff.management.product;

import dao.ProductDAO;
import dto.ProductDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EditProductController", urlPatterns = {"/EditProductController"})
public class EditProductController extends HttpServlet {
    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "staff/editproduct.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR;

        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            ProductDAO dao = new ProductDAO();
            ProductDTO product = dao.getFullProductByID(productID);

            if (product != null) {
                request.setAttribute("PRODUCT", product);
                url = SUCCESS;
            } else {
                request.setAttribute("error", "Product not found!");
            }
        } catch (Exception e) {
            log("Error at EditProductController: " + e.getMessage());
            request.setAttribute("error", "Error: " + e.getMessage());
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
}