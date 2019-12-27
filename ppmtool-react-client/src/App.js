import React from "react";
import "./App.css";
import Dashboard from "./components/Dashboard";
import Header from "./components/layout/Header";
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import AddProject from "./components/Project/AddProject";
import { Provider } from "react-redux";
import store from "./store";
import UpdateProject from "./components/Project/UpdateProject";
import ProjectBoard from "./components/ProjectBoard/ProjectBoard";
import AddProjectTask from "./components/ProjectBoard/ProjectTasks/AddProjectTask";
import UpdateProjectTask from "./components/ProjectBoard/ProjectTasks/UpdateProjectTask";
import Landing from "./components/layout/Landing";
import Register from "./components/UserManagement/Register";
import Login from "./components/UserManagement/Login";
import jwt_decode from "jwt-decode";
import setJWToken from "./securityUtils/setJWToken";
import { SET_CURRENT_USER } from "./actions/types";
import { logout } from "./actions/SecurityAction";
import SecureRoute from "./securityUtils/SecureRoute";

const jwtoken = localStorage.jwtToken;

if (jwtoken) {
    setJWToken(jwtoken);
    const decoded = jwt_decode(jwtoken);
    store.dispatch({
        type: SET_CURRENT_USER,
        payload: decoded
    });

    const currentTime = Date.now() / 1000;
    if (decoded.exp < currentTime) {
        //handle logout
        store.dispatch(logout());
        window.location.href = "/";
    }
}

function App() {
    return (
        <Provider store={store}>
            <Router>
                <div className="App">
                    <Header />
                    {
                        //Public Routes
                    }
                    <Route exact path="/" component={Landing} />
                    <Route exact path="/register" component={Register} />
                    <Route exact path="/login" component={Login} />

                    {
                        //Private Routes
                    }
                    <Switch>
                        <SecureRoute
                            exact
                            path="/dashboard"
                            component={Dashboard}
                        />
                        <SecureRoute
                            exact
                            path="/addProject"
                            component={AddProject}
                        />
                        <SecureRoute
                            exact
                            path="/updateProject/:id"
                            component={UpdateProject}
                        />
                        <SecureRoute
                            exact
                            path="/projectBoard/:id"
                            component={ProjectBoard}
                        />
                        <SecureRoute
                            exact
                            path="/addProjectTask/:id"
                            component={AddProjectTask}
                        />
                        <SecureRoute
                            exact
                            path="/updateProjectTask/:backlog_id/:pt_id"
                            component={UpdateProjectTask}
                        />
                    </Switch>
                </div>
            </Router>
        </Provider>
    );
}

export default App;
