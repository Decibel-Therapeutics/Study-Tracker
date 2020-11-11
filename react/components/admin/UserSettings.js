import React from 'react';
import {Button, Card, CardBody, CardHeader, CardTitle} from 'reactstrap';
import AdminUserTable from "./AdminUserTable";
import {UserPlus} from 'react-feather';
import AdminUserForm from "./AdminUserForm";

class UserSettings extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      users: [],
      isLoaded: false,
      isError: false,
      showForm: false,
      showDetails: false,
      selectedUser: null
    };
    this.toggleForm = this.toggleForm.bind(this);
  }

  componentDidMount() {
    fetch("/api/user")
    .then(response => response.json())
    .then(async users => {
      this.setState({
        users: users,
        isLoaded: true
      })
    })
    .catch(error => {
      console.error(error);
      this.setState({
        isError: true,
        error: error
      });
    });
  }

  toggleForm() {
    this.setState({
      showForm: !this.state.showForm
    })
  }

  render() {

    return (
        <React.Fragment>

          <Card>
            <CardHeader>
              <CardTitle tag="h5" className="mb-0">
                Registered Users
                <span className="float-right">
                          <Button color={"primary"} onClick={this.toggleForm}>
                            New User <UserPlus
                              className="feather align-middle ml-2 mb-1"/>
                          </Button>
                        </span>
              </CardTitle>
            </CardHeader>
            <CardBody>
              <AdminUserTable users={this.state.users}/>
            </CardBody>
          </Card>

          {
            !!this.state.showForm
                ? (
                    <Card>
                      <CardHeader>
                        <CardTitle tag="h5">
                          {!!this.props.user ? "Edit User" : "New User"}
                        </CardTitle>
                        <h6 className="card-subtitle text-muted">
                          Users must have unique usernames and email addresses.
                          Users
                          granted admin privileges can create or modify programs,
                          users, and other system attributes. New users will be
                          required to create a new password when they sign in the
                          first time.
                        </h6>
                      </CardHeader>
                      <CardBody>
                        <AdminUserForm
                            user={this.props.user}
                            users={this.state.users}
                        />
                      </CardBody>
                    </Card>
                )
                : ''
          }

        </React.Fragment>
    );
  }

}

export default UserSettings;