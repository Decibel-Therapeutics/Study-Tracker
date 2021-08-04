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
  Card,
  CardBody,
  CardHeader,
  CardTitle,
  Col,
  Container,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  Nav,
  NavItem,
  NavLink,
  Row,
  TabContent,
  TabPane,
  UncontrolledDropdown
} from "reactstrap";
import React from "react";
import {Menu} from "react-feather";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEdit} from "@fortawesome/free-solid-svg-icons";
import {history} from "../../App";
import StudyCollectionStudiesTab from "./StudyCollectionStudiesTab";

const StudyCollectionDetailsHeader = ({collection, user}) => {
  return (
      <Row className="justify-content-between align-items-center">
        <Col>
          <h1>{collection.name}</h1>
        </Col>
      </Row>
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

            <Col lg={5}>
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

                      <h3>{collection.name}</h3>

                      <h6 className="details-label">Description</h6>
                      <p>{collection.name}</p>

                      <h6 className="details-label">Created By</h6>
                      <p>{collection.createdBy.displayName}</p>

                      <h6 className="details-label">Last Modified By</h6>
                      <p>{collection.lastModifiedBy.displayName}</p>

                      <h6 className="details-label">Date Created</h6>
                      <p>{new Date(collection.createdAt).toLocaleString()}</p>

                      <h6 className="details-label">Last Updated</h6>
                      <p>{new Date(collection.updatedAt).toLocaleString()}</p>

                    </Col>
                  </Row>
                </CardBody>

              </Card>
            </Col>

            <Col lg="7">

              {/* Tabs */}
              <div className="tab">
                <Nav tabs>

                  <NavItem>
                    <NavLink
                        className={this.state.activeTab === "1" ? "active" : ''}
                        onClick={() => {
                          this.toggle("1");
                        }}
                    >
                      Studies
                    </NavLink>
                  </NavItem>

                </Nav>

                <TabContent activeTab={this.state.activeTab}>

                  <TabPane tabId="1">
                    <StudyCollectionStudiesTab studies={collection.studies}
                                        user={this.props.user}/>
                  </TabPane>

                </TabContent>
              </div>
            </Col>
          </Row>
        </Container>
    );
  }

}

export default StudyCollectionDetails;