package vn.tpsc.it4u.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.ChannelAttribute;
import vn.tpsc.it4u.models.ChannelDetail;

@Repository
public interface ChannelDetailRepository extends JpaRepository<ChannelDetail, Long> {
	ChannelDetail findById(long id);

	Boolean existsById(long id);

	ChannelDetail findByChannelAttribute(ChannelAttribute channelAttribute);

}
