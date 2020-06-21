package com.micro.modeltree;

import lombok.Data;

@Data
public class LogMethodAnaly {
	private Long requestcount;
	private Long successcount;
	private Long errorcount;
	private Double avgtime;
	private Long maxtime;
	private Long mintime;
}
