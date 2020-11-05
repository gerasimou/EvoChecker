dtmc

evolve double barrierNode_2_detect[0.5..1.0];
module barrierNode_2
barrierNode_2_s : [0..2] init 0;
	[_attack_highNode_0_barrierNode_2] (barrierNode_2_s=uninfected) -> barrierNode_2_detect : (barrierNode_2_s'=barrierNode_2_s) + 1-barrierNode_2_detect : (barrierNode_2_s'=breached);
	[_attack_highNode_2_barrierNode_2] (barrierNode_2_s=uninfected) -> barrierNode_2_detect : (barrierNode_2_s'=barrierNode_2_s) + 1-barrierNode_2_detect : (barrierNode_2_s'=breached);
	[_attack_highNode_1_barrierNode_2] (barrierNode_2_s=uninfected) -> barrierNode_2_detect : (barrierNode_2_s'=barrierNode_2_s) + 1-barrierNode_2_detect : (barrierNode_2_s'=breached);
	[_attack_infectedNode_0_barrierNode_2] (barrierNode_2_s=uninfected) -> barrierNode_2_detect : (barrierNode_2_s'=barrierNode_2_s) + 1-barrierNode_2_detect : (barrierNode_2_s'=breached);
	[_attack_lowNode_0_barrierNode_2] (barrierNode_2_s=uninfected) -> barrierNode_2_detect : (barrierNode_2_s'=barrierNode_2_s) + 1-barrierNode_2_detect : (barrierNode_2_s'=breached);
	[] (barrierNode_2_s=breached) -> infect : (barrierNode_2_s'=infected) + 1-infect : (barrierNode_2_s'=uninfected);
	[_attack_highNode_0_barrierNode_2] (barrierNode_2_s=infected) -> (barrierNode_2_s'=barrierNode_2_s);
	[_attack_highNode_2_barrierNode_2] (barrierNode_2_s=infected) -> (barrierNode_2_s'=barrierNode_2_s);
	[_attack_highNode_1_barrierNode_2] (barrierNode_2_s=infected) -> (barrierNode_2_s'=barrierNode_2_s);
	[_attack_infectedNode_0_barrierNode_2] (barrierNode_2_s=infected) -> (barrierNode_2_s'=barrierNode_2_s);
	[_attack_lowNode_0_barrierNode_2] (barrierNode_2_s=infected) -> (barrierNode_2_s'=barrierNode_2_s);
endmodule

evolve double barrierNode_1_detect[0.5..1.0];
module barrierNode_1
barrierNode_1_s : [0..2] init 0;
	[_attack_infectedNode_0_barrierNode_1] (barrierNode_1_s=uninfected) -> barrierNode_1_detect : (barrierNode_1_s'=barrierNode_1_s) + 1-barrierNode_1_detect : (barrierNode_1_s'=breached);
	[_attack_highNode_0_barrierNode_1] (barrierNode_1_s=uninfected) -> barrierNode_1_detect : (barrierNode_1_s'=barrierNode_1_s) + 1-barrierNode_1_detect : (barrierNode_1_s'=breached);
	[_attack_lowNode_0_barrierNode_1] (barrierNode_1_s=uninfected) -> barrierNode_1_detect : (barrierNode_1_s'=barrierNode_1_s) + 1-barrierNode_1_detect : (barrierNode_1_s'=breached);
	[] (barrierNode_1_s=breached) -> infect : (barrierNode_1_s'=infected) + 1-infect : (barrierNode_1_s'=uninfected);
	[_attack_infectedNode_0_barrierNode_1] (barrierNode_1_s=infected) -> (barrierNode_1_s'=barrierNode_1_s);
	[_attack_highNode_0_barrierNode_1] (barrierNode_1_s=infected) -> (barrierNode_1_s'=barrierNode_1_s);
	[_attack_lowNode_0_barrierNode_1] (barrierNode_1_s=infected) -> (barrierNode_1_s'=barrierNode_1_s);
endmodule

evolve double barrierNode_0_detect[0.5..1.0];
module barrierNode_0
barrierNode_0_s : [0..2] init 0;
	[_attack_lowNode_1_barrierNode_0] (barrierNode_0_s=uninfected) -> barrierNode_0_detect : (barrierNode_0_s'=barrierNode_0_s) + 1-barrierNode_0_detect : (barrierNode_0_s'=breached);
	[_attack_highNode_1_barrierNode_0] (barrierNode_0_s=uninfected) -> barrierNode_0_detect : (barrierNode_0_s'=barrierNode_0_s) + 1-barrierNode_0_detect : (barrierNode_0_s'=breached);
	[_attack_highNode_2_barrierNode_0] (barrierNode_0_s=uninfected) -> barrierNode_0_detect : (barrierNode_0_s'=barrierNode_0_s) + 1-barrierNode_0_detect : (barrierNode_0_s'=breached);
	[] (barrierNode_0_s=breached) -> infect : (barrierNode_0_s'=infected) + 1-infect : (barrierNode_0_s'=uninfected);
	[_attack_lowNode_1_barrierNode_0] (barrierNode_0_s=infected) -> (barrierNode_0_s'=barrierNode_0_s);
	[_attack_highNode_1_barrierNode_0] (barrierNode_0_s=infected) -> (barrierNode_0_s'=barrierNode_0_s);
	[_attack_highNode_2_barrierNode_0] (barrierNode_0_s=infected) -> (barrierNode_0_s'=barrierNode_0_s);
endmodule

evolve double infectedNode_0_detect[0.5..1.0];
module infectedNode_0
infectedNode_0_s : [0..2] init 2;
	[_attack_lowNode_0_infectedNode_0] (infectedNode_0_s=uninfected) -> infectedNode_0_detect : (infectedNode_0_s'=infectedNode_0_s) + 1-infectedNode_0_detect : (infectedNode_0_s'=breached);
	[_attack_lowNode_1_infectedNode_0] (infectedNode_0_s=uninfected) -> infectedNode_0_detect : (infectedNode_0_s'=infectedNode_0_s) + 1-infectedNode_0_detect : (infectedNode_0_s'=breached);
	[_attack_infectedNode_0_barrierNode_2] (infectedNode_0_s=uninfected) -> infectedNode_0_detect : (infectedNode_0_s'=infectedNode_0_s) + 1-infectedNode_0_detect : (infectedNode_0_s'=breached);
	[_attack_infectedNode_0_barrierNode_1] (infectedNode_0_s=uninfected) -> infectedNode_0_detect : (infectedNode_0_s'=infectedNode_0_s) + 1-infectedNode_0_detect : (infectedNode_0_s'=breached);
	[] (infectedNode_0_s=breached) -> infect : (infectedNode_0_s'=infected) + 1-infect : (infectedNode_0_s'=uninfected);
	[_attack_lowNode_0_infectedNode_0] (infectedNode_0_s=infected) -> (infectedNode_0_s'=infectedNode_0_s);
	[_attack_lowNode_1_infectedNode_0] (infectedNode_0_s=infected) -> (infectedNode_0_s'=infectedNode_0_s);
	[_attack_infectedNode_0_barrierNode_2] (infectedNode_0_s=infected) -> (infectedNode_0_s'=infectedNode_0_s);
	[_attack_infectedNode_0_barrierNode_1] (infectedNode_0_s=infected) -> (infectedNode_0_s'=infectedNode_0_s);
endmodule

evolve double lowNode_1_detect[0.5..1.0];
module lowNode_1
lowNode_1_s : [0..2] init 0;
	[_attack_lowNode_1_infectedNode_0] (lowNode_1_s=uninfected) -> lowNode_1_detect : (lowNode_1_s'=lowNode_1_s) + 1-lowNode_1_detect : (lowNode_1_s'=breached);
	[_attack_lowNode_1_lowNode_0] (lowNode_1_s=uninfected) -> lowNode_1_detect : (lowNode_1_s'=lowNode_1_s) + 1-lowNode_1_detect : (lowNode_1_s'=breached);
	[_attack_lowNode_1_barrierNode_0] (lowNode_1_s=uninfected) -> lowNode_1_detect : (lowNode_1_s'=lowNode_1_s) + 1-lowNode_1_detect : (lowNode_1_s'=breached);
	[] (lowNode_1_s=breached) -> infect : (lowNode_1_s'=infected) + 1-infect : (lowNode_1_s'=uninfected);
	[_attack_lowNode_1_infectedNode_0] (lowNode_1_s=infected) -> (lowNode_1_s'=lowNode_1_s);
	[_attack_lowNode_1_lowNode_0] (lowNode_1_s=infected) -> (lowNode_1_s'=lowNode_1_s);
	[_attack_lowNode_1_barrierNode_0] (lowNode_1_s=infected) -> (lowNode_1_s'=lowNode_1_s);
endmodule

evolve double lowNode_0_detect[0.5..1.0];
module lowNode_0
lowNode_0_s : [0..2] init 0;
	[_attack_lowNode_0_infectedNode_0] (lowNode_0_s=uninfected) -> lowNode_0_detect : (lowNode_0_s'=lowNode_0_s) + 1-lowNode_0_detect : (lowNode_0_s'=breached);
	[_attack_lowNode_1_lowNode_0] (lowNode_0_s=uninfected) -> lowNode_0_detect : (lowNode_0_s'=lowNode_0_s) + 1-lowNode_0_detect : (lowNode_0_s'=breached);
	[_attack_lowNode_0_barrierNode_2] (lowNode_0_s=uninfected) -> lowNode_0_detect : (lowNode_0_s'=lowNode_0_s) + 1-lowNode_0_detect : (lowNode_0_s'=breached);
	[_attack_lowNode_0_barrierNode_1] (lowNode_0_s=uninfected) -> lowNode_0_detect : (lowNode_0_s'=lowNode_0_s) + 1-lowNode_0_detect : (lowNode_0_s'=breached);
	[] (lowNode_0_s=breached) -> infect : (lowNode_0_s'=infected) + 1-infect : (lowNode_0_s'=uninfected);
	[_attack_lowNode_0_infectedNode_0] (lowNode_0_s=infected) -> (lowNode_0_s'=lowNode_0_s);
	[_attack_lowNode_1_lowNode_0] (lowNode_0_s=infected) -> (lowNode_0_s'=lowNode_0_s);
	[_attack_lowNode_0_barrierNode_2] (lowNode_0_s=infected) -> (lowNode_0_s'=lowNode_0_s);
	[_attack_lowNode_0_barrierNode_1] (lowNode_0_s=infected) -> (lowNode_0_s'=lowNode_0_s);
endmodule

evolve double highNode_1_detect[0.5..1.0];
module highNode_1
highNode_1_s : [0..2] init 0;
	[_attack_highNode_1_barrierNode_2] (highNode_1_s=uninfected) -> highNode_1_detect : (highNode_1_s'=highNode_1_s) + 1-highNode_1_detect : (highNode_1_s'=breached);
	[_attack_highNode_1_barrierNode_0] (highNode_1_s=uninfected) -> highNode_1_detect : (highNode_1_s'=highNode_1_s) + 1-highNode_1_detect : (highNode_1_s'=breached);
	[_attack_highNode_2_highNode_1] (highNode_1_s=uninfected) -> highNode_1_detect : (highNode_1_s'=highNode_1_s) + 1-highNode_1_detect : (highNode_1_s'=breached);
	[] (highNode_1_s=breached) -> infect : (highNode_1_s'=infected) + 1-infect : (highNode_1_s'=uninfected);
	[_attack_highNode_1_barrierNode_2] (highNode_1_s=infected) -> (highNode_1_s'=highNode_1_s);
	[_attack_highNode_1_barrierNode_0] (highNode_1_s=infected) -> (highNode_1_s'=highNode_1_s);
	[_attack_highNode_2_highNode_1] (highNode_1_s=infected) -> (highNode_1_s'=highNode_1_s);
endmodule

evolve double highNode_0_detect[0.5..1.0];
module highNode_0
highNode_0_s : [0..2] init 0;
	[_attack_highNode_0_barrierNode_2] (highNode_0_s=uninfected) -> highNode_0_detect : (highNode_0_s'=highNode_0_s) + 1-highNode_0_detect : (highNode_0_s'=breached);
	[_attack_highNode_0_barrierNode_1] (highNode_0_s=uninfected) -> highNode_0_detect : (highNode_0_s'=highNode_0_s) + 1-highNode_0_detect : (highNode_0_s'=breached);
	[_attack_highNode_2_highNode_0] (highNode_0_s=uninfected) -> highNode_0_detect : (highNode_0_s'=highNode_0_s) + 1-highNode_0_detect : (highNode_0_s'=breached);
	[] (highNode_0_s=breached) -> infect : (highNode_0_s'=infected) + 1-infect : (highNode_0_s'=uninfected);
	[_attack_highNode_0_barrierNode_2] (highNode_0_s=infected) -> (highNode_0_s'=highNode_0_s);
	[_attack_highNode_0_barrierNode_1] (highNode_0_s=infected) -> (highNode_0_s'=highNode_0_s);
	[_attack_highNode_2_highNode_0] (highNode_0_s=infected) -> (highNode_0_s'=highNode_0_s);
endmodule

evolve double highNode_2_detect[0.5..1.0];
module highNode_2
highNode_2_s : [0..2] init 0;
	[_attack_highNode_2_barrierNode_0] (highNode_2_s=uninfected) -> highNode_2_detect : (highNode_2_s'=highNode_2_s) + 1-highNode_2_detect : (highNode_2_s'=breached);
	[_attack_highNode_2_barrierNode_2] (highNode_2_s=uninfected) -> highNode_2_detect : (highNode_2_s'=highNode_2_s) + 1-highNode_2_detect : (highNode_2_s'=breached);
	[_attack_highNode_2_highNode_0] (highNode_2_s=uninfected) -> highNode_2_detect : (highNode_2_s'=highNode_2_s) + 1-highNode_2_detect : (highNode_2_s'=breached);
	[_attack_highNode_2_highNode_1] (highNode_2_s=uninfected) -> highNode_2_detect : (highNode_2_s'=highNode_2_s) + 1-highNode_2_detect : (highNode_2_s'=breached);
	[] (highNode_2_s=breached) -> infect : (highNode_2_s'=infected) + 1-infect : (highNode_2_s'=uninfected);
	[_attack_highNode_2_barrierNode_0] (highNode_2_s=infected) -> (highNode_2_s'=highNode_2_s);
	[_attack_highNode_2_barrierNode_2] (highNode_2_s=infected) -> (highNode_2_s'=highNode_2_s);
	[_attack_highNode_2_highNode_0] (highNode_2_s=infected) -> (highNode_2_s'=highNode_2_s);
	[_attack_highNode_2_highNode_1] (highNode_2_s=infected) -> (highNode_2_s'=highNode_2_s);
endmodule

formula uninfected = 0;
formula infected = 2;
formula breached = 1;
const double infect = 0.5;
rewards "attacks"
	[_attack_highNode_0_barrierNode_2] true : 1;
	[_attack_highNode_2_barrierNode_2] true : 1;
	[_attack_highNode_1_barrierNode_2] true : 1;
	[_attack_infectedNode_0_barrierNode_2] true : 1;
	[_attack_lowNode_0_barrierNode_2] true : 1;
	[_attack_infectedNode_0_barrierNode_1] true : 1;
	[_attack_highNode_0_barrierNode_1] true : 1;
	[_attack_lowNode_0_barrierNode_1] true : 1;
	[_attack_lowNode_1_barrierNode_0] true : 1;
	[_attack_highNode_1_barrierNode_0] true : 1;
	[_attack_highNode_2_barrierNode_0] true : 1;
	[_attack_lowNode_0_infectedNode_0] true : 1;
	[_attack_lowNode_1_infectedNode_0] true : 1;
	[_attack_lowNode_1_lowNode_0] true : 1;
	[_attack_highNode_2_highNode_1] true : 1;
	[_attack_highNode_2_highNode_0] true : 1;
endrewards

formula highInfected = highNode_0_s=2 & highNode_1_s=2 & highNode_2_s=2;
