const DatePicker = {
    selectors: {
        duetDatePicker: 'duet-date-picker'
    },
    loadFile: function(type, path) {
        let fileref = document.createElement("script");
        if (type == "jsmodule") {
            fileref.setAttribute("type", "module");
            fileref.setAttribute("src", path);
        } else if (type == "nomodule") {
            fileref.setAttribute("nomodule", "true");
            fileref.setAttribute("src", path);
        } else if (type == "css") {
            fileref = document.createElement("link");
            fileref.setAttribute("rel", "stylesheet");
            fileref.setAttribute("type", "text/css");
            fileref.setAttribute("href", path);
        }
        document.getElementsByTagName("head")[0].appendChild(fileref);
    },
    changeDateFormat: function(){
        const pickers = document.querySelectorAll(this.selectors.duetDatePicker);
        const DATE_FORMAT_US = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;
        pickers.forEach(picker => {
            picker.dateAdapter = {
                parse(value = "", createDate) {
                    const matches = value.match(DATE_FORMAT_US)
                    if (matches) {
                    return createDate(matches[3], matches[1], matches[2])
                    }
                },
                format(date) {
                    return `${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()}`
                },
            };
            picker.localization = {
                buttonLabel: "Choose Date",
                placeholder: "MM/DD/YYYY",
                selectedDateMessage: "Selected date is",
                prevMonthLabel: "Previous month",
                nextMonthLabel: "Next month",
                monthSelectLabel: "Month",
                yearSelectLabel: "Year",
                closeLabel: "Close window",
                calendarHeading: "Choose a date",
                dayNames: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
                monthNames: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
                monthNamesShort: ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"],
                locale: "en-US",
            };
        });
    },
    init: function() {
        this.loadFile("jsmodule", "https://cdn.jsdelivr.net/npm/@duetds/date-picker@1.4.0/dist/duet/duet.esm.js");
        this.loadFile("nomodule", "https://cdn.jsdelivr.net/npm/@duetds/date-picker@1.4.0/dist/duet/duet.js");
        this.changeDateFormat();
    }

}

export default DatePicker;