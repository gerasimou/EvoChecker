package pse;

public interface PSEMultManyManager<Mult extends PSEMultMany> extends Releaseable
{
	public void update(int matId, Mult mult, double q);
	public Mult create(int matCnt);
}
