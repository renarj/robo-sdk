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

import com.oberasoftware.robo.api.motion.MotionManager;
import com.oberasoftware.robo.api.servo.ServoDriver;
import com.oberasoftware.robo.api.servo.ServoProperty;
import com.oberasoftware.robo.service.model.MotionModel;
import com.oberasoftware.robo.service.model.ServoModel;
import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.processor.datasource.DataSource;
import com.sdl.odata.api.processor.datasource.DataSourceProvider;
import com.sdl.odata.api.processor.datasource.ODataDataSourceException;
import com.sdl.odata.api.processor.query.QueryOperation;
import com.sdl.odata.api.processor.query.strategy.QueryOperationStrategy;
import com.sdl.odata.api.service.ODataRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class RobotDataSourceProvider implements DataSourceProvider {
    private static final Logger LOG = LoggerFactory.getLogger(RobotDataSourceProvider.class);

    @Autowired
    private RobotDataSource robotDataSource;

    @Autowired
    private ServoDriver servoDriver;

    @Autowired
    private MotionManager motionManager;

    @Override
    public boolean isSuitableFor(ODataRequestContext oDataRequestContext, String entityType) throws ODataDataSourceException {
         Class<?> javaType = oDataRequestContext.getEntityDataModel().getType(entityType).getJavaType();

        return javaType.equals(MotionModel.class) || javaType.equals(ServoModel.class);
    }

    @Override
    public DataSource getDataSource(ODataRequestContext oDataRequestContext) {
        return robotDataSource;
    }

    @Override
    public QueryOperationStrategy getStrategy(ODataRequestContext oDataRequestContext, QueryOperation queryOperation, TargetType targetType) throws ODataException {
        StrategyBuilder builder = new StrategyBuilder();


        List<Predicate<QueryableEntity>> predicateList = builder.buildCriteria(queryOperation);
        int limit = builder.getLimit();

        return () -> {
            LOG.debug("Executing query against Robot data: {}", targetType.typeName());

            Class<?> dataType = oDataRequestContext.getEntityDataModel().getType(targetType.typeName()).getJavaType();
            List<QueryableEntity> entities;
            if(dataType.equals(ServoModel.class)) {
                entities = servoDriver.getServos().stream().map(s -> {
                    int speed = s.getData().getValue(ServoProperty.SPEED);
                    int position = s.getData().getValue(ServoProperty.POSITION);

                    return new ServoModel(s.getId(), speed, position);
                }).collect(Collectors.toList());
            } else {
                entities = motionManager.findMotions().stream()
                        .map(m -> new MotionModel(m.getName()))
                        .collect(Collectors.toList());
            }

            return entities.stream().filter(p -> predicateList.stream()
                    .allMatch(f -> f.test(p))).limit(limit).collect(Collectors.toList());

        };
    }
}
