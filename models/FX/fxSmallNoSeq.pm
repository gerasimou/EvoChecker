//FOREX dtmc model (Simplified: 4 operations with 3 services for each operation)
dtmc

//Evoxxx defined params
//Which services are enabled
evolve const int op1Code [1..7]; //possible combinations for services implementing operation 1
evolve const int op2Code [1..7]; //possible combinations for services implementing operation 2
evolve const int op4Code [1..7]; //possible combinations for services implementing operation 4
evolve const int op5Code [1..7]; //possible combinations for services implementing operation 3

//distribution for probabilistic selection
evolve distribution probOp1 [3];
evolve distribution probOp2 [3];
evolve distribution probOp4 [3];
evolve distribution probOp5 [3];

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
const double op1S1Fail=0.011; //failure probability of service 1 op1
const double op1S2Fail=0.004; //failure probability of service 2 op1
const double op1S3Fail=0.007; //failure probability of service 3 op1

const double op2S1Fail=0.003; //failure probability of service 1 op1
const double op2S2Fail=0.005; //failure probability of service 2 op1
const double op2S3Fail=0.006; //failure probability of service 3 op1

const double op4S1Fail=0.009; //failure probability of service 1 op1
const double op4S2Fail=0.011; //failure probability of service 2 op1
const double op4S3Fail=0.006; //failure probability of service 3 op1

const double op5S1Fail=0.005; //failure probability of service 1 op1
const double op5S2Fail=0.004; //failure probability of service 2 op1
const double op5S3Fail=0.002; //failure probability of service 3 op1

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
//Operation 1: Market watch
/////////////
//PROB
module strategyOp1
	operation1 : [0..8] init 0;

	//select a service probabilistically
	[startOp1] 	operation1 = 0 	->	probOp1 : (operation1'=1) + probOp1 : (operation1'=5) + probOp1 : (operation1'=6); 

	[checkS11]	operation1 = 1	->	(1) : (operation1'=2);// + (1-op1S1) : (operation1'=7); //service1 start
	[runS11]     	operation1 = 2  ->	1-op1S1Fail : (operation1'=8) + op1S1Fail : (operation1'=7); 		

	[checkS12]	operation1 = 5	->	(1) : (operation1'=3);// + (1-op1S2) : (operation1'=7); //service1 start
	[runS12]     	operation1 = 3  ->	1-op1S2Fail : (operation1'=8) + op1S2Fail : (operation1'=7); 		

	[checkS13]	operation1 = 6	->	(1) : (operation1'=4);// + (1-op1S3) : (operation1'=7); //service1 start
	[runS13]     	operation1 = 4  ->	1-op1S3Fail : (operation1'=8) + op1S3Fail : (operation1'=7); 		

	[endOp1Fail]	operation1 = 7	->	1.0 : (operation1'=0);//failed
	[endOp1Succ]	operation1 = 8	->	1.0 : (operation1'=0); //succ
endmodule



/////////////
//Operation 2: Technical Analysis
/////////////
//PROB
module strategyOp2 
	operation2 : [0..8] init 0;

	//select a service probabilistically
	[startOp2] 	operation2 = 0 	->	probOp2 : (operation2'=1) + probOp2 : (operation2'=5) + probOp2 : (operation2'=6); 

	[checkS21]	operation2 = 1	->	(1) : (operation2'=2);// + (1-op2S1) : (operation2'=7); //service1 start
	[runS21]     	operation2 = 2  ->	1-op2S1Fail : (operation2'=8) + op2S1Fail : (operation2'=7); 		

	[checkS22]	operation2 = 5	->	(1) : (operation2'=3);// + (1-op2S2) : (operation2'=7); //service1 start
	[runS22]     	operation2 = 3  ->	1-op2S2Fail : (operation2'=8) + op2S2Fail : (operation2'=7); 		

	[checkS23]	operation2 = 6	->	(1) : (operation2'=4);// + (1-op2S3) : (operation2'=7); //service1 start
	[runS23]     	operation2 = 4  ->	1-op2S3Fail : (operation2'=8) + op2S3Fail : (operation2'=7); 		

	[endOp2Fail]	operation2 = 7	->	1.0 : (operation2'=0);//failed
	[endOp2Succ]	operation2 = 8	->	1.0 : (operation2'=0); //succ
endmodule



/////////////
//Operation 3: Alarm
/////////////
//PROB
module strategyOp3
	operation3 : [0..2] init 0;

	//select a service probabilistically
	[startOp3] 	operation3 = 0 	->	0.00001 : (operation3'=1) + 0.99999 : (operation3'=2);

	[endOp3Fail]	operation3 = 1	->	1.0 : (operation3'=0);//failed
	[endOp3Succ]	operation3 = 2	->	1.0 : (operation3'=0); //succ
endmodule



/////////////
//Operation 4: Fundamental Analysis
/////////////
//PROB
module strategyOp4
	operation4 : [0..8] init 0;

	//select a service probabilistically
	[startOp4] 	operation4 = 0 	->	probOp4 : (operation4'=1) + probOp4 : (operation4'=5) + probOp4 : (operation4'=6); 

	[checkS41]	operation4 = 1	->	(1) : (operation4'=2);// + (1-op4S1) : (operation4'=7); //service1 start
	[runS41]     	operation4 = 2  ->	1-op4S1Fail : (operation4'=8) + op4S1Fail : (operation4'=7); 		

	[checkS42]	operation4 = 5	->	(1) : (operation4'=3);// + (1-op4S2) : (operation4'=7); //service1 start
	[runS42]     	operation4 = 3  ->	1-op4S2Fail : (operation4'=8) + op4S2Fail : (operation4'=7); 		

	[checkS43]	operation4 = 6	->	(1) : (operation4'=4);// + (1-op4S3) : (operation4'=7); //service1 start
	[runS43]     	operation4 = 4  ->	1-op4S3Fail : (operation4'=8) + op4S3Fail : (operation4'=7); 		

	[endOp4Fail]	operation4 = 7	->	1.0 : (operation4'=0);//failed
	[endOp4Succ]	operation4 = 8	->	1.0 : (operation4'=0); //succ
endmodule


/////////////
//Operation 5: 
/////////////
//SEQ
//PROB
module strategyOp5
	operation5 : [0..8] init 0;

	//select a service probabilistically
	[startOp5] 	operation5 = 0 	->	probOp5 : (operation5'=1) + probOp5 : (operation5'=5) + probOp5 : (operation5'=6); 

	[checkS51]	operation5 = 1	->	(1) : (operation5'=2);// + (1-op5S1) : (operation5'=7); //service1 start
	[runS51]     	operation5 = 2  ->	1-op5S1Fail : (operation5'=8) + op5S1Fail : (operation5'=7); 		

	[checkS52]	operation5 = 5	->	(1) : (operation5'=3);// + (1-op5S2) : (operation5'=7); //service1 start
	[runS52]     	operation5 = 3  ->	1-op5S2Fail : (operation5'=8) + op5S2Fail : (operation5'=7); 		

	[checkS53]	operation5 = 6	->	(1) : (operation5'=4);// + (1-op5S3) : (operation5'=7); //service1 start
	[runS53]     	operation5 = 4  ->	1-op5S3Fail : (operation5'=8) + op5S3Fail : (operation5'=7); 		

	[endOp5Fail]	operation5 = 7	->	1.0 : (operation5'=0);//failed
	[endOp5Succ]	operation5 = 8	->	1.0 : (operation5'=0); //succ
endmodule



/////////////
//Operation 6: Notify trader
/////////////
//PROB
module strategyOp6
	operation6 : [0..2] init 0;

	//select a service probabilistically
	[startOp6] 		operation6 = 0 	->	0.00001 : (operation6'=1) + 0.99999 : (operation6'=2); 

	[endOp6Fail]	operation6 = 1	->	1.0 : (operation6'=0);//failed
	[endOp6Succ]	operation6 = 2	->	1.0 : (operation6'=0); //succ
endmodule

const int STRATEGYOP1 = 0;
const int STRATEGYOP2 = 0;
const int STRATEGYOP4 = 0;
const int STRATEGYOP5 = 0;

////////////
//Rewards
////////////
rewards "time"
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : 2.5;
	operation1 = 3 & (STRATEGYOP1=0) : 1.8;
	operation1 = 4 & (STRATEGYOP1=0) : 2.1;

	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : 2.2 ;
	operation2 = 3 & (STRATEGYOP2=0) : 3.2 ;
	operation2 = 4 & (STRATEGYOP2=0) : 3.8 ;

	//OP3
	operation3 = 1 | operation3 = 2 : 1.5 ;

	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : 3.8 ;
	operation4 = 3 & (STRATEGYOP4=0) : 3.9 ;
	operation4 = 4 & (STRATEGYOP4=0) : 3.7 ;

	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : 4.1 ;
	operation5 = 3 & (STRATEGYOP5=0) : 3.7 ;
	operation5 = 4 & (STRATEGYOP5=0) : 3.4 ;

	//OP6
	operation6 = 1 | operation6 = 2 : 2.5 ;
endrewards




rewards "cost"
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : 3;
	operation1 = 3 & (STRATEGYOP1=0) : 15;
	operation1 = 4 & (STRATEGYOP1=0) : 8;
	
	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : 13  ;
	operation2 = 3 & (STRATEGYOP2=0) : 6 ;
	operation2 = 4 & (STRATEGYOP2=0) : 4 ;

	//OP3: 
	operation3 = 1 | operation3 = 2: 7;

	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : 3.8 ;
	operation4 = 3 & (STRATEGYOP4=0) : 1.5 ;
	operation4 = 4 & (STRATEGYOP4=0) : 10  ;

	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : 5 ;
	operation5 = 3 & (STRATEGYOP5=0) : 9 ;
	operation5 = 4 & (STRATEGYOP5=0) : 12 ;

	//OP6: 
	operation6 = 1 | operation6 = 2: 11;
endrewards
