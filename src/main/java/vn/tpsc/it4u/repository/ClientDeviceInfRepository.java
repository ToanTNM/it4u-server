package vn.tpsc.it4u.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.model.ClientDeviceInf;

@Repository
public interface ClientDeviceInfRepository extends JpaRepository<ClientDeviceInf, Long>{
    ClientDeviceInf findById(long id);

    @Query(value = " SELECT"
    + " *"
    + " FROM client_device_inf"
    + " LIMIT :num", nativeQuery = true)
    List<ClientDeviceInf> findLimitByNum(@Param(value = "num") Long num);
}
