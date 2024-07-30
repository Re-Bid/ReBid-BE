package server.rebid.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatType {
    system("지시문"),
    user("질문"),
    assistant("답변");

    private final String description;
}
