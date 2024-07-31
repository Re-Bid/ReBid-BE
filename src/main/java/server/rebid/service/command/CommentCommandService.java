package server.rebid.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.rebid.common.exception.GeneralException;
import server.rebid.common.exception.GlobalErrorCode;
import server.rebid.dto.response.CommentResponse;
import server.rebid.entity.Comment;
import server.rebid.entity.Member;
import server.rebid.repository.CommentRepository;
import server.rebid.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public CommentResponse.CommentId addComment(Long memberId, Long materialId, String content) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(GlobalErrorCode.COMMENT_NOT_FOUND));
        Comment comment = Comment.builder()
                .content(content)
                .build();
        Long commentId = commentRepository.save(comment).getId();
        return CommentResponse.CommentId.builder().commentId(commentId).build();
    }
}
