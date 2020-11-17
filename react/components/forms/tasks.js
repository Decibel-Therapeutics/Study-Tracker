import React from 'react';
import {
  Button,
  Card,
  CardBody,
  Col,
  FormGroup,
  Input,
  Label,
  Row
} from "reactstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlusCircle} from "@fortawesome/free-solid-svg-icons";
import {Trash} from "react-feather";
import dragula from 'react-dragula';

class TaskInputList extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  handleContainerLoaded = container => {
    if (container) {
      this.props.onContainerLoaded(container);
    }
  }

  render() {

    return (
        <div id="task-input-container" ref={this.handleContainerLoaded}>
          {this.props.children}
        </div>
    )

  }

}

const TaskInputCard = ({task, index, handleTaskUpdate, handleRemoveTaskClick}) => {
  return (
      <Card className="mb-3 bg-light cursor-grab border">
        <CardBody className="p-3">
          <Row>

            <Col xs={5}>
              <FormGroup>
                <Label></Label>
                <Input
                    type="text"
                    value={task.label}
                    onChange={(e) => handleTaskUpdate({"label": e.target.value},
                        index)}
                />
              </FormGroup>
            </Col>

            <Col xs={5}>
              <FormGroup>
                <Label></Label>
                <Input
                    type="select"
                    value={task.status}
                    onChange={(e) => {
                      handleTaskUpdate({"status": e.target.value}, index);
                    }}
                >
                  <option value="TODO">To Do</option>
                  <option value="COMPLETE">Complete</option>
                  <option value="INCOMPLETE">Incomplete</option>
                </Input>
              </FormGroup>
            </Col>

            <Col xs={2}>
              <a className="text-danger" title={"Remove task"}
                 onClick={() => handleRemoveTaskClick(index)}>
                <Trash className="align-middle mt-3" size={18}/>
              </a>
            </Col>

          </Row>
        </CardBody>
      </Card>
  )
};

export class TaskInputs extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      tasks: props.tasks.sort((a, b) => {
        if (a.order > b.order) {
          return 1;
        } else if (b.order > a.order) {
          return -1;
        }
        return 0;
      }) || []
    }

    this.containers = [];

    // this.onContainerReady = this.onContainerReady.bind(this);
    this.handleAddTaskClick = this.handleAddTaskClick.bind(this);
    this.handleRemoveTaskClick = this.handleRemoveTaskClick.bind(this);
    this.handleTaskUpdate = this.handleTaskUpdate.bind(this);
  }

  componentDidMount() {
    dragula(this.containers);
  }

  onContainerReady = container => {
    console.log(container);
    this.containers.push(container);
  };

  handleTaskUpdate(data, index) {
    let tasks = this.state.tasks;
    tasks[index] = {
      ...tasks[index],
      ...data
    };
    this.setState({
      tasks: tasks
    });
    this.props.handleUpdate(tasks);
  }

  handleAddTaskClick() {
    const newTasks = [
      ...this.state.tasks,
      {label: "", status: "TODO"}
    ];
    this.setState({
      tasks: newTasks
    });
    this.props.handleUpdate(newTasks);
  }

  handleRemoveTaskClick(index) {
    let updated = this.state.tasks;
    updated.splice(index, 1);
    this.setState({
      tasks: updated
    });
    this.props.handleUpdate(updated);
  }

  render() {

    const cards = this.props.tasks.map((task, index) => {
      return (
          <Row form key={'task-inputs-' + index} data-index={index}>
            <Col xs={12}>
              <TaskInputCard
                  task={task}
                  index={index}
                  handleRemoveTaskClick={this.handleRemoveTaskClick}
                  handleTaskUpdate={this.handleTaskUpdate}
              />
            </Col>
          </Row>
      )
    });

    return (
        <React.Fragment>

          <Row form>

            <Col xs={5}>
              <FormGroup>
                <Label>Label</Label>
              </FormGroup>
            </Col>

            <Col xs={5}>
              <FormGroup>
                <Label>Status</Label>
              </FormGroup>
            </Col>

            <Col xs={2}></Col>

          </Row>

          <TaskInputList onContainerLoaded={this.onContainerReady}>
            {cards}
          </TaskInputList>

          <Row form>
            <Col md={12}>
              <Button
                  size="lg"
                  color="info"
                  onClick={this.handleAddTaskClick}>
                <FontAwesomeIcon icon={faPlusCircle}/> Add Task
              </Button>
            </Col>
          </Row>

        </React.Fragment>
    );

  }

}