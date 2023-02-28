package com.yhz.senbeiforummain.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.omg.CORBA.UNKNOWN;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * 获取ip的工具类
 * @author yanhuanzhan
 * @date 2023/2/28 - 15:30
 */
@Slf4j
public class IpUtils {
    private static final String IP_UTILS_FLAG = ",";
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IP1 = "127.0.0.1";

    /**
     * 判断ip地址
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getIpPossession(String ip) throws Exception {
        String cityInfo = IpUtils.getCityInfo(ip);
        if (!StringUtils.isEmpty(cityInfo)) {
            cityInfo = cityInfo.replace("|", " ");
            String[] cityList = cityInfo.split(" ");
            if (cityList.length > 0) {
                // 国内的显示到具体的省
                if ("中国".equals(cityList[0])) {
                    if (cityList.length > 3) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(cityList[0]).append(cityList[1]).append(cityList[2]).append(cityList[3]);
                        return stringBuilder.toString();
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(cityList[0]).append(cityList[1]).append(cityList[2]);
                        return stringBuilder.toString();
                    }
                    // 国外也显示到省
                } else {
                    if (cityList.length > 2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(cityList[0]).append(cityList[1]).append(cityList[2]);
                        return stringBuilder.toString();
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(cityList[0]).append(cityList[1]);
                        return stringBuilder.toString();
                    }
                }
            }
        }
        return "未知Ip归属地";
    }
    /**
     * 获取ip属地
     * @param ip
     * @return
     * @throws Exception
     */
    public static String getCityInfo(String ip) throws Exception {
        //获得文件流时，因为读取的文件是在打好jar文件里面，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("ip2region.db");
        Resource resource = resources[0];
        InputStream is = resource.getInputStream();
        File target = new File("ip2region.db");
        FileUtils.copyInputStreamToFile(is, target);
        is.close();
        if (StringUtils.isEmpty(String.valueOf(target))) {
            log.error("Error: Invalid ip2region.db file");
            return null;
        }
        DbConfig config = new DbConfig();
        DbSearcher searcher = new DbSearcher(config, String.valueOf(target));
        //查询算法
        //B-tree, B树搜索（更快）
        int algorithm = DbSearcher.BTREE_ALGORITHM;
        try {
            //define the method
            Method method;
            method = searcher.getClass().getMethod("btreeSearch", String.class);
            DataBlock dataBlock;
            if (!Util.isIpAddress(ip)) {
                log.error("Error: Invalid ip address");
            }
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            String ipInfo = dataBlock.getRegion();
            if (!StringUtils.isEmpty(ipInfo)) {
                ipInfo = ipInfo.replace("|0", "");
                ipInfo = ipInfo.replace("0|", "");
            }
            return ipInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取真实ip地址,不返回内网地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        //目前则是网关ip
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                //只获取第一个值
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            //取不到真实ip则返回空，不能返回内网地址。
            return "";
        }
    }



    /**
     * 获取IP地址
     *
     * @return 本地IP地址
     */
    public static String getHostIp()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
        }
        return "127.0.0.1";
    }

    /**
     * 获取主机名
     *
     * @return 本地主机名
     */
    public static String getHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
        }
        return "未知";
    }

    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    public static String getMultistageReverseProxyIp(String ip)
    {
        // 多级反向代理检测
        if (ip != null && ip.indexOf(",") > 0)
        {
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips)
            {
                if (false == isUnknown(subIp))
                {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 检测给定字符串是否为未知，多用于检测HTTP请求相关
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     */
    public static boolean isUnknown(String checkString)
    {
        return StringUtils.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);
    }
}
