package com.ecommerce.product_service.service;

import com.ecommerce.product_service.entity.ActiveSeller;
import com.ecommerce.product_service.repository.ActiveSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActiveSellerServiceImp implements ActiveSellerService{

    private final ActiveSellerRepository activeSellerIdRepository;

    @Override
    public void save(UUID sellerId) {
        if (activeSellerIdRepository.existsById(sellerId)) {
            return;
        }

        ActiveSeller seller = new ActiveSeller(sellerId);
        activeSellerIdRepository.save(seller);
    }

    @Override
    public void delete(UUID sellerId) {
        activeSellerIdRepository.deleteById(sellerId);
    }

    @Override
    public boolean isActive(UUID sellerId) {
        return activeSellerIdRepository.existsById(sellerId);
    }
}
