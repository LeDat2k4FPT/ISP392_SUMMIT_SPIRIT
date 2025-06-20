package staff.management.product;

import dao.ProductDAO;
import dto.ProductDTO;
import dao.ProductVariantDAO;
import dao.ProductImageDAO;
import dto.ProductVariantDTO;
import dto.ProductImageDTO;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "UpdateProductController", urlPatterns = {"/UpdateProductController"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB

public class UpdateProductController extends HttpServlet {

    private static final String SUCCESS = "staff/productlist.jsp";
    private static final String ERROR = "staff/editproduct.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR;
        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            String productName = request.getParameter("productName");
            String description = request.getParameter("description");
            String status = request.getParameter("status");
            int cateID = Integer.parseInt(request.getParameter("cateID"));
            double price = Double.parseDouble(request.getParameter("price"));
            int colorID = Integer.parseInt(request.getParameter("colorID"));
            int sizeID = Integer.parseInt(request.getParameter("sizeID"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            Part filePart = request.getPart("image");

            ProductDTO product = new ProductDTO();
            product.setProductID(productID);
            product.setProductName(productName);
            product.setDescription(description);
            product.setStatus(status);
            product.setCateID(cateID);

            ProductDAO dao = new ProductDAO();
            boolean updated = dao.updateProductByID(product);

            // Update variant
            ProductVariantDAO variantDAO = new ProductVariantDAO();
            variantDAO.deleteByProductID(productID);
            ProductVariantDTO variant = new ProductVariantDTO(0, productID, colorID, sizeID, price, quantity);
            variantDAO.insertVariant(variant);

            // Update image
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String path = getServletContext().getRealPath("/images/" + fileName);
                filePart.write(path);
                ProductImageDAO imageDAO = new ProductImageDAO();
                imageDAO.deleteByProductID(productID);
                imageDAO.insertImage(new ProductImageDTO(fileName, productID));
            }

            if (updated) {
                url = SUCCESS;
            } else {
                request.setAttribute("error", "Cập nhật sản phẩm thất bại!");
            }
        } catch (Exception e) {
            log("Error at UpdateProductController: " + e.getMessage());
            request.setAttribute("error", "Lỗi: " + e.getMessage());
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
        return "Cập nhật thông tin sản phẩm";
    }
}
