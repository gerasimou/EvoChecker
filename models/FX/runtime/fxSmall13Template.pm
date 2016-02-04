//FOREX dtmc model (Simplified: 4 operations with 3 services for each operation)
//Adaptation Step13: Disruptive event - 50% of services implementations fail

dtmc


//flag indicating whether a service is selected or not (will be assembled based on the chromosome value)
const int op1S1 = mod(op1Code,2)>0?1:0;
const int op1S2 = mod(op1Code,4)>1?1:0;
const int op1S3 = mod(op1Code,8)>3?1:0;
const int op2S1 = mod(op2Code,2)>0?1:0;
const int op2S2 = mod(op2Code,4)>1?1:0;
const int op2S3 = mod(op2Code,8)>3?1:0;
const int op4S1 = mod(op4Code,2)>0?1:0;
const int op4S2 = mod(op4Code,4)>1?1:0;
const int op4S3 = mod(op4Code,8)>3?1:0;
const int op5S1 = mod(op5Code,2)>0?1:0;
const int op5S2 = mod(op5Code,4)>1?1:0;
const int op5S3 = mod(op5Code,8)>3?1:0;

// // user-defined params parameters
//const double op1S1Fail=0.11; //failure probability of service 1 op1
//const double op1S2Fail=0.004; //failure probability of service 2 op1
//const double op1S3Fail=0.7; //failure probability of service 3 op1

//const double op2S1Fail=0.3; //failure probability of service 1 op2
//const double op2S2Fail=0.5; //failure probability of service 2 op2
//const double op2S3Fail=0.006; //failure probability of service 3 op2

//const double op4S1Fail=0.9; //failure probability of service 1 op3
//const double op4S2Fail=0.11; //failure probability of service 2 op3
//const double op4S3Fail=0.006; //failure probability of service 3 op3

//const double op5S1Fail=0.5; //failure probability of service 1 op4
//const double op5S2Fail=0.4; //failure probability of service 2 op4
//const double op5S3Fail=0.002; //failure probability of service 3 op4


// user-defined params parameters: Services response time
const double op1S1Time=2.5; //response time of service 1 op1
const double op1S2Time=1.8; //response time of service 2 op1
const double op1S3Time=2.1; //response time of service 3 op1

const double op2S1Time=2.2; //response time of service 1 op2
const double op2S2Time=3.2; //response time of service 2 op2
const double op2S3Time=3.8; //response time of service 3 op2

const double op4S1Time=3.8; //response time of service 1 op3
const double op4S2Time=2.9; //response time of service 2 op3
const double op4S3Time=3.5; //response time of service 3 op3

const double op5S1Time=4.1; //response time of service 1 op4
const double op5S2Time=3.7; //response time of service 2 op4
const double op5S3Time=3.4; //response time of service 3 op4


const int STEPMAX  = 4;

/////////////
//Workflow
/////////////
module forex
	//local state
	state : [0..15] init 0;

	//Init
	[fxStart]	state = 0 	->	0.66 : (state'=1) + 0.34 : (state'=9);

	//Op1: Market watch
	[startOp1]		state = 1	->	1.0  : (state'=2);  //invoke op1
	[endOp1Fail]	state = 2 	->	1.0  : (state'=5);	//failed op1
	[endOp1Succ] 	state = 2	->	1.0  : (state'=3);  	//succ   op1

	//Op2: Technical Analysis
	[startOp2]		state = 3	->	1.0  : (state'=4);	//invoke op2
	[endOp2Fail]	state = 4   ->	1.0  : (state'=5);	//failed op2
	[endOp2Succ] 	state = 4	->	1.0  : (state'=6);  	//succ   op2

	//Technical analysis result
	[taResult]		state=6 	->	0.61 : (state'=1) + 0.28 : (state'=11) + 0.11 : (state'=7);

	//Op3: Alarm
	[startOp3]		state=7		->	1.0  : (state'=8);
	[endOp3Fail]	state=8		->	1.0  : (state'=5);
	[endOp3Succ]	state=8		->	1.0  : (state'=13);

	//Op4: Fundamental Analysis
	[startOp4]		state=9		-> 	1.0  : (state'=10);
	[endOp4Fail]	state = 10 	->	1.0  : (state'=5);	//failed op4
	[endOp4Succ]	state=10	->	0.53  : (state'=0) + 0.27 : (state'=11) + 0.20 : (state'=9); //succ op4

	//Op5: Place Order
	[startOp5]		state=11	->	1.0  : (state'=12);
	[endOp5Fail]	state = 12 	->	1.0  : (state'=5);	//failed op2
	[endOp5Succ]	state=12	->	1.0  : (state'=13);

	//Op6: Notify trader
	[startOp6]		state=13	->	1.0  : (state'=14);
	[endOp6Fail]	state=14	->	1.0  : (state'=5);
	[endOp6Succ]	state=14	->	1.0  : (state'=15);

	//End
	[fxFail]		state = 5	->	1.0  : (state'=5);	//failed fx
	[fxSucc]		state = 15	->	1.0  : (state'=0);	//succ   fx
endmodule



/////////////
//Operation 3: Alarm
/////////////
module strategyOp3
	operation3 : [0..2] init 0;

	//select a service probabilistically
	[startOp3] 	operation3 = 0 	->	0.00001 : (operation3'=1) + 0.99999 : (operation3'=2);

	[endOp3Fail]	operation3 = 1	->	1.0 : (operation3'=0);//failed
	[endOp3Succ]	operation3 = 2	->	1.0 : (operation3'=0); //succ
endmodule



/////////////
//Operation 6: Notify trader
/////////////
module strategyOp6
	operation6 : [0..2] init 0;

	//select a service probabilistically
	[startOp6] 		operation6 = 0 	->	0.00001 : (operation6'=1) + 0.99999 : (operation6'=2); 

	[endOp6Fail]	operation6 = 1	->	1.0 : (operation6'=0);//failed
	[endOp6Succ]	operation6 = 2	->	1.0 : (operation6'=0); //succ
endmodule


////////////
//Rewards
////////////
rewards "time"
	//OP1: SEQ
	operation1 = 2 & (STRATEGYOP1>0) : op1S1Time *op1S1;
	operation1 = 3 & (STRATEGYOP1>0) : op1S2Time *op1S2;
	operation1 = 4 & (STRATEGYOP1>0) : op1S3Time *op1S3;
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : op1S1Time;
	operation1 = 3 & (STRATEGYOP1=0) : op1S2Time;
	operation1 = 4 & (STRATEGYOP1=0) : op1S3Time;

	//OP2: SEQ
	operation2 = 2 & (STRATEGYOP2>0) : op2S1Time *op2S1;
	operation2 = 3 & (STRATEGYOP2>0) : op2S2Time *op2S2;
	operation2 = 4 & (STRATEGYOP2>0) : op2S3Time *op2S3;
	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : op2S1Time ;
	operation2 = 3 & (STRATEGYOP2=0) : op2S2Time ;
	operation2 = 4 & (STRATEGYOP2=0) : op2S3Time ;

	//OP3
	operation3 = 1 | operation3 = 2 : 1.5 ;

	//OP4: SEQ		
	operation4 = 2 & (STRATEGYOP4>0) : op4S1Time *op4S1;
	operation4 = 3 & (STRATEGYOP4>0) : op4S2Time *op4S2;
	operation4 = 4 & (STRATEGYOP4>0) : op4S3Time *op4S3;
	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : op4S1Time ;
	operation4 = 3 & (STRATEGYOP4=0) : op4S2Time ;
	operation4 = 4 & (STRATEGYOP4=0) : op4S3Time ;

	//OP5: SEQ
	operation5 = 2 & (STRATEGYOP5>0) : op5S1Time *op5S1;
	operation5 = 3 & (STRATEGYOP5>0) : op5S2Time *op5S2;
	operation5 = 4 & (STRATEGYOP5>0) : op5S3Time *op5S3;
	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : op5S1Time;
	operation5 = 3 & (STRATEGYOP5=0) : op5S2Time;
	operation5 = 4 & (STRATEGYOP5=0) : op5S3Time;

	//OP6
	operation6 = 1 | operation6 = 2 : 2.5 ;
endrewards




rewards "cost"
	//OP1: SEQ
	operation1 = 2 & (STRATEGYOP1>0) : 3  *op1S1;
	operation1 = 3 & (STRATEGYOP1>0) : 15 *op1S2;
	operation1 = 4 & (STRATEGYOP1>0) : 8  *op1S3;
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : 3;
	operation1 = 3 & (STRATEGYOP1=0) : 15;
	operation1 = 4 & (STRATEGYOP1=0) : 8;
	//OP1: PAR cost
	// operation1 = 1 & STRATEGYOP1=0 : 20 * op1S1 + 15 * op1S2 + 50 * op1S3;
	
	//OP2: SEQ
	operation2 = 2 & (STRATEGYOP2>0) : 13 *op2S1;
	operation2 = 3 & (STRATEGYOP2>0) : 6  *op2S2;
	operation2 = 4 & (STRATEGYOP2>0) : 4  *op2S3;
	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : 13  ;
	operation2 = 3 & (STRATEGYOP2=0) : 6 ;
	operation2 = 4 & (STRATEGYOP2=0) : 4 ;
	//OP2: PAR cost
	// operation2 = 1 & STRATEGYOP2=0 : 3 * op2S1 + 25 * op2S2 + 15 * op2S3;

	//OP3: 
	operation3 = 1 | operation3 = 2: 7;

	//OP4: SEQ
	operation4 = 2 & (STRATEGYOP4>0) : 3.8 *op4S1;
	operation4 = 3 & (STRATEGYOP4>0) : 1.5 *op4S2;
	operation4 = 4 & (STRATEGYOP4>0) : 10  *op4S3;
	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : 3.8 ;
	operation4 = 3 & (STRATEGYOP4=0) : 1.5 ;
	operation4 = 4 & (STRATEGYOP4=0) : 10  ;
	//OP4: PAR cost
	// operation4 = 1 & STRATEGYOP4=0 : 10 * op4S1 + 1.5 * op4S2 + 3.8 * op4S3;

	//OP5: SEQ
	operation5 = 2 & (STRATEGYOP5>0) : 5  *op5S1;
	operation5 = 3 & (STRATEGYOP5>0) : 9  *op5S2;
	operation5 = 4 & (STRATEGYOP5>0) : 12 *op5S3;
	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : 5 ;
	operation5 = 3 & (STRATEGYOP5=0) : 9 ;
	operation5 = 4 & (STRATEGYOP5=0) : 12 ;
	//OP5: PAR cost
	// operation5 = 1 & STRATEGYOP5=0 : 60 * op5S1 + 15 * op5S2 + 25 * op5S3;

	//OP3: 
	operation6 = 1 | operation6 = 2: 11;

endrewards

