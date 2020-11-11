import React from 'react';
import swal from "sweetalert";
import {history} from "../../App";
import {
  Button,
  Col,
  Form,
  FormFeedback,
  FormGroup,
  FormText,
  Input,
  Label,
  Row
} from "reactstrap";
import Select from "react-select";

class AdminUserForm extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      user: props.user || {
        admin: false,
        credentialsExpired: true
      },
      validation: {
        usernameIsValid: true,
        usernameIsUnique: true,
        nameIsValid: true,
        emailIsValid: true,
        emailIsUnique: true
      }
    }
    this.handleFormUpdate = this.handleFormUpdate.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.validateForm = this.validateForm.bind(this);
  }

  /**
   * Updates the user state when an input is changed.
   *
   * @param data
   */
  handleFormUpdate(data) {
    const user = {
      ...this.state.user,
      ...data
    };
    console.log(user);
    this.setState({
      user: user
    })
  }

  validateForm(user) {
    let isError = false;
    let validation = this.state.validation;

    // Name
    if (!user.displayName) {
      isError = true;
      validation.nameIsValid = false;
    } else {
      validation.nameIsValid = true;
    }

    // Username
    if (!user.username) {
      isError = true;
      validation.usernameIsValid = false;
    } else {
      validation.usernameIsValid = true;
    }
    if (!user.id) {
      for (let u of this.props.users) {
        if (!!user.username && u.username.toLowerCase()
            === user.username.toLowerCase()) {
          isError = true;
          validation.usernameIsUnique = false;
        }
      }
    }

    // Email
    if (!user.email) {
      isError = true;
      validation.emailIsValid = false;
    } else {
      validation.emailIsValid = true;
    }
    if (!user.id) {
      for (let u of this.props.users) {
        if (!!user.email && u.email.toLowerCase()
            === user.email.toLowerCase()) {
          isError = true;
          validation.emailIsUnique = false;
        }
      }
    }

    this.setState({
      validation: validation
    });
    return isError;
  }

  handleSubmit() {

    let isError = this.validateForm(this.state.user);
    console.log(this.state);

    if (isError) {

      swal("Looks like you forgot something...",
          "Check that all of the required inputs have been filled and then try again.",
          "warning");
      console.warn("Validation failed.");

    } else {

      const isUpdate = !!this.state.user.id;
      const url = isUpdate
          ? "/api/user/" + this.state.user.id
          : "/api/user";
      this.setState({showLoadingOverlay: true});

      fetch(url, {
        method: isUpdate ? "PUT" : "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(this.state.user)
      })
      .then(async response => {

        const json = await response.json();
        console.log(json);
        if (response.ok) {
          history.go(0);
        } else {
          this.setState({showLoadingOverlay: false})
          swal("Something went wrong",
              !!json.message
                  ? "Error: " + json.message :
                  "The request failed. Please check your inputs and try again. If this error persists, please contact Study Tracker support."
          );
          console.error("Request failed.");
        }

      }).catch(e => {
        this.setState({showLoadingOverlay: false})
        swal(
            "Something went wrong",
            "The request failed. Please check your inputs and try again. If this error persists, please contact Study Tracker support."
        );
        console.error(e);
      });
    }
  }

  render() {
    return (

        <Form className="user-form">

          <Row form>

            <Col md="6">
              <FormGroup>
                <Label>Name *</Label>
                <Input
                    type="text"
                    invalid={!this.state.validation.nameIsValid}
                    defaultValue={this.state.user.displayName || ''}
                    onChange={(e) => this.handleFormUpdate(
                        {"displayName": e.target.value})}
                />
                <FormFeedback>
                  {"Name must not be empty."}
                </FormFeedback>
              </FormGroup>
            </Col>

            <Col md="6">
              <FormGroup>
                <Label>Role</Label>
                <Select
                    className="react-select-container"
                    classNamePrefix="react-select"
                    options={[
                      {
                        value: false,
                        label: "User"
                      },
                      {
                        value: true,
                        label: "Admin"
                      }
                    ]}
                    defaultValue={
                      this.state.user.admin ?
                          {
                            value: true,
                            label: "Admin"
                          } : {
                            value: false,
                            label: "User"
                          }
                    }
                    onChange={(selected) => this.handleFormUpdate(
                        {"admin": selected.value})}
                />
              </FormGroup>
            </Col>

            <Col md={6}>
              <FormGroup>
                <Label>Username *</Label>
                <Input
                    type="text"
                    invalid={!this.state.validation.usernameIsValid
                    || !this.state.validation.usernameIsUnique}
                    defaultValue={this.state.user.username || ''}
                    onChange={(e) => this.handleFormUpdate(
                        {"username": e.target.value})}
                    disabled={!!this.state.user.id}
                />
                <FormFeedback>
                  {
                    !this.state.validation.usernameIsUnique
                        ? "A user with this username already exists."
                        : "Name must not be empty."
                  }
                </FormFeedback>
                <FormText>Must be unique.</FormText>
              </FormGroup>
            </Col>

            <Col md={6}>
              <FormGroup>
                <Label>Email *</Label>
                <Input
                    type="text"
                    invalid={!this.state.validation.emailIsValid
                    || !this.state.validation.emailIsUnique}
                    defaultValue={this.state.user.email || ''}
                    onChange={(e) => this.handleFormUpdate(
                        {"email": e.target.value})}
                    disabled={!!this.state.user.id}
                />
                <FormFeedback>
                  {
                    !this.state.validation.emailIsUnique
                        ? "A user with this emil address already exists."
                        : "Email must not be empty."
                  }
                </FormFeedback>
              </FormGroup>
            </Col>

            <Col md={6}>
              <FormGroup>
                <Label>Title</Label>
                <Input
                    type="text"
                    defaultValue={this.state.user.title || ''}
                    onChange={(e) => this.handleFormUpdate(
                        {"title": e.target.value})}
                />
              </FormGroup>
            </Col>

            <Col md={6}>
              <FormGroup>
                <Label>Department</Label>
                <Input
                    type="text"
                    defaultValue={this.state.user.department || ''}
                    onChange={(e) => this.handleFormUpdate(
                        {"department": e.target.value})}
                />
              </FormGroup>
            </Col>

          </Row>

          {/*Buttons*/}
          <Row form>
            <Col className="text-center">
              <FormGroup>
                <Button size="lg" color="primary"
                        onClick={this.handleSubmit}>Submit</Button>
                &nbsp;&nbsp;
                <Button size="lg" color="secondary"
                        onClick={() => this.props.handleCancel()}>Cancel</Button>
              </FormGroup>
            </Col>
          </Row>

        </Form>
    )
  }

}

export default AdminUserForm;