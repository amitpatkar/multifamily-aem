"use strict";
use(function () {
    var total = this.total;
    var capAt = this.capAt;
    
    return {
        "remainder": total-capAt,
        "isRemainder": (total-capAt) > 0
    }
});