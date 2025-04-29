package com.develop.backend.domain.service;

import com.develop.backend.application.dto.OrderDetailDto;

public interface OrderDetailService {
    OrderDetailDto saveOrderDetails(OrderDetailDto orderDetailDto);
}
