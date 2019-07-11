package com.drago.spring.demo.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailNotInUseValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Documented
public @interface EmailNotInUse {

	String message() default "Email already in use!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
