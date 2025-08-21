package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Result {
    private final ResultType resultType;
    private final List<String> messages;

    public Result(ResultType resultType, String... messages) {
        this.resultType = resultType;
        this.messages = Arrays.stream(messages).toList();
    }

    public Result(ResultType resultType, List<String> messages) {
        this.resultType = resultType;
        this.messages = new ArrayList<>(messages);
    }

    public ResultType getResultType() {
        return resultType;
    }

    public List<String> getMessages() {
        return messages;
    }
}
