import React from 'react';
import {
    Form, Input, Select, Button, DatePicker, message, Card
} from 'antd';
import {API_ROOT, ORDER_NUM, USER_ID} from '../constants';
import { List, Avatar, Icon } from 'antd';
export class UserProfile extends React.Component{
    constructor(props) {
        fetch(`${API_ROOT}/profile?user_id=${ localStorage.getItem(USER_ID)}`, {
            method: 'GET',
        }).then((response) => {
            if (response.ok) {
                console.log("success", response)
                return response.json();
            }
            throw new Error(response.statusText);
        })
            .then((data) => {
                console.log(data);
                // message.success('Sending Succeed!');
                this.setState({userInfo: data});
                //console.log(this.state.routes);
                // this.props.handleResponse(data);
                //this.props.history.push('/payment');
            })
            .catch((e) => {
                console.log(e);
                message.error('Getting User Information Failed.');
            });
        super(props);

    }

    state = {
        userInfo: [

        ] }

    render() {

        return(
            <div className="user-info">
                {this.state.userInfo.first_name != undefined ?<h2>{`Welcome back, ${this.state.userInfo.first_name} `}</h2> : null}


            </div>
        );
    }
}