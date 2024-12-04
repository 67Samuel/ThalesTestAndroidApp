# ThalesTestAndroidApp
This is a view-based app that interfaces with a test backend for Products and another one for Product Images.

## Screenshots
<img src="https://github.com/user-attachments/assets/549ace19-e5ef-4c55-8c08-26494d5ac62b" height="500">
<img src="https://github.com/user-attachments/assets/dcc12ac3-2fb1-42e2-b48a-dafaf3f04f53" height="500">
<img src="https://github.com/user-attachments/assets/827e0b72-188c-4172-a6ea-2717cc768d6b" height="500">
<img src="https://github.com/user-attachments/assets/c2789e56-a5e4-44de-8507-d96826e63bc3" height="500">
<img src="https://github.com/user-attachments/assets/c0e29137-cb3c-4852-a2e6-5808435fc7d7" height="500">

## Features
- A list of Products that contain name, type, picture, price, and description properties
- Create and edit one Product
- Sort Products by price and name
- Search and filter Products by name
- 
## Backend
### Product
`GET` all Products: `https://673f398fa9bc276ec4b7b67c.mockapi.io/product`<br/>
`GET` one Product: `https://673f398fa9bc276ec4b7b67c.mockapi.io/product/{id}`<br/>
`PUT` one Product: `https://673f398fa9bc276ec4b7b67c.mockapi.io/product/{id}`<br/>
`POST` one Product: `https://673f398fa9bc276ec4b7b67c.mockapi.io/product`
### Product Image
`PUT` one Product Image: `https://673f398fa9bc276ec4b7b67c.mockapi.io/image/{id}`<br/>
`POST` one Product Image: `https://673f398fa9bc276ec4b7b67c.mockapi.io/image`
