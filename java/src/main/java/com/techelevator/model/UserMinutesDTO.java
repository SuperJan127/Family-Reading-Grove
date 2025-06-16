package com.techelevator.model;


    public class UserMinutesDTO {
        private int userId;
        private String username;
        
        private int totalMinutes;
    
        // Constructors, getters, setters
        public UserMinutesDTO() {
        }
        public UserMinutesDTO(int userId, String username, int totalMinutes) {
            this.userId = userId;
            this.username = username;
            
            this.totalMinutes = totalMinutes;
        }
        public int getUserId() {
            return userId;
        }
        public void setUserId(int userId) {
            this.userId = userId;
        }
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public int getTotalMinutes() {
            return totalMinutes;
        }
        public void setTotalMinutes(int totalMinutes) {
            this.totalMinutes = totalMinutes;
        }
        
    }
