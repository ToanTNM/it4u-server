package vn.tpsc.it4u.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.CustomerBreakdowns;

@Repository
public interface CustomerBreakdownsRepository extends JpaRepository<CustomerBreakdowns, Long> {
    // List<CustomerBreakdowns> findAll();
}
