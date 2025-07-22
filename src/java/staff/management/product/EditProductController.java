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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            ProductDAO dao = new ProductDAO();

            ProductDTO product = dao.getProductByID(productID);

            if (product != null) {
                request.getSession().setAttribute("PRODUCT", product);

                // Redirect thay vì forward
                request.setAttribute("page", "staff/editproduct.jsp");
                request.getSession().setAttribute("PRODUCT", product);
                request.getRequestDispatcher("staffDashboard.jsp").forward(request, response);

            } else {
                response.sendRedirect("staffDashboard.jsp?page=staff/productlist.jsp&msg=Product+not+found&type=danger");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staffDashboard.jsp?page=staff/productlist.jsp&msg=Error+loading+product&type=danger");
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
