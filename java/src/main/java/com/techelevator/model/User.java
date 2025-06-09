package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

   private int id;
   private String username;
   @JsonIgnore
   private String password;
   @JsonIgnore
   private boolean activated;
   
   private Integer familyId;
   private String role;
   

   public User() { }

   public User(int id, String username, String password, String role) {
      this.id = id;
      this.username = username;
      this.password = password;
      this.role = role;
      this.activated = true;
      
   }
   
   public User(int id, String username, String password, boolean activated, String role,  Integer familyId) {
      this.id = id;
      this.username = username;
      this.password = password;
      this.activated = activated;
      this.role = role;
      this.familyId = familyId;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public boolean isActivated() {
      return activated;
   }

   public void setActivated(boolean activated) {
      this.activated = activated;
   }

   
   public Integer getFamilyId(){
      return familyId;
   }

   public void setFamilyId(Integer familyId) {
      this.familyId = familyId;
   }
   public String getRole() {
      return role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return id == user.id &&
              activated == user.activated &&
              Objects.equals(username, user.username) &&
              Objects.equals(password, user.password) &&
              Objects.equals(role, user.role);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, username, password, activated, role);
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", activated=" + activated +
              ", role=" + role +
              '}';
   }
}
