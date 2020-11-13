package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tpsc.it4u.model.ChannelValue;

@Repository
public interface ChannelValueRepository extends JpaRepository<ChannelValue, Long>{
    ChannelValue findByServicePack(String servicePack);
    ChannelValue findByValue(String value);
}
