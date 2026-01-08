//package com.hms.modules;
//
//import org.hibernate.envers.RevisionListener;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//public class CustomeRevisionListiner implements RevisionListener{
//
//	@Override
//	public void newRevision(Object revisionEntity) {
//		AuditLog revisonAuditLog = (AuditLog) revisionEntity;
//		
//		//capture the user name of the authenticated user
//		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		revisonAuditLog.setModifiedBy(username);
//		
//	}
//
//}
