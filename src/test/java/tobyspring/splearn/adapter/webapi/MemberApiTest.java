package tobyspring.splearn.adapter.webapi;

import static org.assertj.core.api.Assertions.*;
import static tobyspring.splearn.AssertThatUtils.*;
import static tobyspring.splearn.AssertThatUtils.nonNull;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class MemberApiTest {
	final MockMvcTester mvcTester;
	final ObjectMapper objectMapper;
	final MemberRepository memberRepository;
	final MemberRegister memberRegister;

	@Test
	void register() throws UnsupportedEncodingException {
		MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
		String requestJson = objectMapper.writeValueAsString(request);

		MvcTestResult result = mvcTester.post()
			.uri("/api/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson)
			.exchange();

		assertThat(result)
			.hasStatusOk()
			.bodyJson()
			.hasPathSatisfying("$.memberId", nonNull())
			.hasPathSatisfying("$.email", equalsTo(request));

		MemberRegisterResponse response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			MemberRegisterResponse.class
		);

		Member member = memberRepository.findById(response.memberId()).orElseThrow();

		assertThat(member.getEmail().address()).isEqualTo(request.email());
		assertThat(member.getNickname()).isEqualTo(request.nickname());
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void duplicateEmail() {
		memberRegister.register(MemberFixture.createMemberRegisterRequest());

		MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
		String requestJson = objectMapper.writeValueAsString(request);

		MvcTestResult result = mvcTester.post()
			.uri("/api/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestJson)
			.exchange();

		assertThat(result)
			.apply(MockMvcResultHandlers.print())
			.hasStatus(HttpStatus.CONFLICT);

	}
}
