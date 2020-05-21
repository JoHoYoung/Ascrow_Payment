package com.sharescrow.payment.context.pay.request.openbank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OpenBankApiCancelRequest {
	private String cntr_account_type; // N
	private String cntr_account_num; // 00012345678901234
	private String wd_pass_phrase; // 790d56ed........6b821a69
	private String wd_print_content; // 환불금액
	private String name_check_option; // on
	private String sub_frnc_name; //  하위가맹점,
	private String sub_frnc_num; // 123456789012”,
	private String sub_frnc_business_num; // 1234567890”,
	private String tran_dtime; // 20190910101921
	private String req_cnt; // 25
	private List<ReqList> req_list;
}
