const Dorama=require('../models/Dorama.model');
const doramasController={};


doramasController.obtenerDoramas=async(req,res)=>{
    const doramas=await Dorama.find();
    res.json(doramas);
};
doramasController.obtenerDorama=async(req,res)=>{
    const dorama=await Dorama.findOne({nombre:req.params.cb});
    res.json(dorama);   
     
};
doramasController.insertarDorama=async(req,res)=>{
    const doramaInsertado=new Dorama(req.body);
    await doramaInsertado.save();
    res.json({
        status:"Dorama insertado"
    });
};
doramasController.actualizarDorama=async(req,res)=>{
    const resp = await Dorama.findOneAndUpdate({nombre:req.params.cb},req.body);
    /*res.json({
        status:"Dorama actualizado"
    });*/
    if(res!=null)
        res.json({status: "Dorama actualizado"});
    else
        res.json({status: "No encontrado"});
};
doramasController.eliminarDorama=async(req,res)=>{
    const resp = await Dorama.findOneAndDelete({nombre:req.params.cb});
    /*res.json({
        status:"Dorama eliminado"
    });*/

    if(res!=null)
        res.json({status: "Articulo Eliminado"});
    else
        res.json({status: "Not Found"});

};
module.exports=doramasController;