package com.erp.erp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Data Transfer Object for Store entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewStoreRequest {
  private String name;
  private Long clientId;
  private String address;
  /**
   * IDs of users associated with this store
   */
  private Set<Long> userIds;
}
