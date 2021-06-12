package SpeedCodeBKD.Common.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLevel {

    /**
     * @Type: int
     * @Description: 最低访问等级，默认为 -256(所有人可访问)
     */
    int value() default -256;

    /**
     * @Type: int
     * @Description: 最高访问等级，如果为 和最低访问等级相同 将为无上限
     */
    int max() default -256;

    /**
     * @Type: boolean
     * @Description: 如果为真的话，若管理员(>155)超过最高访问等级，也不放行，假则放行
     */
    boolean ignore_admins() default false;
}
