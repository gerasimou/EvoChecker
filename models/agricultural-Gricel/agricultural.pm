dtmc
evolve int worker2_maxRetry_t1l4 [1..5];
evolve int worker2_maxRetry_t3l4 [1..5];
evolve int worker2_maxRetry_t1l7 [1..5];
evolve int worker2_maxRetry_t3l7 [1..5];
evolve int worker2_maxRetry_t3l9 [1..5];
evolve int worker2_maxRetry_t1l6b [1..5];
evolve int worker2_maxRetry_t1l6a [1..5];
evolve int r1_maxRetry_t2l5 [1..10];
evolve int r1_maxRetry_t2l8a [1..10];
evolve int r1_maxRetry_t2l8b [1..10];

const double p_worker2_t1l4_ORIGINAL= 0.85;
const double p_worker2_t3l4_ORIGINAL=0.8;
const double p_worker2_t1l7_ORIGINAL=0.85;
const double p_worker2_t3l7_ORIGINAL=0.89;
const double p_worker2_t3l9_ORIGINAL=0.92;
const double p_worker2_t1l6b_ORIGINAL=0.86;
const double p_worker2_t1l6a_ORIGINAL=0.85;
const double p_r1_t2l5_ORIGINAL=0.9;
const double p_r1_t2l8a_ORIGINAL=0.92;
const double p_r1_t2l8b_ORIGINAL=0.94;

const double e = 2.718281828459045;
const double steepness = 1.5;
formula p_worker2_t1l4 = 2 * (1 - p_worker2_t1l4_ORIGINAL) * (1 / (1 + 1/pow(e,(worker2retry_t1l4 * steepness)))) + (2 * p_worker2_t1l4_ORIGINAL - 1);
formula p_worker2_t3l4 = 2 * (1 - p_worker2_t3l4_ORIGINAL) * (1 / (1 + 1/pow(e,(worker2retry_t3l4 * steepness)))) + (2 * p_worker2_t3l4_ORIGINAL -1);
formula p_worker2_t1l7 = 2 * (1 - p_worker2_t1l7_ORIGINAL) * (1 / (1 + 1/pow(e,(worker2retry_t1l7 * steepness)))) + (2 * p_worker2_t1l7_ORIGINAL -1);
formula p_worker2_t3l7 = 2 * (1 - p_worker2_t3l7_ORIGINAL) * (1 / (1 + 1/pow(e,(worker2retry_t3l7 * steepness)))) + (2 * p_worker2_t3l7_ORIGINAL -1);
formula p_worker2_t3l9 = 2 * (1 - p_worker2_t3l9_ORIGINAL) * (1 / (1 + 1/pow(e,(worker2retry_t3l9 * steepness)))) + (2 * p_worker2_t3l9_ORIGINAL -1);
formula p_worker2_t1l6b = 2 * (1 - p_worker2_t1l6b_ORIGINAL) * (1 / (1 + 1/pow(e,(worker2retry_t1l6b * steepness)))) + (2 * p_worker2_t1l6b_ORIGINAL -1);
formula p_worker2_t1l6a = 2 * (1 - p_worker2_t1l6a_ORIGINAL) * (1 / (1 + 1/pow(e,(worker2retry_t1l6a * steepness)))) + (2 * p_worker2_t1l6a_ORIGINAL -1);
formula p_r1_t2l5       = 2 * (1 - p_r1_t2l5_ORIGINAL) * (1 / (1 + 1/pow(e,(r1retry_t2l5 * steepness))))       + (2 * p_r1_t2l5_ORIGINAL -1);
formula p_r1_t2l8a      = 2 * (1 - p_r1_t2l8a_ORIGINAL) * (1 / (1 + 1/pow(e,(r1retry_t2l8a * steepness))))      + (2 * p_r1_t2l8a_ORIGINAL -1);
formula p_r1_t2l8b      = 2 * (1 - p_r1_t2l8b_ORIGINAL) * (1 / (1 + 1/pow(e,(r1retry_t2l8b * steepness))))      + (2 * p_r1_t2l8b_ORIGINAL -1);



const int worker2Final = 12;
const int worker2Fail = 13;
const int r1Final = 6;
const int r1Fail = 7;

module _worker2
  worker2 : [0..14];
  worker2retry_t1l4 : [0..worker2_maxRetry_t1l4] init 0;
  worker2retry_t3l4 : [0..worker2_maxRetry_t3l4] init 0;
  worker2retry_t1l7 : [0..worker2_maxRetry_t1l7] init 0;
  worker2retry_t3l7 : [0..worker2_maxRetry_t3l7] init 0;
  worker2retry_t3l9 : [0..worker2_maxRetry_t3l9] init 0;
  worker2retry_t1l6b : [0..worker2_maxRetry_t1l6b] init 0;
  worker2retry_t1l6a : [0..worker2_maxRetry_t1l6a] init 0;

  [worker2movel4] worker2=0-> 1:(worker2'=0+1);
  [worker2dot1l4Retry] worker2=1 & worker2retry_t1l4 < worker2_maxRetry_t1l4 -> p_worker2_t1l4 : (worker2'=worker2+1) + (1-p_worker2_t1l4) : (worker2'=worker2) & (worker2retry_t1l4' = worker2retry_t1l4+1);
  [worker2dot1l4] worker2=1 & worker2retry_t1l4 >= worker2_maxRetry_t1l4 -> 1:(worker2'=worker2Fail);
  [worker2dot3l4Retry] worker2=2 & worker2retry_t3l4 < worker2_maxRetry_t3l4 -> p_worker2_t3l4 : (worker2'=worker2+1) + (1-p_worker2_t3l4) : (worker2'=worker2) & (worker2retry_t3l4' = worker2retry_t3l4+1);
  [worker2dot3l4] worker2=2 & worker2retry_t3l4 >= worker2_maxRetry_t3l4 -> 1:(worker2'=worker2Fail);
  [worker2movel7] worker2=3-> 1:(worker2'=3+1);
  [worker2dot1l7Retry] worker2=4 & worker2retry_t1l7 < worker2_maxRetry_t1l7 -> p_worker2_t1l7 : (worker2'=worker2+1) + (1-p_worker2_t1l7) : (worker2'=worker2) & (worker2retry_t1l7' = worker2retry_t1l7+1);
  [worker2dot1l7] worker2=4 & worker2retry_t1l7 >= worker2_maxRetry_t1l7 -> 1:(worker2'=worker2Fail);
  [worker2dot3l7Retry] worker2=5 & worker2retry_t3l7 < worker2_maxRetry_t3l7 -> p_worker2_t3l7 : (worker2'=worker2+1) + (1-p_worker2_t3l7) : (worker2'=worker2) & (worker2retry_t3l7' = worker2retry_t3l7+1);
  [worker2dot3l7] worker2=5 & worker2retry_t3l7 >= worker2_maxRetry_t3l7 -> 1:(worker2'=worker2Fail);
  [worker2movel8] worker2=6-> 1:(worker2'=6+1);
  [worker2movel9] worker2=7-> 1:(worker2'=7+1);
  [worker2dot3l9Retry] worker2=8 & worker2retry_t3l9 < worker2_maxRetry_t3l9 -> p_worker2_t3l9 : (worker2'=worker2+1) + (1-p_worker2_t3l9) : (worker2'=worker2) & (worker2retry_t3l9' = worker2retry_t3l9+1);
  [worker2dot3l9] worker2=8 & worker2retry_t3l9 >= worker2_maxRetry_t3l9 -> 1:(worker2'=worker2Fail);
  [worker2movel6] worker2=9-> 1:(worker2'=9+1);
  [worker2dot1l6bRetry] worker2=10 & worker2retry_t1l6b < worker2_maxRetry_t1l6b -> p_worker2_t1l6b : (worker2'=worker2+1) + (1-p_worker2_t1l6b) : (worker2'=worker2) & (worker2retry_t1l6b' = worker2retry_t1l6b+1);
  [worker2dot1l6b] worker2=10 & worker2retry_t1l6b >= worker2_maxRetry_t1l6b -> 1:(worker2'=worker2Fail);
  [worker2dot1l6aRetry] worker2=11 & worker2retry_t1l6a < worker2_maxRetry_t1l6a -> p_worker2_t1l6a : (worker2'=worker2+1) + (1-p_worker2_t1l6a) : (worker2'=worker2) & (worker2retry_t1l6a' = worker2retry_t1l6a+1);
  [worker2dot1l6a] worker2=11 & worker2retry_t1l6a >= worker2_maxRetry_t1l6a -> 1:(worker2'=worker2Fail);
endmodule

module _r1
  r1 : [0..8];
  r1retry_t2l5 : [0..r1_maxRetry_t2l5] init 0;
  r1retry_t2l8a : [0..r1_maxRetry_t2l8a] init 0;
  r1retry_t2l8b : [0..r1_maxRetry_t2l8b] init 0;

  [r1movel4] r1=0-> 1:(r1'=0+1);
  [r1movel5] r1=1-> 1:(r1'=1+1);
  [r1dot2l5Retry] r1=2 & r1retry_t2l5 < r1_maxRetry_t2l5 -> p_r1_t2l5 : (r1'=r1+1) + (1-p_r1_t2l5) : (r1'=r1) & (r1retry_t2l5' = r1retry_t2l5+1);
  [r1dot2l5] r1=2 & r1retry_t2l5 >= r1_maxRetry_t2l5 -> 1:(r1'=r1Fail);
  [r1movel8] r1=3-> 1:(r1'=3+1);
  [r1dot2l8aRetry] r1=4 & r1retry_t2l8a < r1_maxRetry_t2l8a -> p_r1_t2l8a : (r1'=r1+1) + (1-p_r1_t2l8a) : (r1'=r1) & (r1retry_t2l8a' = r1retry_t2l8a+1);
  [r1dot2l8a] r1=4 & r1retry_t2l8a >= r1_maxRetry_t2l8a -> 1:(r1'=r1Fail);
  [r1dot2l8bRetry] r1=5 & r1retry_t2l8b < r1_maxRetry_t2l8b -> p_r1_t2l8b : (r1'=r1+1) + (1-p_r1_t2l8b) : (r1'=r1) & (r1retry_t2l8b' = r1retry_t2l8b+1);
  [r1dot2l8b] r1=5 & r1retry_t2l8b >= r1_maxRetry_t2l8b -> 1:(r1'=r1Fail);
endmodule

rewards "cost"
  [worker2movel4] true:1;
  [worker2dot1l4] true:3;
  [worker2dot1l4Retry] true:3;
  [worker2dot3l4] true:5;
  [worker2dot3l4Retry] true:5;
  [worker2movel7] true:1;
  [worker2dot1l7] true:3;
  [worker2dot1l7Retry] true:3;
  [worker2dot3l7] true:5;
  [worker2dot3l7Retry] true:5;
  [worker2movel8] true:1;
  [worker2movel9] true:1;
  [worker2dot3l9] true:5;
  [worker2dot3l9Retry] true:5;
  [worker2movel6] true:1;
  [worker2dot1l6b] true:3;
  [worker2dot1l6bRetry] true:3;
  [worker2dot1l6a] true:3;
  [worker2dot1l6aRetry] true:3;
  [r1movel4] true:1;
  [r1movel5] true:1;
  [r1dot2l5] true:1;
  [r1dot2l5Retry] true:1;
  [r1movel8] true:1;
  [r1dot2l8a] true:1;
  [r1dot2l8aRetry] true:1;
  [r1dot2l8b] true:1;
  [r1dot2l8bRetry] true:1;
endrewards