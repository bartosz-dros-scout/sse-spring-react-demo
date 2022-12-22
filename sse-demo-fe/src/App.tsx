import React, {useState} from 'react';
import './App.css';
import axios from 'axios';

function App() {
    const [requestLog, setRequestLog] = useState<string[]>([])

    const addLog = (message: string) => {
        setRequestLog(prevState => {
            const date = new Date()
            return [date.toUTCString() + ' ' + message, ...prevState.slice(0, 9)]
        })
    }

    const [subscriptions, setSubscriptions] = useState<EventSource[]>([])

    const addSubscription = (subscription: EventSource) => {
        setSubscriptions(prevState => [...prevState, subscription])
    }

    const clearSubscriptions = () => {
        subscriptions.forEach(s => s.close());
        setSubscriptions([]);
    }

    const axiosLong = () => {
        addLog('Starting axios long request')
        axios.get("http://localhost:8090/long")
            .then((response) => addLog('Long Response: ' + JSON.stringify(response.data)))
    }

    const eventSourceLong = () => {
        addLog('Started eventSource connection')
        const subscription = new EventSource('http://localhost:8090/long')
        subscription.onmessage = (data) => {
            addLog('Message from eventSource ' + data.data)
        }
        subscription.onerror = (event) => {
            console.log(event);
            subscription.close()
        }
    }

    const subscribe = () => {
        addLog('Started infinite subscription')
        const subscription = new EventSource('http://localhost:8090/subscription')
        subscription.onmessage = (data) => {
            addLog('Message on infinite subscription ' + data.data)
        }
        subscription.onerror = (event) => {
            console.log(event);
            subscription.close()
        }
    }

    return (
        <div className="App">
            <header className="App-header">
                <p>
                    ServerSentEvent
                </p>

                <div>
                    Log:
                    {requestLog.map((r, index) => <p className="small" key={index}>{r}</p>)}
                </div>

                <button onClick={axiosLong}>Axios Long example</button>
                <button onClick={eventSourceLong}>EventSource Long example</button>

                <button onClick={subscribe}>Subscribe to notifications</button>

                <button onClick={clearSubscriptions}>Clear all subscriptions</button>

                <FormToSend/>
            </header>
        </div>
    );
}

function FormToSend() {
    const [notification, setNotification] = useState("")


    const inputNotification = (event: any) => {
        setNotification(event.target.value)
    }

    const sendNotification = () => {
        try {
            axios.post('http://localhost:8090/subscription/notify', {message: notification})
                .then(resp => setNotification(""))
        } catch (error) {
            console.log(error)
        }
    }

    const checkEnter = (event: any) => {
        if (event.key === 'Enter') {
            sendNotification()
        }
    }

    return (
        <div>
            <input onChange={inputNotification} value={notification} onKeyDown={checkEnter}/>
            <button onClick={sendNotification}>Send</button>
        </div>
    )
}

export default App;
