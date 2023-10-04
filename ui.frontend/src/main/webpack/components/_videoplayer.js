
const VideoPlayer= {
    selectors: {
        self: '[data-cmp-component="videoplayer"]',
        videoElement: 'video',
        videoControlBtn: 'button.btn-videoControl',
        videoControlText: 'button.btn-videoControl span',
        soundControlBtn: 'button.btn-soundControl',
        soundControlText: 'button.btn-soundControl span',
    },
    init: function () {
        const {selectors} = this;
        const videoSections= document.querySelectorAll(selectors.self);
        videoSections.forEach(section => {
            const video = section.querySelector(selectors.videoElement);
            const  videoControl = section.querySelector(selectors.videoControlBtn);
            const videoControlText = section.querySelector(selectors.videoControlText);
              if (video.hasAttribute("autoplay")){
                video.removeAttribute("autoplay");
              }

              if (videoControl.classList.contains("btn-videoControl--pause")) {
                videoControl.classList.remove("btn-videoControl--pause");
                videoControl.classList.add("btn-videoControl--play");
                videoControlText.textContent="Play the video";
              }
              videoControl.addEventListener('click', function(){
                if (video.paused) {
                  video.play(); 
                  section.classList.add("video-playing");
                  section.classList.remove("video-paused");
                  videoControl.classList.remove("btn-videoControl--play");
                  videoControl.classList.add("btn-videoControl--pause");
                  videoControlText.textContent="Pause the video";
                } else {
                  video.pause();
                  section.classList.remove("video-playing");
                  section.classList.add("video-paused");
                  videoControl.classList.remove("btn-videoControl--pause");
                  videoControl.classList.add("btn-videoControl--play");
                  videoControlText.textContent="Play the video";
                }
              });

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
    },
};
export default VideoPlayer;
