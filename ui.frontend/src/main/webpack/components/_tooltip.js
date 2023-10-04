export default class Tooltip {
    constructor(element) {
        this.element = element
        this.button = element.querySelector('button')
        this.tooltip = element.querySelector('[role=tooltip]')
        this.globalEscapeBound = this.globalEscape.bind(this)
        this.bindEvents()
    }
    
    bindEvents() {
        this.element && this.element.addEventListener('mouseenter', this.open.bind(this))
        this.button && this.button.addEventListener('focus', this.open.bind(this))
        this.element && this.element.addEventListener('mouseleave', this.close.bind(this))
        this.button && this.button.addEventListener('blur', this.close.bind(this))
    }
    
    open() {
        this.showTooltip()
        this.attachGlobalListener()
    }
    
    close() {
        this.hideTooltip()
        this.removeGlobalListener()
    }
    
    attachGlobalListener() {
        document.addEventListener('keydown', this.globalEscapeBound)
    }
    
    removeGlobalListener() {
        document.removeEventListener('keydown', this.globalEscapeBound)
    }
    
    globalEscape(event) {
        if (event.key === 'Escape' || event.key === 'Esc') {
            this.close()
        }
    }
    
    showTooltip() {
        this.tooltip.removeAttribute('hidden')
    }
    
    hideTooltip() {
        this.tooltip.setAttribute('hidden', 'hidden')
    }
}