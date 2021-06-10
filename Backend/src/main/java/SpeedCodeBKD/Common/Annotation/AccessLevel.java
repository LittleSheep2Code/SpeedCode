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
}
