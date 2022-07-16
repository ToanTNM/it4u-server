package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.CustomerSupport;

@Repository
public interface CustomerSupportRepository extends JpaRepository<CustomerSupport, Long> {

}
