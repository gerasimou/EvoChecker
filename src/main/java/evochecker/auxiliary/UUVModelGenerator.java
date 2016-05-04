package evochecker.auxiliary;

public class UUVModelGenerator {

	private final static int SENSORS 			= 10;
	
	private final static double R[] 			= new double[]{5, 4, 4.5, 6, 8, 5.5, 3.5, 6, 6.5, 7, 5};
	
	private final static double ALPHA[] 		= new double[]{0.05, 0.04, 0.045, 0.05, 0.04, 0.045, 0.05, 0.04, 0.045, 0.04};

	private final static double ENERGY_M[] 		= new double[]{0.4, 0.5, 0.6, 0.65, 0.55, 0.3, 0.45, 0.55, 0.4, 0.8};

	private final static double ENERGY_ON[] 	= new double[]{10, 8, 9, 5, 9, 8, 5, 9, 7, 8};

	private final static double ENERGY_OFF[] 	= new double[]{2.4, 2, 3, 4.5, 3.3, 4, 2.5, 3.6, 4, 5};
	
	public static void main(String[] args) {
		
		System.out.println(makeConstants());
		System.out.println(makeParams());
		System.out.println(makeModule(getModule()));
		System.out.println(makeMesurementsReward());
		System.out.println(makeEnergyReward());
	}

	
	
	private static String makeConstants(){
		return "ctmc\n\n"
				+ "const int rON	= 10;\n"
				+ "const int rOFF	= 20;\n";
//				+ "const int rPREP	= 100;\n";
	}
	
	
	private static String makeModule(String input){
		StringBuilder str 	= new StringBuilder("\n");
//		String template 	= "module sensor%=sensor1 [s1=s%, x1=x%, p1=p%, r1=r%, checkS1=checkS%, startS1=startS%,"
//							  + "succS1=succS%, failS1=failS%, prepS1=prepS%, stopS1=stopS%] endmodule\n";
//		String template 	= "module sensor%=sensor1 [s1=s%, x1=x%, p1=p%, r1=r%, startS1=startS%,"
//				  			  + "succS1=succS%, failS1=failS%, prepS1=prepS%] endmodule\n";
		for (int i=0; i<SENSORS; i++){
			str.append(input.replaceAll("%", (i+1)+"")+"\n\n");
		}
		return str.toString();
	}
	
	
	private static String makeParams(){
		StringBuilder str 	= new StringBuilder();
		String template		= "const int x%;\n"
							  + "const double r%	= £;\n"
							  + "const double p%	= 1-<>*sp;\n";
		
		str.append(	"evolve const double sp [1..10];\n\n");
		
		for (int i=0; i<SENSORS; i++){
			str.append("evolve const int x% [0..1];\n".replaceAll("%", (i+1)+""));
		}

		str.append("\n");
		for (int i=0; i<SENSORS; i++){
			str.append("evolve const int rPREP%	 [1..100];\n".replaceAll("%", (i+1)+""));
		}

		str.append("\n");
		for (int i=0; i<SENSORS; i++){
			str.append("const double r%	= £;\n".replaceAll("£", R[i]+"").replaceAll("%", (i+1)+""));
		}
		str.append("\n");
		for (int i=0; i<SENSORS; i++){
			str.append("const double p%	= 1-<>*sp;\n".replaceFirst("<>", ALPHA[i]+"").replaceAll("%", (i+1)+""));
		}
		return str.toString();
	}
	
	
	private static String makeMesurementsReward(){
		StringBuilder str 	= new StringBuilder("rewards \"measurements\"\n");
		String template		= "\t[succS%] true : 1;\n";		
		for (int i=0; i<SENSORS; i++){
			str.append(template.replaceAll("%", (i+1)+""));

		}
		str.append("endrewards\n\n");
		return str.toString();
	}
	
	
	private static String makeEnergyReward(){
		StringBuilder str 	= new StringBuilder("rewards \"energy\"\n");
		String template		=	"\t[startS%] true : x%=1 ? £ : @;\n" +
							 	"\t[succS%]  true : <>;\n" +
							 	"\t[failS%]  true : <>;\n";		
		
		for (int i=0; i<SENSORS; i++){
			String temp = template.replaceAll("%", (i+1)+"");
			temp		= temp.replaceAll("£", ENERGY_ON[i]+"");
			temp		= temp.replaceAll("@", "0");//ENERGY_OFF[i]+"");
			temp		= temp.replaceAll("<>", ENERGY_M[i]+"");
			str.append(temp);
		}
		str.append("endrewards\n\n");
		return str.toString();
	}	
	
	private static String getModule(){
//		return "module sensor1\n"
//				+ "\ts1: [0..5] init 0;\n"
//				+ "\t[checkS1] s1=0 -> x1 		: (s1'=1) + (1-x1): (s1'=5);\n"
//				+ "\t[startS1] s1=1 -> rON		: (s1'=2);\n"
//				+ "\t[succS1]  s1=2 -> p1*r1   	: (s1'=3);\n"
//				+ "\t[failS1]  s1=2 -> (1-p1)*r1	: (s1'=4);"
//				+ "\t[prepS1]  s1=3 -> rPREP   	: (s1'=2);\n"
//				+ "\t[prepS1]  s1=4 -> rPREP   	: (s1'=2);\n"
//				+ "\t[stopS1]  s1=5 -> 1.0     	: (s1'=1);\n"
//				+ "endmodule";

		return "module sensor%\n"
				+ "\ts%: [0..2] init 0;\n"
				+ "\t[startS%] s%=0 -> x%*rON 	: (s%'=1) + (1-x%)*rOFF: (s%'=0);\n"
				+ "\t[succS%]  s%=1 -> p%*r%   	: (s%'=2);\n"
				+ "\t[failS%]  s%=1 -> (1-p%)*r1: (s%'=2);\n"
				+ "\t[prepS%]  s%=2 -> rPREP%   	: (s%'=1);\n"
				+ "endmodule";

	}
	
}
