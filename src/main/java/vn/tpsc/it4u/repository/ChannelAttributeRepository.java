package vn.tpsc.it4u.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tpsc.it4u.model.ChannelAttribute;
import vn.tpsc.it4u.model.ChannelValue;

/**
 * ChannelValueRepository
 */
@Repository
public interface ChannelAttributeRepository extends JpaRepository <ChannelAttribute, Long>{
    
    Boolean existsByChannelValue(ChannelValue channelValue);

    ChannelAttribute findById(long id);

    List<ChannelAttribute> findByStatus(String status);

    List<ChannelAttribute> findByChannelValue(ChannelValue channelValue);

}
