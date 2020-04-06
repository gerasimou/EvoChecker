dtmc

// op1 parameters
evolve param double p11 [0.1..0.2];//    = 0.1;
evolve param double c11 [0.2..0.3];//    = 0.2;
evolve param double t11 [0.3..0.4];//    = 0.3;
evolve param double p12 [0.3..0.4];//    = 0.3;
evolve param double c12 [0.4..0.5];//    = 0.4;
evolve param double t12 [0.4..0.5];//    = 0.5;
evolve param double x   [0.5..0.6];//      = 0.6;

// op2 parameters
evolve param double alpha1 [0.6..0.7];// = 0.6;
//const double alpha2;// = 0.4;
evolve param double p21 [0.1..0.2];//    = 0.1;
evolve param double c21 [0.2..0.3];//    = 0.2;
evolve param double t21 [0.3..0.4];//    = 0.3;
evolve param double p22 [0.4..0.5];//    = 0.4;
evolve param double c22 [0.5..0.6];//    = 0.5;
evolve param double t22 [0.6..0.7];//    = 0.6;

// op3 parameters
evolve param double p31 [0.1..0.2];//     =0.1;
evolve param double c31 [0.1..0.3];//     =0.2;
evolve param double t31 [0.3..0.4];//     =0.3;
evolve param double p32 [0.3..0.4];//     =0.4;
evolve param double c32 [0.4..0.5];//     =0.5;
evolve param double t32 [0.5..0.6];//     =0.6;
evolve param double r   [0.7..0.75];//       =0.7;
evolve double y   		[0.8..0.85];//       =0.8;

module Workflow
  s : [0..14] init 0;

  // op1
  [] s=0 -> p11:(s'=2) + (1-p11):(s'=1);
  [] s=1 -> p12:(s'=2) + (1-p12):(s'=3);
  [] s=2 -> x:(s'=4) + (1-x):(s'=9);
  [] s=3 -> 1:(s'=13);

   // op2
   [] s=4 -> alpha1:(s'=5) + (1-alpha1):(s'=6);
   [] s=5 -> p21:(s'=7) + (1-p21):(s'=8);
   [] s=6 -> p22:(s'=7) + (1-p22):(s'=8);
   [] s=7 -> 1:(s'=14);
   [] s=8 -> 1:(s'=13);

   // op3
   [] s=9 -> p31:(s'=12) + (1-p31):(s'=10);
   [] s=10 -> p32:(s'=12) + (1-p32)*r:(s'=9) + (1-p32)*(1-r):(s'=11);
   [] s=11 -> 1:(s'=13);
   [] s=12 -> y:(s'=0) + (1-y):(s'=14);

   // fail
   [] s=13 -> 1:(s'=13);

   // succ
   [] s=14 -> 1:(s'=14); 
endmodule

rewards "cost"
  s=1: c11;
  s=2: c12;
  s=5: c21;
  s=6: c22;
  s=9: c31;
  s=10: c32;
endrewards

rewards "time"
  s=1: t11;
  s=2: t12;
  s=5: t21;
  s=6: t22;
  s=9: t31;
  s=10: t32;
endrewards

