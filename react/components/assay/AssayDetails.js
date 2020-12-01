import React from 'react';
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
  Nav,
  NavItem,
  NavLink,
  Row,
  TabContent,
  TabPane,
  UncontrolledDropdown
} from "reactstrap";
import {SelectableStatusButton, StatusButton} from "../status";
import {Book, Folder, Menu} from "react-feather";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEdit, faShare, faTrash} from "@fortawesome/free-solid-svg-icons";
import {history} from "../../App";
import {StudyTeam} from "../studyMetadata";
import AssayTimelineTab from "./AssayTimelineTab";
import AssayFilesTab from "./AssayFilesTab";

const AssayDetailHeader = ({study, assay, user}) => {
  return (
      <Row className="justify-content-between align-items-center">

        <Col>
          <h5 className="text-muted">{assay.assayType.name} Assay</h5>
          <h1>{assay.name}</h1>
          <h4>{assay.code}</h4>
        </Col>

        <Col className="col-auto">

          {
            !assay.active ? <Button size="lg" className="mr-1 mb-1"
                                    color="danger">Inactive Assay</Button> : ''
          }
          {
            !!user
                ? <SelectableStatusButton status={assay.status}
                                          assayId={assay.id}/>
                : <StatusButton status={assay.status}/>
          }

        </Col>
      </Row>
  );
};

export default class AssayDetails extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      activeTab: "1"
    }
  }

  toggle(tab) {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab
      });
    }
  }

  render() {

    const assay = this.props.assay;
    const study = this.props.study;
    const createMarkup = (content) => {
      return {__html: content};
    };

    return (
        <Container fluid className="animated fadeIn">

          <Row>
            <Col>
              <Breadcrumb>

                <BreadcrumbItem>
                  <a href={"/"}>Home</a>
                </BreadcrumbItem>

                <BreadcrumbItem>
                  <a href={"/study/" + study.code}>
                    Study {study.code}
                  </a>
                </BreadcrumbItem>

                <BreadcrumbItem>
                  Assay Detail
                </BreadcrumbItem>

              </Breadcrumb>
            </Col>
          </Row>

          <AssayDetailHeader assay={assay} study={study}
                             user={this.props.user}/>

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
                        <DropdownItem onClick={() => console.log("Share!")}>
                          <FontAwesomeIcon icon={faShare}/>
                          &nbsp;
                          Share
                        </DropdownItem>
                        {
                          !!this.props.user ? <DropdownItem divider/> : ''
                        }
                        {
                          !!this.props.user ? (
                              <DropdownItem onClick={() => history.push(
                                  "/study/" + study.code + "/assay/"
                                  + assay.code + "/edit")}>
                                <FontAwesomeIcon icon={faEdit}/>
                                &nbsp;
                                Edit
                              </DropdownItem>
                          ) : ''
                        }
                        {
                          !!this.props.user ? (
                              <DropdownItem
                                  onClick={() => console.log("Delete!")}>
                                <FontAwesomeIcon icon={faTrash}/>
                                &nbsp;
                                Delete
                              </DropdownItem>
                          ) : ''
                        }
                      </DropdownMenu>
                    </UncontrolledDropdown>
                  </div>
                  <CardTitle tag="h5" className="mb-0 text-muted">
                    Assay Overview
                  </CardTitle>
                </CardHeader>

                <CardBody>
                  <Row>
                    <Col xs="12">

                      <h6 className="details-label">Description</h6>
                      <div dangerouslySetInnerHTML={createMarkup(
                          assay.description)}/>


                      <h6 className="details-label">Created By</h6>
                      <p>{assay.createdBy.displayName}</p>

                      <h6 className="details-label">Last Updated</h6>
                      <p>{new Date(assay.updatedAt).toLocaleString()}</p>

                      <h6 className="details-label">Start Date</h6>
                      <p>{new Date(assay.startDate).toLocaleString()}</p>

                      <h6 className="details-label">End Date</h6>
                      <p>
                        {
                          !!assay.endDate
                              ? new Date(assay.endDate).toLocaleString()
                              : "n/a"
                        }
                      </p>

                    </Col>
                  </Row>
                </CardBody>

                <CardBody>
                  <Row>
                    <Col xs={12}>
                      <h6 className="details-label">Assay Team</h6>
                      <StudyTeam users={assay.users} owner={assay.owner}/>
                    </Col>
                  </Row>
                </CardBody>

                <CardBody>
                  <Row>
                    <Col xs={12}>
                      <h6 className="details-label">Workspaces</h6>
                      {
                        !!assay.storageFolder
                            ? (
                                <a href={assay.storageFolder.url}
                                   target="_blank"
                                   className="btn btn-info mr-2">
                                  Assay Storage Folder
                                  <Folder
                                      className="feather align-middle ml-2 mb-1"/>
                                </a>
                            ) : ''
                      }
                      {
                        !!assay.notebookFolder
                            ? (
                                <a href={assay.notebookFolder.url}
                                   target="_blank"
                                   className="btn btn-info mt-2 mr-2">
                                  Assay ELN Folder
                                  <Book
                                      className="feather align-middle ml-2 mb-1"/>
                                </a>
                            ) : ''
                      }

                    </Col>
                  </Row>
                </CardBody>

              </Card>
            </Col>

            <Col lg={7}>

              {/*Tabs*/}
              <div className="tab">
                <Nav tabs>

                  <NavItem>
                    <NavLink
                        className={this.state.activeTab === "1" ? "active" : ''}
                        onClick={() => {
                          this.toggle("1");
                        }}
                    >
                      Timeline
                    </NavLink>
                  </NavItem>

                  <NavItem>
                    <NavLink
                        className={this.state.activeTab === "2" ? "active" : ''}
                        onClick={() => {
                          this.toggle("2");
                        }}
                    >
                      Files
                    </NavLink>
                  </NavItem>

                  <NavItem>
                    <NavLink
                        className={this.state.activeTab === "3" ? "active" : ''}
                        onClick={() => {
                          this.toggle("3");
                        }}
                    >
                      Results
                    </NavLink>
                  </NavItem>

                </Nav>

                {/*Tab content*/}
                <TabContent activeTab={this.state.activeTab}>

                  <TabPane tabId="1">
                    <AssayTimelineTab assay={assay} user={this.props.user}/>
                  </TabPane>

                  <TabPane tabId="2">
                    <AssayFilesTab assay={assay} user={this.props.user}/>
                  </TabPane>

                  <TabPane tabId="3">
                    <p className="text-center">
                      Results will go here.
                    </p>
                  </TabPane>

                </TabContent>
              </div>
            </Col>

          </Row>

        </Container>
    );
  }

}