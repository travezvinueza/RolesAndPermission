package com.develop.backend.insfraestructure.controller;


import com.develop.backend.application.dto.request.PaypalPaymentRequest;
import com.develop.backend.domain.repository.OrderDetailRepository;
import com.develop.backend.domain.service.OrderService;
import com.develop.backend.domain.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paypal")
@RequiredArgsConstructor
public class PayPalController {

    private final PaypalService paypalService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderService orderService;
    private static final String SUCCESS_URL  = "http://localhost:8081/api/paypal/success";
    private static final String CANCEL_URL   = "http://localhost:8081/api/paypal/cancel";

    @PostMapping("/pay")
    public ResponseEntity<String> makePayment(
            @RequestParam Long userId,
            @RequestBody List<Long> orderIds
    ) {
        try {
            BigDecimal total = orderDetailRepository.findTotalPriceByUserId(userId);

            if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.ok("No hay ordenes para pagar");
            }
            String description = "Pago de ordenes";

            String customData = userId + "," + orderIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")); // "userId,orderId1,orderId2,orderId3"

            PaypalPaymentRequest request = new PaypalPaymentRequest(
                    total.doubleValue(),
                    "USD",
                    "paypal",
                    "sale",
                    description,
                    CANCEL_URL,
                    SUCCESS_URL,
                    customData
            );

            Payment payment = paypalService.createPayment(request);

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Error creando el pago de PayPal: " + e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the payment");
    }


    @GetMapping("/success")
    public ResponseEntity<Resource> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.execute(paymentId, payerId);
            if ("approved".equalsIgnoreCase(payment.getState())) {

                // 👇 Recuperamos los datos que enviamos en "custom"
                String customData = payment.getTransactions().getFirst().getCustom();
                String[] parts = customData.split(",");
                Long userId = Long.parseLong(parts[0]);
                Long orderId = Long.parseLong(parts[1]);

                return orderService.exportInvoice(userId, orderId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error durante el pago o exportación de factura: " + e.getMessage(), e);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("El pago fue cancelado por el usuario.");
    }


//    @GetMapping("/payment/cancel")
//    public String paymentCancel() {
//        return "paymentCancel";
//    }
//
//    @GetMapping("/payment/error")
//    public String paymentError() {
//        return "paymentError";
//    }

    @GetMapping("exportInvoice")
    public ResponseEntity<Resource> exportInvoice(@RequestParam Long idUser, @RequestParam Long idOrden){
        return orderService.exportInvoice(idUser, idOrden);
    }

}
