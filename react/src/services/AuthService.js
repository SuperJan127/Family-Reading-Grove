import axios from 'axios';

/**
 * This service class is used to interact with the server's Authentication API.
 * All methods return a Promise so that the calling code can handle both success and
 * error responses appropriately.
 */
export default {

  login(user) {
    return axios.post('/login', user);
  },

  register({ username, password, confirmPassword, role, newFamilyName }) {
    return axios.post('/register', {
      username,
      password,
      confirmPassword,
      role,
      newFamilyName
    });
  },

  getUserProfile(userId) {
    return axios.get(`/users/${userId}`);
  },

  addFamilyMember(familyId, memberData) {
    return axios.post(`/families/${familyId}/members`, memberData);
  },
  
}
