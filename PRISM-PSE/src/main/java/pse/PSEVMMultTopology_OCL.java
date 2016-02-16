package pse;

import org.jocl.*;
import static org.jocl.CL.*;

final public class PSEVMMultTopology_OCL implements Releaseable
{
    public PSEVMMultTopology_OCL(PSEVMCreateData_CSR data, cl_context clContext)
    {
        this.stCnt = data.stCnt;

        this.enabledMatNP = data.matNPTrgBeg[stCnt] > 0;
        this.enabledMatIO = data.matIOTrgBeg[stCnt] > 0;
        this.enabledMatI = data.matITrgBeg[stCnt] > 0;

        this.matIOSrcHost = data.matIOSrc;
        this.matIOTrgBegHost = data.matIOTrgBeg;

        this.matISrcHost = data.matISrc;
        this.matITrgBegHost = data.matITrgBeg;

        this.matNPSrcHost = data.matNPSrc;
        this.matNPTrgBegHost = data.matNPTrgBeg;

        if (enabledMatIO) {
            // Setup mat IO
            final Pointer matIOSrc_ = Pointer.to(matIOSrcHost);
            final Pointer matIOTrgBeg_ = Pointer.to(matIOTrgBegHost);

            this.matIOSrc = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_uint * matIOTrgBegHost[stCnt], matIOSrc_, null);
            this.matIOTrgBeg = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_uint * (stCnt + 1), matIOTrgBeg_, null);
        }

        if (enabledMatI) {
            // Setup mat min
            final Pointer matISrc_ = Pointer.to(matISrcHost);
            final Pointer matITrgBeg_ = Pointer.to(matITrgBegHost);

            this.matISrc = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_uint * matITrgBegHost[stCnt], matISrc_, null);
            this.matITrgBeg = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_uint * (stCnt + 1), matITrgBeg_, null);
        }

        if (enabledMatNP) {
            // Setup mat
            final Pointer matSrc_ = Pointer.to(matNPSrcHost);
            final Pointer matTrgBeg_ = Pointer.to(matNPTrgBegHost);

            this.matNPSrc = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_uint * matNPTrgBegHost[stCnt], matSrc_, null);
            this.matNPTrgBeg = clCreateBuffer(clContext, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                    Sizeof.cl_uint * (stCnt + 1), matTrgBeg_, null);
        }
    }

    @Override
    final public void release()
    {
        if (enabledMatIO) {
            clReleaseMemObject(matIOSrc);
            clReleaseMemObject(matIOTrgBeg);
        }

        if (enabledMatI) {
            clReleaseMemObject(matISrc);
            clReleaseMemObject(matITrgBeg);
        }

        if (enabledMatNP) {
            clReleaseMemObject(matNPSrc);
            clReleaseMemObject(matNPTrgBeg);
        }
    }

    final public int stCnt;

    final public int[] matIOSrcHost;
    final public int[] matIOTrgBegHost;

    final public int[] matISrcHost;
    final public int[] matITrgBegHost;

    final public int[] matNPSrcHost;
    final public int[] matNPTrgBegHost;

    public cl_mem matIOSrc;
    public cl_mem matIOTrgBeg;

    public cl_mem matISrc;
    public cl_mem matITrgBeg;

    public cl_mem matNPSrc;
    public cl_mem matNPTrgBeg;

    final public boolean enabledMatNP;
    final public boolean enabledMatIO;
    final public boolean enabledMatI;
}
