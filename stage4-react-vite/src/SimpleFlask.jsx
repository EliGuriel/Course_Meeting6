import React, { useState } from 'react';
import { useAuth } from './AuthContext';

const SimpleFlask = () => {
    const { authenticatedFetch } = useAuth();
    const [response, setResponse] = useState('');
    const [inputText, setInputText] = useState('Hello Flask!');

    const handleGet = async () => {
        try {
            const res = await authenticatedFetch('http://localhost:8080/api/simple/hello');
            const data = await res.json();
            setResponse(JSON.stringify(data, null, 2));
        } catch (error) {
            setResponse('Error: ' + error.message);
        }
    };

    const handlePost = async () => {
        try {
            const res = await authenticatedFetch('http://localhost:8080/api/simple/data', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: inputText })
            });
            const data = await res.json();
            setResponse(JSON.stringify(data, null, 2));
        } catch (error) {
            setResponse('Error: ' + error.message);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>Simple Flask Test</h2>

            <button onClick={handleGet} style={{ margin: '10px' }}>
                GET from Flask
            </button>

            <div>
                <input
                    value={inputText}
                    onChange={(e) => setInputText(e.target.value)}
                    style={{ margin: '10px', padding: '5px' }}
                />
                <button onClick={handlePost}>
                    POST to Flask
                </button>
            </div>

            <div style={{
                marginTop: '20px',
                padding: '10px',
                backgroundColor: '#f5f5f5',
                whiteSpace: 'pre-wrap'
            }}>
                {response}
            </div>
        </div>
    );
};

export default SimpleFlask;