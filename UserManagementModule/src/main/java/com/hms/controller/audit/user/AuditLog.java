//package com.hms.controller.audit.user;
//import jakarta.persistence.*;
//import org.hibernate.annotations.CreationTimestamp;
//import java.time.LocalDateTime;
//
//
//@Entity
//@Table(name = "audit_logs")
//public class AuditLog {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    
//    private String username;
//    private String action;
//    
//    @CreationTimestamp
//    private LocalDateTime timestamp;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getAction() {
//		return action;
//	}
//
//	public void setAction(String action) {
//		this.action = action;
//	}
//
//	public LocalDateTime getTimestamp() {
//		return timestamp;
//	}
//
//	public void setTimestamp(LocalDateTime timestamp) {
//		this.timestamp = timestamp;
//	}
//    
//    
//}
