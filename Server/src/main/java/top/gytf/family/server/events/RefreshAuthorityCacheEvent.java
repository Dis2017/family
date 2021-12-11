package top.gytf.family.server.events;

import org.springframework.context.ApplicationEvent;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 刷新权限缓存事件<br>
 * CreateDate:  2021/12/12 1:29 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class RefreshAuthorityCacheEvent extends ApplicationEvent {
    private final static String TAG = RefreshAuthorityCacheEvent.class.getName();

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public RefreshAuthorityCacheEvent(Object source) {
        super(source);
    }
}
