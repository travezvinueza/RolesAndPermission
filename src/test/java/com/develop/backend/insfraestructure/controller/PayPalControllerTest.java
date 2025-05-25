package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.request.PaypalPaymentRequest;
import com.develop.backend.domain.repository.OrderDetailRepository;
import com.develop.backend.domain.service.OrderService;
import com.develop.backend.domain.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PayPalControllerTest {

    @InjectMocks
    private PayPalController payPalController;

    @Mock
    private PaypalService paypalService;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private OrderService orderService;

    @Test
    void makePayment_whenTotalPriceIsZero() throws PayPalRESTException {
        when(orderDetailRepository.findTotalPriceByUserIdAndOrderIds(anyLong(), anyList())).thenReturn(BigDecimal.ZERO);
        ResponseEntity<String> response = payPalController.makePayment(1L, Arrays.asList(10L, 11L));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No hay ordenes para pagar", response.getBody());
        verify(paypalService, never()).createPayment(any());
    }

    @Test
    void makePayment_whenTotalPriceIsNull() throws PayPalRESTException {
        when(orderDetailRepository.findTotalPriceByUserIdAndOrderIds(anyLong(), anyList())).thenReturn(null);
        ResponseEntity<String> response = payPalController.makePayment(1L, Arrays.asList(10L, 11L));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("No hay ordenes para pagar", response.getBody());
        verify(paypalService, never()).createPayment(any());
    }

    @Test
    void makePayment_successful() throws PayPalRESTException {
        when(orderDetailRepository.findTotalPriceByUserIdAndOrderIds(anyLong(), anyList())).thenReturn(new BigDecimal("100.00"));

        Payment mockPayment = mock(Payment.class);
        Links mockLinks = mock(Links.class);
        String approvalUrl = "http://approval.url";

        when(mockLinks.getRel()).thenReturn("approval_url");
        when(mockLinks.getHref()).thenReturn(approvalUrl);
        when(mockPayment.getLinks()).thenReturn(Collections.singletonList(mockLinks));
        when(paypalService.createPayment(any(PaypalPaymentRequest.class))).thenReturn(mockPayment);

        ResponseEntity<String> response = payPalController.makePayment(1L, Arrays.asList(10L, 11L));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(approvalUrl, response.getBody());
        verify(paypalService).createPayment(any(PaypalPaymentRequest.class));
    }

    @Test
    void makePayment_noApprovalUrl() throws PayPalRESTException {
        when(orderDetailRepository.findTotalPriceByUserIdAndOrderIds(anyLong(), anyList())).thenReturn(new BigDecimal("100.00"));

        Payment mockPayment = mock(Payment.class);
        Links mockLinks = mock(Links.class);

        when(mockLinks.getRel()).thenReturn("other_rel"); // Not "approval_url"
        when(mockPayment.getLinks()).thenReturn(Collections.singletonList(mockLinks));
        when(paypalService.createPayment(any(PaypalPaymentRequest.class))).thenReturn(mockPayment);

        ResponseEntity<String> response = payPalController.makePayment(1L, Arrays.asList(10L, 11L));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error processing the payment", response.getBody());
        verify(paypalService).createPayment(any(PaypalPaymentRequest.class));
    }


    @Test
    void makePayment_throwsPayPalRESTException() throws PayPalRESTException {
        when(orderDetailRepository.findTotalPriceByUserIdAndOrderIds(anyLong(), anyList())).thenReturn(new BigDecimal("100.00"));
        when(paypalService.createPayment(any(PaypalPaymentRequest.class))).thenThrow(new PayPalRESTException("Test Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            payPalController.makePayment(1L, Arrays.asList(10L, 11L));
        });
        assertTrue(exception.getMessage().contains("Error creando el pago de PayPal: Test Error"));
    }

    @Test
    void paymentSuccess_approved() throws Exception {
        Payment mockPayment = mock(Payment.class);
        Transaction mockTransaction = mock(Transaction.class);
        String paymentId = "PAYID-123";
        String payerId = "PAYERID-456";
        Long userId = 1L;
        List<Long> orderIds = Arrays.asList(101L, 102L);
        String customData = userId + "," + orderIds.get(0) + "," + orderIds.get(1);

        when(mockPayment.getState()).thenReturn("approved");
        when(mockTransaction.getCustom()).thenReturn(customData);
        when(mockPayment.getTransactions()).thenReturn(Collections.singletonList(mockTransaction));
        when(paypalService.execute(eq(paymentId), eq(payerId))).thenReturn(mockPayment);
        when(orderService.exportInvoice(eq(userId), eq(orderIds))).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<String> response = payPalController.paymentSuccess(paymentId, payerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("El pago fue exitoso. ID de pago: " + paymentId + ", ID de orden: " + orderIds, response.getBody());
        verify(orderService).exportInvoice(eq(userId), eq(orderIds));
    }

    @Test
    void paymentSuccess_notApproved() throws Exception {
        Payment mockPayment = mock(Payment.class);
        String paymentId = "PAYID-123";
        String payerId = "PAYERID-456";

        when(mockPayment.getState()).thenReturn("failed");
        when(paypalService.execute(eq(paymentId), eq(payerId))).thenReturn(mockPayment);

        ResponseEntity<String> response = payPalController.paymentSuccess(paymentId, payerId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(orderService, never()).exportInvoice(anyLong(), anyList());
    }

    @Test
    void paymentSuccess_executeThrowsPayPalRESTException() throws Exception {
        String paymentId = "PAYID-123";
        String payerId = "PAYERID-456";
        when(paypalService.execute(eq(paymentId), eq(payerId))).thenThrow(new PayPalRESTException("Execute Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            payPalController.paymentSuccess(paymentId, payerId);
        });
        assertTrue(exception.getMessage().contains("Error durante el pago o exportación de factura: Execute Error"));
        verify(orderService, never()).exportInvoice(anyLong(), anyList());
    }

    @Test
    void paymentSuccess_exportInvoiceThrowsException() throws Exception {
        Payment mockPayment = mock(Payment.class);
        Transaction mockTransaction = mock(Transaction.class);
        String paymentId = "PAYID-123";
        String payerId = "PAYERID-456";
        Long userId = 1L;
        List<Long> orderIds = Arrays.asList(101L, 102L);
        String customData = userId + "," + orderIds.get(0) + "," + orderIds.get(1);

        when(mockPayment.getState()).thenReturn("approved");
        when(mockTransaction.getCustom()).thenReturn(customData);
        when(mockPayment.getTransactions()).thenReturn(Collections.singletonList(mockTransaction));
        when(paypalService.execute(eq(paymentId), eq(payerId))).thenReturn(mockPayment);
        when(orderService.exportInvoice(eq(userId), eq(orderIds))).thenThrow(new RuntimeException("Invoice Export Error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            payPalController.paymentSuccess(paymentId, payerId);
        });
        assertTrue(exception.getMessage().contains("Error durante el pago o exportación de factura: Invoice Export Error"));
        verify(orderService).exportInvoice(eq(userId), eq(orderIds));
    }

    @Test
    void paymentCancel() {
        ResponseEntity<String> response = payPalController.paymentCancel();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("El pago fue cancelado por el usuario.", response.getBody());
    }

    @Test
    void paymentError() {
        ResponseEntity<String> response = payPalController.paymentError();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hubo un error en el proceso de pago.", response.getBody());
    }

    @Test
    void exportInvoice() {
        Long userId = 1L;
        List<Long> orderIds = Arrays.asList(10L, 20L);
        Resource mockResource = new ByteArrayResource("Invoice content".getBytes());
        ResponseEntity<Resource> expectedResponse = ResponseEntity.ok().body(mockResource);

        when(orderService.exportInvoice(eq(userId), eq(orderIds))).thenReturn(expectedResponse);

        ResponseEntity<Resource> actualResponse = payPalController.exportInvoice(userId, orderIds);

        assertEquals(expectedResponse, actualResponse);
        verify(orderService).exportInvoice(eq(userId), eq(orderIds));
    }
}
