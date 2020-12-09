//FOREX dtmc model (Simplified: 4 operations with 3 services for each operation)
dtmc

//Evoxxx defined params
//Which services are enabled
evolve int op2Code [1..7]; //possible combinations for services implementing operation 2

//Sequence of service execution
evolve int seqOp2  [1..6]; //(#services op2)!

//distribution for probabilistic selection
evolve param distribution probOp2 [3];

//flag indicating whether a service is selected or not (will be assembled based on the chromosome value)
const int op2S1 = mod(op2Code,2)>0?1:0;
const int op2S2 = mod(op2Code,4)>1?1:0;
const int op2S3 = mod(op2Code,8)>3?1:0;

// // user-defined params parameters
const double op2S1Fail=0.003; //failure probability of service 1 op1
const double op2S2Fail=0.005; //failure probability of service 2 op1
const double op2S3Fail=0.006; //failure probability of service 3 op1

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
//Operation 1: Market watch
/////////////

//PROB
module strategyOp1
	operation1:[0..2] init 0;

	[startOp1]	operation1 = 0 -> 0.00001:(operation1' = 1) + 0.99999:(operation1' = 2);
	[endOp1Fail]	operation1 = 1 -> 1.0:(operation1' = 0);
	[endOp1Succ]	operation1 = 2 -> 1.0:(operation1' = 0);
endmodule 


/////////////
//Operation 2: Technical Analysis
/////////////


//SEQ
evolve module strategyOp2
	operation2 : [0..6] init 0;
	stepOp2	 : [1..4] init 1;

	[startOp2]	operation2 = 0			-> 1.0 : (operation2'=1);

	[check21]	operation2 = 1 & stepOp2 = 1	-> ((seqOp2=1 | seqOp2=2)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=3 | seqOp2=4)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=5 | seqOp2=6)? 1 : 0) : (operation2'=4) ;

	[check22]	operation2 = 1 & stepOp2 = 2	-> ((seqOp2=3 | seqOp2=5)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=1 | seqOp2=6)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=2 | seqOp2=4)? 1 : 0) : (operation2'=4) ;

	[check23]	operation2 = 1 & stepOp2 = 3	-> ((seqOp2=4 | seqOp2=6)? 1 : 0) : (operation2'=2) +
                                           	   	   ((seqOp2=2 | seqOp2=5)? 1 : 0) : (operation2'=3) +
                                           	   	   ((seqOp2=1 | seqOp2=3)? 1 : 0) : (operation2'=4) ;
	[check24]	operation2 = 1 & stepOp2 > 3 -> 1.0 : (operation2'=5);

	[runS21]	operation2 = 2		-> (op2S1=1?1.0:0)*(1-op2S1Fail) : (operation2'=6) + (op2S1=0?1.0:op2S1Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));
	[runS22]	operation2 = 3		-> (op2S2=1?1.0:0)*(1-op2S2Fail) : (operation2'=6) + (op2S2=0?1.0:op2S2Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));
	[runS23]	operation2 = 4		-> (op2S3=1?1.0:0)*(1-op2S3Fail) : (operation2'=6) + (op2S3=0?1.0:op2S3Fail) : (operation2'=1) & (stepOp2'=min(STEPMAX,stepOp2+1));	

	[endOp2Fail]	operation2 = 5		->	1.0 : (operation2'=0);//failed
	[endOp2Succ]	operation2 = 6		->	1.0 : (operation2'=0); //succ
endmodule



//PROB
evolve module strategyOp2 
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
	operation4 : [0..2] init 0;

	//select a service probabilistically
	[startOp4] 	operation4 = 0 	->	0.00001 : (operation4'=1) + 0.99999 : (operation4'=2);

	[endOp4Fail]	operation4 = 1	->	1.0 : (operation4'=0);//failed
	[endOp4Succ]	operation4 = 2	->	1.0 : (operation4'=0); //succ
endmodule





/////////////
//Operation 5: 
/////////////

//PROB
module strategyOp5
	operation5:[0..2] init 0;

	[startOp5]	operation5 = 0 -> 0.00001:(operation5' = 1) + 0.99999:(operation5' = 2);
	[endOp5Fail]	operation5 = 1 -> 1.0:(operation5' = 0);
	[endOp5Succ]	operation5 = 2 -> 1.0:(operation5' = 0);
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


////////////
//Rewards
////////////
rewards "time"
	operation1=1|operation1=2:1.5;
	operation2=2&( STRATEGYOP2>0 ):2.2*op2S1;
	operation2=3&( STRATEGYOP2>0 ):3.2*op2S2;
	operation2=4&( STRATEGYOP2>0 ):3.8*op2S3;
	operation2=2&( STRATEGYOP2=0 ):2.2;
	operation2=3&( STRATEGYOP2=0 ):3.2;
	operation2=4&( STRATEGYOP2=0 ):3.8;
	operation3=1|operation3=2:1.5;
	operation4=1|operation4=2:1.5;
	operation5=1|operation1=5:1.5;
	operation6=1|operation6=2:2.5;
endrewards

rewards "cost"
	operation1=1|operation1=2:7;
	operation2=2&( STRATEGYOP2>0 ):13*op2S1;
	operation2=3&( STRATEGYOP2>0 ):6*op2S2;
	operation2=4&( STRATEGYOP2>0 ):4*op2S3;
	operation2=2&( STRATEGYOP2=0 ):13;
	operation2=3&( STRATEGYOP2=0 ):6;
	operation2=4&( STRATEGYOP2=0 ):4;
	operation3=1|operation3=2:7;
	operation4=1|operation4=2:7;
	operation5=1|operation5=2:5;
	operation6=1|operation6=2:11;
endrewards