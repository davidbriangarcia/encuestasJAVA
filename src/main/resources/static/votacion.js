document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const encuestaId = params.get('id');

    if (!encuestaId) {
        document.getElementById('encuesta-titulo').textContent = 'Error: ID de encuesta no encontrado.';
        return;
    }

    let stompClient = null;

    function connectWebSocket() {
        // Compatibilidad con Stomp global o StompJs.Stomp
        const StompClass = window.Stomp || (window.StompJs && window.StompJs.Stomp);
        if (!StompClass) {
            alert('No se pudo cargar la librería STOMP. Revisa la consola y la carga de scripts.');
            return;
        }
        stompClient = StompClass.over(() => new SockJS('/ws'));
        stompClient.connect({}, (frame) => {
            console.log('Conectado a WebSocket: ' + frame);
        }, (error) => {
            console.error('Error de conexión WebSocket:', error);
        });
    }

    async function cargarEncuesta() {
        try {
            const response = await fetch(`/api/encuestas/${encuestaId}`);
            if (!response.ok) throw new Error('No se pudo cargar la encuesta.');
            const encuesta = await response.json();
            
            document.getElementById('encuesta-titulo').textContent = encuesta.titulo;
            const container = document.getElementById('preguntas-container');
            container.innerHTML = ''; // Limpiar

            encuesta.preguntas.forEach(pregunta => {
                const preguntaDiv = document.createElement('div');
                preguntaDiv.className = 'card mb-4';
                
                let opcionesHtml = '<ul class="list-group list-group-flush">';
                pregunta.opciones.forEach(opcion => {
                    opcionesHtml += `
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            ${opcion.texto}
                            <button class="btn btn-primary btn-sm btn-votar" data-pregunta-id="${pregunta.id}" data-opcion-id="${opcion.id}">
                                Votar
                            </button>
                        </li>`;
                });
                opcionesHtml += '</ul>';

                preguntaDiv.innerHTML = `
                    <div class="card-header">${pregunta.texto}</div>
                    ${opcionesHtml}
                `;
                container.appendChild(preguntaDiv);
            });

            document.querySelectorAll('.btn-votar').forEach(button => {
                button.addEventListener('click', enviarVoto);
            });

        } catch (error) {
            console.error(error);
            document.getElementById('encuesta-titulo').textContent = 'Error al cargar la encuesta.';
        }
    }
    
    function enviarVoto(event) {
        const button = event.currentTarget;
        const voto = {
            preguntaId: Number(button.getAttribute('data-pregunta-id')),
            opcionId: Number(button.getAttribute('data-opcion-id'))
        };
        stompClient.send(`/app/votar/${encuestaId}`, {}, JSON.stringify(voto));

        // Deshabilitar botones de la pregunta votada
        const cardBody = button.closest('.card');
        cardBody.querySelectorAll('.btn-votar').forEach(btn => {
            btn.disabled = true;
            btn.textContent = 'Votado';
        });
        button.classList.remove('btn-primary');
        button.classList.add('btn-success');
    }

    cargarEncuesta();
    connectWebSocket();
});