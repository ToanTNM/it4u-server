package vn.tpsc.it4u.models.audit;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public abstract class DateAudit implements Serializable {

	@CreatedDate
	@Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP")
	private Instant createdAt;

	@LastModifiedDate
	@Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP")
	private Instant updatedAt;
}
