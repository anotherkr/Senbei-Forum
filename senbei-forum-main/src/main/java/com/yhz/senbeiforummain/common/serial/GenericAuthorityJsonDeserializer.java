package com.yhz.senbeiforummain.common.serial;


import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;

/**
 * 定义一个自定义的反序列化器类,解决springsecurity中的SimpleGranted(没有无参构造)无法反序列化
 * @author yanhuanzhan
 * @date 2023/2/25 - 19:47
 */
public class GenericAuthorityJsonDeserializer extends JsonDeserializer<SimpleGrantedAuthority> {

    @Override
    public SimpleGrantedAuthority deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, org.codehaus.jackson.JsonProcessingException {
        String authority = jsonParser.readValueAs(String.class);
        return new SimpleGrantedAuthority(authority);
    }
}
