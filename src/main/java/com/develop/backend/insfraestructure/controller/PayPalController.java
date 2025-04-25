package com.develop.backend.insfraestructure.controller;

import com.develop.backend.domain.service.EmailService;
import com.develop.backend.domain.service.FacturaService;
import com.develop.backend.domain.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/paypal")
@RequiredArgsConstructor
public class PayPalController {
    private final FacturaService facturaService;
    private final EmailService emailService;

    private final PaypalService paypalService;
    private static final String SUCCESS_URL  = "http://localhost:8081/api/paypal/success";
    private static final String CANCEL_URL   = "http://localhost:8081/api/paypal/cancel";

    @PostMapping("/pay")
    public String makePayment(@RequestParam double amount){

        try {
            Payment    payment = paypalService.createPayment(
                    amount,
                    "USD",
                    "paypal",
                    "sale",
                    "payment description",
                    CANCEL_URL,
                    SUCCESS_URL);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "Redirect to: "+links.getHref();

                }
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
        return "Error processing the payment";
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.execute(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                // Simula datos para la factura (puedes reemplazar con datos reales del pago o del usuario)
                Map<String, Object> parametros = new HashMap<>();
//                parametros.put("cliente", "Ricardo Travez");
                parametros.put("monto", payment.getTransactions().getFirst().getAmount().getTotal());
//                parametros.put("fecha", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

                // Generar el PDF
                byte[] facturaPdf = facturaService.generarFactura(parametros);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf");

                // Enviar por email
                emailService.sendEmailWithAttachment("travezvinueza@gmail.com", "Factura de pago", "Factura de pago", facturaPdf, "report.pdf");

                return "payment is successfully done and email sent";
            }
        } catch (Exception e) {
            throw new RuntimeException("Error durante el pago o envío de factura: " + e.getMessage(), e);
        }

        return "payment failed";
    }

}
