import React from 'react';
import {Card, CardBody, Media} from "reactstrap";
import {Activity, Clipboard, Star, ThumbsUp, Users} from "react-feather";

export const StudyUpdatesToday = ({count}) => {
  return (
      <Card className="flex-fill">
        <CardBody className="py-4">
          <Media>
            <div className="d-inline-block mt-2 mr-3">
              <Activity className="feather-lg text-warning"/>
            </div>
            <Media body>
              <h3 className="mb-2">{count}</h3>
              <div className="mb-0">Study Updates Today</div>
            </Media>
          </Media>
        </CardBody>
      </Card>
  )
}

export const ActiveUsers = ({count}) => {
  return (
      <Card className="flex-fill">
        <CardBody className="py-4">
          <Media>
            <div className="d-inline-block mt-2 mr-3">
              <Users className="feather-lg text-primary"/>
            </div>
            <Media body>
              <h3 className="mb-2">{count}</h3>
              <div className="mb-0">Active Users</div>
            </Media>
          </Media>
        </CardBody>
      </Card>
  )
}

export const NewStudiesThisWeek = ({count}) => {
  return (
      <Card className="flex-fill">
        <CardBody className="py-4">
          <Media>
            <div className="d-inline-block mt-2 mr-3">
              <Star className="feather-lg text-warning"/>
            </div>
            <Media body>
              <h3 className="mb-2">{count}</h3>
              <div className="mb-0">New Studies This Week</div>
            </Media>
          </Media>
        </CardBody>
      </Card>
  )
}

export const CompletedStudiesThisMonth = ({count}) => {
  return (
      <Card className="flex-fill">
        <CardBody className="py-4">
          <Media>
            <div className="d-inline-block mt-2 mr-3">
              <ThumbsUp className="feather-lg text-success"/>
            </div>
            <Media body>
              <h3 className="mb-2">{count}</h3>
              <div className="mb-0">Studies Completed This Month</div>
            </Media>
          </Media>
        </CardBody>
      </Card>
  )
}

export const TotalStudies = ({count}) => {
  return (
      <Card className="flex-fill">
        <CardBody className="py-4">
          <Media>
            <div className="d-inline-block mt-2 mr-3">
              <Clipboard className="feather-lg text-primary"/>
            </div>
            <Media body>
              <h3 className="mb-2">{count}</h3>
              <div className="mb-0">Total Studies</div>
            </Media>
          </Media>
        </CardBody>
      </Card>
  )
}