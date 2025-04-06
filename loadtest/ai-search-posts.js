import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '10s',
};

export default function() {
    const url = 'http://localhost:8080/api/ai-search-posts';
    const payload = JSON.stringify({
        question: '사랑에 관한 아카이브 보여줘.'
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