package com.epam.test_generator.dao.impl;

import static java.util.stream.Collectors.groupingBy;

import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.PropertyDifference;
import com.epam.test_generator.pojo.SuitVersion;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.javers.core.Javers;
import org.javers.core.commit.CommitId;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.CollectionChange;
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ElementValueChange;
import org.javers.core.diff.changetype.container.ValueAdded;
import org.javers.core.diff.changetype.container.ValueRemoved;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SuitVersionDAOImpl implements SuitVersionDAO {

    @Autowired
    private Javers javers;

    @Autowired
    private AuthorProvider authorProvider;

    @Override
    public List<SuitVersion> findAll(Long suitId) {
        List<Change> changes = javers.findChanges(
            QueryBuilder.byInstanceId(suitId, Suit.class).withNewObjectChanges().build());

        return getSuitVersions(changes);
    }

    @Override
    public Suit findByCommitId(Long suitId, String commitId) {
        Optional<Shadow<Object>> suitShadows = javers
            .findShadows(QueryBuilder.byInstanceId(suitId, Suit.class)
                .withCommitId(CommitId.valueOf(commitId)).build()).stream().findAny();

        return suitShadows.map(objectShadow -> (Suit) objectShadow.get()).orElse(null);
    }

    @Override
    public void save(Suit suit) {
        javers.commit(authorProvider.provide(), suit);
    }

    @Override
    public void save(Iterable<Suit> suits) {
        if (suits == null) {
            return;
        }

        for (Suit suit : suits) {
            save(suit);
        }
    }

    @Override
    public void delete(Suit suit) {
        javers.commitShallowDelete(authorProvider.provide(), suit);
    }

    @Override
    public void delete(Iterable<Suit> suits) {
        if (suits == null) {
            return;
        }

        for (Suit suit : suits) {
            delete(suit);
        }
    }

    /**
     * Group list of {@link Change} by commitIds and map each group to {@link SuitVersion}
     *
     * @param changes list of {@link Change} objects that represents property changes.
     * @return list {@link SuitVersion} that represent changes made by each commit
     */
    private List<SuitVersion> getSuitVersions(List<Change> changes) {
        TreeMap<CommitMetadata, List<Change>> changesWithSameCommitId =
            groupByCommitId(changes);

        List<SuitVersion> suitVersions = new ArrayList<>(changesWithSameCommitId.size());
        changesWithSameCommitId.forEach(
            (commitMetadata, commitChanges) -> {

                String commitId = commitMetadata.getId().toString();
                Date updatedDate = Date.from(commitMetadata.getCommitDate().atZone(
                    ZoneId.systemDefault()).toInstant());
                String author = commitMetadata.getAuthor();
                List<PropertyDifference> propertyDifferences = getPropertyDifferences(
                    commitChanges);

                suitVersions.add(new SuitVersion(
                    commitId,
                    updatedDate,
                    author,
                    propertyDifferences
                ));
            }
        );
        return suitVersions;
    }

    /**
     * Maps list of {@link Change} to {@link PropertyDifference} list. Basically, there can be two
     * scenarios: element of changeList is a simple property value or collection. <br> For example:
     * {@link Suit} object has simple fields "description" and "name" and collection-field "cases"
     * and "tags".
     *
     * @param changeList list of {@link Change} objects that represents property changes.
     * @return list of {@link PropertyDifference} created from {@link Change} list.
     */
    private List<PropertyDifference> getPropertyDifferences(List<Change> changeList) {
        List<PropertyDifference> propertyDifferences = new ArrayList<>();
        for (Change change : changeList) {
            if (change instanceof ValueChange) {
                propertyDifferences.add(
                    getValuePropertyDifference((ValueChange) change));
            } else if (change instanceof CollectionChange) {
                propertyDifferences.addAll(
                    getCollectionPropertiesDifferences((CollectionChange) change));
            }
        }
        return propertyDifferences;
    }

    /**
     * Group and sort changes by commitId.
     *
     * @param changes list of {@link Change} objects that represents property changes.
     * @return grouped and sorted {@link TreeMap}
     */
    private TreeMap<CommitMetadata, List<Change>> groupByCommitId(List<Change> changes) {

        Map<CommitMetadata, List<Change>> changesByCommitId = changes.stream()
            .filter(change -> change.getCommitMetadata().isPresent())
            .collect(groupingBy(change -> change.getCommitMetadata().get()));

        TreeMap<CommitMetadata, List<Change>> sortedChangesByCommitId =
            new TreeMap<>(Comparator.comparing(CommitMetadata::getId));
        sortedChangesByCommitId.putAll(changesByCommitId);

        return sortedChangesByCommitId;
    }

    /**
     * Returns differences for collection-field. Basically, collection-field for {@link Suit}
     * objects is list of {@link Case} objects and set of {@link Tag}. There can be three states of
     * collection-field element: "element changed", "element added" and "element removed".
     *
     * @param change - {@link CollectionChange} for container-field.
     * @return list of {@link PropertyDifference} objects for every element of container-property
     */
    private List<PropertyDifference> getCollectionPropertiesDifferences(CollectionChange change) {

        List<PropertyDifference> propertyDifferences = new LinkedList<>();

        for (ContainerElementChange containerElementChange : change.getChanges()) {
            if (containerElementChange instanceof ElementValueChange) {
                propertyDifferences.add(new PropertyDifference(
                    change.getPropertyName(),
                    ((ElementValueChange) containerElementChange).getLeftValue(),
                    ((ElementValueChange) containerElementChange).getRightValue()
                ));
            } else if (containerElementChange instanceof ValueAdded) {
                propertyDifferences.add(new PropertyDifference(
                    change.getPropertyName(),
                    null,
                    ((ValueAdded) containerElementChange).getAddedValue()
                ));
            } else if (containerElementChange instanceof ValueRemoved) {
                propertyDifferences.add(new PropertyDifference(
                    change.getPropertyName(),
                    ((ValueRemoved) containerElementChange).getRemovedValue(),
                    null
                ));
            }
        }

        return propertyDifferences;
    }

    /**
     * Creates {@link PropertyDifference} object from specified {@link ValueChange} object.
     *
     * @param change - {@link ValueChange} object .
     * @return {@link PropertyDifference} for specified {@link ValueChange} object.
     */
    private PropertyDifference getValuePropertyDifference(ValueChange change) {
        PropertyDifference propertyDifference;
        propertyDifference = new PropertyDifference(
            change.getPropertyName(),
            change.getLeft(),
            change.getRight()
        );
        return propertyDifference;
    }
}
