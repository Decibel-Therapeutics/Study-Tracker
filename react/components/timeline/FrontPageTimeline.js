import React from 'react';
import {Button, Card, Col, Container, Row} from "react-bootstrap";
import {Timeline} from "../activity";
import {
  ActiveUsers,
  CompletedStudies,
  NewStudies,
  StudyUpdates,
  TotalStudies
} from './timelineWidgets';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlusCircle} from "@fortawesome/free-solid-svg-icons";
import {ArrowLeft, ArrowRight} from "react-feather";

const FrontPageTimeline = ({activity, stats, user, pageNumber, pageSize, hasNextPage, hasPreviousPage}) => {

  let activityCount = stats.activityCount || 0;
  let activeUsers = stats.activeUserCount || 0;
  let newStudies = stats.newStudyCount || 0;
  let completedStudies = stats.completedStudyCount || 0;
  let totalStudies = stats.studyCount || 0;

  return (

      <Container fluid className="animated fadeIn">

        <Row className="mb-2 mb-xl-3">

          <Col xs="8" className="d-none d-sm-block">
            <h3>Latest Activity</h3>
          </Col>

          <Col xs="auto" className="ms-auto text-end mt-n1">
            <a href="/studies/new">
              <Button color="primary" className="mr-1 mb-1">
                <FontAwesomeIcon icon={faPlusCircle}/> New Study
              </Button>
            </a>
          </Col>

        </Row>

        <Row>

          <Col lg={3}>

            <Row className="study-statistics">

              <Col xs={6} md={4} lg={12} className="d-flex">
                <StudyUpdates count={activityCount} />
              </Col>

              <Col xs={6} sm={4} md={3} lg={12} className="d-flex">
                <ActiveUsers count={activeUsers}/>
              </Col>

              <Col xs={6} sm={4} md={3} lg={12} className="d-flex">
                <NewStudies count={newStudies} label={"New Studies This Week"}/>
              </Col>

              <Col xs={6} sm={4} md={3} lg={12} className="d-flex">
                <CompletedStudies count={completedStudies}
                                  label={"Completed Studies This Month"}/>
              </Col>

              <Col xs={6} sm={4} md={3} lg={12} className="d-flex">
                <TotalStudies count={totalStudies}/>
              </Col>

            </Row>

          </Col>

          <Col lg={9}>
            <Card>
              <Card.Body>
                <Row>

                  <Col xs={12}>
                    <Timeline activities={activity}/>
                  </Col>

                  <Col xs={12}>
                    <hr/>
                  </Col>

                  <Col xs="auto" className="d-none d-sm-block">
                    {
                      !!hasPreviousPage
                          ? <a
                              href={"/?size=" + pageSize + "&page=" + (pageNumber
                                  - 1)} className="btn btn-primary">
                            <ArrowLeft
                                className="feather align-middle mr-2"/> Previous
                            Page
                          </a>
                          : ''
                    }
                  </Col>

                  <Col xs="auto" className="ms-auto text-end mt-n1">
                    {
                      !!hasNextPage
                          ? <a
                              href={"/?size=" + pageSize + "&page=" + (pageNumber
                                  + 1)} className="btn btn-primary float-right">
                            Next Page <ArrowRight
                              className="feather align-middle mr-2"/>
                          </a>
                          : ''
                    }
                  </Col>

                </Row>
              </Card.Body>
            </Card>
          </Col>

        </Row>

      </Container>
  );

};

export default FrontPageTimeline;