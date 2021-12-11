package top.gytf.family.server.events;

import org.springframework.context.ApplicationEvent;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 刷新所有用户的权限<br>
 * CreateDate:  2021/12/12 1:37 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class RefreshAllUserAuthorityEvent extends ApplicationEvent {
    private final static String TAG = RefreshAllUserAuthorityEvent.class.getName();

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public RefreshAllUserAuthorityEvent(Object source) {
        super(source);
    }
}
