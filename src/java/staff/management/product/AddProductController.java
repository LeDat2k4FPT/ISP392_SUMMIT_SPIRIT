package staff.management.product;

import dao.ProductDAO;
import dao.ProductImageDAO;
import dao.ProductAttributeDAO;
import dto.ProductDTO;
import dto.ProductImageDTO;
import dto.ProductAttributeDTO;

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
        String pPrice_raw = request.getParameter("price");
        String pStatus = request.getParameter("status");

        String size = request.getParameter("size");
        String color = request.getParameter("color");
        String stock_raw = request.getParameter("stock");

        String[] pImageURLs = request.getParameterValues("productImage"); // danh sách URL ảnh

        try {
            int cateID = Integer.parseInt(pCateID_raw);
            double price = Double.parseDouble(pPrice_raw.replace(",", ""));
            int stock = Integer.parseInt(stock_raw);

            //1. Tạo đối tượng ProductDTO
            ProductDTO product = new ProductDTO(
                    0, // ID sẽ được sinh
                    pName,
                    null, // không cần productImage (ảnh lưu riêng)
                    pDescription,
                    null, // size nằm trong ProductAttribute
                    price,
                    pStatus,
                    0, // stock nằm trong ProductAttribute
                    cateID
            );

            // 2. Thêm sản phẩm → lấy ProductID
            ProductDAO productDAO = new ProductDAO();
            int newProductID = productDAO.insertAndReturnID(product);

            if (newProductID != -1) {
                //3. Thêm ProductAttribute
                ProductAttributeDTO attr = new ProductAttributeDTO(newProductID, color, size, stock);
                ProductAttributeDAO attrDAO = new ProductAttributeDAO();
                attrDAO.insertAttribute(attr);

                //4. Thêm ảnh nếu có
                if (pImageURLs != null) {
                    ProductImageDAO imageDAO = new ProductImageDAO();
                    for (String imgUrl : pImageURLs) {
                        if (imgUrl != null && !imgUrl.trim().isEmpty()) {
                            ProductImageDTO image = new ProductImageDTO(imgUrl.trim(), newProductID);
                            imageDAO.insertImage(image);
                        }
                    }
                }
            }

            // ✅ 5. Thành công → chuyển trang
            response.sendRedirect("staff/productlist.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("dashboard/mnproduct.jsp").forward(request, response);
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
        return "Thêm sản phẩm đầy đủ với thuộc tính và ảnh";
    }
}
