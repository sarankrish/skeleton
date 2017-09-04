package controllers;

import api.ReceiptResponse;
import dao.ReceiptDao;
import dao.TagDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;
    final ReceiptDao receipts;

    public TagController(TagDao tags,ReceiptDao receipts) {
        this.tags = tags;
        this.receipts = receipts;
    }

    @PUT
    @Path("/{tag}")
    public void toggleTag(@PathParam("tag") String tagName, Integer id) {
        tags.toggleTag(tagName, id);
    }

    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> getReceiptsForTag(@PathParam("tag") String tagName) {
        List<TagsRecord> tagsRecords = tags.getAllTagsRecords(tagName);
        List<ReceiptsRecord> receiptRecords = new ArrayList<ReceiptsRecord>();
        if(null != tagsRecords && !tagsRecords.isEmpty())
            for(TagsRecord tagRecord : tagsRecords)
                receiptRecords.add(receipts.getReceiptForId(tagRecord.getId()));
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
}
