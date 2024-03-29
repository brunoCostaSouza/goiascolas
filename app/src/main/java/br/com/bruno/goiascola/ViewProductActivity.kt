package br.com.bruno.goiascola

import android.animation.Animator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_view_product.*

class ViewProductActivity : AppCompatActivity() {

    companion object {
        lateinit var TYPE_PRODUCT: ProductsEnum
        lateinit var TYPE_BRICK: TijolosEnum
        lateinit var TYPE_REMD: RendimentoRebocoEnum
        lateinit var TYPE_REBOC: TipoRebocoEnum
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product)

        TYPE_BRICK = TijolosEnum.TIJOLO_14X09
        TYPE_REMD = RendimentoRebocoEnum.RECEBER_REBOCO
        TYPE_REBOC = TipoRebocoEnum.ARGAMASSA_POLIMERICA
        animation.setAnimation("loadingGreen.json")
        setViews()
    }

    private fun setViews(){
        val text1 = intent.extras.getString("TEXT1")
        val text2 = intent.extras.getString("TEXT2")
        val img = intent.extras.getInt("IMG")
        val type = intent.extras.getString("TYPE")
        TYPE_PRODUCT = ProductsEnum.valueOf(type)

        when(TYPE_PRODUCT){
            ProductsEnum.ARGAMASSA_POLIMERICA_BARRICA_50KG,
                ProductsEnum.ARGAMASSA_POLIMERICA_SACO_15KG,
                ProductsEnum.ARGAMASSA_POLIMERICA_BISNAGA_3KG -> {
                spnTipoTijolo.visibility = View.VISIBLE
            }

            ProductsEnum.CHAPISCO_ROLADO_BARRICA_50KG ->{
                spnRendimentoReboco.visibility = View.VISIBLE
            }

            ProductsEnum.REBOCO_PLUS_BALDE_30_KG, ProductsEnum.REBOCO_PLUS_BARRICA_50_KG -> {
                llSpnTipoReboco.visibility = View.VISIBLE
                spnTipoReboco.visibility = View.VISIBLE
            }
        }

        textProduct1.text = text1
        textProduct.text = text2
        imgProduct.setImageResource(img)

        btnCalcular.setOnClickListener {
            animation.visibility = View.VISIBLE
            animation.playAnimation()

            animation.addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(p0: Animator?) {}
                override fun onAnimationCancel(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    animation.visibility = View.GONE
                    animation.pauseAnimation()
                    if(editMQ.text.toString().isNotEmpty()){
                        val result = ResultFactory.getInstanceFactory(TYPE_PRODUCT, TYPE_BRICK, TYPE_REMD, TYPE_REBOC).calcular(editMQ.text.toString().toDouble())
                        textResult.text = result[1]
                        if(result.size > 1) {
                            textObs.visibility = View.VISIBLE
                            textObs.text = result[2]
                        } else {
                            textObs.text = ""
                            textObs.visibility = View.GONE
                        }
                    } else {
                        Toast.makeText(this@ViewProductActivity, "Informe a área em M²", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onAnimationStart(p0: Animator?) {
                    textResult.text = ""
                    textObs.visibility = View.GONE
                }

            })
        }

        editMQ.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textResult.text = ""
                textObs.text = ""
                textObs.visibility = View.GONE
            }

        })
        spnTipoTijolo.onItemSelectedListener = listenerTijolo
        spnRendimentoReboco.onItemSelectedListener = listenerReboco
        spnTipoReboco.onItemSelectedListener = listenerTipoReboco
    }

    object listenerTijolo : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
            TYPE_BRICK = when(pos){
                0 -> {TijolosEnum.TIJOLO_14X09}
                1 -> {TijolosEnum.TIJOLO_19X09}
                2 -> {TijolosEnum.TIJOLO_DEITADO}
                3 -> {TijolosEnum.BLOCO_CONCRETO_19X09}
                else -> {TijolosEnum.TIJOLO_14X09}
            }
        }
        override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
    }

    object listenerReboco : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
            TYPE_REMD = when(pos){
                0 -> {RendimentoRebocoEnum.RECEBER_REBOCO}
                1 -> {RendimentoRebocoEnum.FICAR_EXPOSTO}
                else -> {RendimentoRebocoEnum.RECEBER_REBOCO}
            }
        }
        override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
    }

    object listenerTipoReboco: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
            TYPE_REBOC = when(pos) {
                0 -> {TipoRebocoEnum.ARGAMASSA_POLIMERICA}
                1 -> {TipoRebocoEnum.MASSA_CONVENCIONAL}
                else -> {TipoRebocoEnum.ARGAMASSA_POLIMERICA}
            }
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
}
