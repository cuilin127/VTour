let slideIndex = 0;
let playing = false;
let slideshowInterval;

// Initially, show the first slide
showSlide(0);
document.addEventListener('DOMContentLoaded', function() {
    // Show the loading indicator as soon as the DOM is ready
    document.getElementById('loadingIndicator').style.display = 'flex';

    // Hide the loading indicator and show the content after 2 seconds
    setTimeout(function() {
        document.getElementById('loadingIndicator').style.display = 'none';
        document.getElementById('content').style.display = 'block';
    }, 2000);
});
document.getElementById('view-all').addEventListener('click', function() {
    const wrappers = document.querySelectorAll('.image-wrapper');
    for (let i = 0; i < wrappers.length; i++) {
        wrappers[i].style.display = 'block';
    }
    this.style.display = 'none';
});
function showSlide(index) {
    const slides = document.querySelectorAll('.slide');
    if (index >= slides.length) { slideIndex = 0 }
    if (index < 0) { slideIndex = slides.length - 1 }
    for (let slide of slides) {
        slide.style.display = 'none';
    }
    slides[slideIndex].style.display = 'block';
}

function nextSlide() {
    showSlide(++slideIndex);
}

function prevSlide() {
    showSlide(--slideIndex);
}

function togglePlay() {
    let audio;
    if (playing) {
        clearInterval(slideshowInterval);
        document.getElementById('play').textContent = 'Play';
        audio = document.getElementById("myAudio");
        audio.pause();
    } else {
        slideshowInterval = setInterval(nextSlide, 2000); // 2 seconds per slide
        document.getElementById('play').textContent = 'Pause';
        audio = document.getElementById("myAudio");
        audio.play();
    }
    playing = !playing;
}
function toggleMenu() {
    const navList = document.getElementById('nav-list');
    navList.classList.toggle('active');
}
togglePlay();