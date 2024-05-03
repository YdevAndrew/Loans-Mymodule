package org.jala.university.infrastructure.persistance.mocks;

import org.jala.university.domain.entity.LoanRequestFormEntity;
import org.jala.university.domain.repository.LoanRequestFormRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LoanRequestFormMock implements LoanRequestFormRepository {
    private final Map<UUID, LoanRequestFormEntity> requestForms = new HashMap<>();

    public LoanRequestFormMock() {

    }

    @Override
    public LoanRequestFormEntity findById(UUID id) {
        return requestForms.get(id);
    }

    @Override
    public List<LoanRequestFormEntity> findAll() {
        return requestForms.values().stream().toList();
    }

    @Override
    public LoanRequestFormEntity save(LoanRequestFormEntity loanRequestForm) {
        if (loanRequestForm.getId() == null) {
            loanRequestForm.setId(UUID.randomUUID());
        }
        requestForms.put(loanRequestForm.getId(), loanRequestForm);
        return  loanRequestForm;
    }

    @Override
    public void delete(LoanRequestFormEntity loanRequestForm) {
        requestForms.remove(loanRequestForm.getId());
    }

    @Override
    public void deleteById(UUID uuid) {
        requestForms.remove(uuid);
    }
}
