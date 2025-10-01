package org.example.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatusEnum {
    PENDING("A","准备中"),
    PROCESSING("B","进行中"),
    COMMITED("C","已提交");
    @EnumValue   // 👉 MyBatis-Plus 存储时用这个值
    @JsonValue   // 👉 序列化成 JSON 时用这个值（返回给前端就是 A/B/C）
    private String code;
    private String desc;
    private TaskStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
    public static TaskStatusEnum formCode(String code) {
        for (TaskStatusEnum taskStatusEnum : TaskStatusEnum.values()) {
            if (taskStatusEnum.getCode().equals(code)) {
                return taskStatusEnum;
            }
        }
        throw new RuntimeException("unknown code " + code);
    }

}
