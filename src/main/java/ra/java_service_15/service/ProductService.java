package ra.java_service_15.service;

import ra.java_service_15.model.dto.request.ProductRequest;


import java.util.List;

public interface ProductService {
    List<ProductRequest> findProductByName(String name);
    List<ProductRequest> findAllProduct();
    ProductRequest findProductById(Long id);
    ProductRequest saveProduct(ProductRequest productRequest);
    ProductRequest updateProduct(ProductRequest productRequest, Long id);
    void deleteProduct(Long id);
}
