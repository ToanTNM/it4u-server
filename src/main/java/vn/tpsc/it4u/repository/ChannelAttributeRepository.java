package vn.tpsc.it4u.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tpsc.it4u.model.ChannelAttribute;
import vn.tpsc.it4u.model.ChannelName;

/**
 * ChannelValueRepository
 */
@Repository
public interface ChannelAttributeRepository extends JpaRepository <ChannelAttribute, Long>{
    List<ChannelAttribute> findByStatus(String status);
    List<ChannelAttribute> findByChannelName(ChannelName channelName);
}
