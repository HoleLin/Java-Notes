package com.holelin.mysql;

import com.holelin.mysql.dao.AnalysisDataFromOtherDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MysqlApplicationTest {

    @Autowired
    private AnalysisDataFromOtherDAO analysisDataFromOtherDAO;
    public static final Integer DELETE_NUM = 10000;
    public static final Long TASK_ID = 6L;

    @Test
    void deleteBigData() {
        long maxId = analysisDataFromOtherDAO.queryMaxID(TASK_ID);
        int allCount = analysisDataFromOtherDAO.countByTaskId(TASK_ID);
        int loopTimes = allCount / DELETE_NUM;
        log.info("maxId:{}", maxId);
        log.info("allCount:{}", allCount);
        for (int i = 0; i < loopTimes + 1; i++) {
            int effectRows = analysisDataFromOtherDAO.deleteByTaskId(TASK_ID, maxId, DELETE_NUM);
            log.info("effectRows:{}", effectRows);
        }
    }


}
