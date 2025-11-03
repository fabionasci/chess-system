// File: src/main/resources/static/chess/chess.js
const PIECE_SYMBOL = {
    'K_WHITE': '♔','Q_WHITE':'♕','R_WHITE':'♖','B_WHITE':'♗','N_WHITE':'♘','P_WHITE':'♙',
    'K_BLACK': '♚','Q_BLACK':'♛','R_BLACK':'♜','B_BLACK':'♝','N_BLACK':'♞','P_BLACK':'♟'
};

function pieceToSymbol(piece, row) {
    if (!piece) return '';

    // objeto { type, color }
    if (typeof piece === 'object') {
        const type = (piece.type || '').toUpperCase();
        const color = (piece.color || '').toUpperCase();
        const key = `${type}_${color}`;
        return PIECE_SYMBOL[key] || '';
    }

    // string
    const token = String(piece).trim().toUpperCase();

    // formato já com cor, ex: "P_WHITE" ou "N_BLACK"
    if (token.includes('_')) {
        const [type, color] = token.split('_');
        const key = `${type}_${color}`;
        return PIECE_SYMBOL[key] || '';
    }

    // token de uma letra: inferir cor pela linha
    const type = token; // 'R', 'N', 'B', 'Q', 'K', 'P'
    let color = '';
    if (row <= 1) color = 'BLACK';    // linhas 0 e 1 = peças pretas
    else if (row >= 6) color = 'WHITE'; // linhas 6 e 7 = peças brancas
    else return ''; // meio do tabuleiro: ambíguo -> vazio

    return PIECE_SYMBOL[`${type}_${color}`] || '';
}

function createRankLabels() {
    const ranks = document.getElementById('rank-labels');
    ranks.innerHTML = '';
    // row 0 = topo = rank 8, row 7 = base = rank 1
    for (let row = 0; row < 8; row++) {
        const div = document.createElement('div');
        div.className = 'rank-label';
        div.textContent = 8 - row;
        ranks.appendChild(div);
    }
}

function createFileLabels() {
    const fileLabels = document.getElementById('file-labels');
    fileLabels.innerHTML = '';
    for (let c = 0; c < 8; c++) {
        const div = document.createElement('div');
        div.className = 'file-label';
        div.textContent = String.fromCharCode('a'.charCodeAt(0) + c);
        fileLabels.appendChild(div);
    }
}

function renderBoard(matrix) {
    const board = document.getElementById('board');
    const status = document.getElementById('status');
    board.innerHTML = '';

    if (!Array.isArray(matrix) || matrix.length !== 8) {
        status.textContent = 'Resposta inválida da API.';
        return;
    }

    // matrix[row][col] with row=0 top -> rank 8
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const sq = document.createElement('div');
            sq.className = 'square ' + (((row + col) % 2 === 0) ? 'light' : 'dark');
            sq.dataset.row = row;
            sq.dataset.col = col;

            const cell = document.createElement('div');
            cell.className = 'cell';
            const piece = matrix[row][col];
            cell.textContent = pieceToSymbol(piece, row);
            sq.appendChild(cell);

            board.appendChild(sq);
        }
    }
    status.textContent = 'Tabuleiro carregado.';
}

function loadBoard() {
    createRankLabels();
    createFileLabels();
    fetch('/chess/pieces')
        .then(resp => {
            if (!resp.ok) throw new Error('HTTP ' + resp.status);
            return resp.json();
        })
        .then(json => renderBoard(json))
        .catch(err => {
            document.getElementById('status').textContent = 'Erro: ' + err.message;
        });
}

document.addEventListener('DOMContentLoaded', loadBoard);
