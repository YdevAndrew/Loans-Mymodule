package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.jala.university.commons.domain.BaseEntity;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "loan_request_status")
public class LoanRequestEntity implements BaseEntity<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String status;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name="id")
    private LoanRequestFormEntity loanRequestForm;

    public LoanRequestEntity() {
        super();
    }
}
