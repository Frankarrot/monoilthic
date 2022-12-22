package com.imp.monolithic.product.application;

import com.imp.monolithic.member.domain.Member;
import com.imp.monolithic.member.domain.MemberRepository;
import com.imp.monolithic.product.application.dto.ProductCreateRequest;
import com.imp.monolithic.product.domain.Product;
import com.imp.monolithic.product.domain.ProductRepository;
import com.imp.monolithic.product.application.dto.ProductFindResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public ProductService(final ProductRepository productRepository, final MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public long create(final ProductCreateRequest request, final Long sellerId) {
        final Member member = memberRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매자 아이디"));

        if (!member.canSell()) {
            throw new IllegalArgumentException("역할이 판매자여야만 상품을 등록할 수 있습니다.");
        }

        final Product product = request.toEntity(sellerId);
        final Product savedProduct = productRepository.save(product);

        return savedProduct.getId();
    }

    public ProductFindResponse findById(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        final Member member = memberRepository.findById(product.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return ProductFindResponse.from(product, member);
    }
}
