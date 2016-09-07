package org.openfact.models.jpa.entities.ubl;


/**
 * A document used in the negotiation of a transport service between a transport
 * user and a transport service provider.
 * @author Erik
 * @version 2.1
 * @created 07-Set.-2016 9:21:37 a. m.
 */
public class TransportExecutionPlan {

	/**
	 * Indicates whether this document is a copy (true) or not (false).
	 */
	private boolean CopyIndicator;
	/**
	 * Identifies a user-defined customization of UBL for a specific use.
	 */
	private String CustomizationID;
	/**
	 * A code signifying the status of the Transport Execution Plan (updated,
	 * cancelled, confirmed, etc.)
	 */
	private Document Status_ String DocumentStatusCode;
	/**
	 * A code signifying a reason associated with the status of a Transport Execution
	 * Plan.
	 */
	private String DocumentStatusReasonCode;
	/**
	 * A reason for the status assigned to the Transport Execution Plan, expressed in
	 * text.
	 */
	private String DocumentStatusReasonDescription;
	/**
	 * An identifier for this document, assigned by the sender.
	 */
	private String ID;
	/**
	 * The date, assigned by the sender, on which this document was issued.
	 */
	private LocalDate IssueDate;
	/**
	 * The time, assigned by the sender, at which this document was issued.
	 */
	private LocalTime IssueTime;
	/**
	 * Free-form text pertinent to this document, conveying information that is not
	 * contained explicitly in other structures.
	 */
	private String Note;
	/**
	 * Identifies an instance of executing a profile, to associate all transactions in
	 * a collaboration.
	 */
	private String ProfileExecutionID;
	/**
	 * Identifies a user-defined profile of the customization of UBL being used.
	 */
	private String ProfileID;
	/**
	 * Remarks from the transport service provider regarding the transport operations
	 * referred to in the Transport Execution Plan.
	 */
	private String TransportServiceProviderRemarks;
	/**
	 * Remarks from the transport user regarding the transport operations referred to
	 * in the Transport Execution Plan.
	 */
	private String TransportUserRemarks;
	/**
	 * Identifies the earliest version of the UBL 2 schema for this document type that
	 * defines all of the elements that might be encountered in the current instance.
	 */
	private String UBLVersionID;
	/**
	 * A universally unique identifier for an instance of this document.
	 */
	private String UUID;
	/**
	 * Indicates the current version of the Transport Execution Plan.
	 */
	private String VersionID;
	private Consignment m_Consignment;
	private Contract Transport Contract;
	private DocumentReference Transport Execution Plan DocumentReference;
	private DocumentReference Transport Execution Plan Request DocumentReference;
	private DocumentReference Transport Service Description DocumentReference;
	private DocumentReference Additional DocumentReference;
	private Location To Location;
	private Location From Location;
	private Location At Location;
	private Party Sender Party;
	private Party Transport User Party;
	private Party Receiver Party;
	private Party Bill To Party;
	private Party Transport Service Provider Party;
	private Period Service Start Time Period;
	private Period Service End Time Period;
	private Period Validity Period;
	private Period Transport User Response Required Period;
	private Period Transport Service Provider Response Required Period;
	private Signature m_Signature;
	private Transport Execution Terms m_Transport Execution Terms;
	private TransportationService Additional TransportationService;
	private TransportationService Main TransportationService;

	public Transport Execution Plan(){

	}

	public void finalize() throws Throwable {

	}
	public DocumentReference getAdditional DocumentReference(){
		return Additional DocumentReference;
	}

	public TransportationService getAdditional TransportationService(){
		return Additional TransportationService;
	}

	public Location getAt Location(){
		return At Location;
	}

	public Party getBill To Party(){
		return Bill To Party;
	}

	public Consignment getConsignment(){
		return m_Consignment;
	}

	public boolean getCopyIndicator(){
		return CopyIndicator;
	}

	public String getCustomizationID(){
		return CustomizationID;
	}

	public Document Status_ String getDocumentStatusCode(){
		return DocumentStatusCode;
	}

	public String getDocumentStatusReasonCode(){
		return DocumentStatusReasonCode;
	}

	public String getDocumentStatusReasonDescription(){
		return DocumentStatusReasonDescription;
	}

	public Location getFrom Location(){
		return From Location;
	}

	public String getID(){
		return ID;
	}

	public LocalDate getIssueDate(){
		return IssueDate;
	}

	public LocalTime getIssueTime(){
		return IssueTime;
	}

	public TransportationService getMain TransportationService(){
		return Main TransportationService;
	}

	public String getNote(){
		return Note;
	}

	public String getProfileExecutionID(){
		return ProfileExecutionID;
	}

	public String getProfileID(){
		return ProfileID;
	}

	public Party getReceiver Party(){
		return Receiver Party;
	}

	public Party getSender Party(){
		return Sender Party;
	}

	public Period getService End Time Period(){
		return Service End Time Period;
	}

	public Period getService Start Time Period(){
		return Service Start Time Period;
	}

	public Signature getSignature(){
		return m_Signature;
	}

	public Location getTo Location(){
		return To Location;
	}

	public Contract getTransport Contract(){
		return Transport Contract;
	}

	public DocumentReference getTransport Execution Plan DocumentReference(){
		return Transport Execution Plan DocumentReference;
	}

	public DocumentReference getTransport Execution Plan Request DocumentReference(){
		return Transport Execution Plan Request DocumentReference;
	}

	public Transport Execution Terms getTransport Execution Terms(){
		return m_Transport Execution Terms;
	}

	public DocumentReference getTransport Service Description DocumentReference(){
		return Transport Service Description DocumentReference;
	}

	public Party getTransport Service Provider Party(){
		return Transport Service Provider Party;
	}

	public Period getTransport Service Provider Response Required Period(){
		return Transport Service Provider Response Required Period;
	}

	public Party getTransport User Party(){
		return Transport User Party;
	}

	public Period getTransport User Response Required Period(){
		return Transport User Response Required Period;
	}

	public String getTransportServiceProviderRemarks(){
		return TransportServiceProviderRemarks;
	}

	public String getTransportUserRemarks(){
		return TransportUserRemarks;
	}

	public String getUBLVersionID(){
		return UBLVersionID;
	}

	public String getUUID(){
		return UUID;
	}

	public Period getValidity Period(){
		return Validity Period;
	}

	public String getVersionID(){
		return VersionID;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAdditional DocumentReference(DocumentReference newVal){
		Additional DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAdditional TransportationService(TransportationService newVal){
		Additional TransportationService = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setAt Location(Location newVal){
		At Location = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setBill To Party(Party newVal){
		Bill To Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setConsignment(Consignment newVal){
		m_Consignment = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCopyIndicator(boolean newVal){
		CopyIndicator = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setCustomizationID(String newVal){
		CustomizationID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDocumentStatusCode(Document Status_ String newVal){
		DocumentStatusCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDocumentStatusReasonCode(String newVal){
		DocumentStatusReasonCode = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setDocumentStatusReasonDescription(String newVal){
		DocumentStatusReasonDescription = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setFrom Location(Location newVal){
		From Location = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setID(String newVal){
		ID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setIssueDate(LocalDate newVal){
		IssueDate = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setIssueTime(LocalTime newVal){
		IssueTime = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setMain TransportationService(TransportationService newVal){
		Main TransportationService = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setNote(String newVal){
		Note = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProfileExecutionID(String newVal){
		ProfileExecutionID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setProfileID(String newVal){
		ProfileID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setReceiver Party(Party newVal){
		Receiver Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSender Party(Party newVal){
		Sender Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setService End Time Period(Period newVal){
		Service End Time Period = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setService Start Time Period(Period newVal){
		Service Start Time Period = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setSignature(Signature newVal){
		m_Signature = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTo Location(Location newVal){
		To Location = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport Contract(Contract newVal){
		Transport Contract = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport Execution Plan DocumentReference(DocumentReference newVal){
		Transport Execution Plan DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport Execution Plan Request DocumentReference(DocumentReference newVal){
		Transport Execution Plan Request DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport Execution Terms(Transport Execution Terms newVal){
		m_Transport Execution Terms = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport Service Description DocumentReference(DocumentReference newVal){
		Transport Service Description DocumentReference = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport Service Provider Party(Party newVal){
		Transport Service Provider Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport Service Provider Response Required Period(Period newVal){
		Transport Service Provider Response Required Period = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport User Party(Party newVal){
		Transport User Party = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransport User Response Required Period(Period newVal){
		Transport User Response Required Period = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransportServiceProviderRemarks(String newVal){
		TransportServiceProviderRemarks = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setTransportUserRemarks(String newVal){
		TransportUserRemarks = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUBLVersionID(String newVal){
		UBLVersionID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setUUID(String newVal){
		UUID = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setValidity Period(Period newVal){
		Validity Period = newVal;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setVersionID(String newVal){
		VersionID = newVal;
	}
}//end Transport Execution Plan