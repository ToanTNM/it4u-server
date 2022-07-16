package vn.tpsc.it4u.payloads;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserProfile {
	private Long id;
	private String username;
	private String name;
	private Instant joinedAt;
	private Long pollCount;
	private Long voteCount;
}