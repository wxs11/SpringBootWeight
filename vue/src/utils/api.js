import request from "./request";

export function getBzWt(id) {
    return request({
        url: 'msg/getBzWt',
        method: 'get',
        params: id
    })
}