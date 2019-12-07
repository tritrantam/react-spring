import { combineReducers } from "redux";
import ErrorReducer from "./ErrorReducer";
import ProjectReducer from "./ProjectReducer";

export default combineReducers({
    errors: ErrorReducer,
    projects: ProjectReducer
});
