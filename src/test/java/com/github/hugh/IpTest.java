package com.github.hugh;

import com.github.hugh.util.ip.IpUtils;
import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.util.List;

/**
 * @author AS
 * @date 2020/9/11 16:42
 */
public class IpTest {

    @Test
    public void test001() {
        String str = "113.218.2.1223";
        System.out.println("--1->>" + RegexUtils.isIp(str));
        System.out.println("--2->>" + RegexUtils.isNotIp(str));
        System.out.println("--3->>" + IpUtils.random());
    }

    @Test
    public void test002() {
        String ip = "43.112.134.143";//ip
        String mask = "20";//位数，如果只知道子网掩码不知道位数的话在参考getMaskMap()方法

        //获得起始IP和终止IP的方法（包含网络地址和广播地址）
        String startIp = IpUtils.calcBeginIp(ip, mask);
        String endIp = IpUtils.calcEndIp(ip, mask);
        System.out.println("起始IP：" + startIp + "终止IP：" + endIp);

        //获得起始IP和终止IP的方法（不包含网络地址和广播地址）
        String subStart = startIp.split("\\.")[0] + "." + startIp.split("\\.")[1] + "." + startIp.split("\\.")[2] + ".";
        String subEnd = endIp.split("\\.")[0] + "." + endIp.split("\\.")[1] + "." + endIp.split("\\.")[2] + ".";
        startIp = subStart + (Integer.parseInt(startIp.split("\\.")[3]) + 1);
        endIp = subEnd + (Integer.parseInt(endIp.split("\\.")[3]) - 1);
        System.out.println("起始IP：" + startIp + "终止IP：" + endIp);
        System.out.println("--子网掩码->>" + IpUtils.getNetmask(mask));
        //判断一个IP是否属于某个网段
        boolean flag = IpUtils.isInRange("10.2.0.0", "10.3.0.0/17");
        System.out.println(flag);

        //根据位数查询IP数量
        int ipCount = IpUtils.getIpCount("8");
        System.out.println(ipCount);

        //判断是否是一个IP
//        System.out.println(IpUtils.isIP("192.168.1.0"));

        //把ip转换为数字(mysql中inet_aton()的实现)
//        System.out.println(IpUtils.ipToDouble("192.168.1.1"));

        //打印IP段所有IP（IP过多会内存溢出）
      List<String> list = IpUtils.parseIpMaskRange(ip, mask);
      for (String s : list){
          System.out.println(s);
      }
    }


    public static void main(String[] args) {
        try {
            String s = "123";
            try (BufferedReader in = new BufferedReader(null)) {
                int i = 1 / 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("====");
    }
}
