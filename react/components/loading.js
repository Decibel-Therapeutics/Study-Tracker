/*
 * Copyright 2020 the original author or authors
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

import {Spinner} from 'reactstrap';
import React from "react";

export const CardLoadingMessage = () => {
  return (
      <div>
        <h4>
          Loading...
          <Spinner color={'primary'} className="mr-2"/>
        </h4>
      </div>
  )
};

export const LoadingOverlay = ({isVisible, message}) => {
  const label = message || "Loading..."
  return (
      <div className="spinner-overlay animated fadeIn" hidden={!isVisible}>
        <h2 className="mb-3">
          {label}
        </h2>
        <Spinner color={'primary'} className="mr-2"/>
      </div>
  );
}