dtmc

evolve double p [0.4 .. 0.6];
evolve double q [0.4 .. 0.6];

evolve param int z [1..1];

module die

	// local state
	s : [0..7] init 0;
	// value of the die
	d : [0..6] init 0;
	
	[] s=0 -> p : (s'=1) + 1-p : (s'=2);
	[] s=1 -> 0.5 : (s'=3) + 0.5 : (s'=4);
	[] s=2 -> 0.5 : (s'=5) + 0.5 : (s'=6);
	[] s=3 -> 0.5 : (s'=1) + 0.5 : (s'=7) & (d'=1);
	[] s=4 -> (1-q) : (s'=7) & (d'=2) + q : (s'=7) & (d'=3);
	[] s=5 -> 0.5 : (s'=7) & (d'=4) + 0.5 : (s'=7) & (d'=5);
	[] s=6 -> 0.5 : (s'=2) + 0.5 : (s'=7) & (d'=6);
	[] s=7 -> (s'=7);
	
endmodule