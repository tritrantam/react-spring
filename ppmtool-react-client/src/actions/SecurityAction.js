import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";
import setJWToken from "../securityUtils/setJWToken";
import jwt_decode from "jwt-decode";

export const createNewUser = (newUser, history) => async dispatch => {
    try {
        await axios.post("/api/users/register", newUser);
        history.push("/login");
        dispatch({
            type: GET_ERRORS,
            payload: {}
        });
    } catch (error) {
        dispatch({
            type: GET_ERRORS,
            payload: error.response.data
        });
    }
};

export const login = LoginRequest => async dispatch => {
    try {
        // post endpoint => login request
        const res = await axios.post("/api/users/login", LoginRequest);
        // extract token from res.data
        const { token } = res.data;
        // store token in local storage
        localStorage.setItem("jwtToken", token);
        // set our token in header
        setJWToken(token);
        // decode token on react
        const decoded = jwt_decode(token);
        // dispatch to securityReducer
        dispatch({
            type: SET_CURRENT_USER,
            payload: decoded
        });
    } catch (error) {
        dispatch({
            type: GET_ERRORS,
            payload: error.response.data
        });
    }
};

export const logout = () => dispatch => {
    localStorage.removeItem("jwtToken");
    setJWToken(false);
    dispatch({
        type: SET_CURRENT_USER,
        payload: {}
    });
};
