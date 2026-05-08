const titleInput = document.getElementById('titleInput');
const charCount = document.getElementById('charCount');

titleInput.addEventListener('input', () => {
    charCount.textContent = titleInput.value.length;
});

const previewFileInput = document.getElementById('previewFile');
const previewDropZone = document.getElementById('previewDropZone');
const previewPlaceholder = document.getElementById('previewPlaceholder');
const previewPreview = document.getElementById('previewPreview');
const previewImage = document.getElementById('previewImage');

function handlePreviewFile(file) {
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            previewImage.src = e.target.result;
            previewPlaceholder.classList.add('d-none');
            previewPreview.classList.remove('d-none');
        }
        reader.readAsDataURL(file);
    }
}

previewDropZone.addEventListener('click', () => previewFileInput.click());

previewFileInput.addEventListener('change', (e) => {
    handlePreviewFile(e.target.files[0]);
});

previewDropZone.addEventListener('dragover', (e) => {
    e.preventDefault();
    previewDropZone.classList.add('dragover');
});

previewDropZone.addEventListener('dragleave', () => {
    previewDropZone.classList.remove('dragover');
});

previewDropZone.addEventListener('drop', (e) => {
    e.preventDefault();
    previewDropZone.classList.remove('dragover');
    const file = e.dataTransfer.files[0];
    if (file && file.type.startsWith('image/')) {
        previewFileInput.files = e.dataTransfer.files;
        handlePreviewFile(file);
    }
});

const videoFileInput = document.getElementById('videoFile');
const videoDropZone = document.getElementById('videoDropZone');
const videoPlaceholder = document.getElementById('videoPlaceholder');
const videoSelected = document.getElementById('videoSelected');
const videoFileName = document.getElementById('videoFileName');

videoDropZone.addEventListener('click', () => videoFileInput.click());

videoFileInput.addEventListener('change', (e) => {
    const file = e.target.files[0];
    if (file) {
        videoPlaceholder.classList.add('d-none');
        videoSelected.classList.remove('d-none');
        videoFileName.textContent = file.name;
    }
});

videoDropZone.addEventListener('dragover', (e) => {
    e.preventDefault();
    videoDropZone.classList.add('dragover');
});

videoDropZone.addEventListener('dragleave', () => videoDropZone.classList.remove('dragover'));

videoDropZone.addEventListener('drop', (e) => {
    e.preventDefault();
    videoDropZone.classList.remove('dragover');
    const file = e.dataTransfer.files[0];
    if (file && file.type.startsWith('video/')) {
        videoFileInput.files = e.dataTransfer.files;
        videoPlaceholder.classList.add('d-none');
        videoSelected.classList.remove('d-none');
        videoFileName.textContent = file.name;
    }
});

document.getElementById('uploadForm').addEventListener('submit', function () {
    const btn = document.getElementById('submitBtn');
    btn.disabled = true;
    btn.innerHTML = `<span class="spinner-border spinner-border-sm me-2"></span> Загрузка...`;
});