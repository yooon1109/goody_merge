package com.honeybee.goody.Test;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class Test {
    private String id;
    private String pw;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birth;


}
