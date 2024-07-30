package github.zmz.register;

public interface RegisterConfig {

    /**
     * 获取 ip 和端口
     * ip:端口
     */
    String getAddr();

    /**
     * 获取 ip
     */
    String getHost();

    /**
     * 获取端口
     */
    String getPort();

}
