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

        try {
            String pName = request.getParameter("productName");
            String pDescription = request.getParameter("description");
            String pCateID_raw = request.getParameter("cateID");
            String pStatus = request.getParameter("status");

            String colorName = request.getParameter("color");
            String sizeName = request.getParameter("size");
            String price_raw = request.getParameter("price");
            String quantity_raw = request.getParameter("stock");

            String imageURL = request.getParameter("imageURL");
            Part imageFile = null;
            try {
                imageFile = request.getPart("imageFile");
            } catch (Exception ex) {
                System.out.println("[WARN] No image file uploaded");
            }

            System.out.println("[DEBUG] START AddProductController");
            System.out.println("[DEBUG] Data: Name=" + pName + ", Desc=" + pDescription +
                    ", CateID=" + pCateID_raw + ", Status=" + pStatus +
                    ", Color=" + colorName + ", Size=" + sizeName +
                    ", Price=" + price_raw + ", Stock=" + quantity_raw);

            if (pName == null || pDescription == null || pCateID_raw == null ||
                    colorName == null || sizeName == null || price_raw == null || quantity_raw == null) {
                throw new Exception("Missing required fields");
            }

            int cateID = Integer.parseInt(pCateID_raw);
            double price = Double.parseDouble(price_raw.replace(",", ""));
            int quantity = Integer.parseInt(quantity_raw);

            // Insert Product
            ProductDTO product = new ProductDTO(0, pName, pDescription, cateID, pStatus);
            ProductDAO productDAO = new ProductDAO();
            int productID = productDAO.insertAndReturnID(product);
            if (productID <= 0) throw new Exception("Failed to insert Product");
            System.out.println("[DEBUG] Inserted ProductID = " + productID);

            // Insert Color & Size
            ColorDAO colorDAO = new ColorDAO();
            SizeDAO sizeDAO = new SizeDAO();
            int colorID = colorDAO.getOrInsertColor(colorName.trim());
            int sizeID = sizeDAO.getOrInsertSize(sizeName.trim());
            System.out.println("[DEBUG] ColorID=" + colorID + ", SizeID=" + sizeID);

            // Insert Variant
            ProductVariantDTO variant = new ProductVariantDTO(productID, colorID, sizeID, price, quantity);
            ProductVariantDAO variantDAO = new ProductVariantDAO();
            variantDAO.insertVariant(variant);
            System.out.println("[DEBUG] Inserted variant for ProductID=" + productID);

            // Insert Image
            ProductImageDAO imageDAO = new ProductImageDAO();
            String imgToSave = null;
            if (imageFile != null && imageFile.getSize() > 0) {
                String fileName = java.nio.file.Paths.get(imageFile.getSubmittedFileName()).getFileName().toString();
                String path = getServletContext().getRealPath("/images/" + fileName);
                imageFile.write(path);
                imgToSave = fileName;
                System.out.println("[DEBUG] Saved image file = " + fileName);
            } else if (imageURL != null && !imageURL.trim().isEmpty()) {
                imgToSave = imageURL.trim();
                System.out.println("[DEBUG] Used image URL = " + imgToSave);
            }

            if (imgToSave != null) {
                ProductImageDTO imageDTO = new ProductImageDTO(imgToSave, productID);
                imageDAO.insertImage(imageDTO);
            }

            System.out.println("[DEBUG] END AddProductController SUCCESS");
            response.sendRedirect(request.getContextPath() + "/ProductListController?msg=" 
                + URLEncoder.encode("Product added successfully", "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ERROR] AddProductController: " + e.getMessage());
            request.setAttribute("error", "Error adding product: " + e.getMessage());
            request.getRequestDispatcher("/staff/mnproduct.jsp").forward(request, response);
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
        return "Add new product with variant, image, color, size";
    }
}
