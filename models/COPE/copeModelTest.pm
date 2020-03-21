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
const int action_Chool = 1; // lower:1, upper:2
const int action_Fare = 1; // lower:1, upper:3
const int action_Greta = 1; // lower:1, upper:1
const int action_Hurratta = 1; // lower:1, upper:4
const int action_J1 = 1; // lower:1, upper:3
const int action_J2 = 2; // lower:1, upper:2
const int action_J3 = 1; // lower:1, upper:3
const int action_J4 = 2; // lower:1, upper:3
const int action_Kremshaw = 2; // lower:1, upper:2
const int action_Moona = 3; // lower:1, upper:3
const int action_Pretch = 3; // lower:1, upper:3
const int action_Treebach = 1; // lower:1, upper:3
const int action_Ulla = 1; // lower:1, upper:2

const int action_R_Chool = 1; // lower:1, upper:2
const int action_R_Fare = 1; // lower:1, upper:3
const int action_R_Greta = 1; // lower:1, upper:1
const int action_R_Hurratta = 1; // lower:1, upper:4
const int action_R_J1 = 1; // lower:1, upper:3
const int action_R_J2 = 2; // lower:1, upper:2
const int action_R_J3 = 1; // lower:1, upper:3
const int action_R_J4 = 2; // lower:1, upper:3
const int action_R_Kremshaw = 2; // lower:1, upper:2
const int action_R_Moona = 3; // lower:1, upper:3
const int action_R_Pretch = 3; // lower:1, upper:3
const int action_R_Treebach = 1; // lower:1, upper:3
const int action_R_Ulla = 1; // lower:1, upper:2



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

const double pfChool0 =  0;
const double pfChool1 =  0;
const double pfChool2 =  0;
const double pfChool3 =  0;
const double pfChool4 =  0;

const double pfFare0 =  .1333987127197445;
const double pfFare1 =  .100738306767869;
const double pfFare2 =  .0451622198333237;
const double pfFare3 =  .1199021229505826;
const double pfFare4 =  .1007986377284802;

const double pfGreta0 =  .060330428611174;
const double pfGreta1 =  .0181661818008681;
const double pfGreta2 =  .2385910551919016;
const double pfGreta3 =  .1190147758872623;
const double pfGreta4 =  .0638975585087941;

const double pfHurratta0 =  .0400195856164524;
const double pfHurratta1 =  .0209782969122737;
const double pfHurratta2 =  .1585739039606771;
const double pfHurratta3 =  .1506749643839539;
const double pfHurratta4 =  .1297532491266429;

const double pfJ10 =  .0235487722461097;
const double pfJ11 =  .0192951106196824;
const double pfJ12 =  .0099292849566613;
const double pfJ13 =  .0239715829481971;
const double pfJ14 =  .0232552492293494;

const double pfJ20 =  .028472347089386;
const double pfJ21 =  .0099658692964732;
const double pfJ22 =  .03695535488503;
const double pfJ23 =  .0058518975958882;
const double pfJ24 =  .0187545311332226;

const double pfJ30 =  .0381604437753096;
const double pfJ31 =  .0018360013204065;
const double pfJ32 =  .0129398224207073;
const double pfJ33 =  .0300941071297023;
const double pfJ34 =  .0169696253538743;

const double pfJ40 =  .0207123411057458;
const double pfJ41 =  .024662046467603;
const double pfJ42 =  .0117565822029239;
const double pfJ43 =  .0222379845739202;
const double pfJ44 =  .0206310456498071;

const double pfKremshaw0 =  .1535005150340006;
const double pfKremshaw1 =  .1221802696794543;
const double pfKremshaw2 =  .1452199065618374;
const double pfKremshaw3 =  .005075473223877;
const double pfKremshaw4 =  .0740238355008307;

const double pfMoona0 =  .2112031913506429;
const double pfMoona1 =  .1043512441437384;
const double pfMoona2 =  .0397182138018613;
const double pfMoona3 =  .1154554986700175;
const double pfMoona4 =  .0292718520337399;

const double pfPretch0 =  .1175072655942227;
const double pfPretch1 =  .006173995268758;
const double pfPretch2 =  .1809377023784497;
const double pfPretch3 =  .1782128265477255;
const double pfPretch4 =  .0171682102108441;

const double pfTreebach0 =  .13843590405652;
const double pfTreebach1 =  .141150148538136;
const double pfTreebach2 =  .0587449531012995;
const double pfTreebach3 =  .1543684194915376;
const double pfTreebach4 =  .0073005748125068;

const double pfUlla0 =  .0803791358522881;
const double pfUlla1 =  .0307499025972484;
const double pfUlla2 =  .15911538213082;
const double pfUlla3 =  .1627830947325945;
const double pfUlla4 =  .066972484687049;


module main
	l:[0..26] init 24;
	[rChool0] l=0 & action_R_Chool = 0-> pfChool0:(l'=26) + (1-pfChool0):(l'=1);
	[rChool1] l=0 & action_R_Chool = 1-> pfChool1:(l'=26) + (1-pfChool1):(l'=1);
	[rChool2] l=0 & action_R_Chool = 2-> pfChool2:(l'=26) + (1-pfChool2):(l'=1);
	[rChool3] l=0 & action_R_Chool = 3-> pfChool3:(l'=26) + (1-pfChool3):(l'=1);
	[rChool4] l=0 & action_R_Chool = 4-> pfChool4:(l'=26) + (1-pfChool4):(l'=1);
	[r0_14] l=1 & action_Chool = 1 ->psChool_J4:(l'=14) + prChool_J4:(l'=1) + (1 - psChool_J4 - prChool_J4):(l'=26);
	[r0_22] l=1 & action_Chool = 2 ->psChool_Treebach:(l'=22) + prChool_Treebach:(l'=1) + (1 - psChool_Treebach - prChool_Treebach):(l'=26);

	[rFare0] l=2 & action_R_Fare = 0-> pfFare0:(l'=26) + (1-pfFare0):(l'=3);
	[rFare1] l=2 & action_R_Fare = 1-> pfFare1:(l'=26) + (1-pfFare1):(l'=3);
	[rFare2] l=2 & action_R_Fare = 2-> pfFare2:(l'=26) + (1-pfFare2):(l'=3);
	[rFare3] l=2 & action_R_Fare = 3-> pfFare3:(l'=26) + (1-pfFare3):(l'=3);
	[rFare4] l=2 & action_R_Fare = 4-> pfFare4:(l'=26) + (1-pfFare4):(l'=3);
	[r2_6] l=3 & action_Fare = 1 ->psFare_Hurratta:(l'=6) + prFare_Hurratta:(l'=3) + (1 - psFare_Hurratta - prFare_Hurratta):(l'=26);
	[r2_14] l=3 & action_Fare = 2 ->psFare_J4:(l'=14) + prFare_J4:(l'=3) + (1 - psFare_J4 - prFare_J4):(l'=26);
	[r2_22] l=3 & action_Fare = 3 ->psFare_Treebach:(l'=22) + prFare_Treebach:(l'=3) + (1 - psFare_Treebach - prFare_Treebach):(l'=26);

	[rGreta0] l=4 & action_R_Greta = 0-> pfGreta0:(l'=26) + (1-pfGreta0):(l'=5);
	[rGreta1] l=4 & action_R_Greta = 1-> pfGreta1:(l'=26) + (1-pfGreta1):(l'=5);
	[rGreta2] l=4 & action_R_Greta = 2-> pfGreta2:(l'=26) + (1-pfGreta2):(l'=5);
	[rGreta3] l=4 & action_R_Greta = 3-> pfGreta3:(l'=26) + (1-pfGreta3):(l'=5);
	[rGreta4] l=4 & action_R_Greta = 4-> pfGreta4:(l'=26) + (1-pfGreta4):(l'=5);
	[r4_20] l=5 & action_Greta = 1 ->psGreta_Pretch:(l'=20) + prGreta_Pretch:(l'=5) + (1 - psGreta_Pretch - prGreta_Pretch):(l'=26);

	[rHurratta0] l=6 & action_R_Hurratta = 0-> pfHurratta0:(l'=26) + (1-pfHurratta0):(l'=7);
	[rHurratta1] l=6 & action_R_Hurratta = 1-> pfHurratta1:(l'=26) + (1-pfHurratta1):(l'=7);
	[rHurratta2] l=6 & action_R_Hurratta = 2-> pfHurratta2:(l'=26) + (1-pfHurratta2):(l'=7);
	[rHurratta3] l=6 & action_R_Hurratta = 3-> pfHurratta3:(l'=26) + (1-pfHurratta3):(l'=7);
	[rHurratta4] l=6 & action_R_Hurratta = 4-> pfHurratta4:(l'=26) + (1-pfHurratta4):(l'=7);
	[r6_2] l=7 & action_Hurratta = 1 ->psHurratta_Fare:(l'=2) + prHurratta_Fare:(l'=7) + (1 - psHurratta_Fare - prHurratta_Fare):(l'=26);
	[r6_14] l=7 & action_Hurratta = 2 ->psHurratta_J4:(l'=14) + prHurratta_J4:(l'=7) + (1 - psHurratta_J4 - prHurratta_J4):(l'=26);
	[r6_18] l=7 & action_Hurratta = 3 ->psHurratta_Moona:(l'=18) + prHurratta_Moona:(l'=7) + (1 - psHurratta_Moona - prHurratta_Moona):(l'=26);
	[r6_20] l=7 & action_Hurratta = 4 ->psHurratta_Pretch:(l'=20) + prHurratta_Pretch:(l'=7) + (1 - psHurratta_Pretch - prHurratta_Pretch):(l'=26);

	[rJ10] l=8 & action_R_J1 = 0-> pfJ10:(l'=26) + (1-pfJ10):(l'=9);
	[rJ11] l=8 & action_R_J1 = 1-> pfJ11:(l'=26) + (1-pfJ11):(l'=9);
	[rJ12] l=8 & action_R_J1 = 2-> pfJ12:(l'=26) + (1-pfJ12):(l'=9);
	[rJ13] l=8 & action_R_J1 = 3-> pfJ13:(l'=26) + (1-pfJ13):(l'=9);
	[rJ14] l=8 & action_R_J1 = 4-> pfJ14:(l'=26) + (1-pfJ14):(l'=9);
	[r8_16] l=9 & action_J1 = 1 ->psJ1_Kremshaw:(l'=16) + prJ1_Kremshaw:(l'=9) + (1 - psJ1_Kremshaw - prJ1_Kremshaw):(l'=26);
	[r8_18] l=9 & action_J1 = 2 ->psJ1_Moona:(l'=18) + prJ1_Moona:(l'=9) + (1 - psJ1_Moona - prJ1_Moona):(l'=26);
	[r8_24] l=9 & action_J1 = 3 ->psJ1_Ulla:(l'=24) + prJ1_Ulla:(l'=9) + (1 - psJ1_Ulla - prJ1_Ulla):(l'=26);

	[rJ20] l=10 & action_R_J2 = 0-> pfJ20:(l'=26) + (1-pfJ20):(l'=11);
	[rJ21] l=10 & action_R_J2 = 1-> pfJ21:(l'=26) + (1-pfJ21):(l'=11);
	[rJ22] l=10 & action_R_J2 = 2-> pfJ22:(l'=26) + (1-pfJ22):(l'=11);
	[rJ23] l=10 & action_R_J2 = 3-> pfJ23:(l'=26) + (1-pfJ23):(l'=11);
	[rJ24] l=10 & action_R_J2 = 4-> pfJ24:(l'=26) + (1-pfJ24):(l'=11);
	[r10_12] l=11 & action_J2 = 1 ->psJ2_J3:(l'=12) + prJ2_J3:(l'=11) + (1 - psJ2_J3 - prJ2_J3):(l'=26);
	[r10_24] l=11 & action_J2 = 2 ->psJ2_Ulla:(l'=24) + prJ2_Ulla:(l'=11) + (1 - psJ2_Ulla - prJ2_Ulla):(l'=26);

	[rJ30] l=12 & action_R_J3 = 0-> pfJ30:(l'=26) + (1-pfJ30):(l'=13);
	[rJ31] l=12 & action_R_J3 = 1-> pfJ31:(l'=26) + (1-pfJ31):(l'=13);
	[rJ32] l=12 & action_R_J3 = 2-> pfJ32:(l'=26) + (1-pfJ32):(l'=13);
	[rJ33] l=12 & action_R_J3 = 3-> pfJ33:(l'=26) + (1-pfJ33):(l'=13);
	[rJ34] l=12 & action_R_J3 = 4-> pfJ34:(l'=26) + (1-pfJ34):(l'=13);
	[r12_10] l=13 & action_J3 = 1 ->psJ3_J2:(l'=10) + prJ3_J2:(l'=13) + (1 - psJ3_J2 - prJ3_J2):(l'=26);
	[r12_18] l=13 & action_J3 = 2 ->psJ3_Moona:(l'=18) + prJ3_Moona:(l'=13) + (1 - psJ3_Moona - prJ3_Moona):(l'=26);
	[r12_20] l=13 & action_J3 = 3 ->psJ3_Pretch:(l'=20) + prJ3_Pretch:(l'=13) + (1 - psJ3_Pretch - prJ3_Pretch):(l'=26);

	[rJ40] l=14 & action_R_J4 = 0-> pfJ40:(l'=26) + (1-pfJ40):(l'=15);
	[rJ41] l=14 & action_R_J4 = 1-> pfJ41:(l'=26) + (1-pfJ41):(l'=15);
	[rJ42] l=14 & action_R_J4 = 2-> pfJ42:(l'=26) + (1-pfJ42):(l'=15);
	[rJ43] l=14 & action_R_J4 = 3-> pfJ43:(l'=26) + (1-pfJ43):(l'=15);
	[rJ44] l=14 & action_R_J4 = 4-> pfJ44:(l'=26) + (1-pfJ44):(l'=15);
	[r14_0] l=15 & action_J4 = 1 ->psJ4_Chool:(l'=0) + prJ4_Chool:(l'=15) + (1 - psJ4_Chool - prJ4_Chool):(l'=26);
	[r14_2] l=15 & action_J4 = 2 ->psJ4_Fare:(l'=2) + prJ4_Fare:(l'=15) + (1 - psJ4_Fare - prJ4_Fare):(l'=26);
	[r14_6] l=15 & action_J4 = 3 ->psJ4_Hurratta:(l'=6) + prJ4_Hurratta:(l'=15) + (1 - psJ4_Hurratta - prJ4_Hurratta):(l'=26);

	[rKremshaw0] l=16 & action_R_Kremshaw = 0-> pfKremshaw0:(l'=26) + (1-pfKremshaw0):(l'=17);
	[rKremshaw1] l=16 & action_R_Kremshaw = 1-> pfKremshaw1:(l'=26) + (1-pfKremshaw1):(l'=17);
	[rKremshaw2] l=16 & action_R_Kremshaw = 2-> pfKremshaw2:(l'=26) + (1-pfKremshaw2):(l'=17);
	[rKremshaw3] l=16 & action_R_Kremshaw = 3-> pfKremshaw3:(l'=26) + (1-pfKremshaw3):(l'=17);
	[rKremshaw4] l=16 & action_R_Kremshaw = 4-> pfKremshaw4:(l'=26) + (1-pfKremshaw4):(l'=17);
	[r16_8] l=17 & action_Kremshaw = 1 ->psKremshaw_J1:(l'=8) + prKremshaw_J1:(l'=17) + (1 - psKremshaw_J1 - prKremshaw_J1):(l'=26);
	[r16_22] l=17 & action_Kremshaw = 2 ->psKremshaw_Treebach:(l'=22) + prKremshaw_Treebach:(l'=17) + (1 - psKremshaw_Treebach - prKremshaw_Treebach):(l'=26);

	[rMoona0] l=18 & action_R_Moona = 0-> pfMoona0:(l'=26) + (1-pfMoona0):(l'=19);
	[rMoona1] l=18 & action_R_Moona = 1-> pfMoona1:(l'=26) + (1-pfMoona1):(l'=19);
	[rMoona2] l=18 & action_R_Moona = 2-> pfMoona2:(l'=26) + (1-pfMoona2):(l'=19);
	[rMoona3] l=18 & action_R_Moona = 3-> pfMoona3:(l'=26) + (1-pfMoona3):(l'=19);
	[rMoona4] l=18 & action_R_Moona = 4-> pfMoona4:(l'=26) + (1-pfMoona4):(l'=19);
	[r18_6] l=19 & action_Moona = 1 ->psMoona_Hurratta:(l'=6) + prMoona_Hurratta:(l'=19) + (1 - psMoona_Hurratta - prMoona_Hurratta):(l'=26);
	[r18_8] l=19 & action_Moona = 2 ->psMoona_J1:(l'=8) + prMoona_J1:(l'=19) + (1 - psMoona_J1 - prMoona_J1):(l'=26);
	[r18_12] l=19 & action_Moona = 3 ->psMoona_J3:(l'=12) + prMoona_J3:(l'=19) + (1 - psMoona_J3 - prMoona_J3):(l'=26);

	[rPretch0] l=20 & action_R_Pretch = 0-> pfPretch0:(l'=26) + (1-pfPretch0):(l'=21);
	[rPretch1] l=20 & action_R_Pretch = 1-> pfPretch1:(l'=26) + (1-pfPretch1):(l'=21);
	[rPretch2] l=20 & action_R_Pretch = 2-> pfPretch2:(l'=26) + (1-pfPretch2):(l'=21);
	[rPretch3] l=20 & action_R_Pretch = 3-> pfPretch3:(l'=26) + (1-pfPretch3):(l'=21);
	[rPretch4] l=20 & action_R_Pretch = 4-> pfPretch4:(l'=26) + (1-pfPretch4):(l'=21);
	[r20_4] l=21 & action_Pretch = 1 ->psPretch_Greta:(l'=4) + prPretch_Greta:(l'=21) + (1 - psPretch_Greta - prPretch_Greta):(l'=26);
	[r20_6] l=21 & action_Pretch = 2 ->psPretch_Hurratta:(l'=6) + prPretch_Hurratta:(l'=21) + (1 - psPretch_Hurratta - prPretch_Hurratta):(l'=26);
	[r20_12] l=21 & action_Pretch = 3 ->psPretch_J3:(l'=12) + prPretch_J3:(l'=21) + (1 - psPretch_J3 - prPretch_J3):(l'=26);

	[rTreebach0] l=22 & action_R_Treebach = 0-> pfTreebach0:(l'=26) + (1-pfTreebach0):(l'=23);
	[rTreebach1] l=22 & action_R_Treebach = 1-> pfTreebach1:(l'=26) + (1-pfTreebach1):(l'=23);
	[rTreebach2] l=22 & action_R_Treebach = 2-> pfTreebach2:(l'=26) + (1-pfTreebach2):(l'=23);
	[rTreebach3] l=22 & action_R_Treebach = 3-> pfTreebach3:(l'=26) + (1-pfTreebach3):(l'=23);
	[rTreebach4] l=22 & action_R_Treebach = 4-> pfTreebach4:(l'=26) + (1-pfTreebach4):(l'=23);
	[r22_0] l=23 & action_Treebach = 1 ->psTreebach_Chool:(l'=0) + prTreebach_Chool:(l'=23) + (1 - psTreebach_Chool - prTreebach_Chool):(l'=26);
	[r22_2] l=23 & action_Treebach = 2 ->psTreebach_Fare:(l'=2) + prTreebach_Fare:(l'=23) + (1 - psTreebach_Fare - prTreebach_Fare):(l'=26);
	[r22_16] l=23 & action_Treebach = 3 ->psTreebach_Kremshaw:(l'=16) + prTreebach_Kremshaw:(l'=23) + (1 - psTreebach_Kremshaw - prTreebach_Kremshaw):(l'=26);

	[rUlla0] l=24 & action_R_Ulla = 0-> pfUlla0:(l'=26) + (1-pfUlla0):(l'=25);
	[rUlla1] l=24 & action_R_Ulla = 1-> pfUlla1:(l'=26) + (1-pfUlla1):(l'=25);
	[rUlla2] l=24 & action_R_Ulla = 2-> pfUlla2:(l'=26) + (1-pfUlla2):(l'=25);
	[rUlla3] l=24 & action_R_Ulla = 3-> pfUlla3:(l'=26) + (1-pfUlla3):(l'=25);
	[rUlla4] l=24 & action_R_Ulla = 4-> pfUlla4:(l'=26) + (1-pfUlla4):(l'=25);
	[r24_8] l=25 & action_Ulla = 1 ->psUlla_J1:(l'=8) + prUlla_J1:(l'=25) + (1 - psUlla_J1 - prUlla_J1):(l'=26);
	[r24_10] l=25 & action_Ulla = 2 ->psUlla_J2:(l'=10) + prUlla_J2:(l'=25) + (1 - psUlla_J2 - prUlla_J2):(l'=26);

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
	[rChool0] true : 11.1;
	[rChool1] true : 11.5;
	[rChool2] true : 8.8;
	[rChool3] true : 11.3;
	[rChool4] true : 8.8;
	[rFare0] true : 14.3;
	[rFare1] true : 10.1;
	[rFare2] true : 5.8;
	[rFare3] true : 10.1;
	[rFare4] true : 10.1;
	[rGreta0] true : 7.8;
	[rGreta1] true : 11.3;
	[rGreta2] true : 7.9;
	[rGreta3] true : 7.4;
	[rGreta4] true : 12.0;
	[rHurratta0] true : 8.0;
	[rHurratta1] true : 12.2;
	[rHurratta2] true : 6.7;
	[rHurratta3] true : 7.5;
	[rHurratta4] true : 13.5;
	[rJ10] true : 8.5;
	[rJ11] true : 10.7;
	[rJ12] true : 9.3;
	[rJ13] true : 10.0;
	[rJ14] true : 10.0;
	[rJ20] true : 9.9;
	[rJ21] true : 14.2;
	[rJ22] true : 14.5;
	[rJ23] true : 11.9;
	[rJ24] true : 11.5;
	[rJ30] true : 5.9;
	[rJ31] true : 9.0;
	[rJ32] true : 9.0;
	[rJ33] true : 8.3;
	[rJ34] true : 8.5;
	[rJ40] true : 11.3;
	[rJ41] true : 10.8;
	[rJ42] true : 11.3;
	[rJ43] true : 10.1;
	[rJ44] true : 5.5;
	[rKremshaw0] true : 9.0;
	[rKremshaw1] true : 8.7;
	[rKremshaw2] true : 13.1;
	[rKremshaw3] true : 8.2;
	[rKremshaw4] true : 12.7;
	[rMoona0] true : 9.6;
	[rMoona1] true : 14.1;
	[rMoona2] true : 7.0;
	[rMoona3] true : 13.4;
	[rMoona4] true : 13.3;
	[rPretch0] true : 11.0;
	[rPretch1] true : 5.4;
	[rPretch2] true : 7.3;
	[rPretch3] true : 12.5;
	[rPretch4] true : 9.7;
	[rTreebach0] true : 11.4;
	[rTreebach1] true : 13.8;
	[rTreebach2] true : 8.3;
	[rTreebach3] true : 12.5;
	[rTreebach4] true : 10.4;
	[rUlla0] true : 12.6;
	[rUlla1] true : 8.0;
	[rUlla2] true : 12.4;
	[rUlla3] true : 6.9;
	[rUlla4] true : 5.5;
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
	[rChool0] true : 7.8100000000000005/11.1;
	[rChool1] true : 5.11/11.5;
	[rChool2] true : 8.58/8.8;
	[rChool3] true : 4.76/11.3;
	[rChool4] true : 6.01/8.8;
	[rFare0] true : 5.79/14.3;
	[rFare1] true : 6.859999999999999/10.1;
	[rFare2] true : 4.58/5.8;
	[rFare3] true : 5.9399999999999995/10.1;
	[rFare4] true : 4.33/10.1;
	[rGreta0] true : 8.89/7.8;
	[rGreta1] true : 4.53/11.3;
	[rGreta2] true : 7.529999999999999/7.9;
	[rGreta3] true : 4.6899999999999995/7.4;
	[rGreta4] true : 4.82/12.0;
	[rHurratta0] true : 6.8100000000000005/8.0;
	[rHurratta1] true : 5.77/12.2;
	[rHurratta2] true : 7.7/6.7;
	[rHurratta3] true : 7.9/7.5;
	[rHurratta4] true : 9.32/13.5;
	[rJ10] true : 5.66/8.5;
	[rJ11] true : 4.8/10.7;
	[rJ12] true : 9.309999999999999/9.3;
	[rJ13] true : 4.39/10.0;
	[rJ14] true : 9.02/10.0;
	[rJ20] true : 7.77/9.9;
	[rJ21] true : 7.35/14.2;
	[rJ22] true : 9.469999999999999/14.5;
	[rJ23] true : 9.55/11.9;
	[rJ24] true : 8.95/11.5;
	[rJ30] true : 7.57/5.9;
	[rJ31] true : 9.02/9.0;
	[rJ32] true : 7.91/9.0;
	[rJ33] true : 6.5600000000000005/8.3;
	[rJ34] true : 5.03/8.5;
	[rJ40] true : 6.029999999999999/11.3;
	[rJ41] true : 8.39/10.8;
	[rJ42] true : 8.98/11.3;
	[rJ43] true : 4.2/10.1;
	[rJ44] true : 8.690000000000001/5.5;
	[rKremshaw0] true : 6.0/9.0;
	[rKremshaw1] true : 6.3/8.7;
	[rKremshaw2] true : 7.470000000000001/13.1;
	[rKremshaw3] true : 5.4/8.2;
	[rKremshaw4] true : 8.92/12.7;
	[rMoona0] true : 8.16/9.6;
	[rMoona1] true : 5.58/14.1;
	[rMoona2] true : 6.77/7.0;
	[rMoona3] true : 6.3/13.4;
	[rMoona4] true : 6.68/13.3;
	[rPretch0] true : 4.5600000000000005/11.0;
	[rPretch1] true : 7.49/5.4;
	[rPretch2] true : 5.21/7.3;
	[rPretch3] true : 7.87/12.5;
	[rPretch4] true : 5.03/9.7;
	[rTreebach0] true : 4.7/11.4;
	[rTreebach1] true : 7.4/13.8;
	[rTreebach2] true : 9.08/8.3;
	[rTreebach3] true : 7.54/12.5;
	[rTreebach4] true : 4.25/10.4;
	[rUlla0] true : 5.04/12.6;
	[rUlla1] true : 5.86/8.0;
	[rUlla2] true : 4.02/12.4;
	[rUlla3] true : 8.42/6.9;
	[rUlla4] true : 5.52/5.5;
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
	[rChool0] true : 7.8100000000000005;
	[rChool1] true : 5.11;
	[rChool2] true : 8.58;
	[rChool3] true : 4.76;
	[rChool4] true : 6.01;
	[rFare0] true : 5.79;
	[rFare1] true : 6.859999999999999;
	[rFare2] true : 4.58;
	[rFare3] true : 5.9399999999999995;
	[rFare4] true : 4.33;
	[rGreta0] true : 8.89;
	[rGreta1] true : 4.53;
	[rGreta2] true : 7.529999999999999;
	[rGreta3] true : 4.6899999999999995;
	[rGreta4] true : 4.82;
	[rHurratta0] true : 6.8100000000000005;
	[rHurratta1] true : 5.77;
	[rHurratta2] true : 7.7;
	[rHurratta3] true : 7.9;
	[rHurratta4] true : 9.32;
	[rJ10] true : 5.66;
	[rJ11] true : 4.8;
	[rJ12] true : 9.309999999999999;
	[rJ13] true : 4.39;
	[rJ14] true : 9.02;
	[rJ20] true : 7.77;
	[rJ21] true : 7.35;
	[rJ22] true : 9.469999999999999;
	[rJ23] true : 9.55;
	[rJ24] true : 8.95;
	[rJ30] true : 7.57;
	[rJ31] true : 9.02;
	[rJ32] true : 7.91;
	[rJ33] true : 6.5600000000000005;
	[rJ34] true : 5.03;
	[rJ40] true : 6.029999999999999;
	[rJ41] true : 8.39;
	[rJ42] true : 8.98;
	[rJ43] true : 4.2;
	[rJ44] true : 8.690000000000001;
	[rKremshaw0] true : 6.0;
	[rKremshaw1] true : 6.3;
	[rKremshaw2] true : 7.470000000000001;
	[rKremshaw3] true : 5.4;
	[rKremshaw4] true : 8.92;
	[rMoona0] true : 8.16;
	[rMoona1] true : 5.58;
	[rMoona2] true : 6.77;
	[rMoona3] true : 6.3;
	[rMoona4] true : 6.68;
	[rPretch0] true : 4.5600000000000005;
	[rPretch1] true : 7.49;
	[rPretch2] true : 5.21;
	[rPretch3] true : 7.87;
	[rPretch4] true : 5.03;
	[rTreebach0] true : 4.7;
	[rTreebach1] true : 7.4;
	[rTreebach2] true : 9.08;
	[rTreebach3] true : 7.54;
	[rTreebach4] true : 4.25;
	[rUlla0] true : 5.04;
	[rUlla1] true : 5.86;
	[rUlla2] true : 4.02;
	[rUlla3] true : 8.42;
	[rUlla4] true : 5.52;
endrewards



