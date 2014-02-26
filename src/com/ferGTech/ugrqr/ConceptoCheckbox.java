package com.ferGTech.ugrqr;

public class ConceptoCheckbox 
{
	  
	 String concepto = null;
	 String codqr = null;
	 boolean selected = false;
	  
	 public ConceptoCheckbox(String concepto, String codqr, boolean selected) 
	 {
		  super();
		  this.concepto = concepto;
		  this.codqr = codqr;
		  this.selected = selected;
	 }
	  
	 public String getConcepto() 
	 {
		 return concepto;
	 }
	 public void setConcepto(String concepto) 
	 {
		 this.concepto = concepto;
	 }
	 public String getCodqr() 
	 {
		 return codqr;
	 }
	 public void setCodqr(String codqr) 
	 {
		 this.codqr = codqr;
	 }
	 
	 public boolean isSelected() 
	 {
		 return selected;
	 }
	 public void setSelected(boolean selected) 
	 {
		 this.selected = selected;
	 }
	  
	}