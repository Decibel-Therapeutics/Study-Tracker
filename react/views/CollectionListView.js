import React from 'react';
import LoadingMessage from "../structure/LoadingMessage";
import ErrorMessage from "../structure/ErrorMessage";
import SideBar from "../structure/SideBar";
import NavBar from "../structure/NavBar";
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";
import {compose} from 'redux';
import {CollectionList} from "../components/collections/CollectionList";

class CollectionListView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      isLoaded: false,
      isError: false
    };
  }

  componentDidMount() {
    fetch("/api/studycollection?userId=" + this.props.user.id)
    .then(response => response.json())
    .then(userCollections => {
      fetch("/api/studycollection?public=true")
      .then(response => response.json())
      .then(publicCollections => {
        this.setState({
          userCollections,
          publicCollections,
          isLoaded: true
        })
      });
    })
    .catch(error => {
      console.error(error);
      this.setState({
        isError: true,
        error: error
      });
    });
  }

  render() {

    let content = <LoadingMessage />;

    try {
      if (this.state.isError) {
        content = <ErrorMessage/>;
      } else if (this.state.isLoaded) {
        content = <CollectionList
            myCollections={this.state.userCollections}
            publicCollections={this.state.publicCollections}
            user={this.props.user}
        /> ;

      }
    } catch (e) {
      console.error(e);
      content = <ErrorMessage />;
    }

    return (
        <React.Fragment>
          <div className="wrapper">
            <SideBar />
            <div className="main">
              <NavBar user={this.props.user} />
              <div className="content">
                {content}
              </div>
            </div>
          </div>
        </React.Fragment>
    )

  }

}

export default compose(
    withRouter,
    connect(
        store => ({
          user: store.user
        })
    )
)(CollectionListView);