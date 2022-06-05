package cn.archliu.horus.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-04-27 21:57:09
 * @Description: 分页参数
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class PageEntity {

    /** 页编号 */
    private Integer pageIndex = 1;

    /** 页大小 */
    private Integer pageSize = 10;

    public Integer getPageIndex() {
        if (pageIndex == null || pageIndex <= 0) {
            return 1;
        }
        return pageIndex;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize <= 0) {
            return 10;
        }
        return pageSize;
    }

}
