package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepType;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.StepTransformer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StepServiceTest {

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_STEP_ID = 3L;

    private Suit suit;

    private Case caze;

    private Step step;
    private StepDTO expectedStep;

    private List<StepDTO> expectedListSteps = new ArrayList<>();
    private Set<Tag> setTags = new HashSet<>();

    @Mock
    private StepTransformer stepTransformer;

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private StepDAO stepDAO;

    @InjectMocks
    private StepService stepService;

    @Before
    public void setUp() {
        step = new Step(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN);
        expectedStep = new StepDTO(SIMPLE_STEP_ID, 1, "Step desc", StepType.GIVEN.ordinal());

        List<Step> listSteps = new ArrayList<>();

        listSteps.add(new Step(1L, 1, "Step 1", StepType.GIVEN));
        listSteps.add(new Step(2L, 2, "Step 2", StepType.WHEN));
        listSteps.add(step);

        expectedListSteps.add(new StepDTO(1L, 1, "Step 1", StepType.GIVEN.ordinal()));
        expectedListSteps.add(new StepDTO(2L, 2, "Step 2", StepType.WHEN.ordinal()));
        expectedListSteps.add(expectedStep);

        caze = new Case(SIMPLE_CASE_ID, "Case desc", listSteps, 1, setTags);

        List<Case> listCases = new ArrayList<>();

        listCases.add(new Case(1L, "Case 1", listSteps, 1, setTags));
        listCases.add(new Case(2L, "Case 2", listSteps, 2, setTags));
        listCases.add(caze);

        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, "tag1");
    }

    @Test
    public void getStepsByCaseIdTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepTransformer.toDtoList(anyList())).thenReturn(expectedListSteps);

        List<StepDTO> actualStepList = stepService.getStepsByCaseId(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
        assertEquals(expectedListSteps, actualStepList);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepTransformer).toDtoList(anyList());
    }

	@Test(expected = NotFoundException.class)
	public void getStepsByCaseIdTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStepsByCaseId(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

	@Test(expected = NotFoundException.class)
	public void getStepsByCaseIdTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStepsByCaseId(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

    @Test
    public void getStepTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);
        when(stepTransformer.toDto(any(Step.class))).thenReturn(expectedStep);

        StepDTO actualStep = stepService.getStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
        assertEquals(expectedStep, actualStep);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).toDto(any(Step.class));
    }

	@Test(expected = NotFoundException.class)
	public void getStepTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void getStepTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void getStepTest_expectNotFoundExceptionFromStep() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(caze);
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.getStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

    @Test
    public void addStepToCaseTest() throws Exception {
        Step newStep = new Step(3L, 3, "Step 3", StepType.AND);
        StepDTO newStepDTO = new StepDTO(null, 3, "Step 3", StepType.AND.ordinal());

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.save(any(Step.class))).thenReturn(newStep);
        when(stepTransformer.fromDto(any(StepDTO.class))).thenReturn(newStep);

        Long actualId = stepService.addStepToCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, newStepDTO);
        assertEquals(newStep.getId(), actualId);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).save(any(Step.class));
        verify(stepTransformer).fromDto(any(StepDTO.class));
    }

	@Test(expected = NotFoundException.class)
	public void addStepTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.addStepToCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void addStepTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.addStepToCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new StepDTO());
	}

    @Test
    public void updateStepTest() throws Exception {
        StepDTO updateStepDTO = new StepDTO(null, 3, "New Step desc", null);

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);

        stepService.updateStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, updateStepDTO);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepTransformer).mapDTOToEntity(any(StepDTO.class), eq(step));
        verify(stepDAO).save(eq(step));
    }

	@Test(expected = NotFoundException.class)
	public void updateStepTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.updateStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void updateStepTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.updateStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

	@Test(expected = NotFoundException.class)
	public void updateStepTest_expectNotFoundExceptionFromStep() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(caze);
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.updateStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID, new StepDTO());
	}

    @Test
    public void removeStepTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(stepDAO.findOne(anyLong())).thenReturn(step);

        stepService.removeStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(stepDAO).findOne(eq(SIMPLE_STEP_ID));
        verify(stepDAO).delete(eq(SIMPLE_STEP_ID));
    }

	@Test(expected = NotFoundException.class)
	public void removeStepTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		stepService.removeStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void removeStepTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		stepService.removeStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

	@Test(expected = NotFoundException.class)
	public void removeStepTest_expectNotFoundExceptionFromStep() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(caze);
		when(stepDAO.findOne(anyLong())).thenReturn(null);

		stepService.removeStep(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, SIMPLE_STEP_ID);
	}

}