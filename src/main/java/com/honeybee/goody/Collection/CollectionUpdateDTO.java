package com.honeybee.goody.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class CollectionUpdateDTO {

    private Optional<String> title;
    private Optional<String> explain;
    private Optional<List<String>> hashTags;

}
