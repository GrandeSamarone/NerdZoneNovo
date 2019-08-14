package com.wegeekteste.fulanoeciclano.nerdzone.Leilao;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MainActivity;
import com.wegeekteste.fulanoeciclano.nerdzone.Activits.MinhaConta;
import com.wegeekteste.fulanoeciclano.nerdzone.Adapter.Adapter_MinhasPublicacoes.Adapter_HQ_Producao;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Forum_principal;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Configuracao_Grupo_Activity;
import com.wegeekteste.fulanoeciclano.nerdzone.Forum.Grupo.Page_Chat_grupo;
import com.wegeekteste.fulanoeciclano.nerdzone.HQ.Pag_producao_hq;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.HeaderDecoration;
import com.wegeekteste.fulanoeciclano.nerdzone.Helper.TrocarFundo;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.HQ_Model;
import com.wegeekteste.fulanoeciclano.nerdzone.Model.Item_leilao;
import com.wegeekteste.fulanoeciclano.nerdzone.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tangxiaolv.telegramgallery.Utils.Utilities.random;

public class Novo_Leilao_Activity extends TrocarFundo {

    private Spinner spinner_cat_leilao;
    private TextView data_selecionada;
    private Toolbar toolbar;
    private ImageView icone;
    private Button botao_carregar,botao_data,botao_salvar;
    private RecyclerViewDragDropManager dragMgr;
    private RecyclerView recyclerView_img_leilao;
    private  int reqCode =  14 ;
    private RecyclerView.Adapter adapter;
    private ArrayList<HQ_Model> item_leilaos;
    private ArrayList<String> list= new ArrayList<>();
    private SimpleDateFormat simpleDateFormat;
    private static  final String ARQUIVO_PREFERENCIA_Lista_leilao ="Lista_leilao";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo__leilao_);

    toolbar=findViewById(R.id.toolbarsecundario);
       toolbar.setTitle("");
       data_selecionada=findViewById(R.id.data_selecionada);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fontgeek.ttf");
        TextView toolbarTitle = findViewById(R.id.app_toolbar_title_secundario);
        toolbarTitle.setText(R.string.text_novo_leilao);
        toolbarTitle.setTextColor(getResources().getColor(R.color.branco));
        toolbarTitle.setTypeface(typeface);
        setSupportActionBar(toolbar);

        //configuracoes iniciais
        Locale locale = new Locale("pt","BR");
        //hora e data
        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM HH:mm", Locale.getDefault());
        icone=findViewById(R.id.ic_help_valor);
        botao_data=findViewById(R.id.botao_data);
        botao_salvar=findViewById(R.id.botao_salvar_item_leilao);
      // icone.setImageResource(R.drawable.ic_ajuda);
       spinner_cat_leilao=findViewById(R.id.spinner_leilao_categoria);
        //Balão informação
        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.sample_popup_ajuda, null);
        PopupWindow popupWindow = BubblePopupHelper.create(this, bubbleLayout);
        final Random random = new Random();


        icone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                v.getLocationInWindow(location);
                if (random.nextBoolean()) {
                    bubbleLayout.setArrowDirection(ArrowDirection.TOP_CENTER);
                } else {
                    bubbleLayout.setArrowDirection(ArrowDirection.TOP_CENTER);
                }
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);
            }
        });

        recyclerView_img_leilao=findViewById(R.id.rec_img_leilao);
        botao_carregar=findViewById(R.id.botaoadd_mg_leilao);
        botao_carregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Carregar_HQ();
            }
        });
        botao_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DoubleDateAndTimePickerDialog.Builder(Novo_Leilao_Activity.this)
                        //.bottomSheet()
                        //.curved()
                        //.minutesStep(15)
                        .backgroundColor(Color.WHITE)
                        .mainColor(getResources().getColor(R.color.md_light_blue_700))
                        .titleTextColor(Color.WHITE)
                        .title("Selecione data/Hora")
                        .tab0Text("Inicio")
                        .tab1Text("FIM")
                        .mustBeOnFuture()
                        .listener(new DoubleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(List<Date> dates) {
                                final StringBuilder stringBuilder = new StringBuilder();
                                for (Date date : dates) {
                                    stringBuilder.append(simpleDateFormat.format(date)).append("\n");
                                }
                                data_selecionada.setText(stringBuilder.toString());
                            }
                        }).display();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // carregar spiner
        CarregarDadosSpinner();
    }
    //carregar spinner
    private void CarregarDadosSpinner() {
        //
        String[] artista = getResources().getStringArray(R.array.categoria_leilao);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, artista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cat_leilao.setAdapter(adapter);

        spinner_cat_leilao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validateSpinner(spinner_cat_leilao,"erro");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    boolean validateSpinner(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            TextView selectedTextView = (TextView) selectedView;
            if (selectedTextView.getText().equals("Selecione uma Categoria")) {
                selectedTextView.setError(error);
                return false;
            }
        }
        return true;
    }


    public boolean  onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                desejaSair();
        }
        return super.onOptionsItemSelected(item);
    }



    private void desejaSair(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja Sair?");
        builder.setMessage("As informaçôes serão perdidas.");
        builder.setCancelable(false);
        builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(Novo_Leilao_Activity.this, leilao_page_principal.class);
                startActivity(it);
                finish();

            }
        }).setNegativeButton("não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void Carregar_HQ() {
        //open album
        GalleryConfig config = new GalleryConfig.Build()
                .limitPickPhoto(50)
                .singlePhoto(false)
                .hintOfPick("this is pick hint")
                .filterMimeTypes(new String[]{})
                .build();
        GalleryActivity.openActivity(Novo_Leilao_Activity.this, reqCode, config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Recebe as imagens da galeria
        if(data!=null) {
            list = (ArrayList) data.getSerializableExtra(GalleryActivity.PHOTOS);
            Log.i("sspsdp4", String.valueOf(list));
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : list) {
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
            SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_PREFERENCIA_Lista_leilao, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("list", stringBuilder.toString());
            editor.commit();
            MostrarHQ();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
       // MostrarHQ();
    }


    private void MostrarHQ(){
        dragMgr = new RecyclerViewDragDropManager();
        SharedPreferences sharedPreferences_img = getSharedPreferences(ARQUIVO_PREFERENCIA_Lista_leilao, MODE_PRIVATE);
        String string_img=sharedPreferences_img.getString("list","");
        String[] item_img=string_img.split(",");
        item_leilaos=new ArrayList<>();

        List<String> list_img=new ArrayList<String>();
        for(int i=0; i<item_img.length;i++){
            list_img.add(item_img[i]);
            item_leilaos.add(new HQ_Model(i,item_img[i]));
         //   Log.i("listitem2", "id "+hq_model.get(i).getImg_id()+"caminho "+hq_model.get(i).getImg_name());
        }
        for(int i=0;i<list_img.size();i++){
            Log.i("listitem",list_img.get(i));
        }

        adapter = dragMgr.createWrappedAdapter(new Adapter_HQ_Producao(item_leilaos, getApplicationContext()));
        recyclerView_img_leilao.setAdapter(adapter);
        //recyclerView_hq.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManagerleilao = new LinearLayoutManager(
              Novo_Leilao_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_img_leilao.setLayoutManager (layoutManagerleilao);
        // recyclerView_hq.setHasFixedSize ( true );
        //  dragMgr.attachRecyclerView(recyclerView_hq);




    }

    @Override
    public void onBackPressed() {

    }

}
