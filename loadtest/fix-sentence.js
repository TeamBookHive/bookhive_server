import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '10s',
};

export default function() {
    const url = 'http://localhost:8080/api/fix-sentence';
    const payload = JSON.stringify({
        content: '조은 아침입니 다. 오늘 은 날씨가 맗습니다.'
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            Authorization: '',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'status was 200': (r) => r.status === 200,
    });

    sleep(1);
}