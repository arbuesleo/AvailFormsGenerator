package com.avail.availForms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Form {
	
	public enum Orientacao {
		   HORIZONTAL, VERTICAL
	}
	
	public Orientacao orientacao() default Orientacao.HORIZONTAL;	
	public String[] acoesCrud() default "INC, EDI, DEL";
	public String nomeEntidade();
}
