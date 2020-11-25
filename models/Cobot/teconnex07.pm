dtmc

// const int dpMaint = 0;
// const int dpHCmit = 2; 
// const int dpHCres = 1;

evolve int dpMaint [0..1];
evolve int dpHCmit [0..2]; 
evolve int dpHCres [0..1];


// Weights (e.g. contribution to productivity)
const none = 0; const vl = 1; const l = 3; const m = 5; const h = 7; const vh = 10;
// Duration of time required by macro- and micro-step actions or events
const micro = 1; const macro = 5;

const HCStOffVis = 0; const HCSrmstIdleVis = 1; const HCStOffAud = 2;
const HCres = 0; const HCres2 = 1;
const off = 0; const idle = 1; const exchWrkp = 2; const welding = 3; 
const normal = 0; const hguid = 1; const ssmon = 2; const pflim = 3; const srmst = 4; const stopped = 5; 
const inact = 0; const act = 1; const mit = 2; const mis = 3; const sfd = 4; 
const atTable = 0; const sharedTbl = 1; const inCell = 2; const atWeldSpot = 3; 
const empty = 0; const left = 1; const right = 2; const both = 3; 
const far = 0; const near = 1; const close = 2; 
const ok = 0; const leaveArea = 1; const resetCtr = 2; 
const sc = 0; const wc = 1; const op = 2;
const ag = 2; 

module workcell 
t: 		[0..2] 	init 0;
s:              [0..1]  init 0;
rloc: 		[0..3] 	init 2;
hloc: 		[0..3] 	init 0;
reffocc: 	[0..1] 	init 0; 
wps: 		[0..3] 	init 0;
wpfin: 		[0..1] 	init 0;
mntDone: 	[0..1] 	init 0; 
dntFlg_enterCell: [0..1] init 0; 
dntFlg_exitPlant: [0..1] init 0; 
notif: 		[0..2] 	init 0;
notif_leaveWrkb: [0..1] init 0; 
lgtBar: 	[0..1] 	init 0; 
rngDet: 	[0..2] 	init 0; 
ract: 		[0..3] 	init 0;
wact: 		[0..3] 	init 0;
hact: 		[0..3] 	init 0;
safmod: 	[0..5] 	init 0;
HCp: 		[0..4] 	init 0;

////// cycle control ////////////////////////////////////////////////
[h_start] !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot)
                         | (ract=welding & wact=welding & rngDet=close))
                       & wpfin=1 & wps=empty & reffocc=0 & mntDone=1))
  & safmod!=stopped & wact=off & wpfin=0 & ract=off
  & !((ract=welding & wact=welding & hloc=atWeldSpot)
      | (ract=welding & wact=welding & rngDet=close))
  -> (wact'=idle)&(ract'=exchWrkp)&(hact'=idle);
[p_final] (HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) 
	& !(ract=off & wact=off) & !((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1 -> (hact'=off)&(ract'=off)&(wact'=off);


////// robotArm ////////////////////////////////////////////////////
[wc_idle] t=wc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) -> (t'=sc);

[r_moveToTable] t=wc 
& !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) 
& (safmod=normal|safmod=ssmon|safmod=pflim) & ract=exchWrkp & (rloc != sharedTbl) 
& (((wps!=right) & reffocc=1) | (wps=left & (reffocc=0))) 
-> (rloc'=sharedTbl)&(t'=sc);
[r_grabLeftWorkpiece] t=wc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & (safmod=normal|safmod=ssmon|safmod=pflim|safmod=hguid) & ract=exchWrkp & rloc=sharedTbl & reffocc=0 & wps=left -> (reffocc'=1)&(wpfin'=0)&(wps'=empty)&(t'=sc);
[r_placeWorkpieceRight] t=wc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & (safmod=normal|safmod=ssmon|safmod=pflim|safmod=hguid) & ract=exchWrkp & rloc=sharedTbl & reffocc=1 & wpfin=1 & wps=empty -> (reffocc'=0)&(wps'=right)&(t'=sc);
[r_placeWorkpieceRight] t=wc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & (safmod=normal|safmod=ssmon|safmod=pflim|safmod=hguid) & ract=exchWrkp & rloc=sharedTbl & reffocc=1 & wpfin=1 & wps=left -> (reffocc'=0)&(wps'=both)&(t'=sc);

[r_moveToWelder] t=wc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & (safmod=normal|safmod=ssmon|safmod=pflim) & ract=exchWrkp & reffocc=1 & wpfin=0 -> (ract'=welding)&(rloc'=atWeldSpot)&(t'=sc);


////// robotArm+weldingMachine //////////////////////////////////////////////
[rw_weldStep] t=wc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & (safmod=normal|safmod=ssmon|safmod=pflim) & (safmod=normal|safmod=ssmon) // TODO: simplify
	& ract=welding & (wact=idle|wact=welding) & wpfin=0 
	-> (wpfin'=1)&(wact'=welding)&(t'=sc);
[rw_leaveWelder] t=wc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & (safmod=normal|safmod=ssmon|safmod=pflim) 
	& ract=welding & (wact=welding | wact=idle) & wps!=both & wpfin=1 
	-> (ract'=exchWrkp)&(rloc'=inCell)&(wact'=idle)&(t'=sc); 

////// humanOp //////////////////////////////////////////////////////
[h_idle] t=op & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) -> (t'=sc);

// A:main work
[h_placeWorkpieceLeft] t=op & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & (hact=idle|hact=exchWrkp) & hloc=atTable & wps=empty & reffocc=0 & wpfin=0 -> (hact'=exchWrkp)&(hloc'=sharedTbl)&(wps'=left)&(lgtBar'=1)&(t'=sc);
[h_withDrawArm] t=op & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & hloc=sharedTbl -> (hact'=idle)&(hloc'=atTable)&(lgtBar'=0)&(t'=sc);
[h_grabRightWorkpiece] t=op & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & (hact=idle|hact=exchWrkp) & (hloc=atTable|hloc=sharedTbl) & wps=right -> (hact'=idle)&(hloc'=atTable)&(wps'=empty)&(lgtBar'=0)&(t'=sc);

// A:maintenance
[hi_mayEnterCell] t=op & dpMaint=1 & dntFlg_enterCell=0 & mntDone=0 & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) 		// deontic flag and end-of-cycle check
	& wps!=empty & hact=idle & (hloc!=inCell & hloc!=atWeldSpot) & wpfin=0 	
	-> (((ract!=exchWrkp & ract!=welding & wact!=welding))?.9:.2):(dntFlg_enterCell'=1)	
	  + (((ract!=exchWrkp & ract!=welding & wact!=welding))?.1:.8):true;				
[h_enterCell] t=op & dntFlg_enterCell=1 
	& hact=idle & (hloc!=inCell & hloc!=atWeldSpot) & wpfin=0 
	-> .95:(hloc'=inCell)&(rngDet'=near)&(t'=sc)
	+ .05:(hloc'=inCell)&(t'=sc);		
[h_approachWeldSpot] t=op & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & mntDone=0 & hact=idle & hloc=inCell -> 
	.95:(hloc'=atWeldSpot)&(mntDone'=1)&(dntFlg_enterCell'=0)&(rngDet'=close)&(t'=sc)
	+ .05:(hloc'=atWeldSpot)&(mntDone'=1)&(dntFlg_enterCell'=0)&(t'=sc);
[hi_mayExitPlant] t=op & dntFlg_exitPlant=0 & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1))
        & (hloc=inCell | hloc=atWeldSpot)
	-> ((notif=leaveArea)?.9:.6):(dntFlg_exitPlant'=1)
	  +((notif=leaveArea)?.1:.4):true;
[h_exitPlant] t=op & dntFlg_exitPlant=1 
	& !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & hact!=off & (hloc=inCell | hloc=atWeldSpot) & (mntDone=1 | notif=leaveArea) 
	& (hloc!=inCell | hloc!=atWeldSpot) 
	-> 
	((HCp!=mis & ((ract=welding & wact=welding & rngDet=close) | (ract=welding & wact=welding & hloc=atWeldSpot)))?0.1:0):(HCp'=mis) & (hloc'=atTable)&(dntFlg_enterCell'=0)&(dntFlg_exitPlant'=0)&(rngDet'=far)&(t'=sc)
	+ ((HCp!=mis & ((ract=welding & wact=welding & rngDet=close) | (ract=welding & wact=welding & hloc=atWeldSpot)))?0.9:1):(hloc'=atTable)&(dntFlg_enterCell'=0)&(dntFlg_exitPlant'=0)&(rngDet'=far)&(t'=sc);

////// safetyCtr /////////////////////////////////////////////////////
[si_idle] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & HCp=inact -> (s'=mod(s+1,ag))&(t'=s+1); 

[si_HCact] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & wact=welding & ract=welding & (ract=welding & wact=welding & rngDet=close) & !(HCp=act | HCp=mis) -> (HCp'=act);
[si_HCact] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & wact=off & ract=welding & (ract=welding & wact=welding & rngDet=close) & !(HCp=act | HCp=mis) -> (HCp'=act); 
[si_HCact] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & wact=welding & ract=off & (ract=welding & wact=welding & rngDet=close) & !(HCp=act | HCp=mis) -> (HCp'=act);

[s_HCstop2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffAud & wact=welding & ract=welding & HCp=act -> (ract'=off)&(wact'=off)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCstop] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffVis & wact=welding & ract=welding & HCp=act -> (ract'=off)&(wact'=off)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HChalt] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCSrmstIdleVis & wact=welding & ract=welding & HCp=act -> (wact'=idle)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HChalt] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCSrmstIdleVis & wact=welding & ract=off & HCp=act -> (wact'=idle)&(s'=mod(s+1,ag))&(t'=s+1);

[si_HCSrmstIdleVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCSrmstIdleVis & safmod=normal & HCp=act -> (safmod'=srmst)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCSrmstIdleVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCSrmstIdleVis & safmod=hguid & HCp=act -> (safmod'=srmst)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCSrmstIdleVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCSrmstIdleVis & safmod=ssmon & HCp=act -> (safmod'=srmst)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCSrmstIdleVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCSrmstIdleVis & safmod=pflim & HCp=act -> (safmod'=srmst)&(s'=mod(s+1,ag))&(t'=s+1);

[si_HCStOffAudsafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffAud & safmod=normal & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffAudsafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffAud & safmod=hguid & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffAudsafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffAud & safmod=ssmon & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffAudsafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffAud & safmod=pflim & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffAudsafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffAud & safmod=srmst & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);

[si_HCStOffVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffVis & safmod=normal & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffVis & safmod=hguid & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffVis & safmod=ssmon & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffVis & safmod=pflim & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffVissafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffVis & safmod=srmst & HCp=act -> (safmod'=stopped)&(s'=mod(s+1,ag))&(t'=s+1);

[si_HCSrmstIdleVisfun] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCSrmstIdleVis & (HCp=act) & !((notif=leaveArea)) -> (notif'=leaveArea)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffAudfun] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffAud & (HCp=act) & !((notif=leaveArea)) -> (notif'=leaveArea)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCStOffVisfun] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCmit=HCStOffVis & (HCp=act) & !((notif=leaveArea)) -> (notif'=leaveArea)&(s'=mod(s+1,ag))&(t'=s+1);

[si_HCmit] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & HCp=act & ((notif=leaveArea) | (notif=leaveArea) | (notif=leaveArea)) & !(ract=welding & wact=welding & rngDet=close) -> (HCp'=mit);

[si_HCres2fun] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & HCp=mit & !(ract=welding & wact=welding & rngDet=close) & ((notif=leaveArea) | (notif=leaveArea) | (notif=leaveArea)) & notif=leaveArea & !(hloc=inCell | hloc=atWeldSpot) -> (notif'=ok)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCresfun] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & HCp=mit & !(ract=welding & wact=welding & rngDet=close) & ((notif=leaveArea) | (notif=leaveArea) | (notif=leaveArea)) & notif=leaveArea & !(hloc=inCell | hloc=atWeldSpot) -> (notif'=ok)&(s'=mod(s+1,ag))&(t'=s+1);

[si_HCres2safmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & safmod=normal & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCres2safmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & safmod=hguid & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCres2safmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & safmod=ssmon & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCres2safmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & safmod=pflim & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCres2safmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & safmod=srmst & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCres2safmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & safmod=stopped & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCressafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & safmod=normal & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCressafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & safmod=hguid & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCressafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & safmod=ssmon & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCressafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & safmod=pflim & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCressafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & safmod=srmst & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);
[si_HCressafmod] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & safmod=stopped & HCp=mit & !(hloc=inCell | hloc=atWeldSpot) & (notif=ok) -> (safmod'=normal)&(HCp'=sfd)&(s'=mod(s+1,ag))&(t'=s+1);

[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=exchWrkp & wact=welding & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=welding & wact=welding & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=off & wact=welding & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=exchWrkp & wact=idle & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=welding & wact=idle & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=off & wact=idle & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=exchWrkp & wact=off & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=welding & wact=off & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume2] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres2 & ract=off & wact=off & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=welding)&(wact'=welding)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=exchWrkp & wact=welding & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=welding & wact=welding & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=off & wact=welding & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=exchWrkp & wact=idle & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=welding & wact=idle & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=off & wact=idle & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=exchWrkp & wact=off & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=welding & wact=off & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
[s_HCresume] t=sc & !(HCp=mis | (!((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & wpfin=1 & wps=empty & reffocc=0 & mntDone=1)) & dpHCres=HCres & ract=off & wact=off & HCp=sfd & !(ract=welding & wact=welding & rngDet=close) & (notif=ok) -> (ract'=exchWrkp)&(wact'=exchWrkp)&(HCp'=inact)&(s'=mod(s+1,ag))&(t'=s+1);
endmodule

////////////////////////////////////////////////////////////////////////////////
// Reward structures

// Risk of HC-mishap when performing nominal action ...
rewards "risk_HC"
[r_placeWorkpieceRight] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) 
& HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) : 0;
[rw_leaveWelder] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 5;
[r_grabLeftWorkpiece] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 0;
[h_start] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 0;
[h_approachWeldSpot] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 7;
[h_placeWorkpieceLeft] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 0;
[h_grabRightWorkpiece] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 0;
[h_exitPlant] notif=leaveArea & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 0;
[h_withDrawArm] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 0;
[r_moveToTable] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 5;
[rw_weldStep] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 10;
[r_moveToWelder] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 5;
[h_enterCell] ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close)) & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 0;
endrewards

// Productivity of nominal actions
rewards "prod"
[r_placeWorkpieceRight] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: h;
[rw_leaveWelder] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: h;
[r_grabLeftWorkpiece] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: m;
[h_start] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: l;
[h_approachWeldSpot] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: h;
[h_placeWorkpieceLeft] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: h;
[h_grabRightWorkpiece] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: h;
[h_exitPlant] notif=leaveArea & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: none;
[h_withDrawArm] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: l;
[r_moveToTable] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: h;
[rw_weldStep] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: h;
[r_moveToWelder] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: h;
[h_enterCell] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: none;

[si_idle] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) : none;
[wc_idle] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) : none;
[h_idle] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) : none;
endrewards

// Effective time of the plant
rewards "eff_plant_time"
[r_placeWorkpieceRight] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: macro;
[rw_leaveWelder] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: macro;
[r_grabLeftWorkpiece] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: macro;
[h_start] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: macro;
[h_approachWeldSpot] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 2*macro;
[h_placeWorkpieceLeft] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 2*macro;
[h_grabRightWorkpiece] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: macro;
[h_exitPlant] notif=leaveArea & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: none;
[h_withDrawArm] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: macro;
[r_moveToTable] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 2*macro;
[rw_weldStep] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 3*macro;
[r_moveToWelder] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 2*macro;
[h_enterCell] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: none;

[si_idle] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) : none;
[wc_idle] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) : macro;
[h_idle] HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) : macro;
endrewards

// Expected severity of mishap states
rewards "risk_sev"
[] HCp=mis	: 5.0;
[h_exitPlant] HCp!=mis & ((ract=welding & wact=welding & rngDet=close) | (ract=welding & wact=welding & hloc=atWeldSpot))	: 5.0;
endrewards

// Approximate overall risk level (per safety mode, agent, and activity)
rewards "risk_level"
// Safety modes
[] safmod=normal & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 5.0;
[] safmod=hguid & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 3.0;
[] safmod=ssmon & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 4.0;
[] safmod=pflim & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 3.0;
[] safmod=srmst & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 2.0;
[] safmod=stopped & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 1.0;
// Actor: robotArm
[] ract=off & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 1.0;
[] ract=idle & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 2.0;
[] ract=exchWrkp & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 4.0;
[] ract=welding & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 6.0;
// Actor: weldingMachine
[] wact=off & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 1.0;
[] wact=idle & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 2.0;
[] wact=exchWrkp & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 4.0;
[] wact=welding & HCp!=mis & ((ract=welding & wact=welding & hloc=atWeldSpot) | (ract=welding & wact=welding & rngDet=close) | wpfin=0 | wps!=empty | reffocc=1 | mntDone=0) 	: 6.0;
endrewards

// Risk reduction potential of the safety controller (per critical event)
rewards "potential"
[si_HCSrmstIdleVisfun] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCSrmstIdleVissafmod] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[s_HChalt] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCres2safmod] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[s_HCresume2] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCres2fun] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCStOffAudsafmod] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[s_HCstop2] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCStOffAudfun] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCStOffVissafmod] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCStOffVisfun] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[s_HCstop] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCressafmod] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[s_HCresume] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCresfun] (HCp=act | HCp=mit | HCp=sfd) 	: 2.5;
[si_HCact]	(ract=welding & wact=welding & rngDet=close)	: 10.0;
[si_HCmit]	(ract=welding & wact=welding & rngDet=close)	: 10.0;
endrewards

// Disruptiveness because of stopping/changing the process/activity (per mitigation option)
rewards "disruption"
// HC-mode: HCSrmstIdleVis
[si_HCSrmstIdleVisfun] (HCp=act | HCp=mit) 	: 5.0;
[si_HCSrmstIdleVissafmod] (HCp=act | HCp=mit) 	: 5.0;
[s_HChalt] (HCp=act | HCp=mit) 	: 5.0;
// !HCp=inact & notif=leaveArea	:5.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCres2
[si_HCres2safmod] (HCp=act | HCp=mit) 	: 7.0;
[s_HCresume2] (HCp=act | HCp=mit) 	: 7.0;
[si_HCres2fun] (HCp=act | HCp=mit) 	: 7.0;
// !HCp=inact & notif=ok	:7.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCStOffAud
[si_HCStOffAudsafmod] (HCp=act | HCp=mit) 	: 3.0;
[s_HCstop2] (HCp=act | HCp=mit) 	: 3.0;
[si_HCStOffAudfun] (HCp=act | HCp=mit) 	: 3.0;
// !HCp=inact & notif=leaveArea	:3.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCStOffVis
[si_HCStOffVissafmod] (HCp=act | HCp=mit) 	: 2.0;
[si_HCStOffVisfun] (HCp=act | HCp=mit) 	: 2.0;
[s_HCstop] (HCp=act | HCp=mit) 	: 2.0;
// !HCp=inact & notif=leaveArea	:2.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCres
[si_HCressafmod] (HCp=act | HCp=mit) 	: 2.0;
[s_HCresume] (HCp=act | HCp=mit) 	: 2.0;
[si_HCresfun] (HCp=act | HCp=mit) 	: 2.0;
// !HCp=inact & notif=ok	:2.0; // Commented (PRISM optimisation queries don't allow state rewards.)
endrewards

// Energy consumption or effort to be spent (per mitigation option)
rewards "effort"
// HC-mode: HCSrmstIdleVis
[si_HCSrmstIdleVisfun] (HCp=act | HCp=mit) 	: 8.0;
[si_HCSrmstIdleVissafmod] (HCp=act | HCp=mit) 	: 8.0;
[s_HChalt] (HCp=act | HCp=mit) 	: 8.0;
// !HCp=inact & notif=leaveArea	:8.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCres2
[si_HCres2safmod] (HCp=act | HCp=mit) 	: 7.0;
[s_HCresume2] (HCp=act | HCp=mit) 	: 7.0;
[si_HCres2fun] (HCp=act | HCp=mit) 	: 7.0;
// !HCp=inact & notif=ok	:7.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCStOffAud
[si_HCStOffAudsafmod] (HCp=act | HCp=mit) 	: 5.0;
[s_HCstop2] (HCp=act | HCp=mit) 	: 5.0;
[si_HCStOffAudfun] (HCp=act | HCp=mit) 	: 5.0;
// !HCp=inact & notif=leaveArea	:5.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCStOffVis
[si_HCStOffVissafmod] (HCp=act | HCp=mit) 	: 6.0;
[si_HCStOffVisfun] (HCp=act | HCp=mit) 	: 6.0;
[s_HCstop] (HCp=act | HCp=mit) 	: 6.0;
// !HCp=inact & notif=leaveArea	:6.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCres
[si_HCressafmod] (HCp=act | HCp=mit) 	: 1.0;
[s_HCresume] (HCp=act | HCp=mit) 	: 1.0;
[si_HCresfun] (HCp=act | HCp=mit) 	: 1.0;
// !HCp=inact & notif=ok	:1.0; // Commented (PRISM optimisation queries don't allow state rewards.)
endrewards

// Nuisance (e.g. to the human operator; per mitigation option)
rewards "nuisance"
// HC-mode: HCSrmstIdleVis
[si_HCSrmstIdleVisfun] (HCp=act | HCp=mit) 	: 4.0;
[si_HCSrmstIdleVissafmod] (HCp=act | HCp=mit) 	: 4.0;
[s_HChalt] (HCp=act | HCp=mit) 	: 4.0;
// !HCp=inact & notif=leaveArea	:4.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCres2
[si_HCres2safmod] (HCp=act | HCp=mit) 	: 7.0;
[s_HCresume2] (HCp=act | HCp=mit) 	: 7.0;
[si_HCres2fun] (HCp=act | HCp=mit) 	: 7.0;
// !HCp=inact & notif=ok	:7.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCStOffAud
[si_HCStOffAudsafmod] (HCp=act | HCp=mit) 	: 6.0;
[s_HCstop2] (HCp=act | HCp=mit) 	: 6.0;
[si_HCStOffAudfun] (HCp=act | HCp=mit) 	: 6.0;
// !HCp=inact & notif=leaveArea	:6.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCStOffVis
[si_HCStOffVissafmod] (HCp=act | HCp=mit) 	: 5.0;
[si_HCStOffVisfun] (HCp=act | HCp=mit) 	: 5.0;
[s_HCstop] (HCp=act | HCp=mit) 	: 5.0;
// !HCp=inact & notif=leaveArea	:5.0; // Commented (PRISM optimisation queries don't allow state rewards.)
// HC-mode: HCres
[si_HCressafmod] (HCp=act | HCp=mit) 	: 5.0;
[s_HCresume] (HCp=act | HCp=mit) 	: 5.0;
[si_HCresfun] (HCp=act | HCp=mit) 	: 5.0;
// !HCp=inact & notif=ok	:5.0; // Commented (PRISM optimisation queries don't allow state rewards.)
endrewards

// Time of effectiveness (i.e. up-time) of the controller
rewards "eff_ctr_time"
// HC-mode: HCSrmstIdleVis
[si_HCSrmstIdleVisfun] true 	: 1.0*micro;
[si_HCact] true 	: 1.0*micro;
[si_HCSrmstIdleVissafmod] true 	: 1.0*micro;
[si_HCmit] true 	: 1.0*micro;
[s_HChalt] true 	: 1.0*micro;
// HC-mode: HCres2
[si_HCres2safmod] true 	: 1.0*micro;
[si_HCact] true 	: 1.0*micro;
[s_HCresume2] true 	: 1.0*micro;
[si_HCmit] true 	: 1.0*micro;
[si_HCres2fun] true 	: 1.0*micro;
// HC-mode: HCStOffAud
[si_HCStOffAudsafmod] true 	: 1.0*micro;
[s_HCstop2] true 	: 1.0*micro;
[si_HCact] true 	: 1.0*micro;
[si_HCmit] true 	: 1.0*micro;
[si_HCStOffAudfun] true 	: 1.0*micro;
// HC-mode: HCStOffVis
[si_HCStOffVissafmod] true 	: 1.0*micro;
[si_HCStOffVisfun] true 	: 1.0*micro;
[s_HCstop] true 	: 1.0*micro;
[si_HCact] true 	: 1.0*micro;
[si_HCmit] true 	: 1.0*micro;
// HC-mode: HCres
[si_HCressafmod] true 	: 1.0*micro;
[s_HCresume] true 	: 1.0*micro;
[si_HCact] true 	: 1.0*micro;
[si_HCresfun] true 	: 1.0*micro;
[si_HCmit] true 	: 1.0*micro;
endrewards

// Loss of productive time (i.e. down-time)
rewards "disruption_time"
// Actor: robotArm
[s_HChalt] ract=welding	: 1; 
[s_HChalt] ract=exchWrkp	: 1; 
[s_HCresume2] ract=welding	: 1; 
[s_HCresume2] ract=exchWrkp	: 1; 
[s_HCstop2] ract=welding	: 1; 
[s_HCstop2] ract=exchWrkp	: 1; 
[s_HCstop] ract=welding	: 1; 
[s_HCstop] ract=exchWrkp	: 1; 
[s_HCresume] ract=welding	: 1; 
[s_HCresume] ract=exchWrkp	: 1; 
endrewards

// Disruptiveness because of stopping/changing activity (per agent and mitigation option)
rewards "disruption_ag"
// Actor: robotArm
[s_HChalt] ract=welding	: 5.0; 
[s_HChalt] ract=exchWrkp	: 5.0; 
[s_HCresume2] ract=welding	: 7.0; 
[s_HCresume2] ract=exchWrkp	: 7.0; 
[s_HCstop2] ract=welding	: 3.0; 
[s_HCstop2] ract=exchWrkp	: 3.0; 
[s_HCstop] ract=welding	: 2.0; 
[s_HCstop] ract=exchWrkp	: 2.0; 
[s_HCresume] ract=welding	: 2.0; 
[s_HCresume] ract=exchWrkp	: 2.0; 
endrewards
