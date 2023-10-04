const HeroVideo = {
    selectors: {
        self: '[data-cmp-component="herovideo"]',
        videoElement: 'video',
        soundControlBtn: 'button',
        soundControlText: 'button span',
    },
    init: function() {
        const {selectors} = this;
        const videoSections= document.querySelectorAll(selectors.self);
        videoSections.forEach(section => {
            const video = section.querySelector(selectors.videoElement);
            const  soundControl = section.querySelector(selectors.soundControlBtn);
            const soundControlText = section.querySelector(selectors.soundControlText);
            if (soundControl) {
                soundControl.addEventListener('click', function(){
                    video.muted = !video.muted;
                    if (video.muted) {
                        soundControl.classList.remove("btn-soundControl--mute");
                        soundControl.classList.add("btn-soundControl--unmute");
                        soundControlText.textContent="Unmute the video";
                    } else {
                        soundControl.classList.add("btn-soundControl--mute");
                        soundControl.classList.remove("btn-soundControl--unmute");
                        soundControlText.textContent="Mute the video";
                    }
                });
            }

        });
    }
};

export default HeroVideo;