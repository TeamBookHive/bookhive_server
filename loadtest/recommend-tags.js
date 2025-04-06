import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '10s',
};

export default function() {
    const url = 'http://localhost:8080/api/recommended-tags';
    const payload = JSON.stringify({
        content: '자바에서의 상속이란, 연관있는 클래스에 대해 공통적인 구성 요소를 정의하고, 이를 대표하는 클래스를 정의하는 것을 말한다.'
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            Authorization: 'testToken',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'status was 200': (r) => r.status === 200,
    });

    sleep(1);
}