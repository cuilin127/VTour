let slideIndex = 0;
let playing = false;
let slideshowInterval;

// Initially, show the first slide
showSlide(0);

document.getElementById('view-all').addEventListener('click', function() {
    var wrappers = document.querySelectorAll('.image-wrapper');
    for (var i = 0; i < wrappers.length; i++) {
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
    if (playing) {
        clearInterval(slideshowInterval);
        document.getElementById('play').textContent = 'Play';
        var audio = document.getElementById("myAudio");
        audio.pause();
    } else {
        slideshowInterval = setInterval(nextSlide, 2000); // 2 seconds per slide
        document.getElementById('play').textContent = 'Pause';
        var audio = document.getElementById("myAudio");
        audio.play();
    }
    playing = !playing;
}
function toggleMenu() {
    const navList = document.getElementById('nav-list');
    navList.classList.toggle('active');
}
togglePlay();