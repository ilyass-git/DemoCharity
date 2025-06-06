// Function to get the JWT token from localStorage
function getToken() {
    return localStorage.getItem('token');
}

// Function to check if user is authenticated
function isAuthenticated() {
    return !!getToken();
}

// Function to handle OAuth2 login success
async function handleOAuth2Success(response) {
    try {
        const data = await response.json();
        if (data.token) {
            localStorage.setItem('token', data.token);
    window.location.href = '/';
        }
    } catch (error) {
        console.error('Error handling OAuth2 success:', error);
        window.location.href = '/login?error=Authentication+failed';
    }
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

// Function to logout
function logout() {
    localStorage.removeItem('token');
    window.location.href = '/';
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

// Check for OAuth2 response on page load
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const oauth2Response = urlParams.get('oauth2_response');
    
    if (oauth2Response) {
        handleOAuth2Success(oauth2Response);
    }
    
    updateAuthUI();
}); 