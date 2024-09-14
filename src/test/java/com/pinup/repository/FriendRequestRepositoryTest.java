package com.pinup.repository;

import com.pinup.constants.TestConstants;
import com.pinup.domain.friend.FriendRequest;
import com.pinup.domain.member.entity.Member;
import com.pinup.global.enums.FriendRequestStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pinup.constants.TestConstants.*;
import static com.pinup.global.enums.FriendRequestStatus.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FriendRequestRepositoryTest {

    private Member sender;
    private Member receiver;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() throws Exception {
        friendRequestRepository.deleteAll();
        memberRepository.deleteAll();

        sender = Member.builder()
                .email(TEST_EMAIL)
                .name(TEST_NAME)
                .loginType(TEST_LOGIN_TYPE)
                .profileImageUrl(TEST_IMAGE_URL)
                .nickname(TEST_NICKNAME)
                .socialId(TEST_SOCIAL_ID)
                .build();
        memberRepository.save(sender);

        receiver = Member.builder()
                .email(SECOND_TEST_EMAIL)
                .name(SECOND_TEST_NAME)
                .loginType(TEST_LOGIN_TYPE)
                .profileImageUrl(SECOND_TEST_IMAGE_URL)
                .nickname(SECOND_TEST_NICKNAME)
                .socialId(TEST_SOCIAL_ID)
                .build();
        memberRepository.save(receiver);
    }

    @Test
    @DisplayName("친구 요청을 id로 조회할 수 있다.")
    void 레포지토리에_친구요청이_있으면_id로_조회할_수_있다(){
        //given
        FriendRequest friendRequest = FriendRequest.builder()
                .friendRequestStatus(PENDING)
                .sender(sender)
                .receiver(receiver)
                .build();

        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        //when
        Optional<FriendRequest> findByIdFriendRequest = friendRequestRepository.findById(savedFriendRequest.getId());

        //then
        assertThat(findByIdFriendRequest).isPresent();
        assertThat(findByIdFriendRequest.get().getId()).isEqualTo(savedFriendRequest.getId());
        assertThat(findByIdFriendRequest.get().getFriendRequestStatus()).isEqualTo(PENDING);
        assertThat(findByIdFriendRequest.get().getSender()).isEqualTo(sender);
        assertThat(findByIdFriendRequest.get().getReceiver()).isEqualTo(receiver);
    }

    @Test
    @DisplayName("친구 요청을 sender와 receiver로 조회할 수 있다.")
    void findBySenderAndReceiver_테스트() {
        // given
        FriendRequest friendRequest = FriendRequest.builder()
                .friendRequestStatus(PENDING)
                .sender(sender)
                .receiver(receiver)
                .build();
        FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);

        // when
        List<FriendRequest> foundRequests = friendRequestRepository.findBySenderAndReceiver(sender, receiver);

        // then
        assertThat(foundRequests).hasSize(1);
        assertThat(foundRequests.get(0)).isEqualTo(savedFriendRequest);
    }

    @Test
    @DisplayName("sender로 친구 요청을 페이지네이션하여 조회할 수 있다.")
    void findBySender_테스트() {
        // given
        List<FriendRequest> savedRequests = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FriendRequest friendRequest = FriendRequest.builder()
                    .friendRequestStatus(PENDING)
                    .sender(sender)
                    .receiver(receiver)
                    .build();
            savedRequests.add(friendRequestRepository.save(friendRequest));
        }

        // when
        Page<FriendRequest> foundRequests = friendRequestRepository.findBySender(sender, PageRequest.of(0, 3));

        // then
        assertThat(foundRequests.getContent()).hasSize(3);
        assertThat(foundRequests.getTotalElements()).isEqualTo(5);
        assertThat(foundRequests.getContent()).containsExactlyElementsOf(savedRequests.subList(0, 3));
    }

    @Test
    @DisplayName("receiver로 친구 요청을 페이지네이션하여 조회할 수 있다.")
    void findByReceiver_테스트() {
        // given
        List<FriendRequest> savedRequests = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FriendRequest friendRequest = FriendRequest.builder()
                    .friendRequestStatus(PENDING)
                    .sender(sender)
                    .receiver(receiver)
                    .build();
            savedRequests.add(friendRequestRepository.save(friendRequest));
        }

        // when
        Page<FriendRequest> foundRequests = friendRequestRepository.findByReceiver(receiver, PageRequest.of(0, 3));

        // then
        assertThat(foundRequests.getContent()).hasSize(3);
        assertThat(foundRequests.getTotalElements()).isEqualTo(5);
        assertThat(foundRequests.getContent()).containsExactlyElementsOf(savedRequests.subList(0, 3));
    }
}