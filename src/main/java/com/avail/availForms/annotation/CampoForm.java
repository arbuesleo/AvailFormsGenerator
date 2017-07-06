package com.avail.availForms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.avail.availForms.enuns.TipoCampo;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD}) 
public @interface CampoForm {	
	public String label();
	public String[] opcoes() default "";
	public TipoCampo tipo() default TipoCampo.TEXTO;
	public boolean editavel() default true;
	public boolean pesquisavel() default false;
}
