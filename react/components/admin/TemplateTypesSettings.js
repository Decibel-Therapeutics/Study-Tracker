import React, { useEffect, useState } from 'react';

import { Card, CardHeader, CardBody, CardTitle, Button, Badge } from 'reactstrap';
import {CheckCircle, Edit, PlusCircle, Trash} from "react-feather";
import { Link } from 'react-router-dom';

import ToolkitProvider from "react-bootstrap-table2-toolkit";
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";

export const TemplateTypesSettings = () => {
    const [templateTypes, setTemplateTypes] = useState([]);
    const [error, setError] = useState();
    const templateTableColumns = [
        {
            dataField: 'name',
            text: 'Name',
            sort: true,
        },
        {
            dataField: 'templateId',
            text: 'Template ID',
        },
        {
            dataField: 'active',
            text: 'Active',
            formatter: (cell, row) => {
                return row.active
                    ? <Badge color="success">Active</Badge>
                    : <Badge color="warning">Inactive</Badge>
            }
        },
        {
            dataField: 'actions',
            text: 'Actions',
            formatter: (cell, row) => {
                return (
                    <>
                        <Link
                            title={"Edit assay type"}
                            className="text-warning"
                            to={`/template-types/${row.id}/edit`}
                        >
                            <Edit className="align-middle mr-1" size={18} />
                        </Link>
                    </>
                )
            }
        }
    ]

    useEffect(() => {
        fetch('/api/entryTemplate')
            .then(rs => rs.json())
            .then(json => setTemplateTypes(json))
            .catch(error => setError(error));
    }, []);

    return (
        <TemplateTypesList
            templateTypes={ templateTypes }
            templateTableColumns={ templateTableColumns }
        />
    )
}

export const TemplateTypesList = ({ templateTypes, templateTableColumns }) => {
    return (
        <Card>
            <CardHeader>
                Template Types

                <span className="float-right">
                  <Button
                      color="primary"
                      onClick={() => history.push("/template-types/new")}
                  >
                    New Temlate Type
                    &nbsp;
                    <PlusCircle className="feather align-middle ml-2 mb-1"/>
                  </Button>
                </span>
            </CardHeader>

            <CardBody>
                <ToolkitProvider
                    keyField="id"
                    data={ templateTypes }
                    columns={ templateTableColumns }
                >
                    {props => (
                        <div>
                            <BootstrapTable
                                bootstrap4
                                keyField="id"
                                bordered={false}
                                pagination={paginationFactory({
                                    sizePerPage: 10,
                                    sizePerPageList: [10, 20, 40, 80]
                                })}
                                defaultSorted={[{
                                    dataField: "name",
                                    order: "asc"
                                }]}
                                {...props.baseProps}
                            >
                            </BootstrapTable>
                        </div>
                    )}
                </ToolkitProvider>
            </CardBody>
        </Card>
    )
}

export default TemplateTypesSettings;
