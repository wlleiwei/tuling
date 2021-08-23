package config;

import lombok.*;

/**
 * @ClassName MyConfig.java
 * @Author weilei
 * @Description 配置类
 * @CreateTime 2021年08月23日 17:27
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyConfig {
    private String key;
    private String name;
}
