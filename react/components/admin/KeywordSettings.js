import React from 'react';
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  CardTitle,
  Col,
  FormGroup,
  Input,
  Label,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Row
} from 'reactstrap';
import {Tag} from 'react-feather';
import ToolkitProvider, {Search} from "react-bootstrap-table2-toolkit";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
import Select from 'react-select';

const emptyKeyword = {
  id: null,
  category: null,
  keyword: ''
};

export default class KeywordSettings extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      showModal: false,
      keywords: [],
      categories: [],
      isLoaded: false,
      isError: false,
      selectedKeyword: emptyKeyword
    };
    this.toggleModal = this.toggleModal.bind(this);
    this.handleCategorySelect = this.handleCategorySelect.bind(this);
    this.handleInputUpdate = this.handleInputUpdate.bind(this);
    this.handleKeywordSubmit = this.handleKeywordSubmit.bind(this);
  }

  toggleModal(keyword) {
    this.setState({
      selectedKeyword: keyword || emptyKeyword,
      showModal: !this.state.showModal
    })
  }

  componentDidMount() {
    fetch("/api/keyword/categories")
    .then(response => response.json())
    .then(json => {
      this.setState({
        categories: json,
        isLoaded: true
      })
    })
    .catch(e => {
      console.error(e);
      this.setState({
        isError: true,
        error: e
      })
    });
  }

  handleCategorySelect(category) {
    fetch("/api/keyword?category=" + category)
    .then(response => response.json())
    .then(keywords => {
      this.setState({
        selectedCategory: category,
        keywords: keywords
      })
    })
  }

  handleInputUpdate(input) {
    const keyword = this.state.selectedKeyword;
    this.setState({
      selectedKeyword: {
        ...keyword,
        ...input
      }
    })
  }

  handleKeywordSubmit() {
    console.log(this.state.selectedKeyword);
  }

  render() {

    const columns = [
      {
        dataField: "keyword",
        text: "Keyword",
        sort: true,
        formatter: (c, d, i, x) => d.keyword,
        sortFunc: (a, b, order, dataField, rowA, rowB) => {
          if (rowA.keyword > rowB.keyword) {
            return order === "desc" ? -1 : 1;
          }
          if (rowB.keyword > rowA.keyword) {
            return order === "desc" ? 1 : -1;
          }
          return 0;
        },
      },
      {
        dataField: "controls",
        text: "Options",
        sort: false,
        formatter: (c, d, i, x) => {
          return (
              <React.Fragment>
                n/a
              </React.Fragment>
          )
        }
      }
    ];

    const categoryOptions = this.state.categories
    .sort((a, b) => {
      if (a > b) {
        return 1;
      } else if (a < b) {
        return -1;
      } else {
        return 0;
      }
    })
    .map(c => {
      return {
        value: c,
        label: c
      }
    });

    return (
        <Card>
          <CardHeader>
            <CardTitle tag={"h5"} className={"mb-0"}>
              Keywords
              <span className="float-right">
                <Button color={"primary"}
                        onClick={() => this.toggleModal(true)}>
                  New Keyword
                  &nbsp;
                  <Tag className="feather align-middle ml-2 mb-1"/>
                </Button>
              </span>
            </CardTitle>
          </CardHeader>
          <CardBody>

            <Row>
              <Col xs={12} sm={6}>
                <FormGroup>
                  <Label>Select a keyword category</Label>
                  <Select
                      className="react-select-container"
                      classNamePrefix="react-select"
                      options={categoryOptions}
                      onChange={(selected) => this.handleCategorySelect(
                          selected.value)}
                  />
                </FormGroup>
              </Col>
            </Row>

            <Row>
              <Col xs={12}>
                {
                  !!this.state.selectedCategory
                      ? (
                          <ToolkitProvider
                              keyField="id"
                              data={this.state.keywords}
                              columns={columns}
                              search
                              exportCSV
                          >
                            {props => (
                                <div>
                                  <div className="float-right">
                                    <Search.SearchBar
                                        {...props.searchProps}
                                    />
                                  </div>
                                  <BootstrapTable
                                      bootstrap4
                                      keyField="id"
                                      bordered={false}
                                      pagination={paginationFactory({
                                        sizePerPage: 10,
                                        sizePerPageList: [10, 20, 40, 80]
                                      })}
                                      defaultSorted={[{
                                        dataField: "keyword",
                                        order: "asc"
                                      }]}
                                      {...props.baseProps}
                                  >
                                  </BootstrapTable>
                                </div>
                            )}
                          </ToolkitProvider>
                      ) : (
                          ""
                      )
                }
              </Col>
            </Row>

            <Modal
                isOpen={this.state.showModal}
                toggle={() => this.toggleModal()}
                size={"lg"}
            >
              <ModalHeader toggle={() => this.toggleModal()}>
                {
                  !!this.state.selectedKeyword && this.state.selectedKeyword.id
                      ? "Edit Keyword" : "New Keyword"
                }
              </ModalHeader>
              <ModalBody>
                <Row>

                  <Col xs={12} sm={6}>
                    <FormGroup>
                      <Label>Category</Label>
                      <Select
                          className="react-select-container"
                          classNamePrefix="react-select"
                          options={categoryOptions}
                          onChange={(selected) => this.handleInputUpdate(
                              {category: selected.value})}
                      />
                    </FormGroup>
                  </Col>

                  <Col xs={12} sm={6}>
                    <FormGroup>
                      <Label>Keyword</Label>
                      <Input
                          defaultValue={this.state.selectedKeyword.keyword}
                          onChange={(e) => this.handleInputUpdate(
                              {keyword: e.target.value})}
                      />
                    </FormGroup>
                  </Col>

                </Row>
              </ModalBody>
              <ModalFooter>
                <Button color="secondary" onClick={() => this.toggleModal()}>
                  Cancel
                </Button>
                <Button color="primary" onClick={this.handleKeywordSubmit}>
                  Submit
                </Button>
              </ModalFooter>
            </Modal>

          </CardBody>
        </Card>
    )
  }

}
