package com.honeybee.goody.Collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class CollectionInputDTO {
    private String userId;
    private String title;
    private String content;
    private List<MultipartFile> images;
}
