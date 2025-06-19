package staff.management.product;

import dao.*;
import dto.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    // 1. Insert product - khởi tạo trước với ID = 0 (vì tự tăng)
    List<ProductVariantDTO> variants = new ArrayList<>();

    // 2. Get or insert color
    ColorDAO colorDAO = new ColorDAO();
    int colorID = colorDAO.getOrInsertColor(colorName);

    // 3. Get or insert size
    SizeDAO sizeDAO = new SizeDAO();
    int sizeID = sizeDAO.getOrInsertSize(sizeName);

    // 4. Tạo biến thể đầu tiên và thêm vào danh sách
    ProductVariantDTO variant = new ProductVariantDTO(0, colorID, sizeID, price, quantity);
    variants.add(variant);

    // 5. Mô tả biến thể ngắn gọn (hiển thị)
    String variantDisplay = sizeName + " - " + colorName;

    // 6. Khởi tạo sản phẩm với constructor đầy đủ
    ProductDTO product = new ProductDTO(
        0,                   // productID = 0 (DB tự tăng)
        pName,
        pDescription,
        pStatus,
        cateID,
        imageURL,
        price,
        variantDisplay,
        variants
    );

    // 7. Insert vào DB
    ProductDAO productDAO = new ProductDAO();
    int productID = productDAO.insertAndReturnID(product); // bạn chỉ cần truyền các trường chính trong DAO

    if (productID != -1) {
        // 8. Insert product variant với productID thực tế
        variant.setProductID(productID);
        ProductVariantDAO variantDAO = new ProductVariantDAO();
        variantDAO.insertVariant(variant);

        // 9. Insert ảnh sản phẩm
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
