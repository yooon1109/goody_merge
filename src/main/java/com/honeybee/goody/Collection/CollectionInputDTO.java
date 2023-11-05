package com.honeybee.goody.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class CollectionInputDTO {
    private String title;
    private String explain;
    private List<MultipartFile> filePath;
    private List<String> hashTags;

}
