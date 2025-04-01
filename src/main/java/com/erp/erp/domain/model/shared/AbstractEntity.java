///*
// * ====================================================================================
// *
// * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
// *
// * ====================================================================================
// */
//
//package com.erp.erp.domain.model.shared;
//
//import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
//import java.time.Instant;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EntityListeners;
//import javax.persistence.MappedSuperclass;
//import javax.persistence.Version;
//import org.springframework.data.annotation.CreatedBy;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedBy;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.ToString;
//
///**
// * Abstract Implementation of the {@link Entity} to provide default behavior. Entity classes must Extend this Abstract
// * Class.
// *
// * @author srnagesh
// */
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
//@Getter
//@ToString
//@EqualsAndHashCode
//public abstract class AbstractEntity<T> implements Entity<T> {
//
//  @Column(name = "CREATED_BY", nullable = false, length = 240)
//  @CreatedBy
//  private String createdBy;
//
//  @Column(name = "CREATION_DATE", nullable = false)
//  @JsonSerialize(using = InstantSerializer.class)
//  @CreatedDate
//  private Instant creationDate;
//
//  @Column(name = "LAST_UPDATED_BY", nullable = false, length = 240)
//  @LastModifiedBy
//  private String lastUpdatedBy;
//
//  @Column(name = "LAST_UPDATE_DATE", nullable = false)
//  @JsonSerialize(using = InstantSerializer.class)
//  @LastModifiedDate
//  private Instant lastUpdateDate;
//
//  @Column(name = "VERSION")
//  @Version
//  private long version;
//}
