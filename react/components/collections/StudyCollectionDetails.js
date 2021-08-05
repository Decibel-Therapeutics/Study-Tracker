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

import {
  Breadcrumb,
  BreadcrumbItem,
  Button,
  Card,
  CardBody,
  CardHeader,
  CardTitle,
  Col,
  Container,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  Row,
  UncontrolledDropdown
} from "reactstrap";
import React from "react";
import {File, Menu} from "react-feather";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEdit} from "@fortawesome/free-solid-svg-icons";
import {history} from "../../App";
import {StudyListTable} from "../study/StudyList";

const StudyCollectionDetailsHeader = ({collection, user}) => {
  return (
      <Row className="justify-content-between align-items-center">
        <Col>
          <h1>{collection.name}</h1>
        </Col>
      </Row>
  );
};

const ExportToCsv = (props) => {
  const handleClick = () => {
    props.onExport();
  };
  return (
      <span>
        <Button color={'primary'} onClick={handleClick}>
          Export to CSV
          &nbsp;
          <File className="feather align-middle ml-2 mb-1"/>
        </Button>
      </span>
  );
};

class StudyCollectionDetails extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      activeTab: "1"
    };
  }

  toggle(tab) {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab
      });
    }
  }

  render() {

    const {collection} = this.props;
    const createMarkup = (content) => {
      return {__html: content};
    };

    return (
        <Container fluid className="animated fadeIn">

          {/* Breadcrumb */}
          <Row>
            <Col>
              <Breadcrumb>
                <BreadcrumbItem>
                  <a href={"/collections"}>Collections</a>
                </BreadcrumbItem>
                <BreadcrumbItem active>
                  Collection Detail
                </BreadcrumbItem>
              </Breadcrumb>
            </Col>
          </Row>

          {/* Header */}
          <StudyCollectionDetailsHeader collection={collection} user={this.props.user}/>

          <Row>

            <Col xs={12} md={6}>
              <Card className="details-card">

                <CardHeader>
                  <div className="card-actions float-right">
                    <UncontrolledDropdown>
                      <DropdownToggle tag="a">
                        <Menu/>
                      </DropdownToggle>
                      <DropdownMenu right>
                        {/*<DropdownItem onClick={() => console.log("Share!")}>*/}
                        {/*  <FontAwesomeIcon icon={faShare}/>*/}
                        {/*  &nbsp;*/}
                        {/*  Share*/}
                        {/*</DropdownItem>*/}
                        {/*{*/}
                        {/*  !!this.props.user && !!this.props.user.admin ?*/}
                        {/*      <DropdownItem divider/> : ''*/}
                        {/*}*/}

                        <DropdownItem onClick={() => history.push(
                            "/collection/" + collection.id + "/edit")}>
                          <FontAwesomeIcon icon={faEdit}/>
                          &nbsp;
                          Edit
                        </DropdownItem>

                        {/*{*/}
                        {/*  !!this.props.user && !!this.props.user.admin ? (*/}
                        {/*      <DropdownItem*/}
                        {/*          onClick={() => console.log("Delete!")}>*/}
                        {/*        <FontAwesomeIcon icon={faTrash}/>*/}
                        {/*        &nbsp;*/}
                        {/*        Delete*/}
                        {/*      </DropdownItem>*/}
                        {/*  ) : ''*/}
                        {/*}*/}
                      </DropdownMenu>
                    </UncontrolledDropdown>
                  </div>
                  <CardTitle tag="h5" className="mb-0 text-muted">
                    Summary
                  </CardTitle>
                </CardHeader>

                <CardBody>

                  <Row>

                    <Col xs={12}>
                      <h6 className="details-label">Description</h6>
                      <p>{collection.name}</p>
                    </Col>

                  </Row>

                  <Row>

                    <Col xs={6} sm={4}>
                      <h6 className="details-label">Created By</h6>
                      <p>{collection.createdBy.displayName}</p>
                    </Col>

                    <Col xs={6} sm={4}>
                      <h6 className="details-label">Last Modified By</h6>
                      <p>{collection.lastModifiedBy.displayName}</p>
                    </Col>

                  </Row>

                  <Row>

                    <Col xs={6} sm={4}>
                      <h6 className="details-label">Date Created</h6>
                      <p>{new Date(collection.createdAt).toLocaleString()}</p>
                    </Col>

                    <Col xs={6} sm={4}>
                      <h6 className="details-label">Last Updated</h6>
                      <p>{new Date(collection.updatedAt).toLocaleString()}</p>
                    </Col>

                  </Row>

                </CardBody>

              </Card>
            </Col>

            <Col xs="12">
              <Card className="details-card">

                <CardHeader>
                  <CardTitle tag="h5" className="mb-0 text-muted">
                    Studies
                  </CardTitle>
                </CardHeader>

                <CardBody>
                  <StudyListTable studies={collection.studies} />
                </CardBody>

              </Card>
            </Col>

          </Row>
        </Container>
    );
  }

}

export default StudyCollectionDetails;