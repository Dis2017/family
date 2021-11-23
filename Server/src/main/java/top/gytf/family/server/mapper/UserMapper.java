package top.gytf.family.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.gytf.family.server.dao.UserDao;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   UserMapper
 * Description: 用户Mapper
 * CreateDate:  2021/11/22 23:29
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDao> {
}
