package io.github.celosia.sys.battle;

import java.util.Arrays;
import java.util.List;

public record Result(ResultType resultType, List<String> messages) {
	Result(ResultType resultType, String... messages) {
		this(resultType, Arrays.asList(messages));
	}
}
