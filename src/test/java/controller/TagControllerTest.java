package controller;

import controllers.TagController;
import dao.ReceiptDao;
import dao.TagDao;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class TagControllerTest {

    @Mock
    private TagDao tagDao;

    @Mock
    private ReceiptDao receiptsDao;

    @Test
    public void testToggleTag() {
        MockitoAnnotations.initMocks(this);
        TagController tags = new TagController(tagDao,receiptsDao);
        tags.toggleTag("tag1",1);
        Mockito.verify(tagDao).toggleTag("tag1", 1);
    }

    @Test
    public void testGetReceiptsForTag() {
        MockitoAnnotations.initMocks(this);
        TagController tagController = new TagController(tagDao,receiptsDao);
        tagController.getReceiptsForTag("tag1");
        Mockito.verify(tagDao).getAllTagsRecords("tag1");
    }

}
