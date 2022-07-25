class App {

    static DOMAIN = location.origin;
    static BASE_URL = this.DOMAIN + "/api";

    static GET_ALL_PROVINCES = "https://vapi.vnappmob.com/api/province/"

    static SweetAlert = class {
        static showSuccessAlert(t) {
            Swal.fire({
                icon: 'success',
                title: t,
                position: 'top-end',
                showConfirmButton: false,
                timer: 1500
            })
        }

        static showErrorAlert(t) {
            Swal.fire({
                icon: 'error',
                title: 'Warning',
                text: t,
            })
        }
    }

    static IziToast = class  {
        static showSuccessAlert(m) {
            iziToast.success({
                title: 'Success',
                position: 'topRight',
                timeout: 2500,
                message: m,
            });
        }

        static showErrorAlert(m) {
            iziToast.error({
                title: 'Error',
                position: 'topRight',
                timeout: 2500,
                message: m,
            });
        }
    }

    static formatNumber() {
        $(".num-space").number(true, 0, ',', ' ');
        $(".num-point").number(true, 0, ',', '.');
        $(".num-comma").number(true, 0, ',', ',');
    }

    static formatNumberSpace(x) {
        if (x == null) {
            return x;
        }
        return x.toString().replace(/ /g, "").replace(/\B(?=(\d{3})+(?!\d))/g, " ");
    }
}

class LocationRegion {
    constructor(id, provinceId, provinceName, districtId, districtName, wardId, wardName, address) {
        this.id = id;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.address = address;
    }
}

class Customer {
    constructor(id, fullName, email, phone, locationRegion, balance) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.locationRegion = locationRegion;
        this.balance = balance;
    }
}

class Sender extends Customer {
    constructor(id, fullName, email, phone, locationRegion, balance) {
        super();
    }
}

class Recipient extends Customer {
    constructor(id, fullName, email, phone, locationRegion, balance) {
        super();
    }
}

class Deposit {
    constructor(id, customerId, transactionAmount) {
        this.id = id;
        this.customerId = customerId;
        this.transactionAmount = transactionAmount;
    }
}

class Transfer {
    constructor(id, senderId, recipientId, transferAmount) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.transferAmount = transferAmount;
    }
}

class TransferHistory {
    constructor(id, senderId, senderName, recipientId, recipientName, createdOn, createdAt, transferAmount, fees, feesAmount, transactionAmount) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.createdOn = createdOn;
        this.createdAt = createdAt;
        this.transferAmount = transferAmount;
        this.fees = fees;
        this.feesAmount = feesAmount;
        this.transactionAmount = transactionAmount;
    }
}