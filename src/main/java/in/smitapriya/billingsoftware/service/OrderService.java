package in.smitapriya.billingsoftware.service;

import in.smitapriya.billingsoftware.io.OrderRequest;
import in.smitapriya.billingsoftware.io.OrderResponse;
import in.smitapriya.billingsoftware.io.PaymentVerificationRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    void deleteOrder(String orderId);
    Page<OrderResponse> getOrders(Pageable pageable, String searchValue);
    OrderResponse verifyPayment(PaymentVerificationRequest request);

    Double sumSalesByDate(LocalDate date);
    Long countByOrderDate(LocalDate date);
    List<OrderResponse> findRecentOrders();

    String summarizeOrders();


}
