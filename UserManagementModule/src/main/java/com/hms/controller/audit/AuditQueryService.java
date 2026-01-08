package com.hms.controller.audit;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class AuditQueryService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	
    @Transactional
	public List<Object[]> getUserSignupHistory(){
		 if (entityManager == null) {
	            throw new IllegalStateException("EntityManager is null! Ensure it's properly injected.");
	        }
		 // Convert EntityManager to Hibernate Session
		 Session session = entityManager.unwrap(Session.class);

		    // Create AuditReader
		    AuditReader auditReader = AuditReaderFactory.get(session);
		    @SuppressWarnings("unchecked")
	        List<Object[]> result = (List<Object[]>) auditReader.createQuery()
	                .forRevisionsOfEntity(UserSignupAudit.class, false, true)
	                .getResultList();

	        return result;
	}

}
