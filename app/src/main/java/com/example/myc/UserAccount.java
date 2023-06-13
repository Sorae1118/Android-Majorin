package com.example.myc;

import java.io.Serializable;

/*
    사용자 계정 정보 모델 클래스
 */
public class UserAccount implements Serializable {
    private String idToken;     // Firebase Uid (고유 정보 토큰)
    private String emailId;     // 이메일 아이디
    private String password;    // 비밀번호
    private String nickName;    // 닉네임
    private String image;    // 이미지

    public UserAccount() {}

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String imageUrl) {
        this.image = imageUrl;
    }
}
