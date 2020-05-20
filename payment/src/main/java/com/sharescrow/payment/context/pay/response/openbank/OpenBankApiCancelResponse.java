package com.sharescrow.payment.context.pay.response.openbank;

import com.sharescrow.payment.context.pay.request.openbank.ReqList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenBankApiCancelResponse {
	private String api_tran_id; //2ffd133a-d17a-431d-a6a5
	private String api_tran_dtm; //20190910101921567
	private String rsp_code; //A0000
	private String rsp_message; //
	private String wd_bank_code_std; //097
	private String wd_bank_code_sub; //1230001
	private String wd_bank_name; //오픈은행
	private String wd_account_num_masked; //000-1230000-***
	private String wd_print_content; //출금계좌인자내역
	private String wd_account_holder_name; //허균
	private String res_cnt; //25
	private List<ReqList> res_list;
}
