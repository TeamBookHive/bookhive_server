package bookhive.bookhiveserver.testUtil;

import bookhive.bookhiveserver.domain.tag.dto.request.TagRequest;

public class TagDtoMother {

    public static TagRequest createTagRequest(Long id, String value) {
        TagRequest dto = new TagRequest();
        dto.setId(id);
        dto.setValue(value);

        return dto;
    }
}
