package evochecker.prism;

public class Property {
	
	private boolean maximization;
	
	private double result;
	
	public Property(){
		this.maximization = true;
	}
	
	public Property(boolean maximization){
		this.maximization = maximization;
	}

	public boolean isMaximization() {
		return maximization;
	}

	public void setMaximization(boolean maximization) {
		this.maximization = maximization;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}
	
	

}
