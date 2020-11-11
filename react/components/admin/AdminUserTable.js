import React from 'react';
import {Button} from "reactstrap";
import {File} from "react-feather";
import ToolkitProvider, {Search} from "react-bootstrap-table2-toolkit";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";

const columns = [
  {
    dataField: "username",
    text: "Username",
    sort: true,
    // headerStyle: {width: '20%%'},
    formatter: (c, d, i, x) => {
      return (
          <a href={"#"}>{d.username}</a>
      )
    },
    sortFunc: (a, b, order, dataField, rowA, rowB) => {
      if (rowA.username > rowB.username) {
        return order === "desc" ? -1 : 1;
      }
      if (rowB.username > rowA.username) {
        return order === "desc" ? 1 : -1;
      }
      return 0;
    },
  },
  {
    dataField: "displayName",
    text: "Display Name",
    sort: true,
    // headerStyle: {width: '20%%'},
    formatter: (c, d, i, x) => {
      return (
          <a href={"#"}>{d.displayName}</a>
      )
    },
    sortFunc: (a, b, order, dataField, rowA, rowB) => {
      if (rowA.displayName > rowB.displayName) {
        return order === "desc" ? -1 : 1;
      }
      if (rowB.displayName > rowA.displayName) {
        return order === "desc" ? 1 : -1;
      }
      return 0;
    },
  },
  {
    dataField: "email",
    text: "Email",
    sort: true,
    // headerStyle: {width: '20%'},
    formatter: (cell, d, index, x) => d.email
  },
  {
    dataField: "admin",
    text: "Admin",
    sort: true,
    // headerStyle: {width: '10%'},
    formatter: (c, d, i, x) => {
      if (d.admin) {
        return (
            <div className="badge badge-danger">
              Admin
            </div>
        )
      }
    }
  },
  {
    dataField: "active",
    text: "Active",
    sort: true,
    // headerStyle: {width: '10%'},
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

const AdminUserTable = ({users}) => {
  return (
      <ToolkitProvider
          keyField="id"
          data={users}
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
  )
}

export default AdminUserTable;