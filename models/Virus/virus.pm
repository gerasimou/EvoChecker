mdp

// probabilities
const double infect = 0.5; // probability virus infects a node
evolve const double detect1 [0.5..1.0];  //0.5+0.09902; // probability virus detected by firewall of high/low node
evolve const double detect2 [0.5..1.0]; // 0.5+0.10313; // probability virus detected by firewall of barrier node
evolve const int pippo [1..1];

// low nodes (those above the ceil(N/2) row)

const double detect11=detect1; 
const double detect12=detect1; 
const double detect13=detect1;   

// barrier nodes (those in the ceil(N/2) row)

const double detect21=detect2;
const double detect22=detect2;
const double detect23=detect2;

// high nodes (those below the ceil(N/2) row)

const double detect31=detect1; 
const double detect32=detect1; 
const double detect33=detect1;  

// first column (1..N)
module n11

	s11 : [0..2]; // node uninfected
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 

	// firewall attacked (from an infected neighbour)
	[attack11_21] (s11=0) ->  detect11 : true + (1-detect11) : (s11'=1);
	[attack11_12] (s11=0) ->  detect11 : true + (1-detect11) : (s11'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s11=1) -> infect : (s11'=2) + (1-infect) : (s11'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack21_11] (s11=2) -> true;
	[attack12_11] (s11=2) -> true;
	
endmodule

module n21

	s21 : [0..2]; // node uninfected
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 

	// firewall attacked (from an infected neighbour)
	[attack21_31] (s21=0) -> detect21 : true + (1-detect21) : (s21'=1);
	[attack21_22] (s21=0) -> detect21 : true + (1-detect21) : (s21'=1);
	[attack21_11] (s21=0) -> detect21 : true + (1-detect21) : (s21'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s21=1) -> infect : (s21'=2) + (1-infect) : (s21'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack31_21] (s21=2) -> true;
	[attack22_21] (s21=2) -> true;
	[attack11_21] (s21=2) -> true;

endmodule

module n31
	s31 : [0..2]; // node uninfected
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 

	// firewall attacked (from an infected neighbour)
	[attack31_21] (s31=0) ->  detect31 : true + (1-detect31) : (s31'=1);
	[attack31_32] (s31=0) ->  detect31 : true + (1-detect31) : (s31'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s31=1) -> infect : (s31'=2) + (1-infect) : (s31'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack21_31] (s31=2) -> true;
	[attack32_31] (s31=2) -> true;
	
endmodule

module n12

	s12 : [0..2]; // node uninfected
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 

	// firewall attacked (from an infected neighbour)
	[attack12_13] (s12=0) -> detect12 : true + (1-detect12) : (s12'=1);
	[attack12_22] (s12=0) -> detect12 : true + (1-detect12) : (s12'=1);
	[attack12_11] (s12=0) -> detect12 : true + (1-detect12) : (s12'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s12=1) -> infect : (s12'=2) + (1-infect) : (s12'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack13_12] (s12=2) -> true;
	[attack22_12] (s12=2) -> true;
	[attack11_12] (s12=2) -> true;

endmodule

module n22
	
	s22 : [0..2]; // node uninfected
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 
	
	// firewall attacked (from an infected neighbour)
	[attack22_32] (s22=0) -> detect22 : true + (1-detect22) : (s22'=1);
	[attack22_23] (s22=0) -> detect22 : true + (1-detect22) : (s22'=1);
	[attack22_12] (s22=0) -> detect22 : true + (1-detect22) : (s22'=1);
	[attack22_21] (s22=0) -> detect22 : true + (1-detect22) : (s22'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s22=1) -> infect : (s22'=2) + (1-infect) : (s22'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack32_22] (s22=2) -> true;
	[attack23_22] (s22=2) -> true;
	[attack12_22] (s22=2) -> true;
	[attack21_22] (s22=2) -> true;
	
endmodule

module n32

	s32 : [0..2]; // node uninfected
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 

	// firewall attacked (from an infected neighbour)
	[attack32_33] (s32=0) -> detect32 : true + (1-detect32) : (s32'=1);
	[attack32_22] (s32=0) -> detect32 : true + (1-detect32) : (s32'=1);
	[attack32_31] (s32=0) -> detect32 : true + (1-detect32) : (s32'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s32=1) -> infect : (s32'=2) + (1-infect) : (s32'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack33_32] (s32=2) -> true;
	[attack22_32] (s32=2) -> true;
	[attack31_32] (s32=2) -> true;

endmodule

// columns 3..N-1

// column N

module n13
	s13 : [0..2]; // node uninfected
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 

	// firewall attacked (from an infected neighbour)
	[attack13_23] (s13=0) ->  detect13 : true + (1-detect13) : (s13'=1);
	[attack13_12] (s13=0) ->  detect13 : true + (1-detect13) : (s13'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s13=1) -> infect : (s13'=2) + (1-infect) : (s13'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack23_13] (s13=2) -> true;
	[attack12_13] (s13=2) -> true;
	
endmodule


module n23
	s23 : [0..2]; // node uninfected
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 

	// firewall attacked (from an infected neighbour)
	[attack23_33] (s23=0) -> detect23 : true + (1-detect23) : (s23'=1);
	[attack23_22] (s23=0) -> detect23 : true + (1-detect23) : (s23'=1);
	[attack23_13] (s23=0) -> detect23 : true + (1-detect23) : (s23'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s23=1) -> infect : (s23'=2) + (1-infect) : (s23'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack33_23] (s23=2) -> true;
	[attack22_23] (s23=2) -> true;
	[attack13_23] (s23=2) -> true;

endmodule


module n33

	s33 : [0..2] init 2; // node infected;
	// 0 - node uninfected
	// 1 - node uninfected but firewall breached
	// 2 - node infected 

	// firewall attacked (from an infected neighbour)
	[attack33_32] (s33=0) ->  detect33 : true + (1-detect33) : (s33'=1);
	[attack33_23] (s33=0) ->  detect33 : true + (1-detect33) : (s33'=1);
	// if the firewall has been breached tries to infect the node	
	[] (s33=1) -> infect : (s33'=2) + (1-infect) : (s33'=0);
	// if the node is infected, then it tries to attack its neighbouring nodes
	[attack32_33] (s33=2) -> true;
	[attack23_33] (s33=2) -> true;
	
endmodule

// reward structure (number of attacks)

rewards "cost"
	true : (detect1-0.5)*(detect1-0.5) + (detect2-0.5)*(detect2-0.5);
endrewards


rewards "attacks"

	// corner nodes

	[attack11_12] true : 1;
	[attack11_21] true : 1;
	[attack31_21] true : 1;
	[attack31_32] true : 1;
	[attack13_12] true : 1;
	[attack13_23] true : 1;
	[attack33_32] true : 1;
	[attack33_23] true : 1;
	
	// edge nodes

	[attack12_13] true : 1;
	[attack12_11] true : 1;
	[attack12_22] true : 1;
	[attack21_31] true : 1;
	[attack21_11] true : 1;
	[attack21_22] true : 1;
	[attack32_33] true : 1;
	[attack32_31] true : 1;
	[attack32_22] true : 1;
	[attack23_33] true : 1;
	[attack23_13] true : 1;
	[attack23_22] true : 1;

	// middle nodes
	 
	[attack22_32] true : 1;
	[attack22_23] true : 1;
	[attack22_12] true : 1;
	[attack22_21] true : 1;  

endrewards