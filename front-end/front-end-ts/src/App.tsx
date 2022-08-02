import React from 'react';
// import logo from './logo.svg';
import './App.css';
import Test from '~/views/components/Test'

// const Title = ({ text }) => (
const Title: React.FC<{ text: string }> = ({ text }) => (
  <h1>{text}</h1>
)

if (process.env.ONLY_DEV) {
  console.log('This is only test be better development');
}


function App() {
  return (
    <div className="App">
      <header className="App-header">
        <Test />
        <Title text="Hello World!!" />
        <h2>{process.env.API_URL}</h2>

        {/* <img src={logo} className="App-logo" alt="logo" /> */}
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
