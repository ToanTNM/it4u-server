package vn.tpsc.it4u.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.tpsc.it4u.models.ChannelAttribute;
import vn.tpsc.it4u.models.HistoryChannel;

@Repository
public interface HistoryChannelRepository extends JpaRepository<HistoryChannel, Long> {
	HistoryChannel findById(long id);

	List<HistoryChannel> findByChannelAttribute(ChannelAttribute channelAttribute);
}
