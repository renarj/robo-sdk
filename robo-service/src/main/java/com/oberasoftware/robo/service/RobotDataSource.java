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

import com.oberasoftware.robo.api.MotionManager;
import com.oberasoftware.robo.api.RobotController;
import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.edm.model.EntityDataModel;
import com.sdl.odata.api.parser.ODataUri;
import com.sdl.odata.api.processor.datasource.DataSource;
import com.sdl.odata.api.processor.link.ODataLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author rdevries
 */
@Component
public class RobotDataSource implements DataSource {
    private static final Logger LOG = LoggerFactory.getLogger(RobotDataSource.class);

    @Autowired
    private RobotController controller;

    @Autowired
    private MotionManager motionManager;

    @PostConstruct
    public void initialize() {
        LOG.info("Initializing datasource");

        if(controller.initialize()) {
            LOG.info("Example Robot application container started");
        } else {
            LOG.error("Controller could not initialize");
            System.exit(-1);
        }
    }

    @Override
    public Object create(ODataUri oDataUri, Object o, EntityDataModel entityDataModel) throws ODataException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object update(ODataUri oDataUri, Object o, EntityDataModel entityDataModel) throws ODataException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(ODataUri oDataUri, EntityDataModel entityDataModel) throws ODataException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void createLink(ODataUri oDataUri, ODataLink oDataLink, EntityDataModel entityDataModel) throws ODataException {

    }

    @Override
    public void deleteLink(ODataUri oDataUri, ODataLink oDataLink, EntityDataModel entityDataModel) throws ODataException {

    }
}
