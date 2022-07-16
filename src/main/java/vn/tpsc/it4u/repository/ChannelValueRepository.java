package vn.tpsc.it4u.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.ChannelName;
import vn.tpsc.it4u.models.ChannelValue;

@Repository
public interface ChannelValueRepository extends JpaRepository<ChannelValue, Long> {
	ChannelValue findById(long id);

	Boolean existsByServicePack(String servicePack);

	ChannelValue findByServicePack(String servicePack);

	ChannelValue findByValue(String value);

	List<ChannelValue> findByChannelName(ChannelName channelName);
}
