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
import utils.DBUtils;

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
            Part filePart = request.getPart("image");

            ProductDTO product = new ProductDTO();
            product.setProductID(productID);
            product.setProductName(productName);
            product.setDescription(description);
            product.setStatus(status);
            product.setCateID(cateID);

            ProductDAO dao = new ProductDAO();
            boolean updated = dao.updateProductByID(product);

            // Đồng bộ variant: xóa các variant không còn trên form
            ProductVariantDAO variantDAO = new ProductVariantDAO();
            int variantCount = Integer.parseInt(request.getParameter("variantCount"));
            java.util.Set<Integer> variantIdsOnForm = new java.util.HashSet<>();
            for (int i = 0; i < variantCount; i++) {
                int attributeID = Integer.parseInt(request.getParameter("variantId_" + i));
                String sizeName = request.getParameter("size_" + i);
                String colorName = request.getParameter("color_" + i);
                int qty = Integer.parseInt(request.getParameter("quantity_" + i));
                double prc = Double.parseDouble(request.getParameter("price_" + i));
                int sizeID = new dao.SizeDAO().getOrInsertSize(sizeName);
                int colorID = new dao.ColorDAO().getOrInsertColor(colorName);
                ProductVariantDTO variant = new ProductVariantDTO(attributeID, productID, colorID, sizeID, prc, qty);
                variantDAO.updateVariant(variant);
                if (attributeID > 0) variantIdsOnForm.add(attributeID);
            }
            // Xóa các variant không còn trên form
            java.util.List<ProductVariantDTO> dbVariants = variantDAO.getVariantsByProductId(productID);
            for (ProductVariantDTO v : dbVariants) {
                if (!variantIdsOnForm.contains(v.getAttributeID())) {
                    String sql = "DELETE FROM ProductVariant WHERE AttributeID = ? AND ProductID = ?";
                    try (java.sql.Connection conn = utils.DBUtils.getConnection();
                         java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, v.getAttributeID());
                        ps.setInt(2, productID);
                        ps.executeUpdate();
                    }
                }
            }

            // Update image
            String imageUrl = request.getParameter("imageUrl");
            ProductImageDAO imageDAO = new ProductImageDAO();
            if ((filePart != null && filePart.getSize() > 0) || (imageUrl != null && !imageUrl.trim().isEmpty())) {
                imageDAO.deleteByProductID(productID);
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String path = getServletContext().getRealPath("/images/" + fileName);
                    filePart.write(path);
                    imageDAO.insertImage(new ProductImageDTO(fileName, productID));
                } else if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    imageDAO.insertImage(new ProductImageDTO(imageUrl.trim(), productID));
                }
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
