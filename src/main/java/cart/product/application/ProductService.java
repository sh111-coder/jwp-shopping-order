package cart.product.application;

import java.util.List;
import java.util.stream.Collectors;

import cart.product.application.dto.ProductRequest;
import cart.product.application.dto.ProductResponse;
import cart.product.domain.Product;
import cart.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Product가 존재하지 않습니다."));
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = new Product.Builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .imageUrl(productRequest.getImageUrl())
                .build();
        Product savedProduct = productRepository.save(product);
        System.out.println("savedProduct = " + savedProduct.getId());
        return savedProduct.getId();
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Product가 존재하지 않습니다."));
        findProduct.changeName(productRequest.getName());
        findProduct.changePrice(productRequest.getPrice());
        findProduct.changeImageUrl(productRequest.getImageUrl());
    }

    public void deleteProduct(Long productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 Product가 존재하지 않습니다."));
        productRepository.delete(findProduct);
    }
}
