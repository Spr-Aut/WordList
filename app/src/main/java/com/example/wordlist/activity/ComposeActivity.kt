package com.example.wordlist.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordlist.BoxState
import com.example.wordlist.activity.ui.theme.WordListTheme
import com.example.wordlist.util.MyTools

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyTools.setStatusBar(this)
        setContent {
            WordListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                        DynamicScreen()
                        BlurView()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BlurView(){
    var checked by remember {
        mutableStateOf(true)
    }
    val animatedBlur by animateDpAsState(targetValue = if (checked)10.dp else 0.dp)

    Column(modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "测试文本",
            modifier = Modifier.blur(radius = animatedBlur, edgeTreatment = BlurredEdgeTreatment.Unbounded),
            fontSize = 26.sp,

        )
        Spacer(modifier = Modifier.height(24.dp))
        Switch(checked = checked, onCheckedChange = {checked=!checked})
    }
}

@Preview(showBackground = true)
@Composable
fun DynamicScreen(){
    var isCharge by remember{ mutableStateOf(false) }
    var boxState: BoxState by remember{ mutableStateOf(BoxState.NormalState) }
    var checked by remember {
        mutableStateOf(false)
    }
    val animatedBlur by animateDpAsState(targetValue = if (checked)10.dp else 0.dp)
    val animateSizeAsState by animateSizeAsState(
        targetValue = Size(boxState.width.value,boxState.height.value),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 30.dp)) {
        Box(modifier = Modifier
            .width(animateSizeAsState.width.dp)
            .height(animateSizeAsState.height.dp)
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(15.dp))
            .background(color = Color.Black)
        )
        Button(onClick = {boxState = BoxState.NormalState
                         checked=!checked}, modifier = Modifier.padding(top = 30.dp, bottom = 5.dp)) {
            Text(text="默认状态", modifier = Modifier.blur(radius = animatedBlur, edgeTreatment = BlurredEdgeTreatment.Unbounded))
        }
        Button(onClick = {boxState = BoxState.ChargeState}, modifier = Modifier.padding(vertical = 5.dp)) {
            Text(text="充电状态")
        }
        Button(
            modifier = Modifier.padding(vertical = 5.dp),
            onClick = { boxState = BoxState.PayState }) {
            Text(text = "支付状态")
        }

        Button(
            modifier = Modifier.padding(vertical = 5.dp),
            onClick = { boxState = BoxState.MusicState
            }) {
            Text(text = "音乐状态")
        }
    }
}