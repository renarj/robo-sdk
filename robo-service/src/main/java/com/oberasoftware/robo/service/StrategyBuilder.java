/**
 * Copyright (c) 2015 SDL Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oberasoftware.robo.service;

import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.ODataSystemException;
import com.sdl.odata.api.processor.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 */
public class StrategyBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyBuilder.class);

    private List<Predicate<QueryableEntity>> predicates = new ArrayList<>();
    private int limit = Integer.MAX_VALUE;

    public List<Predicate<QueryableEntity>> buildCriteria(QueryOperation queryOperation) throws ODataException {
        buildFromOperation(queryOperation);

        return predicates;
    }

    public int getLimit() {
        return limit;
    }

    private void buildFromOperation(QueryOperation operation) throws ODataException {
        if (operation instanceof SelectOperation) {
            buildFromSelect((SelectOperation) operation);
        } else if (operation instanceof SelectByKeyOperation) {
            buildFromSelectByKey((SelectByKeyOperation) operation);
        } else if (operation instanceof CriteriaFilterOperation) {
            buildFromFilter((CriteriaFilterOperation)operation);
        } else if (operation instanceof LimitOperation) {
            buildFromLimit((LimitOperation) operation);
        } else if (operation instanceof SkipOperation) {
            //not supported for now
        } else if (operation instanceof ExpandOperation) {
            //not supported for now
        } else if (operation instanceof OrderByOperation) {
            //not supported for now
        } else if (operation instanceof SelectPropertiesOperation) {
            //not supported for now
        } else {
            throw new ODataSystemException("Unsupported query operation: " + operation);
        }
    }

    private void buildFromLimit(LimitOperation operation) throws ODataException {
        this.limit = operation.getCount();
        LOG.debug("Limit has been set to: {}", limit);
        buildFromOperation(operation.getSource());
    }

    private void buildFromSelect(SelectOperation selectOperation) {
        LOG.debug("Selecting all entities, no predicates needed");
    }

    private void buildFromSelectByKey(SelectByKeyOperation selectByKeyOperation) {
        Map<String, Object> keys = selectByKeyOperation.getKeyAsJava();
        String id = (String)keys.get("id");
        LOG.debug("Selecting by key: {}", id);

        predicates.add(entity -> entity.getId().equalsIgnoreCase(id));
    }

    private void buildFromFilter(CriteriaFilterOperation criteriaFilterOperation) {
        Criteria criteria = criteriaFilterOperation.getCriteria();
        if(criteria instanceof ComparisonCriteria) {
            ComparisonCriteria comparisonCriteria = (ComparisonCriteria) criteria;

            //For now we only support here property key/value comparisons, just to keep the example simple
            if(comparisonCriteria.getLeft() instanceof PropertyCriteriaValue
                    && comparisonCriteria.getRight() instanceof LiteralCriteriaValue) {

                PropertyCriteriaValue propertyCriteriaValue = (PropertyCriteriaValue) comparisonCriteria.getLeft();
                LiteralCriteriaValue literalCriteriaValue = (LiteralCriteriaValue) comparisonCriteria.getRight();

                Predicate<QueryableEntity> q = entity -> {
                    Object fieldValue = entity.getProperty(propertyCriteriaValue.getPropertyName());
                    Object queryValue = literalCriteriaValue.getValue();

                    LOG.debug("Comparing equality on value: {} to queried value: {}", fieldValue, queryValue);

                    return fieldValue != null && fieldValue.equals(literalCriteriaValue.getValue());
                };

                predicates.add(q);
            }
        }
    }
}
