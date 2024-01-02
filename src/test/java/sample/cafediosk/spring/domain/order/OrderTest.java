
package sample.cafediosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafediosk.spring.domain.product.Product;
import sample.cafediosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafediosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafediosk.spring.domain.product.ProductType.*;

class OrderTest {

    @Test
    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    public void calculateTotalPrice() {
        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());
        // then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @Test
    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    public void init() {
        // given
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @Test
    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    public void registeredDateTime() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = List.of(
                createProduct("001", 1000),
                createProduct("002", 2000)
        );

        // when
        Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .price(price)
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

}