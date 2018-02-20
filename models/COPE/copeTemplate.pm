// DTMC action model
// built by PrismMapModelGenerator - with location failure model
// Colin Paterson 2017

//Locations in the model have the following indices
// 0:Chool
// 1:Fare
// 2:Greta
// 3:Hurratta
// 4:J1
// 5:J2
// 6:J3
// 7:J4
// 8:Kremshaw
// 9:Moona
// 10:Pretch
// 11:Treebach
// 12:Ulla

// failState : 26

dtmc

//Action command parameters
evolve int action_Chool [1..2];
evolve int action_Fare [1..3];
evolve int pA_Greta [1..1];
evolve int action_Hurratta [1..4];
evolve int action_J1 [1..3];
evolve int action_J2 [1..2];
evolve int action_J3 [1..3];
evolve int action_J4 [1..3];
evolve int action_Kremshaw [1..2];
evolve int action_Moona [1..3];
evolve int action_Pretch [1..3];
evolve int action_Treebach [1..3];
evolve int action_Ulla [1..2];

const double psChool_J4 = 0.7;
const double prChool_J4 = 0.25;
const double psChool_Treebach = 0.85;
const double prChool_Treebach = 0.12;

const double psFare_Hurratta = 0.85;
const double prFare_Hurratta = 0.12;
const double psFare_J4 = 0.7;
const double prFare_J4 = 0.25;
const double psFare_Treebach = 0.7;
const double prFare_Treebach = 0.25;

const double psGreta_Pretch = 0.9;
const double prGreta_Pretch = 0.08;

const double psHurratta_Fare = 0.85;
const double prHurratta_Fare = 0.12;
const double psHurratta_J4 = 0.85;
const double prHurratta_J4 = 0.12;
const double psHurratta_Moona = 0.8;
const double prHurratta_Moona = 0.15;
const double psHurratta_Pretch = 0.9;
const double prHurratta_Pretch = 0.08;

const double psJ1_Kremshaw = 0.8;
const double prJ1_Kremshaw = 0.15;
const double psJ1_Moona = 0.85;
const double prJ1_Moona = 0.12;
const double psJ1_Ulla = 0.8;
const double prJ1_Ulla = 0.15;

const double psJ2_J3 = 0.8;
const double prJ2_J3 = 0.15;
const double psJ2_Ulla = 0.8;
const double prJ2_Ulla = 0.15;

const double psJ3_J2 = 0.8;
const double prJ3_J2 = 0.15;
const double psJ3_Moona = 0.85;
const double prJ3_Moona = 0.12;
const double psJ3_Pretch = 0.9;
const double prJ3_Pretch = 0.08;

const double psJ4_Chool = 0.7;
const double prJ4_Chool = 0.25;
const double psJ4_Fare = 0.7;
const double prJ4_Fare = 0.25;
const double psJ4_Hurratta = 0.85;
const double prJ4_Hurratta = 0.12;

const double psKremshaw_J1 = 0.8;
const double prKremshaw_J1 = 0.15;
const double psKremshaw_Treebach = 0.8;
const double prKremshaw_Treebach = 0.15;

const double psMoona_Hurratta = 0.8;
const double prMoona_Hurratta = 0.15;
const double psMoona_J1 = 0.85;
const double prMoona_J1 = 0.12;
const double psMoona_J3 = 0.85;
const double prMoona_J3 = 0.12;

const double psPretch_Greta = 0.9;
const double prPretch_Greta = 0.08;
const double psPretch_Hurratta = 0.9;
const double prPretch_Hurratta = 0.08;
const double psPretch_J3 = 0.9;
const double prPretch_J3 = 0.08;

const double psTreebach_Chool = 0.85;
const double prTreebach_Chool = 0.12;
const double psTreebach_Fare = 0.7;
const double prTreebach_Fare = 0.25;
const double psTreebach_Kremshaw = 0.8;
const double prTreebach_Kremshaw = 0.15;

const double psUlla_J1 = 0.8;
const double prUlla_J1 = 0.15;
const double psUlla_J2 = 0.8;
const double prUlla_J2 = 0.15;

const double pfChool =  0.0;
const double pfFare =  0.0;
const double pfGreta =  0.0;
const double pfHurratta =  0.0;
const double pfJ1 =  0.0;
const double pfJ2 =  0.0;
const double pfJ3 =  0.0;
const double pfJ4 =  0.0;
const double pfKremshaw =  0.0;
const double pfMoona =  0.0;
const double pfPretch =  0.0;
const double pfTreebach =  0.0;
const double pfUlla =  0.0;

module main
	l:[0..26] init 24;

	//Valid action ranges
	pA_Chool:[1..2] init action_Chool;
	pA_Fare:[1..3] init action_Fare;
	pA_Hurratta:[1..4] init action_Hurratta;
	pA_J1:[1..3] init action_J1;
	pA_J2:[1..2] init action_J2;
	pA_J3:[1..3] init action_J3;
	pA_J4:[1..3] init action_J4;
	pA_Kremshaw:[1..2] init action_Kremshaw;
	pA_Moona:[1..3] init action_Moona;
	pA_Pretch:[1..3] init action_Pretch;
	pA_Treebach:[1..3] init action_Treebach;
	pA_Ulla:[1..2] init action_Ulla;

	[] l=0 -> pfChool:(l'=26) + (1-pfChool):(l'=1);
	[r0_14] l=1 & pA_Chool = 1 ->psChool_J4:(l'=14) + prChool_J4:(l'=1) + (1 - psChool_J4 - prChool_J4):(l'=26);
	[r0_22] l=1 & pA_Chool = 2 ->psChool_Treebach:(l'=22) + prChool_Treebach:(l'=1) + (1 - psChool_Treebach - prChool_Treebach):(l'=26);

	[] l=2 -> pfFare:(l'=26) + (1-pfFare):(l'=3);
	[r2_6] l=3 & pA_Fare = 1 ->psFare_Hurratta:(l'=6) + prFare_Hurratta:(l'=3) + (1 - psFare_Hurratta - prFare_Hurratta):(l'=26);
	[r2_14] l=3 & pA_Fare = 2 ->psFare_J4:(l'=14) + prFare_J4:(l'=3) + (1 - psFare_J4 - prFare_J4):(l'=26);
	[r2_22] l=3 & pA_Fare = 3 ->psFare_Treebach:(l'=22) + prFare_Treebach:(l'=3) + (1 - psFare_Treebach - prFare_Treebach):(l'=26);

	[] l=4 -> pfGreta:(l'=26) + (1-pfGreta):(l'=5);
	[r4_20] l=5 & pA_Greta = 1 ->psGreta_Pretch:(l'=20) + prGreta_Pretch:(l'=5) + (1 - psGreta_Pretch - prGreta_Pretch):(l'=26);

	[] l=6 -> pfHurratta:(l'=26) + (1-pfHurratta):(l'=7);
	[r6_2] l=7 & pA_Hurratta = 1 ->psHurratta_Fare:(l'=2) + prHurratta_Fare:(l'=7) + (1 - psHurratta_Fare - prHurratta_Fare):(l'=26);
	[r6_14] l=7 & pA_Hurratta = 2 ->psHurratta_J4:(l'=14) + prHurratta_J4:(l'=7) + (1 - psHurratta_J4 - prHurratta_J4):(l'=26);
	[r6_18] l=7 & pA_Hurratta = 3 ->psHurratta_Moona:(l'=18) + prHurratta_Moona:(l'=7) + (1 - psHurratta_Moona - prHurratta_Moona):(l'=26);
	[r6_20] l=7 & pA_Hurratta = 4 ->psHurratta_Pretch:(l'=20) + prHurratta_Pretch:(l'=7) + (1 - psHurratta_Pretch - prHurratta_Pretch):(l'=26);

	[] l=8 -> pfJ1:(l'=26) + (1-pfJ1):(l'=9);
	[r8_16] l=9 & pA_J1 = 1 ->psJ1_Kremshaw:(l'=16) + prJ1_Kremshaw:(l'=9) + (1 - psJ1_Kremshaw - prJ1_Kremshaw):(l'=26);
	[r8_18] l=9 & pA_J1 = 2 ->psJ1_Moona:(l'=18) + prJ1_Moona:(l'=9) + (1 - psJ1_Moona - prJ1_Moona):(l'=26);
	[r8_24] l=9 & pA_J1 = 3 ->psJ1_Ulla:(l'=24) + prJ1_Ulla:(l'=9) + (1 - psJ1_Ulla - prJ1_Ulla):(l'=26);

	[] l=10 -> pfJ2:(l'=26) + (1-pfJ2):(l'=11);
	[r10_12] l=11 & pA_J2 = 1 ->psJ2_J3:(l'=12) + prJ2_J3:(l'=11) + (1 - psJ2_J3 - prJ2_J3):(l'=26);
	[r10_24] l=11 & pA_J2 = 2 ->psJ2_Ulla:(l'=24) + prJ2_Ulla:(l'=11) + (1 - psJ2_Ulla - prJ2_Ulla):(l'=26);

	[] l=12 -> pfJ3:(l'=26) + (1-pfJ3):(l'=13);
	[r12_10] l=13 & pA_J3 = 1 ->psJ3_J2:(l'=10) + prJ3_J2:(l'=13) + (1 - psJ3_J2 - prJ3_J2):(l'=26);
	[r12_18] l=13 & pA_J3 = 2 ->psJ3_Moona:(l'=18) + prJ3_Moona:(l'=13) + (1 - psJ3_Moona - prJ3_Moona):(l'=26);
	[r12_20] l=13 & pA_J3 = 3 ->psJ3_Pretch:(l'=20) + prJ3_Pretch:(l'=13) + (1 - psJ3_Pretch - prJ3_Pretch):(l'=26);

	[] l=14 -> pfJ4:(l'=26) + (1-pfJ4):(l'=15);
	[r14_0] l=15 & pA_J4 = 1 ->psJ4_Chool:(l'=0) + prJ4_Chool:(l'=15) + (1 - psJ4_Chool - prJ4_Chool):(l'=26);
	[r14_2] l=15 & pA_J4 = 2 ->psJ4_Fare:(l'=2) + prJ4_Fare:(l'=15) + (1 - psJ4_Fare - prJ4_Fare):(l'=26);
	[r14_6] l=15 & pA_J4 = 3 ->psJ4_Hurratta:(l'=6) + prJ4_Hurratta:(l'=15) + (1 - psJ4_Hurratta - prJ4_Hurratta):(l'=26);

	[] l=16 -> pfKremshaw:(l'=26) + (1-pfKremshaw):(l'=17);
	[r16_8] l=17 & pA_Kremshaw = 1 ->psKremshaw_J1:(l'=8) + prKremshaw_J1:(l'=17) + (1 - psKremshaw_J1 - prKremshaw_J1):(l'=26);
	[r16_22] l=17 & pA_Kremshaw = 2 ->psKremshaw_Treebach:(l'=22) + prKremshaw_Treebach:(l'=17) + (1 - psKremshaw_Treebach - prKremshaw_Treebach):(l'=26);

	[] l=18 -> pfMoona:(l'=26) + (1-pfMoona):(l'=19);
	[r18_6] l=19 & pA_Moona = 1 ->psMoona_Hurratta:(l'=6) + prMoona_Hurratta:(l'=19) + (1 - psMoona_Hurratta - prMoona_Hurratta):(l'=26);
	[r18_8] l=19 & pA_Moona = 2 ->psMoona_J1:(l'=8) + prMoona_J1:(l'=19) + (1 - psMoona_J1 - prMoona_J1):(l'=26);
	[r18_12] l=19 & pA_Moona = 3 ->psMoona_J3:(l'=12) + prMoona_J3:(l'=19) + (1 - psMoona_J3 - prMoona_J3):(l'=26);

	[] l=20 -> pfPretch:(l'=26) + (1-pfPretch):(l'=21);
	[r20_4] l=21 & pA_Pretch = 1 ->psPretch_Greta:(l'=4) + prPretch_Greta:(l'=21) + (1 - psPretch_Greta - prPretch_Greta):(l'=26);
	[r20_6] l=21 & pA_Pretch = 2 ->psPretch_Hurratta:(l'=6) + prPretch_Hurratta:(l'=21) + (1 - psPretch_Hurratta - prPretch_Hurratta):(l'=26);
	[r20_12] l=21 & pA_Pretch = 3 ->psPretch_J3:(l'=12) + prPretch_J3:(l'=21) + (1 - psPretch_J3 - prPretch_J3):(l'=26);

	[] l=22 -> pfTreebach:(l'=26) + (1-pfTreebach):(l'=23);
	[r22_0] l=23 & pA_Treebach = 1 ->psTreebach_Chool:(l'=0) + prTreebach_Chool:(l'=23) + (1 - psTreebach_Chool - prTreebach_Chool):(l'=26);
	[r22_2] l=23 & pA_Treebach = 2 ->psTreebach_Fare:(l'=2) + prTreebach_Fare:(l'=23) + (1 - psTreebach_Fare - prTreebach_Fare):(l'=26);
	[r22_16] l=23 & pA_Treebach = 3 ->psTreebach_Kremshaw:(l'=16) + prTreebach_Kremshaw:(l'=23) + (1 - psTreebach_Kremshaw - prTreebach_Kremshaw):(l'=26);

	[] l=24 -> pfUlla:(l'=26) + (1-pfUlla):(l'=25);
	[r24_8] l=25 & pA_Ulla = 1 ->psUlla_J1:(l'=8) + prUlla_J1:(l'=25) + (1 - psUlla_J1 - prUlla_J1):(l'=26);
	[r24_10] l=25 & pA_Ulla = 2 ->psUlla_J2:(l'=10) + prUlla_J2:(l'=25) + (1 - psUlla_J2 - prUlla_J2):(l'=26);

endmodule

rewards "difficulty"
	[] l=2 : 5.0;
	[] l=18 : 2.0;
	[] l=22 : 2.0;
endrewards


rewards "speed"
	[r0_14] true : 40.0;
	[r0_22] true : 5.0;
	[r2_6] true : 40.0;
	[r2_14] true : 20.0;
	[r2_22] true : 20.0;
	[r4_20] true : 40.0;
	[r6_2] true : 40.0;
	[r6_14] true : 40.0;
	[r6_18] true : 40.0;
	[r6_20] true : 40.0;
	[r8_16] true : 10.0;
	[r8_18] true : 40.0;
	[r8_24] true : 25.0;
	[r10_12] true : 25.0;
	[r10_24] true : 25.0;
	[r12_10] true : 25.0;
	[r12_18] true : 40.0;
	[r12_20] true : 40.0;
	[r14_0] true : 40.0;
	[r14_2] true : 20.0;
	[r14_6] true : 40.0;
	[r16_8] true : 10.0;
	[r16_22] true : 40.0;
	[r18_6] true : 40.0;
	[r18_8] true : 40.0;
	[r18_12] true : 40.0;
	[r20_4] true : 40.0;
	[r20_6] true : 40.0;
	[r20_12] true : 40.0;
	[r22_0] true : 5.0;
	[r22_2] true : 20.0;
	[r22_16] true : 40.0;
	[r24_8] true : 25.0;
	[r24_10] true : 25.0;
endrewards


rewards "risk"
	[] l=2 : 3.0;
	[] l=16 : 2.0;
	[] l=18 : 5.0;
endrewards


rewards "distance"
	[r0_14] true : 105.0;
	[r0_22] true : 249.0;
	[r2_6] true : 186.0;
	[r2_14] true : 84.0;
	[r2_22] true : 132.0;
	[r4_20] true : 162.0;
	[r6_2] true : 186.0;
	[r6_14] true : 162.0;
	[r6_18] true : 201.0;
	[r6_20] true : 225.0;
	[r8_16] true : 117.0;
	[r8_18] true : 165.0;
	[r8_24] true : 180.0;
	[r10_12] true : 222.0;
	[r10_24] true : 135.0;
	[r12_10] true : 222.0;
	[r12_18] true : 69.0;
	[r12_20] true : 96.0;
	[r14_0] true : 105.0;
	[r14_2] true : 84.0;
	[r14_6] true : 162.0;
	[r16_8] true : 117.0;
	[r16_22] true : 189.0;
	[r18_6] true : 201.0;
	[r18_8] true : 165.0;
	[r18_12] true : 69.0;
	[r20_4] true : 162.0;
	[r20_6] true : 225.0;
	[r20_12] true : 96.0;
	[r22_0] true : 249.0;
	[r22_2] true : 132.0;
	[r22_16] true : 189.0;
	[r24_8] true : 180.0;
	[r24_10] true : 135.0;
endrewards


rewards "time"
	[r0_14] true : 105.0/40.0;
	[r0_22] true : 249.0/5.0;
	[r2_6] true : 186.0/40.0;
	[r2_14] true : 84.0/20.0;
	[r2_22] true : 132.0/20.0;
	[r4_20] true : 162.0/40.0;
	[r6_2] true : 186.0/40.0;
	[r6_14] true : 162.0/40.0;
	[r6_18] true : 201.0/40.0;
	[r6_20] true : 225.0/40.0;
	[r8_16] true : 117.0/10.0;
	[r8_18] true : 165.0/40.0;
	[r8_24] true : 180.0/25.0;
	[r10_12] true : 222.0/25.0;
	[r10_24] true : 135.0/25.0;
	[r12_10] true : 222.0/25.0;
	[r12_18] true : 69.0/40.0;
	[r12_20] true : 96.0/40.0;
	[r14_0] true : 105.0/40.0;
	[r14_2] true : 84.0/20.0;
	[r14_6] true : 162.0/40.0;
	[r16_8] true : 117.0/10.0;
	[r16_22] true : 189.0/40.0;
	[r18_6] true : 201.0/40.0;
	[r18_8] true : 165.0/40.0;
	[r18_12] true : 69.0/40.0;
	[r20_4] true : 162.0/40.0;
	[r20_6] true : 225.0/40.0;
	[r20_12] true : 96.0/40.0;
	[r22_0] true : 249.0/5.0;
	[r22_2] true : 132.0/20.0;
	[r22_16] true : 189.0/40.0;
	[r24_8] true : 180.0/25.0;
	[r24_10] true : 135.0/25.0;
endrewards

