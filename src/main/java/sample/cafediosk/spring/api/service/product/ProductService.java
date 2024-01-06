package sample.cafediosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafediosk.spring.api.controller.product.ProductController;
import sample.cafediosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafediosk.spring.api.service.product.response.ProductResponse;
import sample.cafediosk.spring.domain.product.Product;
import sample.cafediosk.spring.domain.product.ProductRepository;
import sample.cafediosk.spring.domain.product.ProductSellingStatus;
import sample.cafediosk.spring.domain.product.ProductType;

import java.util.List;
import java.util.stream.Collectors;

import static sample.cafediosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafediosk.spring.domain.product.ProductType.*;

/**
 * readOnly = true : 읽기전용
 * CRUD 에서 CUD 동작 X / only Read
 * JPA : CUD 스냅샷 저장, 변경감지 X (성능 향상)
 *
 * CQRS - Command / Read
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        // nextProductNumber
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(createNextProductNumber());
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProduct();
        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
