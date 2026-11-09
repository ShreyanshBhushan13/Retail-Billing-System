package in.smitapriya.billingsoftware.specification;

import in.smitapriya.billingsoftware.entity.OrderEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification<OrderEntity> Search (String searchValue){
        return new Specification<OrderEntity>() {
            @Override
            public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
               if(searchValue == null || searchValue.isEmpty()){
                   return criteriaBuilder.conjunction();
               }
               String pattern ="%" + searchValue.toLowerCase() + "%";
               Predicate customerPredicate= criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")),pattern);
               Predicate statusPredicate=criteriaBuilder.like(criteriaBuilder.lower(root.get("paymentDetails").get("status").as(String.class)),pattern );
               Predicate paymentMethodPredicate=criteriaBuilder.like(criteriaBuilder.lower(root.get("paymentMethod").as(String.class)),pattern );
            // combine all conditions using OR
               return criteriaBuilder.or(customerPredicate , paymentMethodPredicate , statusPredicate);
            }

        };
    }
}
