import React from 'react';
import {Card, CardBody, Col, Container, Row} from "reactstrap";

export const SearchHits = ({hits}) => {

  const list = hits.hits.forEach((hit, i) => <SearchHit key={"search-hit-" + i} hit={hit} /> );

  return (
      <Container fluid className="animated fadeIn">

        <Row className="justify-content-between align-items-center">
          <Col xs="12">
            <h1>Search Hits</h1>
          </Col>
        </Row>

        <Row>
          <Col lg="12">
            <Card>
              <CardBody>
                {list}
              </CardBody>
            </Card>
          </Col>
        </Row>

      </Container>
  )

}

const SearchHit = ({hit}) => {
  const study = hit.document;
  return (
      <Row>
        <Col lg={12}>
          <h1>
            <a href={"/studies/" + study.code}>{study.code}: {study.name}</a>
          </h1>
          <h3>{study.program.name}</h3>
          <p>{study.description}</p>
        </Col>
      </Row>
  )
}