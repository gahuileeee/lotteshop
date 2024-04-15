package kr.co.lotte.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.*;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.MarketRepositoryCustom;
import kr.co.lotte.entity.QProdImage;
import kr.co.lotte.entity.QProducts;
import kr.co.lotte.repository.custom.MarketRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MarketRepositoryImpl implements MarketRepositoryCustom {
    @Value("${file.upload.path}")
    private String resourcePath;

    @Autowired
    private ResourceLoader resourceLoader;


    private final JPAQueryFactory jpaQueryFactory;
    private final QProducts qProduct = QProducts.products;
    private final QProdImage qImages = QProdImage.prodImage;


    // 장보기 게시판 게시글 출력 (market/view)
    @Override
    public List<Tuple> selectProduct(int prodno) {
        // select * from `product` as a join `images` as b on a.prodno = b.prodno where a`prodno` = ?
        List<Tuple> joinProduct = jpaQueryFactory
                .select(qProduct, qImages)
                .from(qProduct)
                .join(qImages)
                .on(qProduct.prodNo.eq(qImages.pNo))
                .where(qProduct.prodNo.eq(prodno))
                .fetch();

        log.info("results : " + joinProduct);
        log.info("아아아" +resourcePath);
        Resource arr =resourceLoader.getResource(resourcePath);
        log.info("아아2"+arr);
        return joinProduct;

    }

   /*

    // market/view에서 장바구니에 품목 추가
    @Override
    @Transactional
    public Integer addProductForCart(String uid, int prodno, int prodCount){
        Integer cartNo = jpaQueryFactory
                            .select(qCart.cartNo)
                            .from(qCart)
                            .where(qCart.uid.eq(uid))
                            .fetchOne();

        List<Cart_product> result = jpaQueryFactory
                                    .selectFrom(qCart_product)
                                    .where(qCart_product.cartNo.eq(cartNo).and(qCart_product.prodNo.eq(prodno)))
                                    .fetch();

        if (result.isEmpty()){
            return cartNo;
        }else {
            return -1; // 이미 존재하는 상품
        }
    }

    */
}
