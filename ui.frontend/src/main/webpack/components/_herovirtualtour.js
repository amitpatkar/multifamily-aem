import A11yDialog from 'a11y-dialog';

const HeroVirtualTour = {
    selectors: {
        self: '[data-cmp-component="herovirtualtour-deprecated-by-footervirtualtourplaceholder"]',
        dialogId: 'virtualtour-dialog'
    },
    init: function() {
        const {selectors} = this;
        const dialogEl = document.getElementById(selectors.dialogId);
        if (dialogEl == null) return; //added by AP
        const dialog = new A11yDialog(dialogEl);
        dialog.on('show', function() {
            console.log('show virtual tour dialog!');
        });
    }
};

export default HeroVirtualTour;
