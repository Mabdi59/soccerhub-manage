import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: (credentials) => apiClient.post('/auth/login', credentials),
  register: (userData) => apiClient.post('/auth/register', userData),
};

// Organizations API
export const organizationsAPI = {
  getAll: () => apiClient.get('/organizations'),
  getById: (id) => apiClient.get(`/organizations/${id}`),
  create: (data) => apiClient.post('/organizations', data),
  update: (id, data) => apiClient.put(`/organizations/${id}`, data),
  delete: (id) => apiClient.delete(`/organizations/${id}`),
};

// Tournaments API
export const tournamentsAPI = {
  getAll: (organizationId) => apiClient.get('/tournaments', { params: { organizationId } }),
  getById: (id) => apiClient.get(`/tournaments/${id}`),
  create: (data) => apiClient.post('/tournaments', data),
  update: (id, data) => apiClient.put(`/tournaments/${id}`, data),
  delete: (id) => apiClient.delete(`/tournaments/${id}`),
};

// Divisions API
export const divisionsAPI = {
  getAll: (tournamentId) => apiClient.get('/divisions', { params: { tournamentId } }),
  getById: (id) => apiClient.get(`/divisions/${id}`),
  create: (data) => apiClient.post('/divisions', data),
  update: (id, data) => apiClient.put(`/divisions/${id}`, data),
  delete: (id) => apiClient.delete(`/divisions/${id}`),
};

// Teams API
export const teamsAPI = {
  getAll: (divisionId) => apiClient.get('/teams', { params: { divisionId } }),
  getById: (id) => apiClient.get(`/teams/${id}`),
  create: (data) => apiClient.post('/teams', data),
  update: (id, data) => apiClient.put(`/teams/${id}`, data),
  delete: (id) => apiClient.delete(`/teams/${id}`),
};

// Players API
export const playersAPI = {
  getAll: (teamId) => apiClient.get('/players', { params: { teamId } }),
  getById: (id) => apiClient.get(`/players/${id}`),
  create: (data) => apiClient.post('/players', data),
  update: (id, data) => apiClient.put(`/players/${id}`, data),
  delete: (id) => apiClient.delete(`/players/${id}`),
};

// Venues API
export const venuesAPI = {
  getAll: () => apiClient.get('/venues'),
  getById: (id) => apiClient.get(`/venues/${id}`),
  create: (data) => apiClient.post('/venues', data),
  update: (id, data) => apiClient.put(`/venues/${id}`, data),
  delete: (id) => apiClient.delete(`/venues/${id}`),
};

// Matches API
export const matchesAPI = {
  getAll: (params) => apiClient.get('/matches', { params }),
  getById: (id) => apiClient.get(`/matches/${id}`),
  create: (data) => apiClient.post('/matches', data),
  update: (id, data) => apiClient.put(`/matches/${id}`, data),
  updateResult: (id, result) => apiClient.patch(`/matches/${id}/result`, result),
  delete: (id) => apiClient.delete(`/matches/${id}`),
};

// Standings API
export const standingsAPI = {
  getByDivision: (divisionId) => apiClient.get('/standings', { params: { divisionId } }),
};

export default apiClient;
