const {Router}=require('express');
const doramasController=require('../controllers/doramas.controller');

const router=Router();
router.get('/',doramasController.obtenerDoramas); 
router.get('/:cb',doramasController.obtenerDorama);
router.post('/insert',doramasController.insertarDorama);
router.put('/actualizar/:cb',doramasController.actualizarDorama);
router.delete('/borrar/:cb',doramasController.eliminarDorama);
module.exports=router; 