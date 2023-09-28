import React, { useState, useEffect } from "react";
import axios from "axios";

interface UserData {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  userName: string;
  password: string;
  userAccess: string;
}

function Admin() {
  const [users, setUsers] = useState<UserData[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [selectedUser, setSelectedUser] = useState<UserData | null>(null);
  const [editedUser, setEditedUser] = useState<UserData | null>(null);

  const handleUserAccessChange = (id: number, newAccess: string) => {
    // Update the userAccess for the specific user
    const updatedUsers = users.map((user) =>
      user.id === id ? { ...user, userAccess: newAccess } : user,
    );

    setUsers(updatedUsers);
  };

  const handleEditUser = async (user: UserData) => {
    setEditedUser(user);
    setSelectedUserId(user.id);
  };


  const handleSaveUser = async (user: UserData) => {
    try {
      await axios.post(`/api/v1/admin/users/${user.id}/update?userAccess=${user.userAccess}`);
      setSelectedUserId(null);
    } catch (error) {
      console.error("API request error:", error);
    }
  };


  useEffect(() => {
    axios
      .get("/api/v1/admin/fetch/users")
      .then((response) => {
        setUsers(response.data);
        setIsLoading(false);
      })
      .catch((error) => {
        console.error("API request error:", error);
      });
  }, []);

  if (isLoading) {
    return <div className="loding"> Loading...</div>;
  }

  return (
    <div className="dashboard">
      <div className="title">Admin Dashboard</div>
      <div className="admin-container">
        <h2>User Management</h2>
        <table className="user-table">
          <thead>
            <tr>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Email</th>
              <th>Username</th>
              <th>Password</th>
              <th>User Access (Editable)</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.firstName}</td>
                <td>{user.lastName}</td>
                <td>{user.email}</td>
                <td>{user.userName}</td>
                <td>{user.password}</td>
                <td>
                  {selectedUserId === user.id ? (
                    <select
                      value={user.userAccess}
                      onChange={(e) =>
                        handleUserAccessChange(user.id, e.target.value)
                      }
                    >
                      <option value="PERMITTED">PERMITTED</option>
                      <option value="RESTRICTED">RESTRICTED</option>
                    </select>
                  ) : (
                    user.userAccess
                  )}
                </td>
                <td>
                  {selectedUserId === user.id ? (
                    <button onClick={() => handleSaveUser(user)}>Save</button>
                  ) : (
                    <button onClick={() => handleEditUser(user)}>Edit</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default Admin;
