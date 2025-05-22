package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.OrderDto;
import com.develop.backend.application.dto.projection.OrderDetailProjection;
import com.develop.backend.domain.entity.Order;
import com.develop.backend.domain.entity.OrderDetail;
import com.develop.backend.domain.entity.Product;
import com.develop.backend.domain.entity.User;
import com.develop.backend.domain.enums.OrderState;
import com.develop.backend.domain.repository.OrderDetailRepository;
import com.develop.backend.domain.repository.OrderRepository;
import com.develop.backend.domain.repository.ProductRepository;
import com.develop.backend.domain.service.EmailService;
import com.develop.backend.domain.service.OrderService;
import com.develop.backend.insfraestructure.exception.OrderNotFoundException;
import com.develop.backend.insfraestructure.exception.ProductNotFoundException;
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


    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = Order.fromDto(orderDto);
        order.setUser(User.builder().id(orderDto.getUserId()).build());
        order.setOrderCode(generateOrderCode());
        order.setOrderState(OrderState.PENDING);

        List<OrderDetail> orderDetails = orderDto.getOrderDetails().stream()
                .map(dto -> {
                    Product product = productRepository.findById(dto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found ID: " + dto.getProductId()));

                    int updatedRows = productRepository.reduceInventory(product.getId(), dto.getQuantity());
                    if (updatedRows == 0) {
                        throw new ProductNotFoundException("No hay suficiente stock para el producto ID: " + product.getId());
                    }

                    return OrderDetail.builder()
                            .order(order)
                            .product(product)
                            .quantity(dto.getQuantity())
                            .unitPrice(dto.getUnitPrice())
                            .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())))
                            .build();
                }).toList();

        order.setOrderDetails(orderDetails);

        Order savedOrder = orderRepository.save(order);

        return OrderDto.fromEntity(savedOrder);
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
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Orden no encontrada con ID: " + id));

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
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        for (OrderDetail detail : orderDetails) {
            productRepository.addStock(detail.getProduct().getId(), detail.getQuantity());
        }
    }

    @Override
    public ResponseEntity<Resource> exportInvoice(Long idUser, List<Long> idOrder) {
        List<Order> orders = orderRepository.findByIdAndUserId(idUser, idOrder);

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Order order = orders.getFirst();
//        List<OrderDetailProjection> orderDetailProjections = orderDetailRepository.findAllByUserId(idUser);
//        BigDecimal total = orderDetailRepository.findTotalPriceByUserId(idUser);

        List<OrderDetailProjection> orderDetailProjections = orderDetailRepository.findAllByUserIdAndOrderId(idUser, idOrder);
        BigDecimal total = orderDetailRepository.findTotalPriceByUserIdAndOrderIds(idUser, idOrder);


        try {
            File file = ResourceUtils.getFile("classpath:report/reports.jasper");
            File imageLogo = ResourceUtils.getFile("classpath:report/logo.png");
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
