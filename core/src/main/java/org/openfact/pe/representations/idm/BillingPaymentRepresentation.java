package org.openfact.pe.representations.idm;

import java.math.BigDecimal;

public class BillingPaymentRepresentation {
protected BigDecimal paidAmount;
protected String instructionID;
public BigDecimal getPaidAmount() {
	return paidAmount;
}
public void setPaidAmount(BigDecimal paidAmount) {
	this.paidAmount = paidAmount;
}
public String getInstructionID() {
	return instructionID;
}
public void setInstructionID(String instructionID) {
	this.instructionID = instructionID;
}

}
