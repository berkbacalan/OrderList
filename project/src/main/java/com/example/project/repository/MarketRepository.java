package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.project.model.Market;

@SuppressWarnings("unused")
@Repository
public interface MarketRepository extends JpaRepository<Market, Long>, JpaSpecificationExecutor<Market> {
	List<Market> findAllByMarketid(long marketid);
}
