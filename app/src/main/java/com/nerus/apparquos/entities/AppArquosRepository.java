package com.nerus.apparquos.entities;

import android.app.Application;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;


import com.google.android.gms.common.api.Response;
import com.nerus.apparquos.AppArquos;
import com.nerus.apparquos.daos.AnomaliaDAO;
import com.nerus.apparquos.daos.CausaNoEjecucionDAO;
import com.nerus.apparquos.daos.CuentaDAO;
import com.nerus.apparquos.daos.EmpleadoDAO;
import com.nerus.apparquos.daos.LecturaDAO;
import com.nerus.apparquos.daos.MaterialCapturadoDAO;
import com.nerus.apparquos.daos.MaterialDAO;
import com.nerus.apparquos.daos.OrdenCerradaDAO;
import com.nerus.apparquos.daos.OrdenDAO;
import com.nerus.apparquos.daos.ParametroDAO;
import com.nerus.apparquos.daos.RutaDAO;
import com.nerus.apparquos.daos.SectorDAO;
import com.nerus.apparquos.daos.SubsistemaDAO;
import com.nerus.apparquos.daos.UsuarioDAO;
import com.nerus.apparquos.helpers.clsFecha;
import com.nerus.apparquos.tasks.AnomaliasDownloadTask;
import com.nerus.apparquos.tasks.CausasDownloadTask;
import com.nerus.apparquos.tasks.CuentaUpdateTask;
import com.nerus.apparquos.tasks.CuentasDownloadTask;
import com.nerus.apparquos.tasks.EmpleadosDownloadTask;
import com.nerus.apparquos.tasks.LecturaInsertTask;
import com.nerus.apparquos.tasks.LecturaUpLoadTask;
import com.nerus.apparquos.tasks.LecturaUpdateTask;
import com.nerus.apparquos.tasks.LoginEmpleadoTask;
import com.nerus.apparquos.tasks.LoginUserArquosTask;
import com.nerus.apparquos.tasks.MatCapturadoCRUDTask;
import com.nerus.apparquos.tasks.MaterialUpLoadTask;
import com.nerus.apparquos.tasks.MaterialesDownloadTask;
import com.nerus.apparquos.tasks.OTCerradaCRUDTask;
import com.nerus.apparquos.tasks.OrdenCRUDTask;
import com.nerus.apparquos.tasks.OrdenUpLoadTask;
import com.nerus.apparquos.tasks.OrdenesDownloadTask;
import com.nerus.apparquos.tasks.OrderByDownloadTask;
import com.nerus.apparquos.tasks.ParametroInsertTask;
import com.nerus.apparquos.tasks.ParametroQueryTask;
import com.nerus.apparquos.tasks.ParametrosInsertTask;
import com.nerus.apparquos.tasks.RutaDeleteTask;
import com.nerus.apparquos.tasks.RutasDownloadTask;
import com.nerus.apparquos.tasks.SectoresDownloadTask;
import com.nerus.apparquos.tasks.SetUpLoadedLecturasTask;
import com.nerus.apparquos.tasks.SubsistemasDownloadTask;
import com.nerus.apparquos.tasks.TasksCallBacks;
import com.nerus.apparquos.tasks.UsuarioDeleteTask;
import com.nerus.apparquos.viewmodels.OrdenViewModel;
import com.nerus.apparquos.viewmodels.ParametroViewModel;


import java.net.PortUnreachableException;
import java.util.List;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

public class AppArquosRepository implements TasksCallBacks {
    private static AppArquosRepository INSTANCE;
    private static AppArquos sAppArquos;
    private final AppArquosDataBase db;

    //private static String mUrlApi = "http://192.168.1.1:2525/api";  //BANDERILLA
    //private static String mUrlApi = "http://192.168.1.200:2506/api";  //COATEPEC
    //private static String mUrlApi = "http://arquos-coatepec.ddns.net:2506/api";  //COATEPEC
    private static String mUrlApi = "";  //LAP
    private static String mTokenApp = "";
    private static String mTokenUser = "";
    private static String mFingerPrint = "";
    //private final MediatorLiveData<String> mObservableUrlApi;

    //Objetos DAO
    private ParametroDAO mParametroDAO;
    private UsuarioDAO mUsuarioDAO;
    private CuentaDAO mCuentaDAO;
    private SectorDAO mSectorDAO;
    private SubsistemaDAO mSubsistemaDAO;
    private RutaDAO mRutaDAO;
    private AnomaliaDAO mAnomaliaDAO;
    private LecturaDAO mLecturaDAO;
    private EmpleadoDAO mEmpleadoDAO;
    private OrdenDAO mOrdenDAO;
    private CausaNoEjecucionDAO mCausaNoEjecucionDAO;
    private OrdenCerradaDAO mOrdenCerradaDAO;
    private MaterialDAO mMaterialDAO;
    private MaterialCapturadoDAO mMaterialCapturadoDAO;

    //Objetos Observables
    private LiveData<List<Usuario>> mAllUsuarios;
    //private LiveData<List<Cuenta>> mCuentas;
    private LiveData<List<Subsistema>> mAllSubsistemas;
    private LiveData<List<Sector>> mAllSectores;
    //private LiveData<List<Ruta>> mRutas;
    private LiveData<List<Ruta>> mResumenRutas;
    //private LiveData<List<Anomalia>> mAllAnomalias;
    //private LiveData<Cuenta> mCuenta;
    private LiveData<List<Lectura>> mLectura;
    private LiveData<List<Parametro>> mParametros;
    //private LiveData<List<Empleado>> mEmpleados;
    //private LiveData<Usuario> mUsuario;
    private LiveData<List<Lectura>> mLecturas;
    private LiveData<List<Trabajo>> mTrabajos;
    private LiveData<List<Trabajo>> mOTColonias;

    private TasksCallBacks mLoginListener;
    private TasksCallBacks mSetUpLoadedListener;



    public AppArquosRepository(final AppArquos appArquos, final AppArquosDataBase database) {
        db = database;
        sAppArquos = appArquos;

        this.getObjectsDAO();
        this.initObservables();
        mFingerPrint = clsFecha.getFootPrint();

        /*
        mObservableUrlApi = new MediatorLiveData<>();

        mObservableUrlApi.addSource(db.mParametroDAO().getParametroByName("API_URL"), new Observer<Parametro>() {
            @Override
            public void onChanged(Parametro urlApi) {
                LogSNE.d("NERUS","mObservableUrlApi.onChanged: "+urlApi.getValor());
                mObservableUrlApi.postValue(urlApi.getValor());
            }
        });
        */

    }
/* urlApi -> {
                    if (db.getDatabase().getValue() != null) {
                        mObservableUrlApi.postValue(urlApi.getValor());
                    }
                }*/
    private void getObjectsDAO(){
        //Crear Objetos DAO
        mCuentaDAO = db.mCuentaDAO();
        mSubsistemaDAO = db.mSubsistemaDAO();
        mSectorDAO = db.mSectorDAO();
        mRutaDAO = db.mRutaDAO();
        mAnomaliaDAO = db.mAnomaliaDAO();
        mLecturaDAO = db.mLecturaDAO();
        mEmpleadoDAO = db.mEmpleadoDAO();
        mParametroDAO = db.mParametroDAO();
        mUsuarioDAO = db.mUsuarioDAO();
        mOrdenDAO = db.mOrdenDAO();
        mCausaNoEjecucionDAO = db.mCausaNoEjecucionDAO();
        mOrdenCerradaDAO = db.mOrdenCerradaDAO();
        mMaterialDAO = db.mMaterialDAO();
        mMaterialCapturadoDAO = db.mMaterialCapturadoDAO();
    }
    private void initObservables(){
        mAllSectores = new MutableLiveData<>();
        //mRutas = new MutableLiveData<>();
        mResumenRutas = new MutableLiveData<>();
        //mCuentas = new MutableLiveData<>();
        //mAllAnomalias = new MutableLiveData<>();
        mLectura = new MutableLiveData<>();
        mParametros = new MutableLiveData<>();
        //mEmpleados = new MutableLiveData<>();
        //mUsuario  = new MutableLiveData<>();
        mLecturas = new MutableLiveData<>();
        mTrabajos = new MutableLiveData<>();
        mOTColonias = new MutableLiveData<>();
    }


    public static AppArquosRepository getRepository(AppArquos appArquos, final AppArquosDataBase database) {
        LogSNE.d("NERUS", "The AppArquosRepository is getRepository");
        if (INSTANCE == null) {
            synchronized (AppArquosRepository.class) {
                if (INSTANCE == null) {
                    LogSNE.d("NERUS", "new AppArquosRepository");
                    INSTANCE = new AppArquosRepository(appArquos, database);
                }
            }
        }

        return INSTANCE;
    }


    //BEGIN Functions Getters of Objects Observables
    public LiveData<List<Subsistema>> getAllSubsistemas() {
        mAllSubsistemas = mSubsistemaDAO.getAllSubsistemas();
        return mAllSubsistemas;
    }
    public LiveData<List<Anomalia>> getAllAnomalias() {
        return mAnomaliaDAO.getAllAnomalias();
    }
    public LiveData<List<CausaNoEjecucion>> getAllCausas() {
        return mCausaNoEjecucionDAO.getAllCausas();
    }
    public LiveData<List<Material>> getAllMateriales() {
        return mMaterialDAO.getAllMateriales();
    }

    public LiveData<List<Sector>> getSectoresBySb(Integer sub) {
        mAllSectores = mSectorDAO.getSectoresBySb(sub);
        return mAllSectores;
    }

    public LiveData<List<Ruta>> getRutasBySbSec(Integer sub, Integer sec) {
        return mRutaDAO.getDistinctRutasBySbSec(sub, sec);
    }

    public LiveData<List<Cuenta>> getCuentasByRuta(Integer sub, Integer sec, Integer ruta) {
        return mCuentaDAO.getCuentasByRuta(sub, sec, ruta);
    }

    public LiveData<List<Lectura>> getLecturasByRuta(Integer sub, Integer sec, Integer ruta) {
        mLecturas = mLecturaDAO.getLecturasByRuta(sub, sec, ruta);
        return  mLecturas;
    }

    public LiveData<Cuenta> getCuentaByIdPadron(Integer idpadron) {
        return mCuentaDAO.getCuentaByIdPadron(idpadron);
    }
    public LiveData<List<Lectura>> getLecturaByIdPadron(Integer idpadron) {
        return mLecturaDAO.getLecturaByIdPadron(idpadron);
    }
    public LiveData<List<Ruta>> getResumenByRuta(Integer sub, Integer sec, Integer ruta) {
        mResumenRutas = mCuentaDAO.getResumenByRuta(sub,sec,ruta);
        return mResumenRutas;
    }
    public LiveData<List<Parametro>> getAllParametros() {
        mParametros = mParametroDAO.getAllParametros();
        return mParametros;
    }
    public LiveData<List<Empleado>> getAllEmpleados() {
        return mEmpleadoDAO.getAllEmpleados();
    }
    public LiveData<Empleado> getEmpleadoLogged() {
        return mEmpleadoDAO.getEmpleadoLogged();
    }

    public LiveData<List<Ruta>> getResumenDeRutas() {
        mResumenRutas = mCuentaDAO.getResumenDeRutas();
        return mResumenRutas;
    }
    public LiveData<Parametro> getParametroByName(String name) {
        LiveData<Parametro> mValor = mParametroDAO.getDistinctParametroByName(name);
        return mValor;
    }
    /*
    public Parametro getTokenUser() {
        Parametro mValor = mParametroDAO.getTokenUser();
        return mValor;
    }
    */
    public LiveData<Usuario> getUsuario() {
        return mUsuarioDAO.getUsuario();
    }
    public LiveData<List<Trabajo>> getResumenByTrabajo(Integer idEmpleado) {
        mTrabajos = mOrdenDAO.getResumenOT(idEmpleado);
        return  mTrabajos;
    }
    /*
    public LiveData<List<Trabajo>> getResumenByColonia(Integer idEmpleado) {
        mOTColonias = mOrdenDAO.getResumenPob(idEmpleado);
        return  mOTColonias;
    }
    */
    public LiveData<List<Orden>> getOrdenes(Integer idEmpleado) {
        return  mOrdenDAO.getOrdenesByEmpleado(idEmpleado);
    }
    public LiveData<List<Orden>> getOrdenesByTrabajo(Integer idEmpleado, Integer idTrabajo) {
        return  mOrdenDAO.getOrdenesByTrabajo(idEmpleado, idTrabajo);
    }
    public LiveData<List<Orden>> getOrdenesByColonia(Integer idEmpleado, String pob_colonia) {
        return  mOrdenDAO.getOrdenesByColonia(idEmpleado, pob_colonia);
    }
    public LiveData<OrdenCerrada> getOrdenCerrada(String idOrden) {
        return  mOrdenCerradaDAO.getOrdenCerrada(idOrden);
    }
    public LiveData<List<MaterialCapturado>> getMaterialesByIdOrden(String idOrden) {
        return  mMaterialCapturadoDAO.getMaterialesByIdOrden(idOrden);
    }
    public LiveData<List<OrdenCerrada>> getOrdenesCerradas(Integer idPersonal) {
        return  mOrdenCerradaDAO.getOrdenesCerradas(idPersonal);

    }
    public LiveData<List<MaterialCapturado>> getMaterialUsado(Integer idPersonal) {
        return  mMaterialCapturadoDAO.getMaterialUsado(idPersonal);
    }
    /*
    public void upLoadRuta(Ruta ruta) {
        mCuentas = getCuentasByRuta(ruta.getSb(),ruta.getSector(),ruta.getIdRuta());
    }*/

    public LiveData<Material> getMaterialById(String id) {
        return mMaterialDAO.getMaterial(id);
    }


    //END Functions Getters of Objects Observables

    private void getGlobals(){
        mUrlApi =  sAppArquos.getUrlApi();
        mTokenApp = sAppArquos.getTokenApp();
        mTokenUser = sAppArquos.getTokenUser();

        LogSNE.d("NERUS","getGlobals -> mTokenApp:"+mTokenApp+" mTokenUser:"+mTokenUser);
    }

    //Functions Download remote data of Objects Observables
    public void downloadSubsistemas(TasksCallBacks listener) {
        getGlobals();
        SubsistemasDownloadTask downloadTask = new SubsistemasDownloadTask(mUrlApi, mTokenApp, mTokenUser, mSubsistemaDAO);
        downloadTask.setCallBacks(listener);
        downloadTask.execute("");
    }
    public void downloadAnomalias() {
        getGlobals();
        AnomaliasDownloadTask downloadTask = new AnomaliasDownloadTask(mUrlApi, mTokenApp, mTokenUser, mAnomaliaDAO);
        downloadTask.setCallBacks(this);
        downloadTask.execute("");
    }
    public void downloadCausasNoEjecucion() {
        getGlobals();
        CausasDownloadTask downloadTask = new CausasDownloadTask(mUrlApi, mTokenApp, mTokenUser, mCausaNoEjecucionDAO);
        downloadTask.setCallBacks(this);
        downloadTask.execute("");
    }
    public void downloadSectoresDelSb(Integer sub, TasksCallBacks listener) {
        getGlobals();
        SectoresDownloadTask downloadTask = new SectoresDownloadTask(mUrlApi, mTokenApp, mTokenUser, mSectorDAO);
        downloadTask.setCallBacks(listener);
        downloadTask.execute(sub.toString());
        //LogSNE.d("NERUS", "The AppArquosRepository.downloadSectoresDelSb " + sub.toString());

    }
    public void downloadRutasDelSbySec(Integer sub, Integer sec, TasksCallBacks listener) {
        getGlobals();
        RutasDownloadTask downloadTask = new RutasDownloadTask(mUrlApi, mTokenApp, mTokenUser, mRutaDAO);
        downloadTask.setCallBacks(listener);
        downloadTask.execute(sub.toString(), sec.toString());
        //LogSNE.d("NERUS", "The AppArquosRepository.downloadRutasDelSbySec " + sub.toString() + "," + sec.toString());
    }
    public void downloadCuentasDeRuta(Ruta rutaToDownLoad, Integer nOrderBY, TasksCallBacks listener) {
        getGlobals();
        CuentasDownloadTask downloadTask = new CuentasDownloadTask(mUrlApi, mTokenApp, mTokenUser, mRutaDAO, mCuentaDAO, mLecturaDAO, rutaToDownLoad);
        downloadTask.setCallBacks(listener);
        downloadTask.execute(rutaToDownLoad.getSb().toString(),rutaToDownLoad.getSector().toString(),rutaToDownLoad.getIdRuta().toString(), nOrderBY.toString(), sAppArquos.getEmpresa().getRFC());
        //LogSNE.d("NERUS", "The AppArquosRepository.downloadCuentasDeRuta " + sub.toString() + "," + sec.toString()+ "," + rutaToDownLoad.toString());
    }
    public void downloadEmpleados(TasksCallBacks listener) {
        getGlobals();
        EmpleadosDownloadTask downloadTask = new EmpleadosDownloadTask(mUrlApi, mTokenApp, mTokenUser, mEmpleadoDAO);
        downloadTask.setCallBacks(listener);
        downloadTask.execute("");
    }

    public void getAuthorization(Integer idPersonal, String validar, TasksCallBacks listener) {
        getGlobals();
        LoginEmpleadoTask downloadTask = new LoginEmpleadoTask(mUrlApi, mTokenApp, mTokenUser, mEmpleadoDAO, mParametroDAO);
        downloadTask.setCallBacks(listener);
        downloadTask.execute(idPersonal.toString(), validar, mFingerPrint);
    }
    public void getUserArquos(String usuario, String validar, TasksCallBacks listener) {
        getGlobals();
        mLoginListener = listener;
        LoginUserArquosTask downloadTask = new LoginUserArquosTask(mUrlApi, mTokenApp, mTokenUser, mUsuarioDAO, mParametroDAO);
        downloadTask.setCallBacks(this);
        downloadTask.execute(usuario, validar, mFingerPrint);
    }
    public void upLoadLectura(Lectura lectura, Usuario usuario, TasksCallBacks listener) {
        getGlobals();
        String cIDUsuario = "";
        if (usuario!=null) cIDUsuario=usuario.getIdUsuario();
        LecturaUpLoadTask downloadTask = new LecturaUpLoadTask(mUrlApi, mTokenApp, mTokenUser, cIDUsuario, mFingerPrint, mLecturaDAO, mParametroDAO);
        downloadTask.setCallBacks(listener);
        downloadTask.execute(lectura);
    }

    public void downloadOrdenes(Integer idEmpleado, TasksCallBacks listener) {
        getGlobals();

        OrdenesDownloadTask downloadTask = new OrdenesDownloadTask(mUrlApi, mTokenApp, mTokenUser, mOrdenDAO);
        downloadTask.setCallBacks(listener);
        downloadTask.execute(idEmpleado.toString());
    }
    public void downloadOrderBy(TasksCallBacks listener) {
        getGlobals();

        OrderByDownloadTask downloadTask = new OrderByDownloadTask(mUrlApi, mTokenApp, mTokenUser);
        downloadTask.setCallBacks(listener);
        downloadTask.execute();
    }
    public void downloadMateriales(TasksCallBacks listener) {
        getGlobals();

        MaterialesDownloadTask downloadTask = new MaterialesDownloadTask(mUrlApi, mTokenApp, mTokenUser,mMaterialDAO);
        downloadTask.setCallBacks(listener);
        downloadTask.execute();
    }
    public void upLoadOrdenes(String xmlToSend, Integer idPersonal, String idUsuario, TasksCallBacks listener) {
        getGlobals();

        OrdenUpLoadTask uploadTask = new OrdenUpLoadTask(mUrlApi, mTokenApp, mTokenUser, mFingerPrint, xmlToSend, idPersonal, idUsuario);
        uploadTask.setCallBacks(listener);
        uploadTask.execute();

    }
    public void upLoadMateriales(String xmlToSend, Integer idPersonal, String idUsuario, TasksCallBacks listener) {
        getGlobals();

        MaterialUpLoadTask uploadTask = new MaterialUpLoadTask(mUrlApi, mTokenApp, mTokenUser, mFingerPrint, xmlToSend, idPersonal, idUsuario);
        uploadTask.setCallBacks(listener);
        uploadTask.execute();
    }

    @Override
    public void onRequestBeforeStart(String fromTask) {
        LogSNE.d("NERUS", "The onRequestBeforeStart of: " + fromTask);
    }

    @Override
    public void onProgressUpdate(String fromTask, Integer... progress) {
        //LogSNE.d("NERUS", "The onProgressUpdate of: "+fromTask);
    }

    @Override
    public void onRequestError(String fromTask, Exception error) {
        LogSNE.d("NERUS", "The onRequestError of: " + fromTask + " error:" + error.getMessage());

        if (fromTask.equals(SectoresDownloadTask.TASK_NAME)) {
            //LogSNE.d("NERUS", "The onRequestError of: "+fromTask + " SB:"+ mSb.toString());
            //mAllSectores = mSectorDAO.getSectoresBySb(mSb);

        } else if (fromTask.equals(LoginUserArquosTask.TASK_NAME)) {

            mLoginListener.onRequestError(fromTask, error);
            //delete_AllUsuarios();
            //mUsuario =  mUsuarioDAO.getUsuario();
            //LogSNE.d("NERUS", "The onRequestError of: "+fromTask + " mUsuario:"+ mUsuario.toString());
        }
    }

    @Override
    public void onRequestCancel(String fromTask, Exception error) {
        LogSNE.d("NERUS", "The onRequestCancel of: " + fromTask);
        if (fromTask.equals(AnomaliasDownloadTask.TASK_NAME) ) {
            this.getAllAnomalias();
        }
    }

    @Override
    public void onRequestSuccess(String fromTask, Object response) {
        LogSNE.d("NERUS", "onRequestSuccess of " + fromTask);

        if (fromTask.equals(SubsistemasDownloadTask.TASK_NAME)) {
            //mAllSubsistemas = mSubsistemaDAO.getAllSubsistemas();
            //LogSNE.d("NERUS", "The mAllSubsistemas  is: " + mAllSubsistemas.toString());
        } else if (fromTask.equals(SectoresDownloadTask.TASK_NAME)) {
            //Integer mSb = Integer.valueOf((String) response);
            //mAllSectores.setValue(( List<Sector>) mSectorDAO.getSectoresBySb(mSb));
            //LogSNE.d("NERUS", "onRequestSuccess The mAllSectores  of: " + mSb.toString() + "-" + mAllSectores.toString());
        } else if (fromTask.equals(CuentasDownloadTask.TASK_NAME)) {
            //Integer mSb = Integer.valueOf((String) response);
            //mAllSectores.setValue(( List<Sector>) mSectorDAO.getSectoresBySb(mSb));
            //LogSNE.d("NERUS", "onRequestSuccess The mAllSectores  of: " + mSb.toString() + "-" + mAllSectores.toString());
        } else if (fromTask.equals(LoginUserArquosTask.TASK_NAME)) {
            //mUsuario = this.getUsuario();
            mLoginListener.onRequestSuccess(fromTask, response);
        } else if (fromTask.equals(LecturaUpLoadTask.TASK_NAME)) {
        } else if (fromTask.equals(SetUpLoadedLecturasTask.TASK_NAME)) {
            mSetUpLoadedListener.onRequestSuccess(fromTask, response);
        } else if (fromTask.equals(ParametroInsertTask.TASK_NAME)) {
            LogSNE.d("NERUS", "Parametro saved of " + ((Parametro)response).toString() );
        } else if (fromTask.equals(ParametroQueryTask.TASK_NAME)) {
            //mTokenUser = ((Parametro) response).getValor();
        } else if (fromTask.equals(CausasDownloadTask.TASK_NAME)) {

        }
    }


    public void insert_lectura(Lectura lectura) {
        LecturaInsertTask crudTask = new LecturaInsertTask(mLecturaDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(lectura);
    }

    public void update_lectura(Lectura lectura) {
        LecturaUpdateTask crudTask = new LecturaUpdateTask(mLecturaDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(lectura);
    }



    public void insertOTCerrada(OrdenCerrada ordenCerrada) {
        OTCerradaCRUDTask crudTask = new OTCerradaCRUDTask(mOrdenCerradaDAO,"INSERT");
        crudTask.setCallBacks(this);
        crudTask.execute(ordenCerrada);
    }

    public void updateOTCerrada(OrdenCerrada ordenCerrada) {
        OTCerradaCRUDTask crudTask = new OTCerradaCRUDTask(mOrdenCerradaDAO,"UPDATE");
        crudTask.setCallBacks(this);
        crudTask.execute(ordenCerrada);
    }

    public void delete_AllUsuarios() {
        UsuarioDeleteTask crudTask = new UsuarioDeleteTask(mUsuarioDAO);
        crudTask.setCallBacks(this);
        crudTask.execute();
    }

    public void deleteRuta(Ruta ruta) {
        RutaDeleteTask crudTask = new RutaDeleteTask(mCuentaDAO, mLecturaDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(ruta);
    }


    public void setUpLoadedLecturas(List<Lectura> lecturas, TasksCallBacks listener) {
        mSetUpLoadedListener=listener;
        SetUpLoadedLecturasTask crudTask = new SetUpLoadedLecturasTask(mLecturaDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(lecturas);
    }

    public void saveLastRuta(Ruta ruta) {

        String cFecha = clsFecha.getFechaYMDhms();
        Parametro parametro = new Parametro("LAST_RUTA",ruta.getIdRuta().toString(), ruta.getDescripcion(),"Ultima Ruta en captura",cFecha);


        ParametroInsertTask crudTask = new ParametroInsertTask(mParametroDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(parametro);
    }
    public void saveParametro(Parametro parametro) {

        String cFecha = clsFecha.getFechaYMDhms();
        parametro.setFecha(cFecha);

        ParametroInsertTask crudTask = new ParametroInsertTask(mParametroDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(parametro);
    }
    public void saveParametros(List<Parametro> parametroList) {
        ParametrosInsertTask crudTask = new ParametrosInsertTask(mParametroDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(parametroList);
    }
    public void saveLastCuenta(Cuenta cuenta) {

        String cFecha = clsFecha.getFechaYMDhms();
        Parametro parametro = new Parametro("LAST_CUENTA",cuenta.getId_Padron().toString(), cuenta.getRazon_social(),"Ultima cuenta en captura",cFecha);

        ParametroInsertTask crudTask = new ParametroInsertTask(mParametroDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(parametro);
    }

    public void setCapturado(Cuenta cuenta) {
        CuentaUpdateTask crudTask = new CuentaUpdateTask(mCuentaDAO);
        crudTask.setCallBacks(this);
        crudTask.execute(cuenta);
    }
    public void setCapturado(Orden orden) {
        orden.setCapturado(true);
        OrdenCRUDTask crudTask = new OrdenCRUDTask(mOrdenDAO,"UPDATE");
        crudTask.setCallBacks(this);
        crudTask.execute(orden);

    }

    public void insertMatCapturado(MaterialCapturado item) {

        MatCapturadoCRUDTask crudTask = new MatCapturadoCRUDTask(mMaterialCapturadoDAO,"INSERT");
        crudTask.setCallBacks(this);
        crudTask.execute(item);
    }

    public void deleteMatCapturado(MaterialCapturado item) {
        MatCapturadoCRUDTask crudTask = new MatCapturadoCRUDTask(mMaterialCapturadoDAO,"DELETE");
        crudTask.setCallBacks(this);
        crudTask.execute(item);
    }


    public void setOTEnviadas(List<Orden> otEnviadas, TasksCallBacks listener) {

        OrdenCRUDTask crudTask = new OrdenCRUDTask(mOrdenDAO,"SET_UPLOAD", otEnviadas);
        crudTask.setCallBacks(listener);
        crudTask.execute();
    }
}

