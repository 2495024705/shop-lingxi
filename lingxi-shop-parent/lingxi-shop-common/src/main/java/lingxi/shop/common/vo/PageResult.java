package lingxi.shop.common.vo;

import lombok.Data;

import java.util.List;
@Data
public class PageResult<T> {
        public Long total;// 总条数
        public Long totalPage;// 总页数
        public List<T> items;// 当前页数据

        public PageResult(Long total, List<T> items) {
                this.total = total;

                this.items = items;
        }

        public PageResult(Long total, Long totalPage, List<T> items) {
                this.total = total;
                this.totalPage = totalPage;
                this.items = items;
        }

        public PageResult() {
        }
}