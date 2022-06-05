package cn.archliu.horus.server.domain.metrics.enums;

import cn.archliu.horus.common.exception.sub.ParamErrorException;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * @Author: Arch
 * @Date: 2022-04-27 11:57:49
 * @Description: TDEngine 数据类型
 */
@Getter
public enum TDDataType {

    /** 时间戳。缺省精度毫秒，可支持微秒和纳秒。从格林威治时间 1970-01-01 00:00:00.000 (UTC/GMT)开始，计时不能早于该时间 */
    TIMESTAMP("Timestamp", "java.sql.Timestamp"),
    INT("Integer", "java.lang.Integer"), // 整型，范围 [-2^31+1, 2^31-1], -2^31 用作 NULL
    BIGINT("Long", "java.lang.Long"), // 长整型，范围 [-2^63+1, 2^63-1], -2^63 用于 NULL
    FLOAT("Float", "java.lang.Float"), // 浮点型，有效位数 6-7，范围 [-3.4E38, 3.4E38]
    DOUBLE("Double", "java.lang.Double"), // 双精度浮点型，有效位数 15-16，范围 [-1.7E308, 1.7E308]
    SMALLINT("Short", "java.lang.Short"), // 短整型， 范围 [-32767, 32767], -32768 用于 NULL
    TINYINT("Byte", "java.lang.Byte"), // 单字节整型，范围 [-127, 127], -128 用于 NULL
    BOOL("Boolean", "java.lang.Boolean"), // 布尔型 true, false
    /**
     * 记录包含多字节字符在内的字符串，如中文字符。每个 nchar 字符占用 4 bytes 的存储空间。字符串两端使用单引号引用，字符串内的单引号需用转义字符
     * \’。nchar 使用时须指定字符串大小，类型为 nchar(10) 的列表示此列的字符串最多存储 10 个 nchar 字符，会固定占用 40
     * bytes 的空间。如果用户字符串长度超出声明长度，将会报错。
     */
    NCHAR("String", "java.lang.String"),
    JSON("String", "java.lang.String"); // json数据类型， 只有tag类型可以是json格式

    /** 对应 java 的数据结构 */
    private String javaType;

    /** 对应 java 数据结构的路径 */
    private String classPath;

    private TDDataType(String javaType, String classPath) {
        this.javaType = javaType;
        this.classPath = classPath;
    }

    public static TDDataType convert(String dataType) {
        for (TDDataType item : values()) {
            if (item.name().equals(dataType)) {
                return item;
            }
        }
        return null;
    }

    public static TDDataType convertThrowE(String dataType) {
        if (StrUtil.isBlank(dataType)) {
            throw ParamErrorException.throwE("TD 数据类型错误！");
        }
        for (TDDataType item : values()) {
            if (dataType.startsWith(NCHAR.name())) {
                return NCHAR;
            }
            if (item.name().equals(dataType)) {
                return item;
            }
        }
        throw ParamErrorException.throwE("TD 数据类型错误！");
    }

}
