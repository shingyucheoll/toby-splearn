package tobyspring.splearn;

import java.util.function.Consumer;

import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.springframework.test.json.JsonPathValueAssert;

import tobyspring.splearn.domain.member.MemberRegisterRequest;

public class AssertThatUtils {

	public static @NonNull Consumer<AssertProvider<JsonPathValueAssert>> nonNull() {
		return value -> Assertions.assertThat(value).isNotNull();
	}

	public static @NonNull Consumer<AssertProvider<JsonPathValueAssert>> equalsTo(MemberRegisterRequest request) {
		return value -> Assertions.assertThat(value).isEqualTo(request.email());
	}
}
