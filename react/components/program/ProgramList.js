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

import React from "react";
import {
  Button,
  Card,
  CardBody,
  Col,
  Container,
  FormGroup,
  Input,
  Label,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Row,
  UncontrolledAlert
} from "reactstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlusCircle} from "@fortawesome/free-solid-svg-icons";
import BootstrapTable from "react-bootstrap-table-next";
import ToolkitProvider, {Search} from 'react-bootstrap-table2-toolkit';
import paginationFactory from "react-bootstrap-table2-paginator";
import {File} from "react-feather";
import Select from "react-select";

const columns = [
  {
    dataField: "name",
    text: "Name",
    sort: true,
    headerStyle: {width: '50%%'},
    formatter: (c, d, i, x) => d.name
  },
  {
    dataField: "code",
    text: "Code",
    sort: true,
    headerStyle: {width: '25%'},
    formatter: (cell, d, index, x) => d.code
  },
  {
    dataField: "active",
    text: "Active",
    sort: true,
    headerStyle: {width: '25%'},
    formatter: (c, d, i, x) => {
      if (d.active) {
        return (
            <div className="badge badge-success">
              Active
            </div>
        )
      } else {
        return (
            <div className="badge badge-warning">
              Inactive
            </div>
        )
      }
    }
  }
];

const ExportToCsv = (props) => {
  const handleClick = () => {
    props.onExport();
  };
  return (
      <span>
        <Button color={'primary'} onClick={handleClick}>
          Export to CSV
          &nbsp;
          <File className="feather align-middle ml-2 mb-1"/>
        </Button>
      </span>
  );
};

class ProgramList extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      programs: props.programs,
      modalIsOpen: false,
      modalError: null,
      newProgram: {
        name: '',
        code: '',
        active: true
      }
    };
    this.toggleModal = this.toggleModal.bind(this);
    this.handleNewProgramChange = this.handleNewProgramChange.bind(this);
    this.handleNewProgramSubmit = this.handleNewProgramSubmit.bind(this);
  }

  toggleModal() {
    this.setState({
      modalIsOpen: !this.state.modalIsOpen
    })
  }

  handleNewProgramChange(props) {
    this.setState({
      newProgram: {
        ...this.state.newProgram,
        ...props
      }
    })
  }

  handleNewProgramSubmit() {
    console.log(this.state.newProgram);
    let p = this.state.newProgram;
    if (!p.name || !p.code) {
      this.setState({
        modalError: "One or more required fields are missing. Please check your inputs and then try again."
      });
      return;
    }
    let duplicate = false;
    for (let program of this.state.programs) {
      if (p.name === program.name) {
        duplicate = true;
      }
    }
    if (duplicate) {
      this.setState({
        modalError: "A program with this name already exists. Please provide a unique name and try again."
      });
      return;
    }
    return fetch("/api/program", {
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(p)
    })
    .then(response => response.json())
    .then(program => {
      this.setState({
        programs: [...this.state.programs, program],
        newProgram: {
          name: '',
          code: '',
          active: true
        },
        modalError: null
      });
      this.toggleModal();
    })
    .catch(e => {
      console.error(e);
      this.setState({
        modalError: e.message
      });
    })
  }

  render() {

    let {title, filters, user} = this.props;

    return (
        <Container fluid className="animated fadeIn">

          <Row className="justify-content-between align-items-center">
            <Col xs="8">
              <h1>{title}</h1>
            </Col>
            <Col className="col-auto">
              {
                !!user && !!user.admin
                    ? (
                        <a onClick={() => this.toggleModal()}>
                          <Button color="primary" className="mr-1 mb-1">
                            <FontAwesomeIcon icon={faPlusCircle}/> New Program
                          </Button>
                        </a>
                    ) : ''
              }
            </Col>
          </Row>

          <Row>
            <Col lg="12">
              <Card>
                <CardBody>
                  <ToolkitProvider
                      keyField="id"
                      data={this.state.programs}
                      columns={columns}
                      search
                      exportCSV
                  >
                    {props => (
                        <div>
                          <div className="float-right">
                            <ExportToCsv{...props.csvProps} />
                            &nbsp;&nbsp;
                            <Search.SearchBar
                                {...props.searchProps}
                            />
                          </div>
                          <BootstrapTable
                              bootstrap4
                              keyField="id"
                              // data={studies}
                              // columns={columns}
                              bordered={false}
                              pagination={paginationFactory({
                                sizePerPage: 10,
                                sizePerPageList: [10, 20, 40, 80]
                              })}
                              defaultSorted={[{
                                dataField: "updatedAt",
                                order: "desc"
                              }]}
                              {...props.baseProps}
                          >
                          </BootstrapTable>
                        </div>
                    )}
                  </ToolkitProvider>
                </CardBody>
              </Card>
            </Col>
          </Row>

          <Modal
              isOpen={this.state.modalIsOpen}
              toggle={() => this.toggleModal()}
              size={"md"}
          >

            <ModalHeader toggle={() => this.toggleModal()}>
              Add New Program
            </ModalHeader>

            <ModalBody className="m-3">

              <Row form>

                <Col sm={12}>
                  <p>
                    Please provide a unique name to identify the program, and an
                    alphanumeric code that will be used as a prefix for all new
                    study codes. The program code does not need to be unique.
                    All fields are required.
                  </p>
                </Col>

                <Col sm="12">
                  <FormGroup>
                    <Label>Name *</Label>
                    <Input
                        type="text"
                        defaultValue={this.state.newProgram.name}
                        onChange={(e) => this.handleNewProgramChange({
                          name: e.target.value
                        })}
                    />
                  </FormGroup>
                </Col>

                <Col sm="12">
                  <FormGroup>
                    <Label>Program Code *</Label>
                    <Input
                        type="text"
                        value={this.state.newProgram.code}
                        onChange={(e) => this.handleNewProgramChange({
                          code: e.target.value.toUpperCase().replace(
                              /[^A-Z0-9]/g, "")
                        })}
                    />
                  </FormGroup>
                </Col>

                <Col sm="12">
                  <FormGroup>
                    <Label>Is this program active?</Label>
                    <Select
                        className="react-select-container"
                        classNamePrefix="react-select"
                        options={[
                          {
                            value: true,
                            label: "Active"
                          },
                          {
                            value: false,
                            label: "Inactive"
                          }
                        ]}
                        defaultValue={{
                          value: true,
                          label: "Active"
                        }}
                        onChange={(selected) => this.handleNewProgramChange({
                          active: selected.value
                        })}
                    />
                  </FormGroup>
                </Col>

              </Row>
              {
                !!this.state.modalError
                    ? (
                        <Row>
                          <Col sm={12}>
                            <UncontrolledAlert color={"warning"}>
                              <div className="alert-message mr-4">
                                {this.state.modalError}
                              </div>
                            </UncontrolledAlert>
                          </Col>
                        </Row>
                    ) : ''
              }

            </ModalBody>

            <ModalFooter>
              <Button color={"secondary"} onClick={() => this.toggleModal()}>
                Cancel
              </Button>
              <Button color={"primary"}
                      onClick={this.handleNewProgramSubmit}>
                Save
              </Button>
            </ModalFooter>

          </Modal>

        </Container>
    );
  }

}

export default ProgramList;