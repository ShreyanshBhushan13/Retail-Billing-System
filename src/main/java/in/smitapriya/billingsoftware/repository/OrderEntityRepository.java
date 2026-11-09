package in.smitapriya.billingsoftware.repository;

import in.smitapriya.billingsoftware.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderEntityRepository extends JpaRepository<OrderEntity,Long> , JpaSpecificationExecutor<OrderEntity> {
    @Query("""
SELECT DISTINCT o FROM OrderEntity o
JOIN o.items i
WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :searchByItemName, '%'))
""")
    Page<OrderEntity>  findByItemName(@Param("searchByItemName") String searchByItemName ,Pageable pageable);
//    Page<OrderEntity> findByCustomerNameContainingIgnoreCase(String searchValue, Pageable pageable);
    Optional<OrderEntity> findByOrderId(String orderId);
    List<OrderEntity> findAllByOrderByCreatedAtDesc();
    @Query("SELECT SUM(o.grandTotal) from OrderEntity o Where DATE(o.createdAt) = :date")
    Double sumSalesByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(o) from OrderEntity o Where DATE(o.createdAt) = :date")
    Long countByOrderDate(@Param("date") LocalDate date);

    @Query("SELECT o FROM OrderEntity o ORDER BY o.createdAt DESC")
    List<OrderEntity> findRecentOrders(Pageable pageable);
}
