package com.erp.erp.domain.model.client;

import com.erp.erp.domain.model.shared.AbstractEntity;
import com.erp.erp.domain.model.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "WHITELABEL_STORE")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Store extends AbstractEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ticketSeqGen")
  @TableGenerator(
      name           = "ticketSeqGen",
      table          = "global_sequence",
      pkColumnName   = "seq_name",
      valueColumnName= "next_val",
      pkColumnValue  = "ticket_seq",
      initialValue   = 100000,
      allocationSize = 1
  )
  @Column(name = "STORE_ID", nullable = false)
  @NotNull
  private Long id;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "CLIENT_ID", nullable = false)
  private Client client;

  @Column(name = "STORE_ADDRESS", length = 5000, nullable = false)
  private String address;

  @ManyToMany
  @JoinTable(name="USER_STORE",
    joinColumns=@JoinColumn(name="STORE_ID"),
    inverseJoinColumns=@JoinColumn(name="USER_ID"))
  private Set<User> users = new HashSet<>();
}