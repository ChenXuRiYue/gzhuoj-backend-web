package common.model.pojo;


import lombok.Data;

import java.util.List;

@Data
public  class ProblemPrint {
    Object problemDO;
    Object problemDescrDO;
    List<Object> testExampleDO;
}