//package com.hms.controller.audit.user;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuditService {
//
//    @Autowired
//    private AuditLogRepository auditLogRepository;
//
//    public void logUserSignIn(String username) {
//        AuditLog auditLog = new AuditLog();
//        auditLog.setUsername(username);
//        auditLog.setAction("User signed in");
//        auditLogRepository.save(auditLog);
//    }
//}
