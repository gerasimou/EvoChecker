//FOREX dtmc model (6 operations with 4 services for each operation)
dtmc

//Evoxxx defined params
//Which services are enabled
evolve const int op1Code [1..15]; //possible combinations for services implementing operation 1
evolve const int op2Code [1..15]; //possible combinations for services implementing operation 2
evolve const int op3Code [1..15]; //possible combinations for services implementing operation 2
evolve const int op4Code [1..15]; //possible combinations for services implementing operation 4
evolve const int op5Code [1..15]; //possible combinations for services implementing operation 5
evolve const int op6Code [1..15]; //possible combinations for services implementing operation 6

//distribution for probabilistic selection
evolve distribution probOp1 [4];
evolve distribution probOp2 [4];
evolve distribution probOp3 [4];
evolve distribution probOp4 [4];
evolve distribution probOp5 [4];
evolve distribution probOp6 [4];

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


//user-define params parameters
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


/////////////
//Operation 1: Market watch
/////////////
//PROB
module strategyOp1
	operation1 : [0..10] init 0;

	//select a service probabilistically
	[startOp1] 		operation1 = 0 	->	probOp1 : (operation1'=1) + probOp1 : (operation1'=10) + probOp1 : (operation1'=6) + probOp1 : (operation1'=9); 

	[checkS11]		operation1 = 1	->	(1) : (operation1'=2);
	[runS11]     	operation1 = 2  ->	1-op1S1Fail : (operation1'=8) + op1S1Fail : (operation1'=7); 		

	[checkS12]		operation1 = 10	->	(1) : (operation1'=3);
	[runS12]     	operation1 = 3  ->	1-op1S2Fail : (operation1'=8) + op1S2Fail : (operation1'=7); 		

	[checkS13]		operation1 = 6	->	(1) : (operation1'=4);
	[runS13]     	operation1 = 4  ->	1-op1S3Fail : (operation1'=8) + op1S3Fail : (operation1'=7);

	[checkS14]		operation1 = 9 	->	(1) : (operation1'=10);
	[runS14]		operation1 = 5	->	1-op1S4Fail : (operation1'=8) + op1S4Fail : (operation1'=7);

	[endOp1Fail]	operation1 = 7	->	1.0 : (operation1'=0);//failed
	[endOp1Succ]	operation1 = 8	->	1.0 : (operation1'=0); //succ
endmodule




/////////////
//Operation 2: Technical Analysis
/////////////
//PROB
module strategyOp2
	operation2 : [0..10] init 0;

	//select a service probabilistically
	[startOp2] 		operation2 = 0 	->	probOp2 : (operation2'=1) + probOp2 : (operation2'=10) + probOp2 : (operation2'=6) + probOp2 : (operation2'=9); 

	[checkS21]		operation2 = 1	->	(1) : (operation2'=2);
	[runS21]     	operation2 = 2  ->	1-op2S1Fail : (operation2'=8) + op2S1Fail : (operation2'=7); 		

	[checkS22]		operation2 = 10	->	(1) : (operation2'=3);
	[runS22]     	operation2 = 3  ->	1-op2S2Fail : (operation2'=8) + op2S2Fail : (operation2'=7); 		

	[checkS23]		operation2 = 6	->	(1) : (operation2'=4);
	[runS23]     	operation2 = 4  ->	1-op2S3Fail : (operation2'=8) + op2S3Fail : (operation2'=7);

	[checkS24]		operation2 = 9 	->	(1) : (operation2'=10);
	[runS24]		operation2 = 5	->	1-op2S4Fail : (operation2'=8) + op2S4Fail : (operation2'=7);

	[endOp2Fail]	operation2 = 7	->	1.0 : (operation2'=0);//failed
	[endOp2Succ]	operation2 = 8	->	1.0 : (operation2'=0); //succ
endmodule



/////////////
//Operation 3: Alarm
/////////////
//PROB
module strategyOp3
	operation3 : [0..10] init 0;

	//select a service probabilistically
	[startOp3] 		operation3 = 0 	->	probOp3 : (operation3'=1) + probOp3 : (operation3'=10) + probOp3 : (operation3'=6) + probOp3 : (operation3'=9); 

	[checkS31]		operation3 = 1	->	(1) : (operation3'=2);
	[runS31]     	operation3 = 2  ->	1-op3S1Fail : (operation3'=8) + op3S1Fail : (operation3'=7); 		

	[checkS32]		operation3 = 10	->	(1) : (operation3'=3);
	[runS32]     	operation3 = 3  ->	1-op3S2Fail : (operation3'=8) + op3S2Fail : (operation3'=7); 		

	[checkS33]		operation3 = 6	->	(1) : (operation3'=4);
	[runS33]     	operation3 = 4  ->	1-op3S3Fail : (operation3'=8) + op3S3Fail : (operation3'=7);

	[checkS34]		operation3 = 9 	->	(1) : (operation3'=10);
	[runS34]		operation3 = 5	->	1-op3S4Fail : (operation3'=8) + op3S4Fail : (operation3'=7);

	[endOp3Fail]	operation3 = 7	->	1.0 : (operation3'=0);//failed
	[endOp3Succ]	operation3 = 8	->	1.0 : (operation3'=0); //succ
endmodule




/////////////
//Operation 4: Fundamental Analysis
/////////////
//PROB
module strategyOp4
	operation4 : [0..10] init 0;

	//select a service probabilistically
	[startOp4] 		operation4 = 0 	->	probOp4 : (operation4'=1) + probOp4 : (operation4'=10) + probOp4 : (operation4'=6) + probOp4 : (operation4'=9); 

	[checkS41]		operation4 = 1	->	(1) : (operation4'=2);
	[runS41]     	operation4 = 2  ->	1-op4S1Fail : (operation4'=8) + op4S1Fail : (operation4'=7); 		

	[checkS42]		operation4 = 10	->	(1) : (operation4'=3);
	[runS42]     	operation4 = 3  ->	1-op4S2Fail : (operation4'=8) + op4S2Fail : (operation4'=7); 		

	[checkS43]		operation4 = 6	->	(1) : (operation4'=4);
	[runS43]     	operation4 = 4  ->	1-op4S3Fail : (operation4'=8) + op4S3Fail : (operation4'=7);

	[checkS44]		operation4 = 9 	->	(1) : (operation4'=10);
	[runS44]		operation4 = 5	->	1-op4S4Fail : (operation4'=8) + op4S4Fail : (operation4'=7);

	[endOp4Fail]	operation4 = 7	->	1.0 : (operation4'=0);//failed
	[endOp4Succ]	operation4 = 8	->	1.0 : (operation4'=0); //succ
endmodule




/////////////
//Operation 5: 
/////////////
//PROB
module strategyOp5
	operation5 : [0..10] init 0;

	//select a service probabilistically
	[startOp5] 		operation5 = 0 	->	probOp5 : (operation5'=1) + probOp5 : (operation5'=10) + probOp5 : (operation5'=6) + probOp5 : (operation5'=9); 

	[checkS51]		operation5 = 1	->	(1) : (operation5'=2);
	[runS51]     	operation5 = 2  ->	1-op5S1Fail : (operation5'=8) + op5S1Fail : (operation5'=7); 		

	[checkS52]		operation5 = 10	->	(1) : (operation5'=3);
	[runS52]     	operation5 = 3  ->	1-op5S2Fail : (operation5'=8) + op5S2Fail : (operation5'=7); 		

	[checkS53]		operation5 = 6	->	(1) : (operation5'=4);
	[runS53]     	operation5 = 4  ->	1-op5S3Fail : (operation5'=8) + op5S3Fail : (operation5'=7);

	[checkS54]		operation5 = 9 	->	(1) : (operation5'=10);
	[runS54]		operation5 = 5	->	1-op5S4Fail : (operation5'=8) + op5S4Fail : (operation5'=7);

	[endOp5Fail]	operation5 = 7	->	1.0 : (operation5'=0);//failed
	[endop5Succ]	operation5 = 8	->	1.0 : (operation5'=0); //succ
endmodule



/////////////
//Operation 6: Notify trader
/////////////
//PROB
module strategyOp6
	operation6 : [0..10] init 0;

	//select a service probabilistically
	[startOp6] 		operation6 = 0 	->	probOp6 : (operation6'=1) + probOp6 : (operation6'=10) + probOp6 : (operation6'=6) + probOp6 : (operation6'=9); 

	[checkS61]		operation6 = 1	->	(1) : (operation6'=2);
	[runS61]     	operation6 = 2  ->	1-op6S1Fail : (operation6'=8) + op6S1Fail : (operation6'=7); 		

	[checkS62]		operation6 = 10	->	(1) : (operation6'=3);
	[runS62]     	operation6 = 3  ->	1-op6S2Fail : (operation6'=8) + op6S2Fail : (operation6'=7); 		

	[checkS63]		operation6 = 6	->	(1) : (operation6'=4);
	[runS63]     	operation6 = 4  ->	1-op6S3Fail : (operation6'=8) + op6S3Fail : (operation6'=7);

	[checkS64]		operation6 = 9 	->	(1) : (operation6'=10);
	[runS64]		operation6 = 5	->	1-op6S4Fail : (operation6'=8) + op6S4Fail : (operation6'=7);

	[endOp6Fail]	operation6 = 7	->	1.0 : (operation6'=0);//failed
	[endOp6Succ]	operation6 = 8	->	1.0 : (operation6'=0); //succ
endmodule



const int STRATEGYOP1 = 0;
const int STRATEGYOP2 = 0;
const int STRATEGYOP3 = 0;
const int STRATEGYOP4 = 0;
const int STRATEGYOP5 = 0;
const int STRATEGYOP6 = 0;


////////////
//Rewards
////////////
rewards "time"
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : 2.5;
	operation1 = 3 & (STRATEGYOP1=0) : 1.8;
	operation1 = 4 & (STRATEGYOP1=0) : 2.1;
	operation1 = 5 & (STRATEGYOP1=0) : 1.6;

	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : 2.2 ;
	operation2 = 3 & (STRATEGYOP2=0) : 3.2 ;
	operation2 = 4 & (STRATEGYOP2=0) : 3.8 ;
	operation2 = 5 & (STRATEGYOP2=0) : 4.0 ;
	
	//OP3: PROB
	operation3 = 2 & (STRATEGYOP2=0) : 1.1 ;
	operation3 = 3 & (STRATEGYOP2=0) : 1.0 ;
	operation3 = 4 & (STRATEGYOP2=0) : 2.2 ;
	operation3 = 5 & (STRATEGYOP2=0) : 1.9 ;
	
	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : 3.8 ;
	operation4 = 3 & (STRATEGYOP4=0) : 3.9 ;
	operation4 = 4 & (STRATEGYOP4=0) : 3.7 ;
	operation4 = 5 & (STRATEGYOP4=0) : 3.4 ;

	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : 3.9 ;
	operation5 = 3 & (STRATEGYOP5=0) : 3.6 ;
	operation5 = 4 & (STRATEGYOP5=0) : 3.1 ;
	operation5 = 5 & (STRATEGYOP5=0) : 4.4 ;

	//OP6: PROB
	operation6 = 2 & (STRATEGYOP6=0) : 4.2 ;
	operation6 = 3 & (STRATEGYOP6=0) : 3.1 ;
	operation6 = 4 & (STRATEGYOP6=0) : 2.1 ;
	operation6 = 5 & (STRATEGYOP6=0) : 4.5 ;
endrewards


rewards "cost"
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : 3;
	operation1 = 3 & (STRATEGYOP1=0) : 15;
	operation1 = 4 & (STRATEGYOP1=0) : 8;
	operation1 = 5 & (STRATEGYOP1=0) : 18;

	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : 13 ;
	operation2 = 3 & (STRATEGYOP2=0) : 6  ;
	operation2 = 4 & (STRATEGYOP2=0) : 4  ;
	operation2 = 5 & (STRATEGYOP2=0) : 3  ; 
	
	//OP3: PROB
	operation3 = 2 & (STRATEGYOP2=0) : 19 ;
	operation3 = 3 & (STRATEGYOP2=0) : 22 ;
	operation3 = 4 & (STRATEGYOP2=0) : 10 ;
	operation3 = 5 & (STRATEGYOP2=0) : 11 ;
	
	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : 3.8 ;
	operation4 = 3 & (STRATEGYOP4=0) : 1.5 ;
	operation4 = 4 & (STRATEGYOP4=0) : 10  ;
	operation4 = 5 & (STRATEGYOP4=0) : 13  ;

	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : 6 ;
	operation5 = 3 & (STRATEGYOP5=0) : 8 ;
	operation5 = 4 & (STRATEGYOP5=0) : 12 ;
	operation5 = 5 & (STRATEGYOP5=0) : 3.3 ;

	//OP6: PROB
	operation6 = 2 & (STRATEGYOP6=0) : 9.5  ;
	operation6 = 3 & (STRATEGYOP6=0) : 12   ;
	operation6 = 4 & (STRATEGYOP6=0) : 13.5 ;
	operation6 = 5 & (STRATEGYOP6=0) : 7    ;  
endrewards