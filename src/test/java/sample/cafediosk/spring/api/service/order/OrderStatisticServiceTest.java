package sample.cafediosk.spring.api.service.order;

import org.aspectj.weaver.ast.Or;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import sample.cafediosk.spring.client.mail.MailSendClient;
import sample.cafediosk.spring.domain.history.mail.MailSendHistory;
import sample.cafediosk.spring.domain.history.mail.MailSendHistoryRepository;
import sample.cafediosk.spring.domain.order.Order;
import sample.cafediosk.spring.domain.order.OrderRepository;
import sample.cafediosk.spring.domain.order.OrderStatus;
import sample.cafediosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafediosk.spring.domain.product.Product;
import sample.cafediosk.spring.domain.product.ProductRepository;
import sample.cafediosk.spring.domain.product.ProductSellingStatus;
import sample.cafediosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static sample.cafediosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafediosk.spring.domain.product.ProductType.*;

@SpringBootTest
class OrderStatisticServiceTest {

    @Autowired
    private OrderStatisticService orderStatisticService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    public void sendOrderStatisticMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 3, 5, 00, 0);

        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 3, 4, 23, 59));
        Order order2 = createPaymentCompletedOrder(products, now);
        Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 3, 5, 23, 59));
        Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 3, 6, 0, 0));

        when(mailSendClient.sendMail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStatisticService.sendOrderStatisticMail(LocalDate.of(2023, 3, 5), "test@test.com");

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
    }

    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime now) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .price(price)
                .type(type)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}