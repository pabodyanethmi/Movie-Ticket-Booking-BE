package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.*;
import lk.ijse.backend.service.PaymentService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payhere/initiate")
    public ResponseEntity<StandardResponse<PayHereInitiateResponseDTO>> initiatePayHerePayment(
            @Valid @RequestBody PayHereInitiateRequestDTO initiateRequest) {
        PayHereInitiateResponseDTO response = paymentService.initiatePayHerePayment(initiateRequest);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "PayHere payment initiation payload generated", response)
        );
    }

    @PostMapping(value = "/payhere/notify", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<String> handlePayHereNotification(@RequestParam Map<String, String> requestParams) {
        boolean isSuccess = paymentService.processPayHereNotification(requestParams);
        if (isSuccess) {
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("RECEIVED");
        }
    }

    @PostMapping("/payhere/confirm-order")
    public ResponseEntity<StandardResponse<BookingResponseDTO>> confirmPayHereOrder(@RequestParam String orderId) {
        BookingResponseDTO booking = paymentService.confirmPayHereOrder(orderId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "PayHere order confirmed successfully", booking)
        );
    }

    @PostMapping("/payhere/confirm/{orderId}")
    public ResponseEntity<StandardResponse<BookingResponseDTO>> confirmPayHereOrderPath(@PathVariable String orderId) {
        BookingResponseDTO booking = paymentService.confirmPayHereOrder(orderId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "PayHere order confirmed successfully", booking)
        );
    }

    @PostMapping("/initiate")
    public ResponseEntity<StandardResponse<PaymentInitiateResponseDTO>> initiatePayment(
            @Valid @RequestBody PaymentInitiateRequestDTO initiateRequest) {
        PaymentInitiateResponseDTO response = paymentService.initiatePayment(initiateRequest);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Payment initiation payload generated", response)
        );
    }

    @PostMapping("/process")
    public ResponseEntity<StandardResponse<PaymentResponseDTO>> processPayment(
            @Valid @RequestBody PaymentRequestDTO paymentRequest) {
        PaymentResponseDTO response = paymentService.processPayment(paymentRequest);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Payment processed successfully", response)
        );
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<StandardResponse<PaymentResponseDTO>> getPaymentByBookingId(@PathVariable Long bookingId) {
        PaymentResponseDTO payment = paymentService.getPaymentByBookingId(bookingId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Payment details fetched successfully", payment)
        );
    }
}
