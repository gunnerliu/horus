package cn.archliu.horus.common.util;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Author: Arch
 * @Date: 2022-05-07 08:52:29
 * @Description: sql 语法检查工具
 */
@SuppressWarnings({ "squid:S5843" })
public class SqlCheckUtils {

    /**
     * SQL语法检查正则：符合两个关键字（有先后顺序）才算匹配
     */
    private static final Pattern SQL_SYNTAX_PATTERN = Pattern.compile(
            "(insert|delete|update|select|create|drop|truncate|grant|alter|deny|revoke|call|execute|exec|declare|show|rename|set)"
                    + ".+(into|from|set|where|table|database|view|index|on|cursor|procedure|trigger|for|password|union|and|or)",
            Pattern.CASE_INSENSITIVE);

    /**
     * 查询 SQL语法检查正则,只允许查询语句
     */
    private static final Pattern SQL_QUERY_PATTERN = Pattern.compile("(select)" + ".+(from|where|on|union|and|or)",
            Pattern.CASE_INSENSITIVE);
    /**
     * 使用'、;或注释截断SQL检查正则
     */
    private static final Pattern SQL_COMMENT_PATTERN = Pattern.compile("'.*(or|union|--|#|/*|;)",
            Pattern.CASE_INSENSITIVE);

    /**
     * 检查参数是否存在 SQL 注入
     *
     * @param value 检查参数
     * @return true 非法 false 合法
     */
    public static boolean check(String value) {
        Objects.requireNonNull(value);
        // 处理是否包含SQL注释字符 || 检查是否包含SQL注入敏感字符
        return SQL_COMMENT_PATTERN.matcher(value).find() || SQL_SYNTAX_PATTERN.matcher(value).find();
    }

    /**
     * 检查参数是否存在 SQL 注入，sql是否是 查询语句
     *
     * @param value 检查参数
     * @return true 非法 false 合法
     */
    public static boolean queryCheck(String value) {
        Objects.requireNonNull(value);
        // 处理是否包含SQL注释字符 || 检查是否包含SQL注入敏感字符
        return SQL_COMMENT_PATTERN.matcher(value).find() || SQL_QUERY_PATTERN.matcher(value).find();
    }

    private SqlCheckUtils() {
    }

}
