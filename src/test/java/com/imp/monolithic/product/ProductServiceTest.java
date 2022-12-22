package com.imp.monolithic.product;

import static com.imp.monolithic.support.MemberFixtures.KUN;
import static com.imp.monolithic.support.ProductFixtures.PHONE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.imp.monolithic.member.domain.Member;
import com.imp.monolithic.member.domain.MemberRepository;
import com.imp.monolithic.member.domain.Role;
import com.imp.monolithic.product.application.ProductService;
import com.imp.monolithic.product.application.dto.ProductCreateRequest;
import com.imp.monolithic.product.application.dto.ProductFindResponse;
import com.imp.monolithic.product.domain.Product;
import com.imp.monolithic.product.domain.ProductRepository;
import com.imp.monolithic.product.domain.Quantity;
import com.imp.monolithic.support.ApplicationTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void create_new_product() {
        // given
        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(30000L), 10L);

        // when
        final long actual = productService.create(request, 1L);

        // then
        assertThat(actual).isNotZero();
    }

    @DisplayName("존재하지 않는 회원의 경우 상품 등록에 실패한다")
    @Test
    void create_product_should_throw_if_sellerId_is_invalid() {
        // given
        final ProductCreateRequest request = new ProductCreateRequest("존재하지 않는 상품", BigDecimal.valueOf(300L), 10L);

        // when & then
        assertThatThrownBy(() -> productService.create(request, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 판매자 아이디");
    }

    @DisplayName("회원의 ROLE이 판매자가 아닌 경우 상품 등록에 실패한다.")
    @Test
    void create_MemberIsNotSeller_throwsException() {
        // given
        final Member member = KUN.createWithRole(Role.BUYER);
        final Member savedMember = memberRepository.save(member);
        final ProductCreateRequest request = new ProductCreateRequest("존재하지 않는 상품", BigDecimal.valueOf(300L), 10L);

        // when, then
        assertThatThrownBy(() -> productService.create(request, savedMember.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역할이 판매자여야만 상품을 등록할 수 있습니다.");
    }

    @DisplayName("상품 가격이 1 보다 작을 경우 상품 등록에 실패한다")
    @ValueSource(ints = {-10, -1, 0})
    @ParameterizedTest
    void create_product_should_throw_if_price_is_zero_or_negative(final int price) {
        // given
        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(price), 10L);
        final Member savedMember = memberRepository.save(KUN.create());

        // when & then
        assertThatThrownBy(() -> productService.create(request, savedMember.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 1보다 작을 수 없다");
    }

    @DisplayName("상품 수량이 0 보다 작을 경우 상품 등록에 실패한다")
    @ValueSource(longs = {-10, -1})
    @ParameterizedTest
    void create_product_should_throw_if_quantity_is_zero_or_negative(final long quantity) {
        // given
        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(100L), quantity);
        final Member savedMember = memberRepository.save(KUN.create());

        // when & then
        assertThatThrownBy(() -> productService.create(request, savedMember.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고 수량은 0보다 작을 수 없다.");
    }

    @DisplayName("상품을 생성하고 재고가 1개 이상이면 초기 상태는 SALE이다.")
    @ValueSource(longs = {1, 2})
    @ParameterizedTest
    void product_initial_status_is_sale(final long quantity) {
        // given
        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(100L), quantity);
        final Member savedMember = memberRepository.save(KUN.create());

        // when
        final long id = productService.create(request, savedMember.getId());
        final Product product = productRepository.findById(id).orElseThrow(NullPointerException::new);

        // then
        assertThat(product.isSale()).isTrue();
    }

    @DisplayName("상품을 생성하고 재고가 0개면 초기 상태는 SOLD_OUT이다.")
    @Test
    void product_initial_status_with_zero_quantity_is_sold_out() {
        // given
        final ProductCreateRequest request = new ProductCreateRequest("상품명", BigDecimal.valueOf(100L), 0L);
        final Member savedMember = memberRepository.save(KUN.create());

        // when
        final long id = productService.create(request, savedMember.getId());
        final Product product = productRepository.findById(id).orElseThrow(NullPointerException::new);

        // then
        assertThat(product.isSoldOut()).isTrue();
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void findById() {
        // given
        final Member member = memberRepository.save(KUN.create());
        final Product product = PHONE.create(new Quantity(3L), member.getId());
        final Product savedProduct = productRepository.save(product);

        final ProductFindResponse expected = ProductFindResponse.from(savedProduct, member);

        // when
        final ProductFindResponse productFindResponse = productService.findById(savedProduct.getId());

        // then
        assertAll(
                () -> assertThat(expected).usingRecursiveComparison().ignoringFields("price")
                        .isEqualTo(productFindResponse),
                () -> assertThat(expected.getPrice()).isEqualByComparingTo(productFindResponse.getPrice())
        );
    }
}
