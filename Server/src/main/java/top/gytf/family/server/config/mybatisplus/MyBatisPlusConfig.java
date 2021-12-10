package top.gytf.family.server.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   MyBatisPlusConfig
 * Description: MyBatisPlus配置
 * CreateDate:  2021/11/23 15:45
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Configuration
@MapperScan("top.gytf.family.server.mapper")
public class MyBatisPlusConfig {
    private final static String TAG = MyBatisPlusConfig.class.getName();

    @Bean
    public FillBaseFieldHandler fillBaseFieldHandler() {
        return new FillBaseFieldHandler();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
}
