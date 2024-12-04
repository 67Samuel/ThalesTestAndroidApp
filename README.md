# ThalesTestAndroidApp
This is a view-based app that interfaces with a test backend for Products and another one for Product Images.
## Features
- A list of Products that contain name, type, picture, price, and description properties
- Create and edit one Product
- Sort Products by price and name
- Filter Products by type
- Search Products by name
## Backend
### Product
`GET` all Products: `https://673f398fa9bc276ec4b7b67c.mockapi.io/product`<br/>
`GET` one Product: `https://673f398fa9bc276ec4b7b67c.mockapi.io/product/{id}`<br/>
`PUT` one Product: `https://673f398fa9bc276ec4b7b67c.mockapi.io/product/{id}`<br/>
`POST` one Product: `https://673f398fa9bc276ec4b7b67c.mockapi.io/product`
### Product Image
`PUT` one Product Image: `https://673f398fa9bc276ec4b7b67c.mockapi.io/image/{id}`<br/>
`POST` one Product Image: `https://673f398fa9bc276ec4b7b67c.mockapi.io/image`
