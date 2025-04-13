document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            // Afficher le spinner de chargement
            const submitButton = loginForm.querySelector('button[type="submit"]');
            const originalButtonText = submitButton.innerHTML;
            submitButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Connexion...';
            submitButton.disabled = true;
            
            // Réinitialiser les messages d'erreur
            const errorDiv = document.getElementById('loginError');
            const successDiv = document.getElementById('loginSuccess');
            if (errorDiv) {
                errorDiv.style.display = 'none';
            }
            if (successDiv) {
                successDiv.style.display = 'none';
            }
            
            fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Email ou mot de passe incorrect');
                }
                return response.json();
            })
            .then(data => {
                // Stocker le token JWT
                localStorage.setItem('token', data.token);
                
                // Afficher le message de succès
                if (successDiv) {
                    successDiv.textContent = 'Connexion réussie ! Redirection...';
                    successDiv.style.display = 'block';
                    successDiv.classList.add('alert-success');
                }
                
                // Désactiver le formulaire
                loginForm.querySelectorAll('input').forEach(input => input.disabled = true);
                submitButton.disabled = true;
                
                // Rediriger vers la page d'accueil après 2 secondes
                setTimeout(() => {
                    window.location.href = '/';
                }, 2000);
            })
            .catch(error => {
                // Afficher le message d'erreur
                if (errorDiv) {
                    errorDiv.textContent = error.message;
                    errorDiv.style.display = 'block';
                    errorDiv.classList.add('alert-danger');
                }
                
                // Réactiver le bouton de soumission
                submitButton.innerHTML = originalButtonText;
                submitButton.disabled = false;
            });
        });
    }
}); 