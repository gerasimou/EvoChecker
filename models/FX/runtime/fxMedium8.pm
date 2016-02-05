//FOREX dtmc model (Simplified: 6 operations with 4 services for each operation)
//Adaptation Step8: Nominal services reliability & response time
dtmc

//flag indicating whether a service is selected or not (will be assembled based on the chromosome value)
const int op1S1 = mod(op1Code,2)>0?1:0;
const int op1S2 = mod(op1Code,4)>1?1:0;
const int op1S3 = mod(op1Code,8)>3?1:0;
const int op1S4 = mod(op1Code,16)>7?1:0;

const int op2S1 = mod(op2Code,2)>0?1:0;
const int op2S2 = mod(op2Code,4)>1?1:0;
const int op2S3 = mod(op2Code,8)>3?1:0;
const int op2S4 = mod(op2Code,16)>7?1:0;

const int op3S1 = mod(op3Code,2)>0?1:0;
const int op3S2 = mod(op3Code,4)>1?1:0;
const int op3S3 = mod(op3Code,8)>3?1:0;
const int op3S4 = mod(op3Code,16)>7?1:0;

const int op4S1 = mod(op4Code,2)>0?1:0;
const int op4S2 = mod(op4Code,4)>1?1:0;
const int op4S3 = mod(op4Code,8)>3?1:0;
const int op4S4 = mod(op4Code,16)>7?1:0;

const int op5S1 = mod(op5Code,2)>0?1:0;
const int op5S2 = mod(op5Code,4)>1?1:0;
const int op5S3 = mod(op5Code,8)>3?1:0;
const int op5S4 = mod(op5Code,16)>7?1:0;

const int op6S1 = mod(op6Code,2)>0?1:0;
const int op6S2 = mod(op6Code,4)>1?1:0;
const int op6S3 = mod(op6Code,8)>3?1:0;
const int op6S4 = mod(op6Code,16)>7?1:0;


// user-defined params parameters: Services Reliability
const double op1S1Fail=0.011; //failure probability of service 1 op1
const double op1S2Fail=0.004; //failure probability of service 2 op1
const double op1S3Fail=0.007; //failure probability of service 3 op1
const double op1S4Fail=0.002; //failure probability of service 4 op1

const double op2S1Fail=0.003; //failure probability of service 1 op1
const double op2S2Fail=0.005; //failure probability of service 2 op1
const double op2S3Fail=0.006; //failure probability of service 3 op1
const double op2S4Fail=0.008; //failure probability of service 4 op1

const double op3S1Fail=0.008; //failure probability of service 1 op3
const double op3S2Fail=0.005; //failure probability of service 2 op3
const double op3S3Fail=0.015; //failure probability of service 3 op3
const double op3S4Fail=0.009; //failure probability of service 4 op3

const double op4S1Fail=0.009; //failure probability of service 1 op4
const double op4S2Fail=0.011; //failure probability of service 2 op4
const double op4S3Fail=0.006; //failure probability of service 3 op4
const double op4S4Fail=0.004; //failure probability of service 4 op4

const double op5S1Fail=0.005; //failure probability of service 1 op5
const double op5S2Fail=0.004; //failure probability of service 2 op5
const double op5S3Fail=0.002; //failure probability of service 3 op5
const double op5S4Fail=0.008; //failure probability of service 4 op5

const double op6S1Fail=0.008; //failure probability of service 1 op6
const double op6S2Fail=0.005; //failure probability of service 2 op6
const double op6S3Fail=0.004; //failure probability of service 3 op6
const double op6S4Fail=0.009; //failure probability of service 4 op6


// user-defined params parameters: Services response time
const double op1S1Time=2.5; //response time of service 1 op1
const double op1S2Time=1.8; //response time of service 2 op1
const double op1S3Time=2.1; //response time of service 3 op1
const double op1S4Time=1.6; //response time of service 3 op1

const double op2S1Time=2.2; //response time of service 1 op2
const double op2S2Time=3.2; //response time of service 2 op2
const double op2S3Time=3.8; //response time of service 3 op2
const double op2S4Time=4.0; //response time of service 3 op2

const double op3S1Time=1.1; //response time of service 1 op3
const double op3S2Time=1.0; //response time of service 2 op3
const double op3S3Time=2.2; //response time of service 3 op3
const double op3S4Time=2.9; //response time of service 3 op3

const double op4S1Time=3.8; //response time of service 1 op4
const double op4S2Time=2.9; //response time of service 2 op4
const double op4S3Time=3.5; //response time of service 3 op4
const double op4S4Time=3.4; //response time of service 3 op4

const double op5S1Time=4.1; //response time of service 1 op5
const double op5S2Time=3.7; //response time of service 2 op5
const double op5S3Time=3.4; //response time of service 3 op5
const double op5S4Time=4.4; //response time of service 3 op5

const double op6S1Time=4.2; //response time of service 1 op6
const double op6S2Time=3.1; //response time of service 2 op6
const double op6S3Time=2.1; //response time of service 3 op6
const double op6S4Time=4.5; //response time of service 3 op6


const int STEPMAX  = 5;

/////////////
//Workflow
/////////////
module forex
	//local state
	state : [0..15] init 0;
	//Init
	[fxStart]	state = 0 	->	0.66 : (state'=1) + 0.34 : (state'=9);

	//Op1: Market watch
	[startOp1]		state = 1	->	1.0  : (state'=2);  	//invoke op1
	[endOp1Fail]	state = 2 	->	1.0  : (state'=5);	//failed op1
	[endOp1Succ] 	state = 2	->	1.0  : (state'=3) ;  	//succ   op1

	//Op2: Technical Analysis
	[startOp2]		state = 3	->	1.0  : (state'=4);	//invoke op2
	[endOp2Fail]	state = 4	->	1.0  : (state'=5);	//failed op2
	[endOp2Succ]	state = 4	->	1.0  : (state'=6);	//succ   op2

	//Technical analysis result
	[taResult]		state=6 	->	0.61 : (state'=1) + 0.28 : (state'=11) + 0.11 : (state'=7);

	//Op3: Alarm
	[startOp3]		state=7		->	1.0  : (state'=8);
	[endOp3Fail]	state=8		->	1.0  : (state'=5);
	[endOp3Succ]	state=8		->	1.0  : (state'=13);

	//Op4: Fundamental Analysis
	[startOp4]		state=9		-> 	1.0  : (state'=10);
	[endOp4Fail]	state=10	->	1.0  : (state'=5);
	[endOp4Succ]	state=10	->	0.53  : (state'=0) + 0.27 : (state'=11) + 0.20 : (state'=9);

	//Op5: Place Order
	[startOp5]		state=11	->	1.0  : (state'=12);
	[endOp5Fail]	state=12	->	1.0  : (state'=5);
	[endOp5Succ]	state=12	->	1.0  : (state'=13);

	//Op6: Notify trader
	[startOp6]		state=13	->	1.0  : (state'=14);
	[endOp6Fail]	state=14	->	1.0  : (state'=5);
	[endOp6Succ]	state=14	->	1.0  : (state'=15);

	//End
	[fxFail]		state = 5	->	1.0  : (state'=5);	//failed fx
	[fxSucc]		state = 15	->	1.0  : (state'=15);	//succ   fx
endmodule


////////////
//Rewards
////////////
rewards "time"
	//OP1: SEQ
	operation1 = 2 & (STRATEGYOP1>0) : op1S1Time *op1S1;
	operation1 = 3 & (STRATEGYOP1>0) : op1S2Time *op1S2;
	operation1 = 4 & (STRATEGYOP1>0) : op1S3Time *op1S3;
	operation1 = 5 & (STRATEGYOP1>0) : op1S4Time *op1S4;
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : op1S1Time;
	operation1 = 3 & (STRATEGYOP1=0) : op1S2Time;
	operation1 = 4 & (STRATEGYOP1=0) : op1S3Time;
	operation1 = 5 & (STRATEGYOP1=0) : op1S4Time;

	//OP2: SEQ
	operation2 = 2 & (STRATEGYOP2>0) : op2S1Time *op2S1;
	operation2 = 3 & (STRATEGYOP2>0) : op2S2Time *op2S2;
	operation2 = 4 & (STRATEGYOP2>0) : op2S3Time *op2S3;
	operation2 = 5 & (STRATEGYOP2>0) : op2S4Time *op2S4; 
	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : op2S1Time ;
	operation2 = 3 & (STRATEGYOP2=0) : op2S2Time ;
	operation2 = 4 & (STRATEGYOP2=0) : op2S3Time ;
	operation2 = 5 & (STRATEGYOP2=0) : op2S4Time ;
	
	//OP3: SEQ
	operation3 = 2 & (STRATEGYOP2>0) : op3S1Time *op3S1;
	operation3 = 3 & (STRATEGYOP2>0) : op3S2Time *op3S2;
	operation3 = 4 & (STRATEGYOP2>0) : op3S3Time *op3S3;
	operation3 = 5 & (STRATEGYOP2>0) : op3S4Time *op3S4;
	//OP3: PROB
	operation3 = 2 & (STRATEGYOP2=0) : op3S1Time ;
	operation3 = 3 & (STRATEGYOP2=0) : op3S2Time ;
	operation3 = 4 & (STRATEGYOP2=0) : op3S3Time ;
	operation3 = 5 & (STRATEGYOP2=0) : op3S4Time ;

	//OP4: SEQ		
	operation4 = 2 & (STRATEGYOP4>0) : op4S1Time *op4S1;
	operation4 = 3 & (STRATEGYOP4>0) : op4S2Time *op4S2;
	operation4 = 4 & (STRATEGYOP4>0) : op4S3Time *op4S3;
	operation4 = 5 & (STRATEGYOP4>0) : op4S4Time *op4S4;
	
	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : op4S1Time ;
	operation4 = 3 & (STRATEGYOP4=0) : op4S2Time ;
	operation4 = 4 & (STRATEGYOP4=0) : op4S3Time ;
	operation4 = 5 & (STRATEGYOP4=0) : op4S4Time ;

	//OP5: SEQ
	operation5 = 2 & (STRATEGYOP5>0) : op5S1Time *op5S1;
	operation5 = 3 & (STRATEGYOP5>0) : op5S2Time *op5S2;
	operation5 = 4 & (STRATEGYOP5>0) : op5S3Time *op5S3;
	operation5 = 5 & (STRATEGYOP5>0) : op5S4Time *op5S4;
	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : op5S1Time ;
	operation5 = 3 & (STRATEGYOP5=0) : op5S2Time ;
	operation5 = 4 & (STRATEGYOP5=0) : op5S3Time ;
	operation5 = 5 & (STRATEGYOP5=0) : op5S4Time ;

	//OP6: SEQ
	operation6 = 2 & (STRATEGYOP6>0) : op6S1Time *op6S1;
	operation6 = 3 & (STRATEGYOP6>0) : op6S2Time *op6S2;
	operation6 = 4 & (STRATEGYOP6>0) : op6S3Time *op6S3;
	operation6 = 5 & (STRATEGYOP6>0) : op6S4Time *op6S4;
	//OP6: PROB
	operation6 = 2 & (STRATEGYOP6=0) : op6S1Time ;
	operation6 = 3 & (STRATEGYOP6=0) : op6S2Time ;
	operation6 = 4 & (STRATEGYOP6=0) : op6S3Time ;
	operation6 = 5 & (STRATEGYOP6=0) : op6S4Time ;
endrewards


rewards "cost"
	//OP1: SEQ
	operation1 = 2 & (STRATEGYOP1>0) : 3  *op1S1;
	operation1 = 3 & (STRATEGYOP1>0) : 15 *op1S2;
	operation1 = 4 & (STRATEGYOP1>0) : 8  *op1S3;
	operation1 = 5 & (STRATEGYOP1>0) : 18 *op1S4;
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : 3;
	operation1 = 3 & (STRATEGYOP1=0) : 15;
	operation1 = 4 & (STRATEGYOP1=0) : 8;
	operation1 = 5 & (STRATEGYOP1=0) : 18;

	//OP2: SEQ
	operation2 = 2 & (STRATEGYOP2>0) : 13 *op2S1;
	operation2 = 3 & (STRATEGYOP2>0) : 6  *op2S2;
	operation2 = 4 & (STRATEGYOP2>0) : 4  *op2S3;
	operation2 = 5 & (STRATEGYOP2>0) : 3  *op2S4; 
	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : 13 ;
	operation2 = 3 & (STRATEGYOP2=0) : 6  ;
	operation2 = 4 & (STRATEGYOP2=0) : 4  ;
	operation2 = 5 & (STRATEGYOP2=0) : 3  ; 
	
	//OP3: SEQ
	operation3 = 2 & (STRATEGYOP2>0) : 19 *op3S1;
	operation3 = 3 & (STRATEGYOP2>0) : 22 *op3S2;
	operation3 = 4 & (STRATEGYOP2>0) : 10 *op3S3;
	operation3 = 5 & (STRATEGYOP2>0) : 11 *op3S4;
	//OP3: PROB
	operation3 = 2 & (STRATEGYOP2=0) : 19 ;
	operation3 = 3 & (STRATEGYOP2=0) : 22 ;
	operation3 = 4 & (STRATEGYOP2=0) : 10 ;
	operation3 = 5 & (STRATEGYOP2=0) : 11 ;

	//OP4: SEQ		
	operation4 = 2 & (STRATEGYOP4>0) : 3.8 *op4S1;
	operation4 = 3 & (STRATEGYOP4>0) : 1.5 *op4S2;
	operation4 = 4 & (STRATEGYOP4>0) : 10  *op4S3;
	operation4 = 5 & (STRATEGYOP4>0) : 13  *op4S4;
	
	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : 3.8 ;
	operation4 = 3 & (STRATEGYOP4=0) : 1.5 ;
	operation4 = 4 & (STRATEGYOP4=0) : 10  ;
	operation4 = 5 & (STRATEGYOP4=0) : 13  ;

	//OP5: SEQ
	operation5 = 2 & (STRATEGYOP5>0) : 6  *op5S1;
	operation5 = 3 & (STRATEGYOP5>0) : 8  *op5S2;
	operation5 = 4 & (STRATEGYOP5>0) : 12 *op5S3;
	operation5 = 5 & (STRATEGYOP5>0) : 3.3 *op5S4;
	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : 6 ;
	operation5 = 3 & (STRATEGYOP5=0) : 8 ;
	operation5 = 4 & (STRATEGYOP5=0) : 12 ;
	operation5 = 5 & (STRATEGYOP5=0) : 3.3 ;

	//OP6: SEQ
	operation6 = 2 & (STRATEGYOP6>0) : 9.5  *op6S1;
	operation6 = 3 & (STRATEGYOP6>0) : 12   *op6S2;
	operation6 = 4 & (STRATEGYOP6>0) : 13.5 *op6S3;
	operation6 = 5 & (STRATEGYOP6>0) : 7    *op6S4;
	//OP6: PROB
	operation6 = 2 & (STRATEGYOP6=0) : 9.5  ;
	operation6 = 3 & (STRATEGYOP6=0) : 12   ;
	operation6 = 4 & (STRATEGYOP6=0) : 13.5 ;
	operation6 = 5 & (STRATEGYOP6=0) : 7    ;  
endrewards

