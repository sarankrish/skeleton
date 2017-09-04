package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    ReceiptDao receiptDao;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public void insert(String tag, Integer receiptId) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.ID, TAGS.TAG)
                .values(receiptId, tag)
                .returning(TAGS.ID,TAGS.TAG)
                .fetchOne();

        //checkState(tagsRecord != null && tagsRecord.getId() != null && tagsRecord.getTag() != null, "Insert failed");

    }

    public void toggleTag(String tag, Integer receiptId){
        List<TagsRecord> tagsRecords = dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tag).and(TAGS.ID.eq(receiptId))).fetch();
        if(tagsRecords.size() == 0)
            insert(tag,receiptId);
        else
            delete(tagsRecords);
    }

    public void delete(List<TagsRecord> tagsRecords){
        for(TagsRecord record : tagsRecords)
            dsl.executeDelete(record);
    }


    public List<TagsRecord> getAllTagsRecords(String tagName) {
        return dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tagName)).fetch();
    }
}
