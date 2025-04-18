// Function to get the JWT token from localStorage
function getToken() {
    return localStorage.getItem('token');
}

// Function to check if user is authenticated
function isAuthenticated() {
    return !!getToken();
}

// Function to handle logout
function logout() {
    localStorage.removeItem('token');
    window.location.href = '/';
}

// Function to update UI based on authentication status
function updateAuthUI() {
    const token = getToken();
    const loginButton = document.getElementById('loginButton');
    const logoutButton = document.getElementById('logoutButton');
    
    if (token) {
        if (loginButton) loginButton.classList.add('d-none');
        if (logoutButton) logoutButton.classList.remove('d-none');
    } else {
        if (loginButton) loginButton.classList.remove('d-none');
        if (logoutButton) logoutButton.classList.add('d-none');
    }
}

// Liste des endpoints publics qui ne nécessitent pas d'authentification
const publicEndpoints = [
    '/api/actions',
    '/api/categories',
    '/api/organisations',
    '/api/auth',
    '/api/test'
];

// Vérifie si un endpoint est public
function isPublicEndpoint(url) {
    return publicEndpoints.some(endpoint => url.includes(endpoint));
}

// Add token to all fetch requests
const originalFetch = window.fetch;
window.fetch = function(url, options = {}) {
    const token = getToken();
    if (token && !isPublicEndpoint(url)) {
        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${token}`
        };
    }
    return originalFetch(url, options);
};

// Add token to all XMLHttpRequest requests
const originalXHROpen = XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open = function(method, url, ...args) {
    const token = getToken();
    if (token && !isPublicEndpoint(url)) {
        this.setRequestHeader('Authorization', `Bearer ${token}`);
    }
    return originalXHROpen.call(this, method, url, ...args);
};

// Initialize auth UI when DOM is loaded
document.addEventListener('DOMContentLoaded', updateAuthUI); 