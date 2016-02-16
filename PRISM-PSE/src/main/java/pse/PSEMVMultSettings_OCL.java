package pse;

import org.jocl.*;
import static org.jocl.CL.*;

public class PSEMVMultSettings_OCL implements Releaseable
{
	public static PSEMVMultSettings_OCL Default()
	{
		PSEMVMultSettings_OCL opts  = new PSEMVMultSettings_OCL();
		cl_platform_id[] clPlatformIds = OCLProgram.getPlatformIds();
		cl_device_id[] clDeviceIds = OCLProgram.getDeviceIds(clPlatformIds[0], CL_DEVICE_TYPE_ALL);
		opts.clDeviceIdMax = clDeviceIds[0];
		opts.clDeviceIdMin = clDeviceIds[0];
		opts.clContext = OCLProgram.createContext(clPlatformIds[0], new cl_device_id[]{clDeviceIds[0]});

		return opts;
	}

	@Override
	public void release()
	{
		clReleaseContext(clContext);
	}

	public cl_context clContext;
	public cl_device_id clDeviceIdMin;
	public cl_device_id clDeviceIdMax;
}
