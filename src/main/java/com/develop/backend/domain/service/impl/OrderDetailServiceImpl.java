package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.OrderDetailDto;
import com.develop.backend.domain.entity.OrderDetail;
import com.develop.backend.domain.repository.OrderDetailRepository;
import com.develop.backend.domain.service.OrderDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetailDto saveOrderDetails(OrderDetailDto orderDetailDto) {
        return OrderDetailDto.fromEntity(orderDetailRepository.save(OrderDetail.fromDto(orderDetailDto)));
    }
}
