package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<ReceiptResponse> getAllReceipts() {
        List<ReceiptResponse> response = new ArrayList<ReceiptResponse>();
        List<ReceiptsRecord> receiptsRecords = dsl.selectFrom(RECEIPTS).fetch();

        for(ReceiptsRecord receipt: receiptsRecords){
            response.add(new ReceiptResponse(receipt,getTagsForReceipts(receipt.getId())));
        }

        return response;
    }

    public ReceiptsRecord getReceiptForId(Integer id) {
        return dsl.selectFrom(RECEIPTS).where(RECEIPTS.ID.eq(id)).fetchOne();
    }

    public List<String> getTagsForReceipts(Integer receiptId){
        return dsl.select(TAGS.TAG).from(TAGS).where((TAGS.ID.eq(receiptId))).fetch(TAGS.TAG);
    }
}
