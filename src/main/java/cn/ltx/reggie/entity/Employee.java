package cn.ltx.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Data
@SuppressWarnings("ALL")
public class Employee /*extends Model<Employee> */implements Serializable {


    private static final long serialVersionUID = -3654892125091784531L;
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String name;

    //@TableField(select = false)
    private String password;

    private String phone;

    private String sex;

    private String idNumber;//身份证号码

    private Integer status;

    //fill: 填充
    @TableField(fill = FieldFill.INSERT) //插入时填充字段
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT) //插入时填充字段
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private Long updateUser;

}
