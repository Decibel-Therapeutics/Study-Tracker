import React, {useState} from 'react';
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
            onSubmit={(values => {
              history.push("/search?q=" + values.q);
              history.go(0);
            })}
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

const SearchHitHighlight = ({field, text}) => {
  return (
      <Col lg={12} className={"search-hit-highlight"}>
        <h6>{field}</h6>
        <blockquote>
          <div className="bg-light p-2 font-italic" dangerouslySetInnerHTML={createMarkup(text)}/>
        </blockquote>
      </Col>
  )
}

const SearchHitHighlights = ({hit}) => {
  let list = [];
  for (const [field, highlight] of Object.entries(hit.highlightFields)) {
    // const bits = field.split(".");
    // const field = bits[bits.length-1];
    highlight.forEach(h => {
      const text = h.replace("<em>", "<mark>").replace("</em>", "</mark>");
      list.push(
          <SearchHitHighlight
            key={"search-highlight-" + hit.document.id + "-" + field}
            field={field}
            text={text}
          />
      );
    });
  }
  return (
    <Col sm={12}>
      <h6>Matched Fields</h6>
      <Row>
        {list}
      </Row>
    </Col>
  );
}

const createMarkup = (content) => {
  return {__html: content};
};

const SearchHit = ({hit}) => {
  const [toggle, setToggle] = useState(false);
  const study = hit.document;
  return (
      <Card>
        <CardBody>
          <Row>

            <Col sm={8} md={10}>
              <h4>
                <a href={"/study/" + study.code}>{study.code}: {study.name}</a>
              </h4>
            </Col>

            <Col sm={4} md={2}>
              <p className="text-muted">
                <span className="float-right">{study.program.name}</span>
              </p>
            </Col>

            <Col sm={12}>
              <p>{study.description}</p>
            </Col>

            <Col xs={12}>
              <a href={"/study/" + study.code} className="btn btn-sm btn-outline-primary">
                View Study
              </a>
              &nbsp;&nbsp;
              <a className="btn btn-sm btn-outline-secondary" onClick={() => setToggle(!toggle)}>
                {!!toggle ? "Hide": "Show"} Hit Details
              </a>
            </Col>

            <div hidden={!toggle} style={{width: "100%"}}>

              <Col xs={12}>
                <hr />
              </Col>

              <Col xs={12}>
                <h6>Search Score</h6>
                <p>{hit.score}</p>
              </Col>

              {
                !!hit.highlightFields
                    ? <SearchHitHighlights hit={hit} />
                    : ''
              }

            </div>

          </Row>
        </CardBody>
      </Card>
  )
}