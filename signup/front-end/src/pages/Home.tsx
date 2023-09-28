import { Outlet, Link } from "react-router-dom";

function Home() {
    return (
        <div className="app">
            <div className="nav-container">
                <div className="dropdown">
                    <button className="dropbtn">Menu</button>
                    <div className="dropdown-content">
                        <Link to="/signup">Signup</Link>
                        <Link to="/login">Login</Link>
                        <Link to="/admin">Admin</Link>
                    </div>
                </div>
                <h2>Welcome to Registration</h2>
            </div>
            <Outlet />
        </div>
    );
}

export default Home;