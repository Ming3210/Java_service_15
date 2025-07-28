package ra.java_service_15.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.java_service_15.model.dto.request.ProductRequest;
import ra.java_service_15.model.dto.response.APIResponse;
import ra.java_service_15.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Lấy tất cả sản phẩm
    @GetMapping
    public ResponseEntity<APIResponse<List<ProductRequest>>> getAllProducts() {
        return ResponseEntity.ok(
                new APIResponse<>(true,
                        "Lấy danh sách sản phẩm thành công",
                        productService.findAllProduct(),
                        HttpStatus.OK)
        );
    }

    // Lấy sản phẩm theo ID
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ProductRequest>> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new APIResponse<>(true,
                        "Lấy sản phẩm thành công",
                        productService.findProductById(id),
                        HttpStatus.OK)
        );
    }

    // Tìm sản phẩm theo tên
    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<ProductRequest>>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(
                new APIResponse<>(true,
                        "Tìm kiếm sản phẩm thành công",
                        productService.findProductByName(name),
                        HttpStatus.OK)
        );
    }

    // Thêm sản phẩm mới
    @PostMapping
    public ResponseEntity<APIResponse<ProductRequest>> createProduct(@RequestBody ProductRequest request) {
        return new ResponseEntity<>(
                new APIResponse<>(true,
                        "Tạo sản phẩm thành công",
                        productService.saveProduct(request),
                        HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ProductRequest>> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(
                new APIResponse<>(true,
                        "Cập nhật sản phẩm thành công",
                        productService.updateProduct(request, id),
                        HttpStatus.OK)
        );
    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                new APIResponse<>(true,
                        "Xóa sản phẩm thành công",
                        null,
                        HttpStatus.OK)
        );
    }
}
