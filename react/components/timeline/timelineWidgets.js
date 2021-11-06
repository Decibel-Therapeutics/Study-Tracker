import React from 'react';
import {Card, Col, Row} from "react-bootstrap";
import {Activity, Clipboard, Star, ThumbsUp, Users} from "react-feather";

const IllustrationWidget = ({header, text, image}) => {
  return (
      <Card className="flex-fill illustration">
        <Card.Body className="p-0 d-flex flex-fill">
          <Row className="g-0 w-100">

            <Col xs={6}>
              <div className="illustration-text p-3 m-1">
                <h4 className="illustration-text">
                  {header}
                </h4>
                <p className="mb-0">
                  {text}
                </p>
              </div>
            </Col>

            <Col xs={6} className="align-self-end text-end">
              <img
                  src={image}
                  className="img-fluid illustration-img"
              />
            </Col>

          </Row>
        </Card.Body>
      </Card>
  )
}

const StatWidget = ({label, value, icon: Icon, color}) => {
  return (
      <Card className="flex-fill">
        <Card.Body className="py-4">

          <div className="d-flex align-items-start">

            <div className="flex-grow-1">
              <h3 className="mb-2">{value}</h3>
              <p className="mb-2">{label}</p>
            </div>

            <div className="d-inline-block ms-3">
              <div className="stat">
                <Icon className={"align-md feather-lg " + (!!color ? "text-" + color : "text-primary")}/>
              </div>
            </div>

          </div>

        </Card.Body>
      </Card>
  )
}

export const StudyUpdates = ({count}) => {
  return <StatWidget label={"Updates this week"} icon={Activity} value={count} color={"warning"} />
}

export const ActiveUsers = ({count}) => {
  return <StatWidget label="Active Users" value={count} icon={Users} />
}

export const NewStudies = ({count}) => {
  return <StatWidget label={"New studies this week"} icon={Star} value={count} color={"warning"} />
}

export const CompletedStudies = ({count}) => {
  return <StatWidget label={"Completed studies this month"} icon={ThumbsUp} value={count} color={"success"} />
}

export const TotalStudies = ({count}) => {
  return <StatWidget label={"Total studies"} icon={Clipboard} value={count} />
}