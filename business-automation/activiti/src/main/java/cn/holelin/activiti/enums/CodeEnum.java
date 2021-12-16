package cn.holelin.activiti.enums;
/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2021/12/5 10:23 上午
 * @UpdateUser: HoleLin
 * @UpdateDate: 2021/12/5 10:23 上午
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum CodeEnum {

    SCRYN_CPU_CODE("ScrynCpuCode"),
    SCRYN_GPU_CODE("ScrynGpuCode"),
    SURGI_CPU_CODE("SurgiCpuCode"),
    SURGI_GPU_CODE("SurgiGpuCode");

    CodeEnum(String value) {
        this.value = value;
    }

    public String value;

}
