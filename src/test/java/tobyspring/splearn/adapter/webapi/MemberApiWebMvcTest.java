package tobyspring.splearn.adapter.webapi;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import lombok.RequiredArgsConstructor;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiWebMvcTest {

	final MockMvcTester mvcTester;
	final ObjectMapper objectMapper;

	@MockitoBean
	private MemberRegister memberRegister;

	@Test
	void register() {
		Member member = MemberFixture.createMember(1L);
		when(memberRegister.register(any())).thenReturn(member);

		MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
		String requestJson = objectMapper.writeValueAsString(request);

		assertThat(mvcTester.post()
			.uri("/api/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.hasStatusOk()
			.bodyJson()
			.extractingPath("$.memberId").asNumber().isEqualTo(1);

		verify(memberRegister).register(request);
	}

	@Test
	void registerFail() {
		MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest("invalid email");
		String requestJson = objectMapper.writeValueAsString(request);

		assertThat(mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
			.content(requestJson))
			.hasStatus(HttpStatus.BAD_REQUEST);
	}
}