package lk.ijse.backend.service;

import lk.ijse.backend.dto.PayHereInitiateRequestDTO;
import lk.ijse.backend.dto.PayHereInitiateResponseDTO;
import lk.ijse.backend.dto.PaymentInitiateRequestDTO;
import lk.ijse.backend.dto.PaymentInitiateResponseDTO;
import lk.ijse.backend.dto.PaymentRequestDTO;
import lk.ijse.backend.dto.PaymentResponseDTO;
import lk.ijse.backend.dto.BookingResponseDTO;

import java.util.Map;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest);
    PaymentResponseDTO getPaymentByBookingId(Long bookingId);
    PaymentInitiateResponseDTO initiatePayment(PaymentInitiateRequestDTO initiateRequest);
    PayHereInitiateResponseDTO initiatePayHerePayment(PayHereInitiateRequestDTO initiateRequest);
    boolean processPayHereNotification(Map<String, String> params);
    BookingResponseDTO confirmPayHereOrder(String orderId);
}
