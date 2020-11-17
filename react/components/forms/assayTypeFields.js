import React from 'react';
import {Button, Card, CardBody, CardHeader, CardTitle, Table} from 'reactstrap'
import {Delete, Edit} from 'react-feather'

const FieldCard = ({field}) => {
  return (
      <Card className="mb-3 bg-light cursor-grab border">
        <CardHeader>
          <div className="float-right mr-n2">
            {!!field.required ? <span
                className="badge badge-info">Required</span> : ""}
          </div>
          <CardTitle>{field.displayName} (<code
              className="text-muted">{field.fieldName}</code>)</CardTitle>
        </CardHeader>
        <CardBody className="p-3">
          <p className="text-muted font-weight-bolder">
            {field.type}
          </p>
          <p>
            {field.description}
          </p>
          <Button color={"warning"} onClick={() => console.log("Edit")}>
            <Edit/>
          </Button>
          &nbsp;
          <Button color={"danger"} onClick={() => console.log("Delete")}>
            <Delete/>
          </Button>
        </CardBody>
      </Card>
  )
};

const FieldCards = ({fields}) => {
  const cards = fields.map(
      f => <FieldCard key={'field-card-' + f.fieldName} field={f}/>)
  return (
      <React.Fragment>
        {cards}
      </React.Fragment>
  )
}

const FieldTable = ({fields}) => {
  const rows = fields.map(f => {
    return (
        <tr>
          <td>{f.displayName}</td>
          <td>{f.fieldName}</td>
          <td>{f.type}</td>
          <td>{f.description}</td>
          <td>{f.required ? "Yes" : "No"}</td>
        </tr>
    )
  });
  return (
      <Table striped>
        <thead>
        <tr>
          <th>Display Name</th>
          <th>Field Name</th>
          <th>Field Type</th>
          <th>Description</th>
          <th>Required</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        {rows}
        </tbody>
      </Table>
  )
}

export class AssayTypeFields extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      fields: props.fields || []
    }
  }

  render() {

    return (
        <FieldCards fields={this.state.fields}/>
    )
  }

}

