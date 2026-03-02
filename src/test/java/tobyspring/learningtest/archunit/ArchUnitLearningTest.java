package tobyspring.learningtest.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

@AnalyzeClasses(packages = "tobyspring.learningtest.archunit")
public class ArchUnitLearningTest {
	/**
	 * Application 클래스를 의존하는 클래스는 Application, Adapter Layer의 클래스만 가능합니다.
	 */
	@ArchTest
	void application(JavaClasses classes) {
		// application 패키지 내부의 클래스는
		classes().that().resideInAPackage("..application..")
			// application, adapter package의 클래스만 의존관계를 가질 수 있습니다.
			.should().onlyHaveDependentClassesThat().resideInAnyPackage("..application..", "..adapter..")
			.check(classes);
	}

	/**
	 * Application 클래스는 Adapter의 클래스를 의존하면 안됩니다. (DIP)
	 */
	@ArchTest
	void adapter(JavaClasses classes) {
		// application 패키지 내부의 클래스는
		noClasses().that().resideInAPackage("..application..")
			// adapter 패키지 클래스에 의존하면 안 된다.
			.should().dependOnClassesThat().resideInAPackage("..adapter..")
			.check(classes);
	}

	/**
	 * Domain 클래스는 domain, java 표준 라이브러리의 클래스만 의존해야 합니다.
	 */
	@ArchTest
	void domain(JavaClasses classes) {
		// domain 패키지 내부의 클래스는
		classes().that().resideInAPackage("..domain..")
			// domain 혹은 java 표준 클래스만 의존 가능하다.
			.should().onlyDependOnClassesThat().resideInAnyPackage("..domain..", "java..")
			.check(classes);
	}
}
