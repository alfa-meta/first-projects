
// Select elements
const productsEl = document.querySelector(".products");
const cartItemsEl = document.querySelector(".cart-items");
const subtotalEl = document.querySelector(".subtotal");
const totalItemsInCartEl = document.querySelector(".total-items-in-cart");


// Render products
function renderProducts(){
	products.forEach((product) => {
	productsEl.innerHTML += `
		<div class="item">
            <div class="item-container">
                <div class="item-img">
                    <img src="${product.imgSrc}" alt="${product.name}">
                </div>
                 <div class="desc">
                    <h2>${product.name}</h2>
                    <h2><small>$</small>${product.price}</h2>
                    <p>
                        ${product.description}
                 	</p>
                </div>
                <div class="add-to-cart" onclick="addToCart(${product.id})">
                    <img src="../images/bag-plus.png" alt="add to cart">
                </div>
                </div>
            </div>
        `;
    console.log(product);
	})
}

renderProducts();

// cart array
let cart = JSON.parse(localStorage.getItem("CART")) || [];
updateCart();

// Add to cart
function addToCart(id){
    //Check if product already exists in cart
    if(cart.some((item) => item.id === id)){
        changeNumberOfUnits("plus", id);
    } else {

    const item = products.find((product) => product.id === id);

    cart.push({
        ...item, 
        numberOfUnits : 1
    });

    }

    updateCart();
}


//update cart
function updateCart(){
    renderCartItems();
    renderSubtotal();

    // save cart to local storage
    localStorage.setItem("CART", JSON.stringify(cart));
}


//calculate and render subtotal
function renderSubtotal(){
    let totalPrice = 0, totalItems = 0;

    cart.forEach((item) =>{
        totalPrice += item.price * item.numberOfUnits;
        totalItems += item.numberOfUnits;

    });

    subtotalEl.innerHTML = `Subtotal (${totalItems} items): £${totalPrice.toFixed()}`;
    totalItemsInCartEl.innerHTML = totalItems;
    return totalPrice;
}

// render cart items
function renderCartItems(){
    cartItemsEl.innerHTML = ""; //clear

    cart.forEach((item) =>{
        cartItemsEl.innerHTML += `
            <div class="cart-item">
                <div class="item-info" onclick="removeItemFromCart(${item.id})">
                    <img src="${item.imgSrc}" alt="${item.name}">
                    <h4>${item.name}</h4>
                </div>
                <div class="unit-price">
                    <small>$</small>${item.price}
                </div>
                <div class="units">
                    <div class="btn minus" onclick="changeNumberOfUnits('minus', ${item.id})">-</div>
                    <div class="number">${item.numberOfUnits}</div>
                    <div class="btn plus" onclick="changeNumberOfUnits('plus', ${item.id})">+</div>           
                </div>
            </div>
        `;
    });


}


// remove item from cart
function removeItemFromCart(id){
    cart = cart.filter((item) => item.id !== id);

    updateCart();
}


// Change number of units for an item
function changeNumberOfUnits(action, id){
    cart = cart.map((item) => {

        let numberOfUnits = item.numberOfUnits;

        if(item.id === id){
            if(action === "minus" && numberOfUnits > 0){
                numberOfUnits--;
                if (numberOfUnits === 0){
                    removeItemFromCart(item.id);
                }
            } else if (action === "plus"){
                numberOfUnits++;
            }
        }
        return {
            ...item,
            numberOfUnits,
        };
    });


    updateCart();
}

function checkoutAlert(){
    alert("Thank you for your purchase.");
}

