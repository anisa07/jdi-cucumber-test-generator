package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.file_generator.FileGenerator;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.transformers.SuitTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IOService {

    @Autowired
    private FileGenerator fileGenerator;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private CaseTransformer caseTransformer;

    @Autowired
    private SuitTransformer suitTransformer;

    /**
     * Generates file which contains suit and it's cases.
     * @param suitId id of suit to generate
     * @param caseIds list of ids of suit's cases
     * @return string generated by fileGenerator
     * @throws IOException
     */
    public String generateFile(Long suitId, List<Long> caseIds) throws IOException {
        Suit suit = suitDAO.findOne(suitId);
        List<Case> cases = caseIds.stream()
                .map(caseDAO::findOne)
                .collect(Collectors.toList());

        return fileGenerator
                .generate(suitTransformer.toDto(suit), caseTransformer.toDtoList(cases));
    }
}
