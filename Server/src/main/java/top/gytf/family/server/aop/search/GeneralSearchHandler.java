package top.gytf.family.server.aop.search;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.gytf.family.server.search.GeneralSearchEntity;
import top.gytf.family.server.utils.SearchUtil;
import top.gytf.family.server.utils.reflect.MethodInfo;
import top.gytf.family.server.utils.reflect.ReflectUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 统一查询处理器<br>
 * CreateDate:  2021/12/11 12:11 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
@Aspect
@Order(500)
public class GeneralSearchHandler {
    private final static String TAG = GeneralSearchHandler.class.getName();

    private final ApplicationContext applicationContext;
    private final Map<Class<? extends BaseMapper>, BaseMapper> MAPPER_CACHE = new ConcurrentHashMap<>();
    private final Map<Class, GeneralSearch> GENERAL_SEARCH_CACHE = new ConcurrentHashMap<>();

    public GeneralSearchHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 所有标注了统一查询注解的api
     */
    @Pointcut("@annotation(top.gytf.family.server.aop.search.GeneralSearch)")
    public void allGeneralSearchApi() {}

    /**
     * 所有在controllers包下返回值为IPage的API
     */
    @Pointcut("execution(com.baomidou.mybatisplus.core.metadata.IPage top.gytf.family.server.controllers..*(..))")
    public void allPageReturnTypeApi() {}

    /**
     * 所有在controllers包下返回值为对象数组的API
     */
    @Pointcut("execution(Object[] top.gytf.family.server.controllers..*(..))")
    public void allArrayReturnTypeApi() {}

    /**
     * 分页统一查询处理
     * @param proceedingJoinPoint 接入点
     * @return 分页内容
     * @throws Throwable 处理错误
     */
    @Around("allGeneralSearchApi() && allPageReturnTypeApi()")
    public IPage generalSearchPageApi(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取注解
        GeneralSearch search = getAnnotation(proceedingJoinPoint);

        // 取出统一查询实体
        GeneralSearchEntity entity = parseRequest();

        // 解析请求
        IPage result = getMapper(search.mapper()).selectPage(
                SearchUtil.generatePage(entity.getPages()),
                SearchUtil.parse(search.entityClass(), entity)
        );

        // 调用请求完后的处理逻辑
        proceedingJoinPoint.proceed();
        return result;
    }

    /**
     * 统一查询处理
     * @param proceedingJoinPoint 接入点
     * @return 分页内容
     * @throws Throwable 处理错误
     */
    @Around("allGeneralSearchApi() && allArrayReturnTypeApi()")
    public Object[] generalSearchArrayApi(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取注解
        GeneralSearch search = getAnnotation(proceedingJoinPoint);

        // 取出统一查询实体
        GeneralSearchEntity entity = parseRequest();

        // 解析请求
        Object[] result = getMapper(search.mapper()).selectList(
                SearchUtil.parse(search.entityClass(), entity)
        ).toArray();

        // 调用请求完后的处理逻辑
        proceedingJoinPoint.proceed();
        return result;
    }


    /**
     * 从请求中解析统一查询实体
     * @return 统一查询实体
     */
    private GeneralSearchEntity parseRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null : "请求属性为空";
        HttpServletRequest request = attributes.getRequest();
        GeneralSearchEntity entity = new GeneralSearchEntity();

        entity.setRequests(request.getParameter("requests"));
        entity.setConditions(request.getParameter("conditions"));
        entity.setSorts(request.getParameter("sorts"));
        entity.setPages(request.getParameter("pages"));

        return entity;
    }

    /**
     * 获取Mapper（缓存）
     * @param mapperClass Mapper类型
     * @return Mapper
     */
    private synchronized BaseMapper getMapper(Class<? extends BaseMapper> mapperClass) {
        BaseMapper mapper = MAPPER_CACHE.get(mapperClass);

        if (mapper == null) {
            mapper = applicationContext.getBean(mapperClass);
            MAPPER_CACHE.put(mapperClass, mapper);
        }

        return mapper;
    }

    /**
     * 获取统一查询的注解（缓存）
     * @param proceedingJoinPoint 切入点
     * @return 统一查询注解
     */
    private synchronized GeneralSearch getAnnotation(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Class clazz = signature.getDeclaringType();
        GeneralSearch generalSearch = GENERAL_SEARCH_CACHE.get(clazz);

        if (generalSearch == null) {
            try {
                generalSearch = ReflectUtil.getAnnotation(GeneralSearch.class, new MethodInfo(signature));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("无法获取注解。");
            }
            if (generalSearch != null) {
                GENERAL_SEARCH_CACHE.put(clazz, generalSearch);
            }
        }

        return generalSearch;
    }
}
