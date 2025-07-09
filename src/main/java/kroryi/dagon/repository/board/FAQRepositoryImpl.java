package kroryi.dagon.repository.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.entity.QFAQ;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RequiredArgsConstructor
public class FAQRepositoryImpl implements FAQRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FAQ> searchDynamic(String keyword, Long categoryId, Boolean isActive, Pageable pageable) {
        QFAQ faq = QFAQ.fAQ;
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            builder.and(faq.question.containsIgnoreCase(keyword)
                    .or(faq.answer.containsIgnoreCase(keyword)));
        }
        if (categoryId != null) {
            builder.and(faq.category.id.eq(categoryId));
        }
        if (isActive != null) {
            builder.and(faq.isActive.eq(isActive));
        }

        List<FAQ> results = queryFactory.selectFrom(faq)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(faq.displayOrder.asc())
                .fetch();

        long total = queryFactory.selectFrom(faq)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
} 