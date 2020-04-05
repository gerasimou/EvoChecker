//FOREX dtmc model (8 operations with 4 services for each operation)
dtmc

//Evoxxx defined params
//Which services are enabled
evolve const int op1Code [1..15]; //possible combinations for services implementing operation 1
evolve const int op2Code [1..15]; //possible combinations for services implementing operation 2
evolve const int op3Code [1..15]; //possible combinations for services implementing operation 2
evolve const int op4Code [1..15]; //possible combinations for services implementing operation 4
evolve const int op5Code [1..15]; //possible combinations for services implementing operation 5
evolve const int op6Code [1..15]; //possible combinations for services implementing operation 6
evolve const int op7Code [1..15]; //possible combinations for services implementing operation 7
evolve const int op8Code [1..15]; //possible combinations for services implementing operation 78

//Sequence of service execution
evolve const int seqOp1  [1..24]; //(#services op1)!
evolve const int seqOp2  [1..24]; //(#services op2)!
evolve const int seqOp3  [1..24]; //(#services op3)!
evolve const int seqOp4  [1..24]; //(#services op3)!
evolve const int seqOp5  [1..24]; //(#services op3)!
evolve const int seqOp6  [1..24]; //(#services op3)!
evolve const int seqOp7  [1..24]; //(#services op3)!
evolve const int seqOp8  [1..24]; //(#services op3)!

//distribution for probabilistic selection
evolve distribution probOp1 [4];
evolve distribution probOp2 [4];
evolve distribution probOp3 [4];
evolve distribution probOp4 [4];
evolve distribution probOp5 [4];
evolve distribution probOp6 [4];
evolve distribution probOp7 [4];
evolve distribution probOp8 [4];

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

const int op7S1 = mod(op7Code,2)>0?1:0;
const int op7S2 = mod(op7Code,4)>1?1:0;
const int op7S3 = mod(op7Code,8)>3?1:0;
const int op7S4 = mod(op7Code,16)>7?1:0;

const int op8S1 = mod(op8Code,2)>0?1:0;
const int op8S2 = mod(op8Code,4)>1?1:0;
const int op8S3 = mod(op8Code,8)>3?1:0;
const int op8S4 = mod(op8Code,16)>7?1:0;


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

const double op7S1Fail=0.014; //failure probability of service 1 op4
const double op7S2Fail=0.007; //failure probability of service 2 op4
const double op7S3Fail=0.009; //failure probability of service 3 op4
const double op7S4Fail=0.013; //failure probability of service 4 op4

const double op8S1Fail=0.019; //failure probability of service 1 op3
const double op8S2Fail=0.008; //failure probability of service 2 op3
const double op8S3Fail=0.011; //failure probability of service 3 op3
const double op8S4Fail=0.014; //failure probability of service 4 op3


const int STEPMAX  = 5;

/////////////
//Workflow
/////////////
module forex
	//local state
	state : [0..19] init 0;
	//Init
	[fxStart]	state = 0 	->	0.66 : (state'=1) + 0.34 : (state'=9);

	//Op1: Market watch
	[startOp1]		state = 1	->	1.0  : (state'=2);  	//invoke op1
	[endOp1Fail]	state = 2 	->	1.0  : (state'=5);	//failed op1
	[endOp1Succ] 	state = 2	->	1.0  : (state'=3) ;  	//succ   op1

	//Op2: Technical Analysis
	[startOp2]		state = 3	->	1.0  : (state'=4);	//invoke op2
	[endOp2Fail]	state = 4	->	1.0  : (state'=5);	//failed op2
	[endOp2Succ]	state = 4	->	1.0  : (state'=17);	//succ   op2

	//Technical analysis result
	[taResult]		state=6 	->	0.61 : (state'=1) + 0.28 : (state'=11) + 0.11 : (state'=7);

	//Op3: Alarm
	[startOp3]	state=7		->	1.0  : (state'=8);
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

	//Op7: Notify trader 2
	[startOp7]		state=15	->	1.0  : (state'=16);
	[endOp7Fail]	state=16	->	1.0  : (state'=5);
	[endOp7Succ]	state=16	->	1.0  : (state'=19);

	//Op7: Notify trader 2
	[startOp8]		state=17	->	1.0  : (state'=18);
	[endOp8Fail]	state=18	->	1.0  : (state'=5);
	[endOp8Succ]	state=18	->	1.0  : (state'=6);

	//End
	[fxFail]		state = 5	->	1.0  : (state'=5);	//failed fx
	[fxSucc]		state = 19	->	1.0  : (state'=19);	//succ   fx
endmodule


/////////////
//Operation 1: Market watch
/////////////


//SEQ
evolve module strategyOp1
	operation1 : [0..7] init 0;
	stepOp1	 : [1..5] init 1;

	[startOp1]	operation1 = 0		-> 1.0 : (operation1'=1);

	[checkS11]	operation1 = 1 & stepOp1 = 1	-> ((seqOp1=1  | seqOp1=2  | seqOp1=3   | seqOp1=4  | seqOp1=5  | seqOp1=6)? 1 : 0) : (operation1'=2) +
                                           	   	   ((seqOp1=7  | seqOp1=8  | seqOp1=9   | seqOp1=10 | seqOp1=11 | seqOp1=12)? 1 : 0) : (operation1'=3) +
                                           	   	   ((seqOp1=13 | seqOp1=14 | seqOp1=15  | seqOp1=16 | seqOp1=17 | seqOp1=18)? 1 : 0) : (operation1'=4) +
                                           	   	   ((seqOp1=19 | seqOp1=20 | seqOp1=21  | seqOp1=22 | seqOp1=23 | seqOp1=24)? 1 : 0) : (operation1'=5);

	[checkS12]	operation1 = 1 & stepOp1 = 2	-> ((seqOp1=7 | seqOp1=8 | seqOp1=13 | seqOp1=14 | seqOp1=19 | seqOp1=20)? 1 : 0) : (operation1'=2) +
                                           	   	   ((seqOp1=1 | seqOp1=2 | seqOp1=15 | seqOp1=16 | seqOp1=21 | seqOp1=22)? 1 : 0) : (operation1'=3) +
                                           	   	   ((seqOp1=3 | seqOp1=4 | seqOp1=9  | seqOp1=10 | seqOp1=23 | seqOp1=24)? 1 : 0) : (operation1'=4) +
                                           	   	   ((seqOp1=5 | seqOp1=6 | seqOp1=11 | seqOp1=12 | seqOp1=17 | seqOp1=18)? 1 : 0) : (operation1'=5);

	[checkS13]	operation1 = 1 & stepOp1 = 3	-> ((seqOp1=9 | seqOp1=11 | seqOp1=15 | seqOp1=17 | seqOp1=21 | seqOp1=23)? 1 : 0) : (operation1'=2) +
                                           	   	   ((seqOp1=3 | seqOp1=5  | seqOp1=13 | seqOp1=18 | seqOp1=19 | seqOp1=24)? 1 : 0) : (operation1'=3) +
                                           	   	   ((seqOp1=1 | seqOp1=6  | seqOp1=7  | seqOp1=12 | seqOp1=20 | seqOp1=22)? 1 : 0) : (operation1'=4) +
                                           	   	   ((seqOp1=2 | seqOp1=4  | seqOp1=8  | seqOp1=10 | seqOp1=14 | seqOp1=16)? 1 : 0) : (operation1'=5);


	[checkS14]	operation1 = 1 & stepOp1 = 4	-> ((seqOp1=10 | seqOp1=12 | seqOp1=16 | seqOp1=18 | seqOp1=22 | seqOp1=24)? 1 : 0) : (operation1'=2) +
                                           	   	   ((seqOp1=4  | seqOp1=6  | seqOp1=14 | seqOp1=17 | seqOp1=20 | seqOp1=23)? 1 : 0) : (operation1'=3) +
                                           	   	   ((seqOp1=2  | seqOp1=5  | seqOp1=8  | seqOp1=11 | seqOp1=19 | seqOp1=21)? 1 : 0) : (operation1'=4) +
                                           	   	   ((seqOp1=1  | seqOp1=3  | seqOp1=7  | seqOp1=9  | seqOp1=13 | seqOp1=15)? 1 : 0) : (operation1'=5);


	[checkS15]	operation1 = 1 & stepOp1 > 4 -> 1.0 : (operation1'=6);

	[runS11]	operation1 = 2	-> (op1S1=1?1.0:0)*(1-op1S1Fail) : (operation1'=7) + (op1S1=0?1.0:op1S1Fail) : (operation1'=1) & (stepOp1'=min(STEPMAX,stepOp1+1));
	[runS12]	operation1 = 3	-> (op1S2=1?1.0:0)*(1-op1S2Fail) : (operation1'=7) + (op1S2=0?1.0:op1S2Fail) : (operation1'=1) & (stepOp1'=min(STEPMAX,stepOp1+1));
	[runS13]	operation1 = 4	-> (op1S3=1?1.0:0)*(1-op1S3Fail) : (operation1'=7) + (op1S3=0?1.0:op1S3Fail) : (operation1'=1) & (stepOp1'=min(STEPMAX,stepOp1+1));	
	[runS14]	operation1 = 5	-> (op1S4=1?1.0:0)*(1-op1S4Fail) : (operation1'=7) + (op1S4=0?1.0:op1S4Fail) : (operation1'=1) & (stepOp1'=min(STEPMAX,stepOp1+1));	

	[endOp1Fail]	operation1 = 6	->	1.0 : (operation1'=0);//failed
	[endOp1Succ]	operation1 = 7	->	1.0 : (operation1'=0); //succ
endmodule



//PROB
evolve module strategyOp1
	operation1 : [0..10] init 0;

	//select a service probabilistically
	[startOp1] 	operation1 = 0 	->	probOp1 : (operation1'=1) + probOp1 : (operation1'=10) + probOp1 : (operation1'=6) + probOp1 : (operation1'=9); 

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

//SEQ
evolve module strategyOp2
	operation2 : [0..7] init 0;
	stepOp2	 : [1..5] init 1;

	[startOp2]	operation2 = 0		-> 1.0 : (operation2'=1);


	[checkS21]	operation2 = 1 & stepOp2 = 1	-> ((seqOp2=1  | seqOp2=2  | seqOp2=3   | seqOp2=4  | seqOp2=5  | seqOp2=6)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=7  | seqOp2=8  | seqOp2=9   | seqOp2=10 | seqOp2=11 | seqOp2=12)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=13 | seqOp2=14 | seqOp2=15  | seqOp2=16 | seqOp2=17 | seqOp2=18)? 1 : 0) : (operation2'=4) +
                                           	   	   ((seqOp2=19 | seqOp2=20 | seqOp2=21  | seqOp2=22 | seqOp2=23 | seqOp2=24)? 1 : 0) : (operation2'=5);

	[checkS22]	operation2 = 1 & stepOp2 = 2	-> ((seqOp2=7 | seqOp2=8 | seqOp2=13 | seqOp2=14 | seqOp2=19 | seqOp2=20)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=1 | seqOp2=2 | seqOp2=15 | seqOp2=16 | seqOp2=21 | seqOp2=22)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=3 | seqOp2=4 | seqOp2=9  | seqOp2=10 | seqOp2=23 | seqOp2=24)? 1 : 0) : (operation2'=4) +
                                           	   	   ((seqOp2=5 | seqOp2=6 | seqOp2=11 | seqOp2=12 | seqOp2=17 | seqOp2=18)? 1 : 0) : (operation2'=5);

	[checkS23]	operation2 = 1 & stepOp2 = 3	-> ((seqOp2=9 | seqOp2=11 | seqOp2=15 | seqOp2=17 | seqOp2=21 | seqOp2=23)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=3 | seqOp2=5  | seqOp2=13 | seqOp2=18 | seqOp2=19 | seqOp2=24)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=1 | seqOp2=6  | seqOp2=7  | seqOp2=12 | seqOp2=20 | seqOp2=22)? 1 : 0) : (operation2'=4) +
                                           	   	   ((seqOp2=2 | seqOp2=4  | seqOp2=8  | seqOp2=10 | seqOp2=14 | seqOp2=16)? 1 : 0) : (operation2'=5);


	[checkS24]	operation2 = 1 & stepOp2 = 4	-> ((seqOp2=10 | seqOp2=12 | seqOp2=16 | seqOp2=18 | seqOp2=22 | seqOp2=24)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=4  | seqOp2=6  | seqOp2=14 | seqOp2=17 | seqOp2=20 | seqOp2=23)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=2  | seqOp2=5  | seqOp2=8  | seqOp2=11 | seqOp2=19 | seqOp2=21)? 1 : 0) : (operation2'=4) +
                                           	   	   ((seqOp2=1  | seqOp2=3  | seqOp2=7  | seqOp2=9  | seqOp2=13 | seqOp2=15)? 1 : 0) : (operation2'=5);


	[checkS25]	operation2 = 1 & stepOp2 > 4 -> 1.0 : (operation2'=6);

	[runS21]	operation2 = 2	-> (op2S1=1?1.0:0)*(1-op2S1Fail) : (operation2'=7) + (op2S1=0?1.0:op2S1Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));
	[runS22]	operation2 = 3	-> (op2S2=1?1.0:0)*(1-op2S2Fail) : (operation2'=7) + (op2S2=0?1.0:op2S2Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));
	[runS23]	operation2 = 4	-> (op2S3=1?1.0:0)*(1-op2S3Fail) : (operation2'=7) + (op2S3=0?1.0:op2S3Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));	
	[runS24]	operation2 = 5	-> (op2S4=1?1.0:0)*(1-op2S4Fail) : (operation2'=7) + (op2S4=0?1.0:op2S4Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));	

	[endOp2Fail]	operation2 = 6	->	1.0 : (operation2'=0);//failed
	[endOp2Succ]	operation2 = 7	->	1.0 : (operation2'=0); //succ
endmodule


//PROB
evolve module strategyOp2
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


//SEQ
evolve module strategyOp3
	operation3 : [0..7] init 0;
	stepOp3	 : [1..5] init 1;

	[startOp3]	operation3 = 0		-> 1.0 : (operation3'=1);


	[checkS31]	operation3 = 1 & stepOp3 = 1	-> ((seqOp3=1  | seqOp3=2  | seqOp3=3   | seqOp3=4  | seqOp3=5  | seqOp3=6)? 1 : 0) : (operation3'=2) +
                                           	   	   ((seqOp3=7  | seqOp3=8  | seqOp3=9   | seqOp3=10 | seqOp3=11 | seqOp3=12)? 1 : 0) : (operation3'=3) +
                                           	   	   ((seqOp3=13 | seqOp3=14 | seqOp3=15  | seqOp3=16 | seqOp3=17 | seqOp3=18)? 1 : 0) : (operation3'=4) +
                                           	   	   ((seqOp3=19 | seqOp3=20 | seqOp3=21  | seqOp3=22 | seqOp3=23 | seqOp3=24)? 1 : 0) : (operation3'=5);

	[checkS32]	operation3 = 1 & stepOp3 = 2	-> ((seqOp3=7 | seqOp3=8 | seqOp3=13 | seqOp3=14 | seqOp3=19 | seqOp3=20)? 1 : 0) : (operation3'=2) +
                                           	   	   ((seqOp3=1 | seqOp3=2 | seqOp3=15 | seqOp3=16 | seqOp3=21 | seqOp3=22)? 1 : 0) : (operation3'=3) +
                                           	   	   ((seqOp3=3 | seqOp3=4 | seqOp3=9  | seqOp3=10 | seqOp3=23 | seqOp3=24)? 1 : 0) : (operation3'=4) +
                                           	   	   ((seqOp3=5 | seqOp3=6 | seqOp3=11 | seqOp3=12 | seqOp3=17 | seqOp3=18)? 1 : 0) : (operation3'=5);

	[checkS33]	operation3 = 1 & stepOp3 = 3	-> ((seqOp3=9 | seqOp3=11 | seqOp3=15 | seqOp3=17 | seqOp3=21 | seqOp3=23)? 1 : 0) : (operation3'=2) +
                                           	   	   ((seqOp3=3 | seqOp3=5  | seqOp3=13 | seqOp3=18 | seqOp3=19 | seqOp3=24)? 1 : 0) : (operation3'=3) +
                                           	   	   ((seqOp3=1 | seqOp3=6  | seqOp3=7  | seqOp3=12 | seqOp3=20 | seqOp3=22)? 1 : 0) : (operation3'=4) +
                                           	   	   ((seqOp3=2 | seqOp3=4  | seqOp3=8  | seqOp3=10 | seqOp3=14 | seqOp3=16)? 1 : 0) : (operation3'=5);


	[checkS34]	operation3 = 1 & stepOp3 = 4	-> ((seqOp3=10 | seqOp3=12 | seqOp3=16 | seqOp3=18 | seqOp3=22 | seqOp3=24)? 1 : 0) : (operation3'=2) +
                                           	   	   ((seqOp3=4  | seqOp3=6  | seqOp3=14 | seqOp3=17 | seqOp3=20 | seqOp3=23)? 1 : 0) : (operation3'=3) +
                                           	   	   ((seqOp3=2  | seqOp3=5  | seqOp3=8  | seqOp3=11 | seqOp3=19 | seqOp3=21)? 1 : 0) : (operation3'=4) +
                                           	   	   ((seqOp3=1  | seqOp3=3  | seqOp3=7  | seqOp3=9  | seqOp3=13 | seqOp3=15)? 1 : 0) : (operation3'=5);


	[checkS35]	operation3 = 1 & stepOp3 > 4 -> 1.0 : (operation3'=6);

	[runS31]	operation3 = 2	-> (op3S1=1?1.0:0)*(1-op3S1Fail) : (operation3'=7) + (op3S1=0?1.0:op3S1Fail) : (operation3'=1) & (stepOp3'=min(STEPMAX,stepOp3+1));
	[runS32]	operation3 = 3	-> (op3S2=1?1.0:0)*(1-op3S2Fail) : (operation3'=7) + (op3S2=0?1.0:op3S2Fail) : (operation3'=1) & (stepOp3'=min(STEPMAX,stepOp3+1));
	[runS33]	operation3 = 4	-> (op3S3=1?1.0:0)*(1-op3S3Fail) : (operation3'=7) + (op3S3=0?1.0:op3S3Fail) : (operation3'=1) & (stepOp3'=min(STEPMAX,stepOp3+1));	
	[runS34]	operation3 = 5	-> (op3S4=1?1.0:0)*(1-op3S4Fail) : (operation3'=7) + (op3S4=0?1.0:op3S4Fail) : (operation3'=1) & (stepOp3'=min(STEPMAX,stepOp3+1));	

	[endOp3Fail]	operation3 = 6	->	1.0 : (operation3'=0);//failed
	[endOp3Succ]	operation3 = 7	->	1.0 : (operation3'=0); //succ
endmodule



//PROB
evolve module strategyOp3
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


//SEQ
evolve module strategyOp4
	operation4 : [0..7] init 0;
	stepOp4	 : [1..5] init 1;

	[startOp4]	operation4 = 0		-> 1.0 : (operation4'=1);


	[checkS41]	operation4 = 1 & stepOp4 = 1	-> ((seqOp4=1  | seqOp4=2  | seqOp4=3   | seqOp4=4  | seqOp4=5  | seqOp4=6)? 1 : 0) : (operation4'=2) +
                                           	   	   ((seqOp4=7  | seqOp4=8  | seqOp4=9   | seqOp4=10 | seqOp4=11 | seqOp4=12)? 1 : 0) : (operation4'=3) +
                                           	   	   ((seqOp4=13 | seqOp4=14 | seqOp4=15  | seqOp4=16 | seqOp4=17 | seqOp4=18)? 1 : 0) : (operation4'=4) +
                                           	   	   ((seqOp4=19 | seqOp4=20 | seqOp4=21  | seqOp4=22 | seqOp4=23 | seqOp4=24)? 1 : 0) : (operation4'=5);

	[checkS42]	operation4 = 1 & stepOp4 = 2	-> ((seqOp4=7 | seqOp4=8 | seqOp4=13 | seqOp4=14 | seqOp4=19 | seqOp4=20)? 1 : 0) : (operation4'=2) +
                                           	   	   ((seqOp4=1 | seqOp4=2 | seqOp4=15 | seqOp4=16 | seqOp4=21 | seqOp4=22)? 1 : 0) : (operation4'=3) +
                                           	   	   ((seqOp4=3 | seqOp4=4 | seqOp4=9  | seqOp4=10 | seqOp4=23 | seqOp4=24)? 1 : 0) : (operation4'=4) +
                                           	   	   ((seqOp4=5 | seqOp4=6 | seqOp4=11 | seqOp4=12 | seqOp4=17 | seqOp4=18)? 1 : 0) : (operation4'=5);

	[checkS43]	operation4 = 1 & stepOp4 = 3	-> ((seqOp4=9 | seqOp4=11 | seqOp4=15 | seqOp4=17 | seqOp4=21 | seqOp4=23)? 1 : 0) : (operation4'=2) +
                                           	   	   ((seqOp4=3 | seqOp4=5  | seqOp4=13 | seqOp4=18 | seqOp4=19 | seqOp4=24)? 1 : 0) : (operation4'=3) +
                                           	   	   ((seqOp4=1 | seqOp4=6  | seqOp4=7  | seqOp4=12 | seqOp4=20 | seqOp4=22)? 1 : 0) : (operation4'=4) +
                                           	   	   ((seqOp4=2 | seqOp4=4  | seqOp4=8  | seqOp4=10 | seqOp4=14 | seqOp4=16)? 1 : 0) : (operation4'=5);


	[checkS44]	operation4 = 1 & stepOp4 = 4	-> ((seqOp4=10 | seqOp4=12 | seqOp4=16 | seqOp4=18 | seqOp4=22 | seqOp4=24)? 1 : 0) : (operation4'=2) +
                                           	   	   ((seqOp4=4  | seqOp4=6  | seqOp4=14 | seqOp4=17 | seqOp4=20 | seqOp4=23)? 1 : 0) : (operation4'=3) +
                                           	   	   ((seqOp4=2  | seqOp4=5  | seqOp4=8  | seqOp4=11 | seqOp4=19 | seqOp4=21)? 1 : 0) : (operation4'=4) +
                                           	   	   ((seqOp4=1  | seqOp4=3  | seqOp4=7  | seqOp4=9  | seqOp4=13 | seqOp4=15)? 1 : 0) : (operation4'=5);


	[checkS45]	operation4 = 1 & stepOp4 > 4 -> 1.0 : (operation4'=6);

	[runS41]	operation4 = 2	-> (op4S1=1?1.0:0)*(1-op4S1Fail) : (operation4'=7) + (op4S1=0?1.0:op4S1Fail) : (operation4'=1) & (stepOp4'=min(STEPMAX,stepOp4+1));
	[runS42]	operation4 = 3	-> (op4S2=1?1.0:0)*(1-op4S2Fail) : (operation4'=7) + (op4S2=0?1.0:op4S2Fail) : (operation4'=1) & (stepOp4'=min(STEPMAX,stepOp4+1));
	[runS43]	operation4 = 4	-> (op4S3=1?1.0:0)*(1-op4S3Fail) : (operation4'=7) + (op4S3=0?1.0:op4S3Fail) : (operation4'=1) & (stepOp4'=min(STEPMAX,stepOp4+1));	
	[runS44]	operation4 = 5	-> (op4S4=1?1.0:0)*(1-op4S4Fail) : (operation4'=7) + (op4S4=0?1.0:op4S4Fail) : (operation4'=1) & (stepOp4'=min(STEPMAX,stepOp4+1));	

	[endOp4Fail]	operation4 = 6	->	1.0 : (operation4'=0);//failed
	[endOp4Succ]	operation4 = 7	->	1.0 : (operation4'=0); //succ
endmodule


//PROB
evolve module strategyOp4
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


//SEQ
evolve module strategyOp5
	operation5 : [0..7] init 0;
	stepOp5	 : [1..5] init 1;

	[startOp5]	operation5 = 0		-> 1.0 : (operation5'=1);


	[checkS51]	operation5 = 1 & stepOp5 = 1	-> ((seqOp5=1  | seqOp5=2  | seqOp5=3   | seqOp5=4  | seqOp5=5  | seqOp5=6)? 1 : 0) : (operation5'=2) +
                                           	   	   ((seqOp5=7  | seqOp5=8  | seqOp5=9   | seqOp5=10 | seqOp5=11 | seqOp5=12)? 1 : 0) : (operation5'=3) +
                                           	   	   ((seqOp5=13 | seqOp5=14 | seqOp5=15  | seqOp5=16 | seqOp5=17 | seqOp5=18)? 1 : 0) : (operation5'=4) +
                                           	   	   ((seqOp5=19 | seqOp5=20 | seqOp5=21  | seqOp5=22 | seqOp5=23 | seqOp5=24)? 1 : 0) : (operation5'=5);

	[checkS52]	operation5 = 1 & stepOp5 = 2	-> ((seqOp5=7 | seqOp5=8 | seqOp5=13 | seqOp5=14 | seqOp5=19 | seqOp5=20)? 1 : 0) : (operation5'=2) +
                                           	   	   ((seqOp5=1 | seqOp5=2 | seqOp5=15 | seqOp5=16 | seqOp5=21 | seqOp5=22)? 1 : 0) : (operation5'=3) +
                                           	   	   ((seqOp5=3 | seqOp5=4 | seqOp5=9  | seqOp5=10 | seqOp5=23 | seqOp5=24)? 1 : 0) : (operation5'=4) +
                                           	   	   ((seqOp5=5 | seqOp5=6 | seqOp5=11 | seqOp5=12 | seqOp5=17 | seqOp5=18)? 1 : 0) : (operation5'=5);

	[checkS53]	operation5 = 1 & stepOp5 = 3	-> ((seqOp5=9 | seqOp5=11 | seqOp5=15 | seqOp5=17 | seqOp5=21 | seqOp5=23)? 1 : 0) : (operation5'=2) +
                                           	   	   ((seqOp5=3 | seqOp5=5  | seqOp5=13 | seqOp5=18 | seqOp5=19 | seqOp5=24)? 1 : 0) : (operation5'=3) +
                                           	   	   ((seqOp5=1 | seqOp5=6  | seqOp5=7  | seqOp5=12 | seqOp5=20 | seqOp5=22)? 1 : 0) : (operation5'=4) +
                                           	   	   ((seqOp5=2 | seqOp5=4  | seqOp5=8  | seqOp5=10 | seqOp5=14 | seqOp5=16)? 1 : 0) : (operation5'=5);


	[checkS54]	operation5 = 1 & stepOp5 = 4	-> ((seqOp5=10 | seqOp5=12 | seqOp5=16 | seqOp5=18 | seqOp5=22 | seqOp5=24)? 1 : 0) : (operation5'=2) +
                                           	   	   ((seqOp5=4  | seqOp5=6  | seqOp5=14 | seqOp5=17 | seqOp5=20 | seqOp5=23)? 1 : 0) : (operation5'=3) +
                                           	   	   ((seqOp5=2  | seqOp5=5  | seqOp5=8  | seqOp5=11 | seqOp5=19 | seqOp5=21)? 1 : 0) : (operation5'=4) +
                                           	   	   ((seqOp5=1  | seqOp5=3  | seqOp5=7  | seqOp5=9  | seqOp5=13 | seqOp5=15)? 1 : 0) : (operation5'=5);


	[checkS55]	operation5 = 1 & stepOp5 > 4 -> 1.0 : (operation5'=6);

	[runS51]	operation5 = 2	-> (op5S1=1?1.0:0)*(1-op5S1Fail) : (operation5'=7) + (op5S1=0?1.0:op5S1Fail) : (operation5'=1) & (stepOp5'=min(STEPMAX,stepOp5+1));
	[runS52]	operation5 = 3	-> (op5S2=1?1.0:0)*(1-op5S2Fail) : (operation5'=7) + (op5S2=0?1.0:op5S2Fail) : (operation5'=1) & (stepOp5'=min(STEPMAX,stepOp5+1));
	[runS53]	operation5 = 4	-> (op5S3=1?1.0:0)*(1-op5S3Fail) : (operation5'=7) + (op5S3=0?1.0:op5S3Fail) : (operation5'=1) & (stepOp5'=min(STEPMAX,stepOp5+1));	
	[runS54]	operation5 = 5	-> (op5S4=1?1.0:0)*(1-op5S4Fail) : (operation5'=7) + (op5S4=0?1.0:op5S4Fail) : (operation5'=1) & (stepOp5'=min(STEPMAX,stepOp5+1));	

	[endOp5Fail]	operation5 = 6	->	1.0 : (operation5'=0);//failed
	[endop5Succ]	operation5 = 7	->	1.0 : (operation5'=0); //succ
endmodule


//PROB
evolve module strategyOp5
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


//SEQ
evolve module strategyOp6
	operation6 : [0..7] init 0;
	stepOp6	 : [1..5] init 1;

	[startOp6]	operation6 = 0		-> 1.0 : (operation6'=1);


	[checkS61]	operation6 = 1 & stepOp6 = 1	-> ((seqOp6=1  | seqOp6=2  | seqOp6=3   | seqOp6=4  | seqOp6=5  | seqOp6=6)? 1 : 0) : (operation6'=2) +
                                           	   	   ((seqOp6=7  | seqOp6=8  | seqOp6=9   | seqOp6=10 | seqOp6=11 | seqOp6=12)? 1 : 0) : (operation6'=3) +
                                           	   	   ((seqOp6=13 | seqOp6=14 | seqOp6=15  | seqOp6=16 | seqOp6=17 | seqOp6=18)? 1 : 0) : (operation6'=4) +
                                           	   	   ((seqOp6=19 | seqOp6=20 | seqOp6=21  | seqOp6=22 | seqOp6=23 | seqOp6=24)? 1 : 0) : (operation6'=5);

	[checkS62]	operation6 = 1 & stepOp6 = 2	-> ((seqOp6=7 | seqOp6=8 | seqOp6=13 | seqOp6=14 | seqOp6=19 | seqOp6=20)? 1 : 0) : (operation6'=2) +
                                           	   	   ((seqOp6=1 | seqOp6=2 | seqOp6=15 | seqOp6=16 | seqOp6=21 | seqOp6=22)? 1 : 0) : (operation6'=3) +
                                           	   	   ((seqOp6=3 | seqOp6=4 | seqOp6=9  | seqOp6=10 | seqOp6=23 | seqOp6=24)? 1 : 0) : (operation6'=4) +
                                           	   	   ((seqOp6=5 | seqOp6=6 | seqOp6=11 | seqOp6=12 | seqOp6=17 | seqOp6=18)? 1 : 0) : (operation6'=5);

	[checkS63]	operation6 = 1 & stepOp6 = 3	-> ((seqOp6=9 | seqOp6=11 | seqOp6=15 | seqOp6=17 | seqOp6=21 | seqOp6=23)? 1 : 0) : (operation6'=2) +
                                           	   	   ((seqOp6=3 | seqOp6=5  | seqOp6=13 | seqOp6=18 | seqOp6=19 | seqOp6=24)? 1 : 0) : (operation6'=3) +
                                           	   	   ((seqOp6=1 | seqOp6=6  | seqOp6=7  | seqOp6=12 | seqOp6=20 | seqOp6=22)? 1 : 0) : (operation6'=4) +
                                           	   	   ((seqOp6=2 | seqOp6=4  | seqOp6=8  | seqOp6=10 | seqOp6=14 | seqOp6=16)? 1 : 0) : (operation6'=5);


	[checkS64]	operation6 = 1 & stepOp6 = 4	-> ((seqOp6=10 | seqOp6=12 | seqOp6=16 | seqOp6=18 | seqOp6=22 | seqOp6=24)? 1 : 0) : (operation6'=2) +
                                           	   	   ((seqOp6=4  | seqOp6=6  | seqOp6=14 | seqOp6=17 | seqOp6=20 | seqOp6=23)? 1 : 0) : (operation6'=3) +
                                           	   	   ((seqOp6=2  | seqOp6=5  | seqOp6=8  | seqOp6=11 | seqOp6=19 | seqOp6=21)? 1 : 0) : (operation6'=4) +
                                           	   	   ((seqOp6=1  | seqOp6=3  | seqOp6=7  | seqOp6=9  | seqOp6=13 | seqOp6=15)? 1 : 0) : (operation6'=5);


	[checkS65]	operation6 = 1 & stepOp6 > 4 -> 1.0 : (operation6'=6);

	[runS61]	operation6 = 2	-> (op6S1=1?1.0:0)*(1-op6S1Fail) : (operation6'=7) + (op6S1=0?1.0:op6S1Fail) : (operation6'=1) & (stepOp6'=min(STEPMAX,stepOp6+1));
	[runS62]	operation6 = 3	-> (op6S2=1?1.0:0)*(1-op6S2Fail) : (operation6'=7) + (op6S2=0?1.0:op6S2Fail) : (operation6'=1) & (stepOp6'=min(STEPMAX,stepOp6+1));
	[runS63]	operation6 = 4	-> (op6S3=1?1.0:0)*(1-op6S3Fail) : (operation6'=7) + (op6S3=0?1.0:op6S3Fail) : (operation6'=1) & (stepOp6'=min(STEPMAX,stepOp6+1));	
	[runS64]	operation6 = 5	-> (op6S4=1?1.0:0)*(1-op6S4Fail) : (operation6'=7) + (op6S4=0?1.0:op6S4Fail) : (operation6'=1) & (stepOp6'=min(STEPMAX,stepOp6+1));	

	[endOp6Fail]	operation6 = 6	->	1.0 : (operation6'=0);//failed
	[endOp6Succ]	operation6 = 7	->	1.0 : (operation6'=0); //succ
endmodule


//PROB
evolve module strategyOp6
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



/////////////
//Operation 7: Notify trader 2
/////////////


//SEQ
evolve module strategyOp7
	operation7 : [0..7] init 0;
	stepOp7	 : [1..5] init 1;

	[startOp7]	operation7 = 0		-> 1.0 : (operation7'=1);


	[checkS71]	operation7 = 1 & stepOp7 = 1	-> ((seqOp7=1  | seqOp7=2  | seqOp7=3   | seqOp7=4  | seqOp7=5  | seqOp7=6)? 1 : 0) : (operation7'=2) +
                                           	   	   ((seqOp7=7  | seqOp7=8  | seqOp7=9   | seqOp7=10 | seqOp7=11 | seqOp7=12)? 1 : 0) : (operation7'=3) +
                                           	   	   ((seqOp7=13 | seqOp7=14 | seqOp7=15  | seqOp7=16 | seqOp7=17 | seqOp7=18)? 1 : 0) : (operation7'=4) +
                                           	   	   ((seqOp7=19 | seqOp7=20 | seqOp7=21  | seqOp7=22 | seqOp7=23 | seqOp7=24)? 1 : 0) : (operation7'=5);

	[checkS72]	operation7 = 1 & stepOp7 = 2	-> ((seqOp7=7 | seqOp7=8 | seqOp7=13 | seqOp7=14 | seqOp7=19 | seqOp7=20)? 1 : 0) : (operation7'=2) +
                                           	   	   ((seqOp7=1 | seqOp7=2 | seqOp7=15 | seqOp7=16 | seqOp7=21 | seqOp7=22)? 1 : 0) : (operation7'=3) +
                                           	   	   ((seqOp7=3 | seqOp7=4 | seqOp7=9  | seqOp7=10 | seqOp7=23 | seqOp7=24)? 1 : 0) : (operation7'=4) +
                                           	   	   ((seqOp7=5 | seqOp7=6 | seqOp7=11 | seqOp7=12 | seqOp7=17 | seqOp7=18)? 1 : 0) : (operation7'=5);

	[checkS73]	operation7 = 1 & stepOp7 = 3	-> ((seqOp7=9 | seqOp7=11 | seqOp7=15 | seqOp7=17 | seqOp7=21 | seqOp7=23)? 1 : 0) : (operation7'=2) +
                                           	   	   ((seqOp7=3 | seqOp7=5  | seqOp7=13 | seqOp7=18 | seqOp7=19 | seqOp7=24)? 1 : 0) : (operation7'=3) +
                                           	   	   ((seqOp7=1 | seqOp7=6  | seqOp7=7  | seqOp7=12 | seqOp7=20 | seqOp7=22)? 1 : 0) : (operation7'=4) +
                                           	   	   ((seqOp7=2 | seqOp7=4  | seqOp7=8  | seqOp7=10 | seqOp7=14 | seqOp7=16)? 1 : 0) : (operation7'=5);


	[checkS74]	operation7 = 1 & stepOp7 = 4	-> ((seqOp7=10 | seqOp7=12 | seqOp7=16 | seqOp7=18 | seqOp7=22 | seqOp7=24)? 1 : 0) : (operation7'=2) +
                                           	   	   ((seqOp7=4  | seqOp7=6  | seqOp7=14 | seqOp7=17 | seqOp7=20 | seqOp7=23)? 1 : 0) : (operation7'=3) +
                                           	   	   ((seqOp7=2  | seqOp7=5  | seqOp7=8  | seqOp7=11 | seqOp7=19 | seqOp7=21)? 1 : 0) : (operation7'=4) +
                                           	   	   ((seqOp7=1  | seqOp7=3  | seqOp7=7  | seqOp7=9  | seqOp7=13 | seqOp7=15)? 1 : 0) : (operation7'=5);


	[checkS75]	operation7 = 1 & stepOp7 > 4 -> 1.0 : (operation7'=6);

	[runS71]	operation7 = 2	-> (op7S1=1?1.0:0)*(1-op7S1Fail) : (operation7'=7) + (op7S1=0?1.0:op7S1Fail) : (operation7'=1) & (stepOp7'=min(STEPMAX,stepOp7+1));
	[runS72]	operation7 = 3	-> (op7S2=1?1.0:0)*(1-op7S2Fail) : (operation7'=7) + (op7S2=0?1.0:op7S2Fail) : (operation7'=1) & (stepOp7'=min(STEPMAX,stepOp7+1));
	[runS73]	operation7 = 4	-> (op7S3=1?1.0:0)*(1-op7S3Fail) : (operation7'=7) + (op7S3=0?1.0:op7S3Fail) : (operation7'=1) & (stepOp7'=min(STEPMAX,stepOp7+1));	
	[runS74]	operation7 = 5	-> (op7S4=1?1.0:0)*(1-op7S4Fail) : (operation7'=7) + (op7S4=0?1.0:op7S4Fail) : (operation7'=1) & (stepOp7'=min(STEPMAX,stepOp7+1));	

	[endOp7Fail]	operation7 = 6	->	1.0 : (operation7'=0);//failed
	[endOp7Succ]	operation7 = 7	->	1.0 : (operation7'=0); //succ
endmodule


//PROB
evolve module strategyOp7
	operation7 : [0..10] init 0;

	//select a service probabilistically
	[startOp7] 		operation7 = 0 	->	probOp7 : (operation7'=1) + probOp7 : (operation7'=10) + probOp7 : (operation7'=6) + probOp7 : (operation7'=9); 

	[checkS71]		operation7 = 1	->	(1) : (operation7'=2);
	[runS71]     	operation7 = 2  ->	1-op7S1Fail : (operation7'=8) + op7S1Fail : (operation7'=7); 		

	[checkS72]		operation7 = 10	->	(1) : (operation7'=3);
	[runS72]     	operation7 = 3  ->	1-op7S2Fail : (operation7'=8) + op7S2Fail : (operation7'=7); 		

	[checkS73]		operation7 = 6	->	(1) : (operation7'=4);
	[runS73]     	operation7 = 4  ->	1-op7S3Fail : (operation7'=8) + op7S3Fail : (operation7'=7);

	[checkS74]		operation7 = 9 	->	(1) : (operation7'=10);
	[runS74]		operation7 = 5	->	1-op7S4Fail : (operation7'=8) + op7S4Fail : (operation7'=7);

	[endOp7Fail]	operation7 = 7	->	1.0 : (operation7'=0);//failed
	[endOp7Succ]	operation7 = 8	->	1.0 : (operation7'=0); //succ
endmodule



/////////////
//Operation 8: Notify trader 3
/////////////


//SEQ
evolve module strategyOp8
	operation8 : [0..7] init 0;
	stepOp8	 : [1..5] init 1;

	[startOp8]	operation8 = 0		-> 1.0 : (operation8'=1);


	[checkS81]	operation8 = 1 & stepOp8 = 1	-> ((seqOp8=1  | seqOp8=2  | seqOp8=3   | seqOp8=4  | seqOp8=5  | seqOp8=6)? 1 : 0) : (operation8'=2) +
                                           	   	   ((seqOp8=7  | seqOp8=8  | seqOp8=9   | seqOp8=10 | seqOp8=11 | seqOp8=12)? 1 : 0) : (operation8'=3) +
                                           	   	   ((seqOp8=13 | seqOp8=14 | seqOp8=15  | seqOp8=16 | seqOp8=17 | seqOp8=18)? 1 : 0) : (operation8'=4) +
                                           	   	   ((seqOp8=19 | seqOp8=20 | seqOp8=21  | seqOp8=22 | seqOp8=23 | seqOp8=24)? 1 : 0) : (operation8'=5);

	[checkS82]	operation8 = 1 & stepOp8 = 2	-> ((seqOp8=7 | seqOp8=8 | seqOp8=13 | seqOp8=14 | seqOp8=19 | seqOp8=20)? 1 : 0) : (operation8'=2) +
                                           	   	   ((seqOp8=1 | seqOp8=2 | seqOp8=15 | seqOp8=16 | seqOp8=21 | seqOp8=22)? 1 : 0) : (operation8'=3) +
                                           	   	   ((seqOp8=3 | seqOp8=4 | seqOp8=9  | seqOp8=10 | seqOp8=23 | seqOp8=24)? 1 : 0) : (operation8'=4) +
                                           	   	   ((seqOp8=5 | seqOp8=6 | seqOp8=11 | seqOp8=12 | seqOp8=17 | seqOp8=18)? 1 : 0) : (operation8'=5);

	[checkS83]	operation8 = 1 & stepOp8 = 3	-> ((seqOp8=9 | seqOp8=11 | seqOp8=15 | seqOp8=17 | seqOp8=21 | seqOp8=23)? 1 : 0) : (operation8'=2) +
                                           	   	   ((seqOp8=3 | seqOp8=5  | seqOp8=13 | seqOp8=18 | seqOp8=19 | seqOp8=24)? 1 : 0) : (operation8'=3) +
                                           	   	   ((seqOp8=1 | seqOp8=6  | seqOp8=7  | seqOp8=12 | seqOp8=20 | seqOp8=22)? 1 : 0) : (operation8'=4) +
                                           	   	   ((seqOp8=2 | seqOp8=4  | seqOp8=8  | seqOp8=10 | seqOp8=14 | seqOp8=16)? 1 : 0) : (operation8'=5);


	[checkS84]	operation8 = 1 & stepOp8 = 4	-> ((seqOp8=10 | seqOp8=12 | seqOp8=16 | seqOp8=18 | seqOp8=22 | seqOp8=24)? 1 : 0) : (operation8'=2) +
                                           	   	   ((seqOp8=4  | seqOp8=6  | seqOp8=14 | seqOp8=17 | seqOp8=20 | seqOp8=23)? 1 : 0) : (operation8'=3) +
                                           	   	   ((seqOp8=2  | seqOp8=5  | seqOp8=8  | seqOp8=11 | seqOp8=19 | seqOp8=21)? 1 : 0) : (operation8'=4) +
                                           	   	   ((seqOp8=1  | seqOp8=3  | seqOp8=7  | seqOp8=9  | seqOp8=13 | seqOp8=15)? 1 : 0) : (operation8'=5);


	[checkS85]	operation8 = 1 & stepOp8 > 4 -> 1.0 : (operation8'=6);

	[runS81]	operation8 = 2	-> (op8S1=1?1.0:0)*(1-op8S1Fail) : (operation8'=7) + (op8S1=0?1.0:op8S1Fail) : (operation8'=1) & (stepOp8'=min(STEPMAX,stepOp8+1));
	[runS82]	operation8 = 3	-> (op8S2=1?1.0:0)*(1-op8S2Fail) : (operation8'=7) + (op8S2=0?1.0:op8S2Fail) : (operation8'=1) & (stepOp8'=min(STEPMAX,stepOp8+1));
	[runS83]	operation8 = 4	-> (op8S3=1?1.0:0)*(1-op8S3Fail) : (operation8'=7) + (op8S3=0?1.0:op8S3Fail) : (operation8'=1) & (stepOp8'=min(STEPMAX,stepOp8+1));	
	[runS84]	operation8 = 5	-> (op8S4=1?1.0:0)*(1-op8S4Fail) : (operation8'=7) + (op8S4=0?1.0:op8S4Fail) : (operation8'=1) & (stepOp8'=min(STEPMAX,stepOp8+1));	

	[endOp8Fail]	operation8 = 6	->	1.0 : (operation8'=0);//failed
	[endOp8Succ]	operation8 = 7	->	1.0 : (operation8'=0); //succ
endmodule


//PROB
evolve module strategyOp8
	operation8 : [0..10] init 0;

	//select a service probabilistically
	[startOp8] 		operation8 = 0 	->	probOp8 : (operation8'=1) + probOp8 : (operation8'=10) + probOp8 : (operation8'=6) + probOp8 : (operation8'=9); 

	[checkS81]		operation8 = 1	->	(1) : (operation8'=2);
	[runS81]     	operation8 = 2  ->	1-op8S1Fail : (operation8'=8) + op8S1Fail : (operation8'=7); 		

	[checkS82]		operation8 = 10	->	(1) : (operation8'=3);
	[runS82]     	operation8 = 3  ->	1-op8S2Fail : (operation8'=8) + op8S2Fail : (operation8'=7); 		

	[checkS83]		operation8 = 6	->	(1) : (operation8'=4);
	[runS83]     	operation8 = 4  ->	1-op8S3Fail : (operation8'=8) + op8S3Fail : (operation8'=7);

	[checkS84]		operation8 = 9 	->	(1) : (operation8'=10);
	[runS84]		operation8 = 5	->	1-op8S4Fail : (operation8'=8) + op8S4Fail : (operation8'=7);

	[endOp8Fail]	operation8 = 7	->	1.0 : (operation8'=0);//failed
	[endOp8Succ]	operation8 = 8	->	1.0 : (operation8'=0); //succ
endmodule




////////////
//Rewards
////////////
rewards "time"
	//OP1: SEQ
	operation1 = 2 & (STRATEGYOP1>0) : 2.5 *op1S1;
	operation1 = 3 & (STRATEGYOP1>0) : 1.8 *op1S2;
	operation1 = 4 & (STRATEGYOP1>0) : 2.1 *op1S3;
	operation1 = 5 & (STRATEGYOP1>0) : 1.6 *op1S4;
	//OP1: PROB
	operation1 = 2 & (STRATEGYOP1=0) : 2.5;
	operation1 = 3 & (STRATEGYOP1=0) : 1.8;
	operation1 = 4 & (STRATEGYOP1=0) : 2.1;
	operation1 = 5 & (STRATEGYOP1=0) : 1.6;

	//OP2: SEQ
	operation2 = 2 & (STRATEGYOP2>0) : 2.2 *op2S1;
	operation2 = 3 & (STRATEGYOP2>0) : 3.2 *op2S2;
	operation2 = 4 & (STRATEGYOP2>0) : 3.8 *op2S3;
	operation2 = 5 & (STRATEGYOP2>0) : 4.0 *op2S4; 
	//OP2: ROB
	operation2 = 2 & (STRATEGYOP2=0) : 2.2 ;
	operation2 = 3 & (STRATEGYOP2=0) : 3.2 ;
	operation2 = 4 & (STRATEGYOP2=0) : 3.8 ;
	operation2 = 5 & (STRATEGYOP2=0) : 4.0 ;
	
	//OP3: SEQ
	operation3 = 2 & (STRATEGYOP2>0) : 1.1 *op3S1;
	operation3 = 3 & (STRATEGYOP2>0) : 1.0 *op3S2;
	operation3 = 4 & (STRATEGYOP2>0) : 2.2 *op3S3;
	operation3 = 5 & (STRATEGYOP2>0) : 1.9 *op3S4;
	//OP3: PROB
	operation3 = 2 & (STRATEGYOP2=0) : 1.1 ;
	operation3 = 3 & (STRATEGYOP2=0) : 1.0 ;
	operation3 = 4 & (STRATEGYOP2=0) : 2.2 ;
	operation3 = 5 & (STRATEGYOP2=0) : 1.9 ;

	//OP4: SEQ		
	operation4 = 2 & (STRATEGYOP4>0) : 3.8 *op4S1;
	operation4 = 3 & (STRATEGYOP4>0) : 3.9 *op4S2;
	operation4 = 4 & (STRATEGYOP4>0) : 3.7 *op4S3;
	operation4 = 5 & (STRATEGYOP4>0) : 3.4 *op4S4;
	
	//OP4: PROB
	operation4 = 2 & (STRATEGYOP4=0) : 3.8 ;
	operation4 = 3 & (STRATEGYOP4=0) : 3.9 ;
	operation4 = 4 & (STRATEGYOP4=0) : 3.7 ;
	operation4 = 5 & (STRATEGYOP4=0) : 3.4 ;

	//OP5: SEQ
	operation5 = 2 & (STRATEGYOP5>0) : 3.9 *op5S1;
	operation5 = 3 & (STRATEGYOP5>0) : 3.6 *op5S2;
	operation5 = 4 & (STRATEGYOP5>0) : 3.1 *op5S3;
	operation5 = 5 & (STRATEGYOP5>0) : 4.4 *op5S4;
	//OP5: PROB
	operation5 = 2 & (STRATEGYOP5=0) : 3.9 ;
	operation5 = 3 & (STRATEGYOP5=0) : 3.6 ;
	operation5 = 4 & (STRATEGYOP5=0) : 3.1 ;
	operation5 = 5 & (STRATEGYOP5=0) : 4.4 ;

	//OP6: SEQ
	operation6 = 2 & (STRATEGYOP6>0) : 4.2 *op6S1;
	operation6 = 3 & (STRATEGYOP6>0) : 3.1 *op6S2;
	operation6 = 4 & (STRATEGYOP6>0) : 2.1 *op6S3;
	operation6 = 5 & (STRATEGYOP6>0) : 4.5 *op6S4;
	//OP6: PROB
	operation6 = 2 & (STRATEGYOP6=0) : 4.2 ;
	operation6 = 3 & (STRATEGYOP6=0) : 3.1 ;
	operation6 = 4 & (STRATEGYOP6=0) : 2.1 ;
	operation6 = 5 & (STRATEGYOP6=0) : 4.5 ;

	//OP7: SEQ
	operation7 = 2 & (STRATEGYOP7>0) : 5.2 *op7S1;
	operation7 = 3 & (STRATEGYOP7>0) : 3.3 *op7S2;
	operation7 = 4 & (STRATEGYOP7>0) : 4.1 *op7S3;
	operation7 = 5 & (STRATEGYOP7>0) : 4.5 *op7S4;
	//OP7: PROB
	operation7 = 2 & (STRATEGYOP7=0) : 5.2 ;
	operation7 = 3 & (STRATEGYOP7=0) : 3.3 ;
	operation7 = 4 & (STRATEGYOP7=0) : 4.1 ;
	operation7 = 5 & (STRATEGYOP7=0) : 4.5 ;

	//OP8: SEQ
	operation8 = 2 & (STRATEGYOP8>0) : 4.2 *op8S1;
	operation8 = 3 & (STRATEGYOP8>0) : 2.3 *op8S2;
	operation8 = 4 & (STRATEGYOP8>0) : 2.9 *op8S3;
	operation8 = 5 & (STRATEGYOP8>0) : 3.6 *op8S4;
	//OP8: PROB
	operation8 = 2 & (STRATEGYOP8=0) : 4.2 ;
	operation8 = 3 & (STRATEGYOP8=0) : 2.3 ;
	operation8 = 4 & (STRATEGYOP8=0) : 2.9 ;
	operation8 = 5 & (STRATEGYOP8=0) : 3.6 ;
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

	//OP7: SEQ
	operation7 = 2 & (STRATEGYOP7>0) : 3.2 *op7S1;
	operation7 = 3 & (STRATEGYOP7>0) : 8.3 *op7S2;
	operation7 = 4 & (STRATEGYOP7>0) : 7.1 *op7S3;
	operation7 = 5 & (STRATEGYOP7>0) : 6.5 *op7S4;
	//OP7: PROB
	operation7 = 2 & (STRATEGYOP7=0) : 3.2 ;
	operation7 = 3 & (STRATEGYOP7=0) : 8.3 ;
	operation7 = 4 & (STRATEGYOP7=0) : 7.1 ;
	operation7 = 5 & (STRATEGYOP7=0) : 6.5 ;

	//OP8: SEQ
	operation8 = 2 & (STRATEGYOP8>0) : 6.2  *op8S1;
	operation8 = 3 & (STRATEGYOP8>0) : 12.2 *op8S2;
	operation8 = 4 & (STRATEGYOP8>0) : 8.9  *op8S3;
	operation8 = 5 & (STRATEGYOP8>0) : 6.6  *op8S4;
	//OP8: PROB
	operation8 = 2 & (STRATEGYOP8=0) : 6.2  ;
	operation8 = 3 & (STRATEGYOP8=0) : 12.2 ;
	operation8 = 4 & (STRATEGYOP8=0) : 8.9  ;
	operation8 = 5 & (STRATEGYOP8=0) : 6.6  ;
endrewards
