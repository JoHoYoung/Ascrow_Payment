package com.sharescrow.payment.context.pay.request.openbank;

import lombok.Data;

@Data
public class ReqList {
	private String tran_no; /// 1
	private String bank_tran_id; /// F123456789U4BC34239Z
	private String bank_code_std; /// 097
	private String account_num; /// 1101230000678
	private String account_hloder_name; /// 홍길동
	private String prInteger_content; /// 쇼핑몰환불
	private String tran_amt; /// 10000
	private String req_client_name; /// 홍길동
	private String req_client_bank_code; /// 097
	private String req_client_account_num; /// 00012300000678
	private String req_client_num; /// HONGGILDONG1234
	private String transfer_purpose; //  private String TR”,
	private String recv_bank_tran_id; // F123456789U4BCT4112Z
	private String cms_num; /// 93848103221
}
