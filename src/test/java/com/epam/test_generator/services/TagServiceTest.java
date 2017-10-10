package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.transformers.TagTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {

    @Mock
    private TagDAO tagDAO;

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private TagTransformer tagTransformer;

    @InjectMocks
    private TagService tagService;

    private Set<Tag> expectedTagsList;
    private Set<TagDTO> expectedTagsDTOList;

    private  Tag expectedTag;
    private TagDTO expectedTagDTO;
    private Suit suit;
    private Case caze;

    private static final long SIMPLE_TAG_ID = 3L;
    private static final long SIMPLE_CASE_ID = 2L;

    @Before
    public void setUp() {
        expectedTagsList = new HashSet<>();
        expectedTagsDTOList = new HashSet<>();

        expectedTag = new Tag("tag1");
        expectedTagsList.add(expectedTag);

        expectedTagDTO = new TagDTO("tag1");
        expectedTagsDTOList.add(expectedTagDTO);

        suit = new Suit(1L, "suit1", "desc1", new ArrayList<>(), 1, "tag1");
        caze = new Case(2L, "desc2", new ArrayList<>(), 1, expectedTagsList);
        suit.getCases().add(caze);
    }

    @Test
    public void getAllTagsFromAllCasesInSuitTest() {



        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(tagTransformer.toDto(any(Tag.class))).thenReturn(expectedTagDTO);

        Set<TagDTO> actual = tagService.getAllTagsFromAllCasesInSuit(1L);

        assertEquals(expectedTagsDTOList, actual);
    }

    @Test
    public void getTagTest() {
        when(tagDAO.findOne(anyLong())).thenReturn(expectedTag);
        when(tagTransformer.toDto(any(Tag.class))).thenReturn(expectedTagDTO);

        TagDTO actualTag = tagService.getTag(SIMPLE_TAG_ID);
        assertEquals(expectedTagDTO, actualTag);

        verify(tagDAO).findOne(eq(SIMPLE_TAG_ID));
        verify(tagTransformer).toDto(any(Tag.class));


    }

    @Test
    public void addTagToCaseTest() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("name");

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(null);
        tagDTO.setName("name");

        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(tagTransformer.fromDto(any(TagDTO.class))).thenReturn(tag);
        when(tagDAO.save(any(Tag.class))).thenReturn(tag);

        Long actualLong = tagService.addTagToCase(SIMPLE_CASE_ID, tagDTO);
        assertEquals(tag.getId(),actualLong);

        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(tagTransformer).fromDto(any(TagDTO.class));
        verify(tagDAO).save(any(Tag.class));


    }

    @Test
    public void updateTagTest() {
        when(tagDAO.findOne(anyLong())).thenReturn(expectedTag);
        when(tagDAO.save(any(Tag.class))).thenReturn(expectedTag);

        tagService.updateTag(SIMPLE_TAG_ID,expectedTagDTO);

        verify(tagDAO).findOne(eq(SIMPLE_TAG_ID));
        verify(tagDAO).save(any(Tag.class));





    }

    @Test
    public void removeTagTest() {
        Case caze = new Case();
        caze.setTags(expectedTagsList);

        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(tagDAO.findOne(anyLong())).thenReturn(expectedTag);

        tagService.removeTag(1L, 2L);
        verify(tagDAO).delete(2L);
    }
}
