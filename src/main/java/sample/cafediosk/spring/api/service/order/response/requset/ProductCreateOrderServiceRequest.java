package sample.cafediosk.spring.api.service.order.response.requset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafediosk.spring.domain.product.Product;
import sample.cafediosk.spring.domain.product.ProductSellingStatus;
import sample.cafediosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateOrderServiceRequest {

    private ProductType type;

    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    private ProductCreateOrderServiceRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

}
