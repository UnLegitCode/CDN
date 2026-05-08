document.addEventListener('DOMContentLoaded', () => {
    const video = document.getElementById('mainVideo');

    video.addEventListener('play', () => {
        if (video.muted) video.muted = false;
    }, {once: true});
});