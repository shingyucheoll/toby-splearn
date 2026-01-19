package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmailTest {

    @Test
    void equality() {
        var email1 = new Email("toby@splearn.app");
        var email2 = new Email("toby@splearn.app");

        // 자바의 record가 갖는 특징 중 하나는 component값을 비교하여 equals 연산자를 생성해줍니다.
        // 단 Class 를 사용하여 값 객체를 생성할 때는 Equals & Hashcode를 구현해서 사용해야 합니다. ( lombok - @EqualsAndHashCode )
        assertThat(email1).isEqualTo(email2);
    }

}