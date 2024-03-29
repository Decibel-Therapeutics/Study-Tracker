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

import React from 'react';
import NoSidebarPageWrapper from "../structure/NoSidebarPageWrapper";
import LoadingMessage from "../structure/LoadingMessage";
import ErrorMessage from "../structure/ErrorMessage";
import {connect} from "react-redux";
import AssayTypeForm from "../components/forms/AssayTypeForm";

class AssayTypeFormView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      assayTypeId: props.match.params.assayTypeId || null,
      isLoaded: false,
      isError: false,
    };
  }

  componentDidMount() {

    fetch("/api/assaytype/")
    .then(response => response.json())
    .then(assayTypes => {
      if (!!this.state.assayTypeId) {
        const assayType = assayTypes.find(p => p.id === parseInt(this.state.assayTypeId));
        this.setState({
          selectedAssayType: assayType,
          assayTypes: assayTypes,
          isLoaded: true
        });
      } else {
        this.setState({
          assayTypes: assayTypes,
          isLoaded: true
        });
      }
    }).catch(error => {
      this.setState({
        isError: true,
        error: error
      });
    });

  }

  render() {
    let content = <LoadingMessage/>;
    if (this.state.isError) {
      content = <ErrorMessage/>;
    } else if (!!this.props.user && this.state.isLoaded) {
      content = <AssayTypeForm
          assayType={this.state.selectedAssayType}
          assayTypes={this.state.assayTypes}
      />;
    }
    return (
        <NoSidebarPageWrapper>
          {content}
        </NoSidebarPageWrapper>
    );
  }

}

export default connect(store => ({
  user: store.user
}))(AssayTypeFormView);
