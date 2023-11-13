package com.example.Aiking.Entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(name="fullname",length = 50,nullable = false)
    private String fullName;
    @Column(name="username",length = 50,nullable = false)
    private String username;
    @Column(name="email",length = 50,nullable = false)
    private String email;
    @Column(name="isblock")
    private Boolean isBlock;

    @Column(name="password",nullable = false , unique = true)
    private String password;
    @Column(name = "point",nullable = false)
    private int point;
    @Column(name="createDate",unique = true)
    private Date createDate;
    @Column(name="updateDate",unique = true)
    private Date updateDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="user_role",
            joinColumns =  @JoinColumn(name="userid",referencedColumnName = "userid"),
            inverseJoinColumns = @JoinColumn(name="roleid",referencedColumnName = "roleid")
    )

    private Set<Role> roleList = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> paymentList;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    public void addPosttoList(Post post) {
        this.postList.add(post);
    }
    public void addPaymentToList(Payment payment) {this.paymentList.add(payment);}

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<Role> roleList) {
        this.roleList = roleList;
    }

    public Boolean getBlock() {
        return isBlock;
    }

    public void setBlock(Boolean block) {
        isBlock = block;
    }
}
