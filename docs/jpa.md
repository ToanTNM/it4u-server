# JPA Notes

## Tạo default value cho Entity

>Dùng ```@Column``` hoặc ```@ColumnDefault```

  Thêm vào trước các **field** để định nghĩa default value

  Thêm ```@DynamicInsert``` trước **model** hoặc ```@Generated(GenerationTime.INSERT)``` trước **field** để lượt bỏ các **field** có giá trị null khi **insert** vào DB

  ```java
  @Column(nullable = false, columnDefinition = "varchar(20) default 'en-US'")
  // @ColumnDefault("'en-US'")
  @Generated(GenerationTime.INSERT)
  private String language;
  ```

> Dùng giá trị khởi tạo

  ```java
  @Column(name = "created_on")
  private LocalDateTime createdOn = LocalDateTime.now();
  ```

> Dùng ```@CreationTimestamp``` đối với field có giá trị DateTime

  ```java
  @Column(name = "created_on")
  @CreationTimestamp
  private LocalDateTime createdOn;
  ```