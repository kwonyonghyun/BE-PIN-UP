package com.pinup.entity;

import com.pinup.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member receiver;

    boolean isRead = false;

    private String message;

    public void read(){
        this.isRead = true;
    }

    @Builder
    public Alarm(Member receiver, String message){
        this.receiver = receiver;
        this.message = message;
    }
}