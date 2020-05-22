package com.sharescrow.payment.context.pay.request.openbank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenBankApiRequest {

	private String bank_tran_id; //F123456789U4BC34239Z
	private String cntr_account_type; //N
	private String cntr_account_num; //00012345678901234
	private String dps_prInteger_content; //쇼핑몰환불
	private String fIntegerech_use_num; //123456789012345678901234
	private String wd_prInteger_content; //오픈뱅킹출금
	private String tran_amt; //10000
	private String tran_dtime; //20190910101921
	private String req_client_name; //홍길동
	private String req_client_bank_code; //097
	private String req_client_account_num; //1101230000678
	private String req_client_num; //HONGGILDONG1234
	private String transfer_purpose; //TR
	private String sub_frnc_name; //하위가맹점
	private String sub_frnc_num; //123456789012
	private String sub_frnc_business_num; //1234567890
	private String recv_client_name; //김오픈
	private String recv_client_bank_code; //097
	private String recv_client_account_num; //232000067812

}
