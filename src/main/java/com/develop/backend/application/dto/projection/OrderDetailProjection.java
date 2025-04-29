package com.develop.backend.application.dto.projection;

import java.math.BigDecimal;

public interface OrderDetailProjection {
    Long getId();
    Long getOrderId();
    Long getProductId();
    String getProductCode();
    String getDescription();
    String getProductName();
    Integer getQuantity();
    BigDecimal getUnitPrice();
    BigDecimal getTotalPrice();
}
