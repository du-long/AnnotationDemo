package top.itdl.annotation;

public class TestClass {
    @BindAddress("www.baidu.com")
    String address;
    @BindPort("80")
    private String port;

    private int number;

    public void printInfo() {
        System.out.println("info is " + address + ":" + port);
    }


}