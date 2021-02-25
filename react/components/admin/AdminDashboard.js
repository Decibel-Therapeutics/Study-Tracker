import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import {
  Breadcrumb,
  BreadcrumbItem,
  Card,
  CardHeader,
  CardTitle,
  Col,
  Container,
  ListGroup,
  ListGroupItem,
  Row
} from "reactstrap";
import UserSettings from "./UserSettings";
import AssayTypeSettings from "./AssayTypeSettings";
import TemplateTypesSettings from './TemplateTypesSettings';

export const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('users');

  const getDashboardContent = () => {
    switch(activeTab) {
      case 'users':
        return <UserSettings />
      case 'assay-types':
        return <AssayTypeSettings />
      case 'template-types':
        return <TemplateTypesSettings />
    }
  }

  return (
    <Container fluid>
      <Row>
        <Col>
          <Breadcrumb>
            <BreadcrumbItem>
              <Link to="/">Home</Link>
            </BreadcrumbItem>
            <BreadcrumbItem active>Admin Dashboard</BreadcrumbItem>
          </Breadcrumb>
        </Col>
      </Row>

      <Row className="justify-content-between align-items-center">
        <Col>
          <h1>Admin Dashboard</h1>
        </Col>
      </Row>

      <Row>
        <Col md="3" xl="2">
          <Card>
            <CardHeader>
              <CardTitle tag="h5" className="mb-0">
                Site Settings
              </CardTitle>
            </CardHeader>
            <ListGroup flush>
              <ListGroupItem
                action
                active={activeTab === 'users'}
                onClick={() => setActiveTab('users')}
              >
                Users
              </ListGroupItem>
              <ListGroupItem
                action
                active={activeTab === 'assay-types'}
                onClick={() => setActiveTab('assay-types')}
              >
                Assay Types
              </ListGroupItem>
              <ListGroupItem
                action
                active={activeTab === 'template-types'}
                onClick={() => setActiveTab('template-types')}
              >
                Template Types
              </ListGroupItem>
            </ListGroup>
          </Card>
        </Col>

        <Col md="9" xl="10">
          { getDashboardContent() }
        </Col>
      </Row>
    </Container>
  );
}

export default AdminDashboard;
