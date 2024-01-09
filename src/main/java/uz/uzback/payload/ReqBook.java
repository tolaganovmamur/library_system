package uz.uzback.payload;

import lombok.Data;

@Data
public class ReqBook {
    private Long id;
    private String name;
    private String author;
    private Integer shelfNumber;
    private Integer shelfSpaceNumber;
    private Long bookcaseId;
}
