package ra.java_service_15.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.java_service_15.model.dto.request.ProductRequest;
import ra.java_service_15.model.entity.Category;
import ra.java_service_15.model.entity.Product;
import ra.java_service_15.repository.CategoryRepository;
import ra.java_service_15.repository.ProductRepository;
import ra.java_service_15.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductRequest> findProductByName(String name) {
        return productRepository.findProductsByProductName(name)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductRequest> findAllProduct() {
        return productRepository.findAll()
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductRequest findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        return mapToResponse(product);
    }

    @Override
    public ProductRequest saveProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        Product product = Product.builder()
                .productName(request.getProductName())
                .producer(request.getProducer())
                .yearMaking(request.getYearMaking())
                .expireDate(request.getExpireDate())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(category)
                .build();

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductRequest updateProduct(ProductRequest request, Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        existing.setProductName(request.getProductName());
        existing.setProducer(request.getProducer());
        existing.setYearMaking(request.getYearMaking());
        existing.setExpireDate(request.getExpireDate());
        existing.setPrice(request.getPrice());
        existing.setQuantity(request.getQuantity());
        existing.setCategory(category);

        return mapToResponse(productRepository.save(existing));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        productRepository.delete(product);
    }

    private ProductRequest mapToResponse(Product product) {
        return ProductRequest.builder()
                .id(product.getProductId())
                .productName(product.getProductName())
                .producer(product.getProducer())
                .yearMaking(product.getYearMaking())
                .expireDate(product.getExpireDate())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .categoryId(product.getCategory().getCateId())
                .categoryName(product.getCategory().getCateName())
                .build();
    }
}

