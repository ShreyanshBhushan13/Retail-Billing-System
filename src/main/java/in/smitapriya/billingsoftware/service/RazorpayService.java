package in.smitapriya.billingsoftware.service;

import com.razorpay.RazorpayException;
import in.smitapriya.billingsoftware.io.RazorpayOrderResponse;

public interface RazorpayService {
    RazorpayOrderResponse createOrder(Double amount, String currency) throws RazorpayException;
}
