// Model taken from Daws04
// This version by Ernst Moritz Hahn (emh@cs.uni-sb.de)

dtmc

evolve const double q [0.5..0.7];
//dummy evolvable construct - required for GA (to be fixed in next EvoChecker update)
evolve const int    number [1..1]; 

const double p = 0.1;
const int n = 3;
const int states = n+3;

module zeroconf
  s: [0..states] init 2;

  [b] (s=1) 			-> 1.0 : (s'=0);
  [a] (s=2) 			-> 1-q : (s'=1) + q : (s'=3);
  [a] (s>2) & (s<states) 	-> 1-p : (s'=2)  + p : (s'=s+1);
endmodule

rewards "cost"
 [a] true : 1;
 [b] true : n-1;
endrewards


