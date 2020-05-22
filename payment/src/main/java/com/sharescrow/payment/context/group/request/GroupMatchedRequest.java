package com.sharescrow.payment.context.group.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMatchedRequest {
	private Integer groupId;
	private List<Integer> orderIdList;
}
