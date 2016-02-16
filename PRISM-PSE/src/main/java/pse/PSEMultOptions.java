package pse;

final public class PSEMultOptions
{
	public PSEMultOptions(PSEMultFormat fmt, boolean ocl, int para, int many, boolean adatptiveFoxGlynn)
	{
		this.fmt = fmt;
		this.ocl = ocl;
		this.para = para;
		this.many = many;
		this.adatptiveFoxGlynn = adatptiveFoxGlynn;

		if (fmt == PSEMultFormat.ELL && (!ocl || many < 1)) {
			throw new RuntimeException(
				"PSE_OCL and PSE_MANY has to be enabled (PSE_OCL=1, PSE_MANY>=1) if you want to use PSE_FMT=ELL");
		}

		if (many > 0 && !ocl) {
			throw new RuntimeException(
				"PSE_OCL has to be enabled (PSE_OCL=1) if you want to use PSE_MANY");
		}

		if (many > 0 && para > 0) {
			throw new RuntimeException(
				"PSE_PARA has to be disabled (PSE_PARA=0) if you want to use PSE_MANY");
		}

		if (para < 0) {
			throw new RuntimeException(
				"PSE_PARA has to be set nonnegative integer");
		}

		if (many < 0) {
			throw new RuntimeException(
				"PSE_MANY has to be set nonnegative integer");
		}

		System.err.printf("PSEMultOptions = %s\n", toString());
	}

	@Override
	public String toString()
	{
		return String.format("{ ocl = %s; fmt = %s, para = %s; many = %s; adaptiveFoxGlynn = %s }",
			ocl, fmt, para, many, adatptiveFoxGlynn);
	}

	public PSEMultFormat getFmt()
	{
		return fmt;
	}

	public boolean getOcl()
	{
		return ocl;
	}

	public int getPara()
	{
		return para;
	}

	public int getMany()
	{
		return many;
	}

	public boolean getAdaptiveFoxGlynn()
	{
		return adatptiveFoxGlynn;
	}

	final private PSEMultFormat fmt;
	final private boolean ocl;
	final private int para;
	final private int many;
	final private boolean adatptiveFoxGlynn;
}
