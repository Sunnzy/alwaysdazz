package DDL;

public class ServiceRuntimeException  extends RuntimeException{

    private Integer code;
    private String description;
//异常类 被ValidUrlPrefixParser类调用
    public ServiceRuntimeException(Integer ioException, String s) {
        super();
    }

    public ServiceRuntimeException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
