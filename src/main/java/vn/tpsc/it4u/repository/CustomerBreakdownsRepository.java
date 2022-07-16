package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.CustomerBreakdowns;

@Repository
public interface CustomerBreakdownsRepository extends JpaRepository<CustomerBreakdowns, Long> {
	// List<CustomerBreakdowns> findAll();
}
