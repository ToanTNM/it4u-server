package vn.tpsc.it4u.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.experimental.ExtensionMethod;
import vn.tpsc.it4u.models.ClientSegment;
import vn.tpsc.it4u.payloads.ClientSegmentSummary;
import vn.tpsc.it4u.repository.ClientSegmentRepository;
import vn.tpsc.it4u.utils.StringUtils;

/**
 * ClientSegmentService
 */

@Service
@ExtensionMethod({ StringUtils.class })
public class ClientSegmentService {

	@Autowired
	private ClientSegmentRepository clientSegmentRepository;

	public List<ClientSegmentSummary> findAll() {
		List<ClientSegment> clientSegments = clientSegmentRepository.findAll();
		List<ClientSegmentSummary> listClientSegment = clientSegments.stream()
				.map(clientSegment -> new ClientSegmentSummary(clientSegment.getName()))
				.collect(Collectors.toList());
		return listClientSegment;
	}
}
