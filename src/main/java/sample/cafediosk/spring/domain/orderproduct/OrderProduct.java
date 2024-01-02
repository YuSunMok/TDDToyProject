package sample.cafediosk.spring.domain.orderproduct;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafediosk.spring.domain.BaseEntity;
import sample.cafediosk.spring.domain.order.Order;
import sample.cafediosk.spring.domain.product.Product;

import static jakarta.persistence.FetchType.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Order order;

    @ManyToOne(fetch = LAZY)
    private Product product;

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }
}
