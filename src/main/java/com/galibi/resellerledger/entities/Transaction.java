package com.galibi.resellerledger.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "platform_id")
  private Platform platform;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private BigDecimal value;

  private BigDecimal platformFee;
  private BigDecimal deliveryFee;

  @Column(nullable = false)
  private LocalDate transactionDate;

  public Transaction() {}

  public Transaction(Item item, User user, String type, BigDecimal value,
      LocalDate transactionDate) {
    this.item = item;
    this.user = user;
    this.type = type;
    this.value = value;
    this.transactionDate = transactionDate;
  }

  public Long getId() {
    return id;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Platform getPlatform() {
    return platform;
  }

  public void setPlatform(Platform platform) {
    this.platform = platform;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getPlatformFee() {
    return platformFee;
  }

  public void setPlatformFee(BigDecimal platformFee) {
    this.platformFee = platformFee;
  }

  public BigDecimal getDeliveryFee() {
    return deliveryFee;
  }

  public void setDeliveryFee(BigDecimal deliveryFee) {
    this.deliveryFee = deliveryFee;
  }

  public LocalDate getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(LocalDate transactionDate) {
    this.transactionDate = transactionDate;
  }
}
