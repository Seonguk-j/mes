package team_2p4p.mes.repository;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.thymeleaf.util.StringUtils;
import team_2p4p.mes.dto.SearchDto;
import team_2p4p.mes.entity.Obtain;
import team_2p4p.mes.entity.QObtain;

import javax.persistence.EntityManager;
import java.util.List;

public class ObtainRepositoryCustomImpl implements ObtainRepositoryCustom {
    private JPAQueryFactory queryFactory;


    public ObtainRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

//    private BooleanExpression regDtsAfter(String searchDateType) {
//        LocalDate date = LocalDate.now();
//        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
//            return null;
//        } else if (StringUtils.equals("1d", searchDateType)) {
//            date = date.minusDays(1);
//        } else if (StringUtils.equals("1w", searchDateType)) {
//            date = date.minusWeeks(1);
//        } else if (StringUtils.equals("1m", searchDateType)) {
//            date = date.minusMonths(1);
//        } else if (StringUtils.equals("6m", searchDateType)) {
//            date = date.minusMonths(6);
//        }
//        return QPost.post.regTime.after(date);
//    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("제품명", searchBy)) {
            return QObtain.obtain.item.itemName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("업체명", searchBy)) {
            return QObtain.obtain.customerId.customerName.like("%" + searchQuery + "%");
        }
        return null;
    }


    @Override
    public List<Obtain> getObtainList(SearchDto searchDto) {
        List<Obtain> results = queryFactory
                .selectFrom(QObtain.obtain)
                .where(searchByLike(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .orderBy(QObtain.obtain.obtainId.desc())
                .fetch();

        return results;
    }
}
