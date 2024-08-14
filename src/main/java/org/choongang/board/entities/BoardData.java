package org.choongang.board.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.entities.BaseEntity;
import org.choongang.member.entities.Member;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_board_data", columnList = "notice DESC, createdAt DESC"))//검색을 위한 인덱스 만들기
public class BoardData extends BaseEntity {
    @Id
    @GeneratedValue
    private Long seq;

    @Column(length = 65, nullable = false,updatable = false)
    private String gid;//파일을 위한 gid

    @JoinColumn(name = "bid")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //비회원일때는 비밀번호로 글 수정
    @Column(length = 65)
    private String guestPw;

    @Column(length = 60)
    private String category;//게시글 분류

    private boolean notice;//공지글 여부

    @Column(length = 40, nullable = false)
    private String poster;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    private int viewCount;//조회수

    //에디터를 사용해서 글 작성했는지 여부
    private boolean editorView;

    @Column(length = 20,updatable = false)
    private String ip;//ip 주소

    @Column(updatable = false)
    private String ua;//User-Agent

    // 정수 추가 필드
    private Long num1;
    private Long num2;
    private Long num3;

    //한줄 텍스트 추가 필드
    @Column(length = 100)
    private String text1;
    @Column(length = 100)
    private String text2;
    @Column(length = 100)
    private String text3;

    //여러줄 텍스트 추가 필드
    @Lob
    private String longText1;
    @Lob
    private String longText2;
}
