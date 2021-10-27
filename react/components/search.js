import React from 'react';
import {Field, Form, Formik} from 'formik';
import {
  Card,
  CardBody,
  Col,
  Container,
  InputGroup,
  InputGroupAddon,
  Row
} from "reactstrap";
import {Search} from "react-feather";
import {history} from "../App";

export class SidebarSearch extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      "q": ""
    };
  }

  render() {
    return (
        <Formik
            initialValues={{q: ''}}
            onSubmit={(values => history.push("/search?q=" + values.q))}
        >
          <Form className="ml-3 mr-3" >
            <InputGroup className="mb-3 sidebar-search">
              <Field
                  name={"q"}
                  placeholder={"Enter keywords here..."}
                  aria-label={"Search"}
                  className={"form-control form-control-no-border"}
              />
              <InputGroupAddon addonType={"append"}>
                <button type={"submit"} className={"btn-primary btn"}>
                  <Search className={"feather align-middle"}/>
                </button>
              </InputGroupAddon>
            </InputGroup>
          </Form>
        </Formik>
    )
  }

}

export const SearchHits = ({hits}) => {

  const list = hits.hits
  .sort((a, b) => {
    if (a.score < b.score) return 1;
    if (a.score > b.score) return -1;
    return 0;
  })
  .map((hit, i) => <SearchHit key={"search-hit-" + i} hit={hit} /> );

  return (
      <Container fluid className="animated fadeIn">

        <Row className="justify-content-between align-items-center">
          <Col xs="12">
            <h1>Search Hits</h1>
          </Col>
        </Row>

        <Row>
          <Col lg="12">
            {list}
          </Col>
        </Row>

      </Container>
  )

}

const SearchHit = ({hit}) => {
  const study = hit.document;
  return (
      <Card>
        <CardBody>
          <Row>
            <Col lg={12}>
              <h4>
                <a href={"/study/" + study.code}>{study.code}: {study.name}</a>
              </h4>
              <h5>{study.program.name}</h5>
              <p>{study.description}</p>
              <p>Score: {hit.score}</p>
            </Col>
          </Row>
        </CardBody>
      </Card>
  )
}