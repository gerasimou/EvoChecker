package evochecker.test;

import evochecker.genetic.problem.parametric.RationalFunction;

public class RationalFunctionTester {

	
	public static void main (String args[]) {
//		               1evTrans11^2+2*evTrans12**evTrans11+3evTrans12^2+4*evTrans11+5*evTrans12+
		String f1 = "{ 1 evTrans11^2 + 2 evTrans12 * evTrans11 + 3 evTrans12^2 + 4 evTrans11 + 5 evTrans12 +  | 6 evTrans11^2 + 7 evTrans12 * evTrans11 + 8 evTrans12^2 + 9 evTrans11 + 1 evTrans12 + 2  }";
		String f2 = "{ 147911154955611887527180061184 evTrans11^2 + 238895232632311723049869676928 evTrans12 * evTrans11 + 90984077676699835522689615744 evTrans12^2 + 1576564444345234366384481839104 evTrans11 + 995745037094126991772336065984 evTrans12 + 519307177171126840946577211392  | 2144699496411687368369252416800 evTrans11^2 + 3603974698186910171927863588275 evTrans12 * evTrans11 + 1459275201775222803558611171475 evTrans12^2 + 23125345144458298641677353900800 evTrans11 + 16168280319595439952356187652775 evTrans12 + 10267347691557549156660542118400  }";
		String f3 = "{ 2923723116952006413237465984768 evTrans11^2 + 4805297851803684478996297097256 evTrans12 * evTrans11 + 1881574734851678065758831112488 evTrans12^2 + 31081705270471559135299981252608 evTrans11 + 20458446803690439354010262530568 evTrans12 + 9420406132426657398633093033984  | 1286819697847012421021551450080 evTrans11^2 + 2162384818912146103156718152965 evTrans12 * evTrans11 + 875565121065133682135166702885 evTrans12^2 + 13875207086674979185006412340480 evTrans11 + 9700968191757263971413712591665 evTrans12 + 6160408614934529493996325271040  }";
		String structParams = "QmaxL,QmaxH";
		
		RationalFunction rf1 = new RationalFunction(f1, structParams);
		System.out.println(rf1.toString());

		RationalFunction rf2 = new RationalFunction(f1, structParams);
		System.out.println(rf2.toString());

		RationalFunction rf3 = new RationalFunction(f1, structParams);
		System.out.println(rf3.toString());
	}
}
