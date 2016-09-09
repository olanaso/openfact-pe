package org.openfact.models.jpa.entities.ubl;
import java.util.List; 
import java.util.ArrayList; 
import org.openfact.models.jpa.entities.ublType.*;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PaymentMeansCodeType;


/**
 * A class to describe a means of payment.
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:16:39 a. m.
 */
public class PaymentMeansEntity{

	/**
	 * An identifier for this means of payment.
	 */
	private IdentifierType ID; 
	/**
	 * An identifier for the payment instruction.
	 */
	private IdentifierType instructionID; 
	/**
	 * Free-form text conveying information that is not contained explicitly in other
	 * structures.
	 */
	private TextType instructionNote; 
	/**
	 * A code signifying the payment channel for this means of payment.
	 */
	private CodeType paymentChannelCode; 
	/**
	 * The date on which payment is due for this means of payment.
	 */
	private DateType paymentDueDate; 
	/**
	 * An identifier for a payment made using this means of payment.
	 */
	private IdentifierType paymentID; 
	/**
	 * A code signifying the type of this means of payment.
	 */
	private PaymentMeansCodeType paymentMeansCode; 
	private List<CardAccountEntity> cardAccounts = new ArrayList<>(); 
	private List<CreditAccountEntity> creditAccounts = new ArrayList<>(); 
	private FinancialAccountEntity payeeFinancialAccount; 
	private FinancialAccountEntity payerFinancialAccount; 
	private List<PaymentMandateEntity> paymentMandates = new ArrayList<>(); 
	private List<TradeFinancingEntity> tradeFinancings = new ArrayList<>(); 

}
