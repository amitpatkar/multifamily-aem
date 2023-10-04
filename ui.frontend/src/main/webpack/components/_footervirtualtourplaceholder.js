import A11yDialog from 'a11y-dialog';

const FooterVirtualTourPlaceholder = {
    selectors: {
        self: '[data-cmp-component="footervirtualtourplaceholder"]',
        dialogId: 'virtualtour-dialog'
    },
    init: function() {
        var panoskinInitDone = false;
        const {selectors} = this;
        const dialogEl = document.getElementById(selectors.dialogId);
        if (dialogEl == null) return; //added by AP
        const dialog = new A11yDialog(dialogEl);
        dialog.on('show', function() {
            //console.log('show virtual tour dialog - FooterVirtualTourPlaceholder!');
            if (!panoskinInitDone && window.panoskin_init) {
                panoskin_init();
                panoskinInitDone = true;
            }
        });
    }
};

export default FooterVirtualTourPlaceholder;
