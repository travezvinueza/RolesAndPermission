package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.OrderDetailDto;
import com.develop.backend.application.dto.OrderDto;
import com.develop.backend.application.dto.projection.OrderDetailProjection;
import com.develop.backend.domain.entity.Order;
import com.develop.backend.domain.entity.User;
import com.develop.backend.domain.enums.OrderState;
import com.develop.backend.domain.repository.OrderDetailRepository;
import com.develop.backend.domain.repository.OrderRepository;
import com.develop.backend.domain.repository.ProductRepository;
import com.develop.backend.domain.repository.UserRepository;
import com.develop.backend.domain.service.EmailService;
import com.develop.backend.domain.service.OrderDetailService;
import com.develop.backend.domain.service.OrderService;
import com.develop.backend.insfraestructure.exception.OrderNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final EmailService emailService;
    private final OrderDetailService orderDetailService;
    private final UserRepository userRepository;


    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        Order order = Order.fromDto(orderDto);
        order.setUser(User.builder().id(orderDto.getUserId()).build());
        order.setOrderCode(generateOrderCode());
        order.setOrderState(OrderState.PENDING);
        Order savedOrder = orderRepository.save(order);

        for(OrderDetailDto orderDetailDto : orderDto.getOrderDetails()) {
            orderDetailDto.setOrderId(savedOrder.getId());
            this.orderDetailService.saveOrderDetail(orderDetailDto);
        }

        Order orderWithDetails = orderRepository.findByIdWithDetails(savedOrder.getId())
                .orElseThrow(() -> new RuntimeException("Order not found after saving"));

        return OrderDto.fromEntity(orderWithDetails);
    }

    private String generateOrderCode() {
        return "ORDER-" + UUID.randomUUID().toString().substring(0, 6);
    }

    @Override
    public OrderDto findByIdOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Orden no encontrada con ID: " + id));

        return OrderDto.fromEntity(order);
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found for update"));

        order.setOrderState(orderDto.getOrderState());
        order.setDescription(orderDto.getDescription());
        return OrderDto.fromEntity(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> listOrders(Long userId) {
        return orderRepository.findAllOrdersByUserId(userId).stream()
                .map(OrderDto::fromEntity)
                .toList();
    }

    @Override
    public OrderDto processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Orden de proceso no encontrada con ID : " + orderId));

        order.setOrderState(OrderState.PROCESSING);
        return OrderDto.fromEntity(orderRepository.save(order));
    }

    @Override
    public OrderDto sendOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Orden de envio  no encontrada con ID : " + orderId));

        order.setOrderState(OrderState.SHIPPED);
        return OrderDto.fromEntity(orderRepository.save(order));
    }

    @Override
    public OrderDto deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Orden de entrega no encontrada con ID : " + orderId));

        order.setOrderState(OrderState.DELIVERED);
        return OrderDto.fromEntity(orderRepository.save(order));
    }

    @Override
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Orden de cancelacion no encontrada con ID: " + orderId));

        this.restoreStock(orderId);
        order.setOrderState(OrderState.CANCELLED);
        return OrderDto.fromEntity(orderRepository.save(order));
    }

    @Override
    public OrderDto reactivateOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Orden de reactivacion no encontrada con ID : " + orderId));

        restoreStock(orderId);
        order.setOrderState(OrderState.PENDING);
        return OrderDto.fromEntity(orderRepository.save(order));
    }

    private void restoreStock(Long orderId) {
        List<OrderDetailProjection> orderDetails = orderDetailRepository.findByOrderId(orderId);
        for (OrderDetailProjection detail : orderDetails) {
            productRepository.addStock(detail.getProductId(), detail.getQuantity());
        }
    }


    @Override
    public ResponseEntity<Resource> exportInvoice(Long idUser, Long idOrder) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUserId(idUser, idOrder);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Order order = optionalOrder.get();
        List<OrderDetailProjection> orderDetailProjections = orderDetailRepository.findAllByUserId(idUser);
        BigDecimal total = orderDetailRepository.findTotalPriceByUserId(idUser);

        try {
            File file = ResourceUtils.getFile("classpath:reports.jasper");
            File imageLogo = ResourceUtils.getFile("classpath:logo.png");
            JasperReport report = (JasperReport) JRLoader.loadObject(file);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("imageLogo", new FileInputStream(imageLogo));
            parameters.put("orderCode", order.getOrderCode());
            parameters.put("userName", order.getUser().getUsername());
            parameters.put("userEmail", order.getUser().getEmail());
            parameters.put("creationDate", order.getCreationDate());
            parameters.put("totalPay", total);
            parameters.put("dsInvoice", new JRBeanCollectionDataSource(orderDetailProjections));

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // Preparar cabecera del archivo adjunto
            String sdf = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String filename = "Invoice_" + order.getOrderCode() + "_" + sdf + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build());
            headers.setContentType(MediaType.APPLICATION_PDF);

            // Envía el email
            emailService.sendEmailWithAttachment(
                    order.getUser().getEmail(),
                    "Confirmación de Orden: " + order.getOrderCode(),
                    "<h1>Gracias por tu compra!</h1><p>Adjunto encontrarás tu orden.</p>",
                    pdfBytes,
                    filename
            );

            // Retorna el PDF como descarga
            return ResponseEntity.ok()
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .headers(headers)
                    .body(new ByteArrayResource(pdfBytes));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
