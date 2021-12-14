package top.gytf.family.server.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 对<br>
 * CreateDate:  2021/12/14 23:27 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Pair<T, U> {
    private T first;
    private U second;
}
