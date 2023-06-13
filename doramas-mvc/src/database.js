const mongoose =require('mongoose');
mongoose.connect("mongodb+srv://alfredsolis:alfred123@cluster0.psmnr.mongodb.net/doramas?retryWrites=true&w=majority")
.then(db=>console.log("Mongodb connected..."))
.catch(err=>console.error(err));