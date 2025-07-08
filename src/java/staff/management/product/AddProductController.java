package staff.management.product;

import dao.*;
import dto.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "AddProductController", urlPatterns = {"/AddProductController"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB
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
        Part imageFile = null;
        try { imageFile = request.getPart("imageFile"); } catch(Exception ex) { imageFile = null; }

        try {
            int cateID = -1;
            if ("other".equals(pCateID_raw)) {
                String otherCate = request.getParameter("otherCategory");
                CategoryDAO categoryDAO = new CategoryDAO();
                java.util.List<CategoryDTO> allCate = categoryDAO.getAllCategories();
                boolean exists = false;
                for (CategoryDTO c : allCate) {
                    if (c.getCateName().equalsIgnoreCase(otherCate)) {
                        cateID = c.getCateID();
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    cateID = allCate.stream().mapToInt(CategoryDTO::getCateID).max().orElse(0) + 1;
                    categoryDAO.createCategory(new CategoryDTO(cateID, otherCate));
                }
            } else {
                cateID = Integer.parseInt(pCateID_raw);
            }
            if (cateID == -1) throw new Exception("Invalid category");
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
                String imgToSave = imageURL;
                if (imageFile != null && imageFile.getSize() > 0) {
                    String fileName = java.nio.file.Paths.get(imageFile.getSubmittedFileName()).getFileName().toString();
                    String path = getServletContext().getRealPath("/images/" + fileName);
                    imageFile.write(path);
                    imgToSave = fileName;
                }
                ProductImageDTO imageDTO = new ProductImageDTO(imgToSave, productID);
                ProductImageDAO imageDAO = new ProductImageDAO();
                imageDAO.insertImage(imageDTO);
            }
            String message = URLEncoder.encode("Add product successfully!", "UTF-8");
            response.sendRedirect("staffDashboard.jsp?page=staff/productlist.jsp&msg=" + message + "&type=success");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("staff/productlist.jsp").forward(request, response);
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
