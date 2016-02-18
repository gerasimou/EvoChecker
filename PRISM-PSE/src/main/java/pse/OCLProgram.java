package pse;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.clBuildProgram;
import static org.jocl.CL.clCreateCommandQueue;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clCreateKernel;
import static org.jocl.CL.clCreateProgramWithSource;
import static org.jocl.CL.clGetDeviceIDs;
import static org.jocl.CL.clGetPlatformIDs;
import static org.jocl.CL.clReleaseCommandQueue;
import static org.jocl.CL.clReleaseContext;
import static org.jocl.CL.clReleaseProgram;

import java.util.Scanner;

import org.jocl.CL;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

public final class OCLProgram
{
	public OCLProgram()
	{
		// The platform, device type and device number
		// that will be used
		final int platformIndex = 0;
		final long deviceType = CL_DEVICE_TYPE_ALL;
		final int deviceIndex = 0;

		// Enable exceptions and subsequently omit error checks in this sample
		CL.setExceptionsEnabled(true);

		// Obtain the number of platforms
		int numPlatformsArray[] = new int[1];
		clGetPlatformIDs(0, null, numPlatformsArray);
		int numPlatforms = numPlatformsArray[0];

		// Obtain a platform ID
		cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
		clGetPlatformIDs(platforms.length, platforms, null);
		cl_platform_id platform = platforms[platformIndex];

		// Initialize the context properties
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

		// Obtain the number of devices for the platform
		int numDevicesArray[] = new int[1];
		clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
		int numDevices = numDevicesArray[0];

		// Obtain a device ID
		cl_device_id devices[] = new cl_device_id[numDevices];
		clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
		clDeviceId = devices[deviceIndex];

		// Create a context for the selected device
		clContext = clCreateContext(
			contextProperties, 1, new cl_device_id[]{clDeviceId},
			null, null, null);

		// Create a command-queue
		clCommandQueue = clCreateCommandQueue(clContext, clDeviceId, 0, null);

		// Create the program from the source code
		clProgram = clCreateProgramWithSource(clContext,
			1, new String[]{SOURCE}, null, null);

		// Build the program
		clBuildProgram(clProgram, 0, null, null, null, null);
	}

	public static cl_platform_id[] getPlatformIds()
	{
		int clPlatformCnt[] = new int[1];
		clGetPlatformIDs(0, null, clPlatformCnt);

		cl_platform_id clPlatforms[] = new cl_platform_id[clPlatformCnt[0]];
		clGetPlatformIDs(clPlatforms.length, clPlatforms, null);

		return clPlatforms;
	}

	public static cl_device_id[] getDeviceIds(cl_platform_id clPlatformId, long clDeviceType)
	{
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, clPlatformId);

		int clDevicesCnt[] = new int[1];
		clGetDeviceIDs(clPlatformId, clDeviceType, 0, null, clDevicesCnt);

		cl_device_id clDevices[] = new cl_device_id[clDevicesCnt[0]];
		clGetDeviceIDs(clPlatformId, clDeviceType, clDevicesCnt[0], clDevices, null);

		return clDevices;

	}

	public static cl_context createContext(cl_platform_id clPlatformId, cl_device_id[] clDeviceIds)
	{
		cl_context_properties contextProperties = new cl_context_properties();
		contextProperties.addProperty(CL_CONTEXT_PLATFORM, clPlatformId);

		return clCreateContext(contextProperties, clDeviceIds.length, clDeviceIds, null, null, null);
	}

	public static cl_program createProgram(String source, cl_context clContext)
	{
		cl_program clProgram = clCreateProgramWithSource(clContext,
			1, new String[]{SOURCE}, null, null);

		clBuildProgram(clProgram, 0, null, null, null, null);
		return clProgram;
	}

	public static cl_kernel createKernel(String name, cl_program clProgram)
	{
		return clCreateKernel(clProgram, name, null);
	}

	public final cl_command_queue createCommandQueue()
	{
		return createCommandQueue(0);
	}

	public final cl_command_queue createCommandQueue(int params)
	{
		return clCreateCommandQueue(clContext, clDeviceId, params, null);
	}

	public final cl_kernel createKernel(String name)
	{
		return clCreateKernel(clProgram, name, null);
	}

	public final void release()
	{
		clReleaseProgram(clProgram);
		clReleaseCommandQueue(clCommandQueue);
		clReleaseContext(clContext);
	}

	// The local worksize, can be overwritten by the onv variable OCL_LWS
	public static int localWorkSize(int def)
	{
		String envOCL_LWS = System.getenv("OCL_LWS");
		int res = def;
		if (envOCL_LWS != null)
		{
			res = Integer.parseInt(envOCL_LWS);
		}
		return res;
	}

	public final cl_device_id getDeviceId() {
		return clDeviceId;
	}

	public final cl_context getContext() {
		return clContext;
	}

	public final cl_program getProgram() {
		return clProgram;
	}

	private final cl_context clContext;
	private final cl_command_queue clCommandQueue;
	private final cl_program clProgram;
	private final cl_device_id clDeviceId;

	public static String SOURCE =
		new Scanner(OCLProgram.class.getClassLoader().getResourceAsStream("resources/PSEKernels.cl"), "UTF-8").useDelimiter("\\A").next();
}
