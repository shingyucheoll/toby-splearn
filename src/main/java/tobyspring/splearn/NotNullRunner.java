package tobyspring.splearn;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// @NullMarked         해당 범위 내 모든 타입은 기본적으로 non-null   *** 중요해요
// @Nullable           null 허용
// @NullUnmarked       null 검사 제외 (예외 처리용)

@NullMarked
public class NotNullRunner {

	static void main() {
		String name = null;
		print(name);
	}

	// Nullable -> 빌드 통과  -  사용하지 않을 시 NullMarked로 인해 빌드 에러 발생 (해당 클래스 내부 모든 값은 NonNull로 선언됨)
	static void print(@Nullable String name) {
		System.out.println(name);
	}
}
