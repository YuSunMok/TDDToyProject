package sample.cafediosk.spring.domain.stock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import sample.cafediosk.spring.domain.product.Product;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafediosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafediosk.spring.domain.product.ProductType.HANDMADE;

//@Profile("test")
@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("상품번호 리스트로 재고를 조회한다.")
    public void findAllByProductNumberIn() {
        // given
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 3);
        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        // when
        List<Stock> products = stockRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 1),
                        tuple("002", 2)
                );

    }

}