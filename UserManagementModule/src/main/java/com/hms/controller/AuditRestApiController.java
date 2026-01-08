package com.hms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.controller.audit.AuditQueryService;
//import com.hms.controller.audit.user.AuditLog;
//import com.hms.controller.audit.user.AuditLogRepository;



@RestController
@RequestMapping("/api/v1/audit")
public class AuditRestApiController {
	
	private final AuditQueryService auditQueryService;

	public AuditRestApiController(AuditQueryService auditQueryService) {
		super();
		this.auditQueryService = auditQueryService;
	}
	

	@GetMapping
	public ResponseEntity<?> getSignupHistory(){
		List<Object[]> history = auditQueryService.getUserSignupHistory();
		System.out.println("audit history: " + history);
		return ResponseEntity.ok(history);
	}
	
	
	
//	
//	 @Autowired
//    private AuditLogRepository auditLogRepository;
//    
//    @GetMapping
//    public List<AuditLog> getAuditLogs() {
//        return auditLogRepository.findAll();
//    }
//	
}
