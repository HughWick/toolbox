package com.github.hugh.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 统一返回信息实体类
 *
 * @author hugh
 * @since 1.5.0
 */
@Data
@Builder
@Schema(description = "接口统一返回信息对象")
public class ResultDTO<T> implements java.io.Externalizable {
    // 避免版本不一致问题
    private static final long serialVersionUID = 1L;
    @Schema(required = true, description = "接口状态码", example = "0")
    private String code; // code
    @Schema(required = true, description = "提示信息", example = "success")
    private String message; // 提示信息
    @Schema(required = true, description = "数据")
    private T data; // 数据
    @Schema(required = true, description = "时间戳")
    private long timestamp;

    public ResultDTO() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * code、提示信息
     *
     * @param code    code
     * @param message 提示信息
     */
    public ResultDTO(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * **新增的四参数构造器**
     *
     * @param code      code
     * @param message   提示信息
     * @param data      数据
     * @param timestamp 时间戳
     */
    public ResultDTO(String code, String message, T data, long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    /**
     * 判断code码是否一样
     * <p>当前实体类中的对应与传入的code一致</p>
     *
     * @param code code
     * @return boolean
     * @since 1.7.0
     */
    public boolean equalCode(String code) {
        if (this.code == null) {
            return false;
        }
        return this.code.equals(code);
    }

    /**
     * 判断code码不一样
     *
     * @param code code
     * @return boolean
     * @since 1.7.0
     */
    public boolean notEqualCode(String code) {
        return !equalCode(code);
    }

    @Override
    public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
        // 写入 Code (处理 null 情况，虽然 schema 说 required=true，但在代码层面防空更安全)
        out.writeUTF(code == null ? "" : code);
        // 写入 Message
        out.writeUTF(message == null ? "" : message);
        // 写入 Timestamp
        out.writeLong(timestamp);
        // 写入泛型 Data
        // 注意：T 必须也是可序列化的，或者是基础类型。
        // 由于 T 类型未知，这里必须使用 writeObject，这是唯一的性能瓶颈点，但无法避免。
        out.writeObject(data);
    }

    @Override
    public void readExternal(java.io.ObjectInput in) throws java.io.IOException, ClassNotFoundException {
        this.code = in.readUTF();
        this.message = in.readUTF();
        this.timestamp = in.readLong();
        this.data = (T) in.readObject();
    }

    /**
     * 将对象转换为字节数组
     *
     * @since 3.0.21
     */
    public byte[] toByteArray() {
        // 预估大小：根据字段情况设置初始容量，避免频繁扩容 (例如 512 字节)
        try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream(512);
             java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos)) {
            oos.writeObject(this); // 这里会自动调用 writeExternal
            oos.flush();
            return baos.toByteArray();
        } catch (java.io.IOException e) {
            throw new RuntimeException("ResultDTO serialization failed", e);
        }
    }

    /**
     * 反序列化
     *
     * @since 3.0.21
     */
    public static <T> ResultDTO<T> fromByteArray(byte[] bytes) {
        try (java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(bytes);
             java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais)) {
            @SuppressWarnings("unchecked")
            ResultDTO<T> dto = (ResultDTO<T>) ois.readObject();
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("ResultDTO deserialization failed", e);
        }
    }
}
