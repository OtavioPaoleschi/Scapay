package br.scapegoat.scapay;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    SurfaceView cameraPreview;
    TextView textResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    private ListView listview;
    ArrayAdapter<String> adapter;
    ArrayList<String> listProdutos=new ArrayList<String>();
    final Handler handler = new Handler();
    double valortot = 0;
    boolean aux1 = false;
    boolean aux2 = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void mudaPage(View arg0){
        if(aux1&&aux2){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_confirmar_pagamento);
        dialog.setTitle("Confirmar Pagamento !");
        TextView text = (TextView) dialog.findViewById(R.id.txtNomeVendedor);
        text.setText("Você comprou do "+textResult.getText());
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        }else{
            Toast.makeText(MainActivity.this, "Você precisa Escanear um vendedor e ao menos um produto para pagar!", Toast.LENGTH_LONG).show();
        }
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setListAdapter(adapter);
        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        textResult = (TextView) findViewById(R.id.txtNomeLoja);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).build();
        listview = (ListView) findViewById(R.id.listViewProdutos);
        adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,listProdutos);
        listview.setAdapter(adapter);
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });



        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if(qrCodes.size() != 0 ){
                    textResult.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(500);
                            String aux = qrCodes.valueAt(0).displayValue;
                            String[] separated = aux.split(":");
                            if(separated[0].equalsIgnoreCase("V")){
                                textResult.setText("Vendedor: "+separated[1]);
                            }else if(separated[0].equalsIgnoreCase("P")){
                                listProdutos.add("Produto: "+separated[1]+", SKU: "+separated[2]+", Preço: R$"+separated[3]);
                                valortot = valortot + Double.parseDouble(separated[3]);
                                Button botao = (Button) findViewById(R.id.buttonConfirmarPagamento);
                                botao.setText("Confimar Pagamento R$"+valortot);
                                adapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(MainActivity.this, "Favor escaneie ou um vendedor ou um produto válido", Toast.LENGTH_LONG).show();
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                }
                            }, 1000);
                        }
                    });
                }
            }
        });
    }
}
