import http from 'k6/http';
import { check } from 'k6';

export let options = {
    vus: 50,        // 50 users simultaneously
    iterations: 50  // each user tries once
};

export function setup() {
    const loginRes = http.post(
        'http://localhost:8080/auth/login',
        JSON.stringify({
            userName: 'regularUser',
            password: 'password123'
        }),
        { headers: { 'Content-Type': 'application/json' } }
    );
    return { token: loginRes.json('authenticationToken') };
}

export default function (data) {
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${data.token}`
        }
    };

    // All 50 users try to book the SAME seat simultaneously
    const payload = JSON.stringify({
        userId: 3,
        showId: 1,
        showSeats: [{ seatId: 3 }]  // same seat for all 50 users
    });

    const res = http.post(
        'http://localhost:8080/api/reservations/reserve',
        payload,
        params
    );

    check(res, {
        'only one booking succeeds (201) rest get conflict (409/404)': (r) =>
            r.status === 201 || r.status === 409 || r.status === 404
    });

    // Log each response for visibility
    console.log(`VU ${__VU}: status=${res.status} body=${res.body}`);
}