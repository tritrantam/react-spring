import React, { Component } from "react";
import { Link } from "react-router-dom";
import Backlog from "./Backlog";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { getBacklog } from "../../actions/BacklogAction";

class ProjectBoard extends Component {
    componentDidMount() {
        const { id } = this.props.match.params;
        this.props.getBacklog(id);
    }

    render() {
        const { project_tasks } = this.props.backlog;
        const { id } = this.props.match.params;
        return (
            <div className="container">
                <Link
                    to={`/addProjectTask/${id}`}
                    className="btn btn-primary mb-3"
                >
                    <i className="fas fa-plus-circle"> Create Project Task</i>
                </Link>
                <br />
                <hr />
                <Backlog project_tasks_props={project_tasks} />
            </div>
        );
    }
}

ProjectBoard.propTypes = {
    backlog: PropTypes.object.isRequired,
    getBacklog: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    backlog: state.backlogs
});
export default connect(mapStateToProps, { getBacklog })(ProjectBoard);
