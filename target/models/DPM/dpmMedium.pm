ctmc


evolve const int QmaxL [1..15]; 
evolve const int QmaxH [1..15]; 
evolve distribution evTrans1 [2][0.0 .. 0.8];
evolve distribution evTrans2 [2][0.2 .. 0.6];
evolve distribution evTrans3 [2][0.2 .. 0.6];

evolve module PM
	p: [0..2];
	
	[sleep2idle] p = 2 -> (p'=p);	
	[idle2sleep] p = 2 -> (p'=p);
	[wait2sleep] (QL=0 & QH=0)|(QL=1 & QH=0) -> (p'=p);
	[wait2idle] (QL=0 & QH > 0)|(QL=1 & QH > 0)|QL > 1 -> (p'=p);
	[sleep2wait] (QL=0 & QH=2)|(QL > 0 & QH > 0) -> (p'=p);
	[requestH] QL=0 & QH=0 & sp=3 -> evTrans1 : (p'=0) + evTrans1 : (p'=1);
	[requestL] QL=3 & QH=0 & sp=3 -> evTrans2 : (p'=0) + evTrans2 : (p'=1);
	[sleep2wait] p = 1 -> (p'=0);
	[requestH]! (QL=0 & QH=0 & sp=3) -> (p'=p);
	[requestL]!(QL=3 & QH=0 & sp=3) -> (p'=p);
endmodule


evolve module PM
	p: [0..2];

	[sleep2idle] false -> true;
	[idle2sleep] false -> true;
	[sleep2wait] false -> true;
	[wait2sleep] false -> true;
	[sleep2wait] false -> true;
	[wait2idle] (QL=0 & QH > 0)|(QL=1 & QH > 0)|QL > 1 -> (p'=p);
	[requestH] QL=0 & QH=0 & sp=3 -> evTrans2 : (p'=0) + evTrans2 : (p'=1);
	[requestL] QL=3 & QH=0 & sp=3 -> evTrans3 : (p'=0) + evTrans3 : (p'=1);
	[requestH]! (QL=0 & QH=0 & sp=3) -> (p'=p);
	[requestL]!(QL=3 & QH=0 & sp=3) -> (p'=p);
endmodule

const double service = 0.2; 
const double idle2wait = 1;
const double idle2sleep = 0.5;
const double wait2idle = 0.454;
const double wait2sleep = 1.5;
const double sleep2wait = 1.5;
const double sleep2idle = 0.166; 

module SP
sp:[0..3] init 0; //states: 0–busy, 1–idle, 2–wait, 3–sleep
[sleep2wait] sp = 3 -> sleep2wait : (sp' = 2);
[wait2sleep] sp = 2 -> wait2sleep : (sp' = 3);
[wait2idle]sp=2 & QL=0 & QH=0 -> wait2idle: (sp' =1);
[wait2idle]sp=2 & (QL>0|QH>0) -> wait2idle: (sp' =0);
[idle2wait]sp=1&QL=0&QH=0 ->idle2wait: (sp' =2);
[sleep2idle] sp = 3 & QL=0 & QH=0 ->sleep2idle: (sp' =1);
[sleep2idle] sp = 3 & (QL > 0|QH > 0) -> sleep2idle : (sp' = 0);
[idle2sleep]sp=1 & QL=0 & QH=0->idle2sleep:(sp' =3);
[requestL] sp = 1 -> (sp' = 0);
[requestH] sp = 1 -> (sp' = 0);
[requestL] sp != 1 -> (sp' = sp);
[requestH] sp != 1 -> (sp' = sp);
[serveH] sp=0 & QH=1 & QL=0 -> service: (sp' =1);
[serveL] sp=0 & QH=0 & QL=1 -> service:(sp' =1);
[serveH]sp=0 & (QL+QH>1) -> service:(sp' =sp);
[serveL]sp=0 & QH=0 & QL>1 -> service:(sp' =sp); 
endmodule

const double reqL = 0.05; 
const double reqH = 0.15;


module SRQL

QL: [0..QmaxL];
[requestL] true -> reqL : (QL' = min(QL + 1, QmaxL));
[serveL] QL>0 & QH=0 -> (QL' = QL-1);
endmodule

module SRQH
QH: [0..QmaxH];
[requestH] true -> reqH : (QH' = min(QH + 1, QmaxH));
[serveH] QH >0 -> (QH' =QH-1);
endmodule


rewards "QHLength"
	true : QH;
endrewards

rewards "QLLength"
	true : QL;
endrewards

rewards "TotalLength"
	true : QL+QH;
endrewards

rewards "QHLost"
	[requestH] QH=QmaxH : 1;
endrewards

rewards "QLLost"
	[requestL] QL=QmaxL : 1;
endrewards

rewards "TotalLost"
	[requestH] QH=QmaxH : 1;
	[requestL] QL=QmaxL : 1;
endrewards


rewards "energy"
	[sleep2idle] true : 5.1;
	[sleep2idle] sp = 3 & (QL > 0|QH > 0)  : 7; //sleep2busy
	[wait2sleep] true : 0.006;
	[wait2idle]sp=2 & (QL>0|QH>0) : 2; //wait2busy
	[idle2sleep] true : 0.067;
endrewards

rewards "power"
	sp=0 : 2.15;
	sp=1 : 0.95;
	sp=2 : 0.35;
	sp=3 : 0.13;
endrewards

rewards "cost"
	true: QmaxH+QmaxL;
endrewards
