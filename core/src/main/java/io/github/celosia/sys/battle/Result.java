package io.github.celosia.sys.battle;

public class Result {
    private final ResultType resultType;
    private final String[] messages;

    public Result(ResultType resultType, String... messages) {
        this.resultType = resultType;
        this.messages = messages;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public String[] getMessages() {
        return messages;
    }
}
