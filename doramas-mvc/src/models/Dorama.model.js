const {Schema,model}=require('mongoose');
const doramaSchema=new Schema({
    nombre:{
        type:String,
        unique:true,
        require:true
    },
    año:Number,
    tipo:String,
    temporada:Number
},{
    versionKey:false,
    timestamps:true
});
module.exports=model('doramas',doramaSchema);