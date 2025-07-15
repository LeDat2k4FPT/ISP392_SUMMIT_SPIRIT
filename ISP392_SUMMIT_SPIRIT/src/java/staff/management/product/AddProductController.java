package staff.management.product;

import dao.*;
import dto.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AddProductController", urlPatterns = {"/AddProductController"})
public class AddProductController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String pName = request.getParameter("productName");
        String pDescription = request.getParameter("description");
        String pCateID_raw = request.getParameter("cateID");
        String pStatus = request.getParameter("status");

        String colorName = request.getParameter("color");
        String sizeName = request.getParameter("size");
        String price_raw = request.getParameter("price");
        String quantity_raw = request.getParameter("stock");

        String imageURL = request.getParameter("imageURL"); // ảnh sản phẩm
        if (imageURL == null || imageURL.trim().isEmpty()) {
            imageURL = "default.jpg";
        }

        try {
            int cateID = Integer.parseInt(pCateID_raw);
            double price = Double.parseDouble(price_raw.replace(",", ""));
            int quantity = Integer.parseInt(quantity_raw);

            // 1. Insert product
            ProductDTO product = new ProductDTO(0, pName, pDescription, cateID, pStatus);
            ProductDAO productDAO = new ProductDAO();
            int productID = productDAO.insertAndReturnID(product);

            if (productID != -1) {
                // 2. Get or insert color
                ColorDAO colorDAO = new ColorDAO();
                int colorID = colorDAO.getOrInsertColor(colorName);

                // 3. Get or insert size
                SizeDAO sizeDAO = new SizeDAO();
                int sizeID = sizeDAO.getOrInsertSize(sizeName);

                // 4. Insert product variant
                ProductVariantDTO variant = new ProductVariantDTO(productID, colorID, sizeID, price, quantity);
                ProductVariantDAO variantDAO = new ProductVariantDAO();
                variantDAO.insertVariant(variant);

                // 5. Insert product image
                ProductImageDTO imageDTO = new ProductImageDTO(imageURL, productID);
                ProductImageDAO imageDAO = new ProductImageDAO();
                imageDAO.insertImage(imageDTO);
            }

            response.sendRedirect("staff/productlist.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("staff/mnproduct.jsp").forward(request, response);
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
        return "Thêm sản phẩm đầy đủ với ảnh, màu, size, giá và tồn kho";
    }
}
