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
    + " WHERE sitename like %:param% OR isp_wan1 like %:param% OR isp_wan2 like %:param% OR isp_wan3 like %:param% OR isp_wan4 like %:param%"
    + " OR model_wan1 like %:param% OR model_wan2 like %:param% OR model_wan3 like %:param% OR model_wan4 like %:param% OR model_lb like %:param% OR model_other like %:param%"
    + " OR method like %:param% OR method like %:param% OR reboot like %:param% OR monitor like %:param% OR firmware like %:param%",nativeQuery = true)
    List<ClientDeviceInf> findAllByParam(@Param(value = "param") String param);
}
