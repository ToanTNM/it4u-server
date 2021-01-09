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

    @Query(value = "SELECT"
    + " *"
    + " FROM client_device_inf c"
    + " INNER JOIN sitesname ON c.sitename_id=sitesname.id"
    + " INNER JOIN contract ON c.contract_id=contract.id"
    + " WHERE sitename=:param OR isp_wan1=:param OR isp_wan2=:param OR isp_wan3=:param OR isp_wan4=:param"
    + " OR model_wan1=:param OR model_wan2=:param OR model_wan3=:param OR model_wan4=:param OR model_lb=:param OR model_other=:param"
    + " OR method=:param OR method=:param OR reboot=:param OR monitor=:param OR firmware=:param",nativeQuery = true)
    List<ClientDeviceInf> findAllByParam(@Param(value = "param") String param);
}
