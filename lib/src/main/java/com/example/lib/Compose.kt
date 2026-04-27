@file:OptIn(ExperimentalFoundationStyleApi::class)

package com.example.styles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.StyleScope
import androidx.compose.foundation.style.StyleStateKey
import androidx.compose.foundation.style.pressed
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * This file showcases the differences between the traditional Modifier-based styling
 * and the new Experimental Style API.
 */


@Preview(showBackground = true)
@Composable
fun ComparisonPreview() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Traditional Modifiers", fontWeight = FontWeight.Bold)
        ModifierExample()

        Text("New Style API", fontWeight = FontWeight.Bold)
        StyleExample()

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Interactive & Animated (Modifier)", fontWeight = FontWeight.Bold)
        InteractiveModifierExample()

        Text("Interactive & Animated (Modifier - Optimized)", fontWeight = FontWeight.Bold)
        InteractiveModifierOptimizedExample()

        Text("Interactive & Animated (Style)", fontWeight = FontWeight.Bold)
        InteractiveStyleExample()

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Style Inheritance", fontWeight = FontWeight.Bold)
        InheritanceExample()

        Text("Traditional Inheritance (CompositionLocal)", fontWeight = FontWeight.Bold)
        InheritanceCompositionLocalExample()

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Style Merging", fontWeight = FontWeight.Bold)
        MergingExample()

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Style Precedence (Priority)", fontWeight = FontWeight.Bold)
        PrecedenceExample()

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Custom Style", fontWeight = FontWeight.Bold)
        StyleStateKeySample()
    }
}

/**
 * 1. STATIC STYLING
 *
 * Using Modifiers:
 * Each property is a separate modifier in a chain.
 * Changing any property usually requires recomposition of the Composable if values are dynamic.
 */
@Composable
fun ModifierExample() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text("Hi")
    }
}

/**
 * Using Style API:
 * Properties are grouped in a Style object.
 * Note the use of 'contentPadding' and 'externalPadding' which maps to how CSS handles boxes.
 * Invalidation is scoped to the styleable modifier, potentially avoiding full recompositions.
 */

@Composable
fun StyleExample() {
    val myStyle = Style {
        size(100.dp)
        background(Color.LightGray)
        shape(RoundedCornerShape(8.dp))
        border(2.dp, Color.Black)
        contentPadding(16.dp)
    }

    Box(modifier = Modifier.styleable(style = myStyle)) {
        Text("Hi")
    }
}

/**
 * 2. INTERACTIVE & ANIMATED STYLING
 *
 * Using Modifiers:
 * Requires manual state tracking (InteractionSource) and individual animate*AsState calls.
 * This leads to recompositions every time the animation value or interaction state updates.
 */
@Composable
fun InteractiveModifierExample() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Manual animation setup
    val color by animateColorAsState(if (isPressed) Color.Red else Color.Blue, label = "color")
    val size by animateDpAsState(if (isPressed) 120.dp else 100.dp, label = "size")

    Box(
        modifier = Modifier
            .size(size)
            .clickable(interactionSource = interactionSource, indication = null) { }
            .background(color, RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Press Me", color = Color.White)
    }
}

/**
 * Using Style API:
 * Interactivity and animations are declared INSIDE the style.
 * The Style API handles the InteractionSource internally via styleState.
 * CRITICAL DIFFERENCE: Changes to 'pressed' or 'animate' blocks do NOT trigger
 * recomposition of this Composable function. They only trigger re-draw or re-layout
 * within the styleable node.
 */

@Composable
fun InteractiveStyleExample() {
    val interactionSource = remember { MutableInteractionSource() }
    // rememberUpdatedStyleState links the InteractionSource to the Style system
    val styleState = rememberUpdatedStyleState(interactionSource)

    val myInteractiveStyle = Style {
        size(100.dp)
        background(Color.Blue)
        shape(RoundedCornerShape(8.dp))
        contentColor(Color.White)

        // Declarative interaction state
        pressed {
            // Declarative animation
            animate {
                background(Color.Red)
                size(120.dp)
            }
        }
    }

    Box(
        modifier = Modifier
            .clickable(interactionSource = interactionSource, indication = null) { }
            .styleable(styleState, myInteractiveStyle),
        contentAlignment = Alignment.Center
    ) {
        // Text color is automatically inherited from Style's contentColor(Color.White)
        Text("Press Me")
    }
}

/**
 * Optimized Modifier version:
 * To avoid recomposition during animations and interaction state changes, we must:
 * 1. Avoid reading 'isPressed' or animation values in the Composable body.
 * 2. Use 'LaunchedEffect' to collect interactions manually.
 * 3. Use lambda-based modifiers like 'layout' and 'drawBehind' to read values
 *    only during the Draw or Layout phases.
 *
 * Note how much more boilerplate this requires compared to the Style API!
 */
@Composable
fun InteractiveModifierOptimizedExample() {
    val interactionSource = remember { MutableInteractionSource() }

    // We use Animatable to hold values. To avoid recomposition, we MUST NOT
    // read these values in the Composable body.
    val colorAlpha = remember { Animatable(0f) }
    val animatedSize = remember { Animatable(100.dp, Dp.VectorConverter) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    launch { colorAlpha.animateTo(1f) }
                    launch { animatedSize.animateTo(120.dp) }
                }

                is PressInteraction.Release, is PressInteraction.Cancel -> {
                    launch { colorAlpha.animateTo(0f) }
                    launch { animatedSize.animateTo(100.dp) }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            // By reading animatedSize.value inside the 'layout' lambda, we only trigger
            // the Layout phase when the value changes, skipping Recomposition.
            // This ensures neighboring components are pushed away correctly.
            .layout { measurable, _ ->
                val pxSize = animatedSize.value.roundToPx()
                val placeable = measurable.measure(Constraints.fixed(pxSize, pxSize))
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }
            .clickable(interactionSource = interactionSource, indication = null) { }
            .drawBehind {
                drawRoundRect(
                    color = lerp(Color.Blue, Color.Red, colorAlpha.value),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Press Me", color = Color.White)
    }
}

/**
 * 3. INHERITANCE
 *
 * Modifiers: No inheritance. You must pass values down manually or use CompositionLocals.
 * Style: Certain properties (like textStyle, contentColor, fontSize) are inherited by
 * any 'Text' or 'styleable' child components.
 */

@Composable
fun InheritanceExample() {
    val parentStyle = Style {
        contentColor(Color.Magenta)
        fontSize(20.sp)
        fontWeight(FontWeight.Bold)
    }

    Row(
        modifier = Modifier
            .styleable(style = parentStyle)
    ) {
        // These texts inherit the Magenta color and 20.sp size automatically
        Text("Inherited")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Styles")
    }
}

/**
 * Traditional way to handle inheritance:
 * Requires manually providing values via CompositionLocalProvider.
 * This can become deeply nested and harder to read with many properties.
 */
@Composable
fun InheritanceCompositionLocalExample() {
    CompositionLocalProvider(
        LocalContentColor provides Color.Magenta,
        LocalTextStyle provides TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    ) {
        Row {
            // These texts inherit from the providers above
            Text("Inherited")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Locals")
        }
    }
}

/**
 * 4. MERGING
 *
 * Modifiers: Use .then() or simple chaining.
 * Style: Use 'then' infix or the Style() constructor to combine multiple styles.
 * Properties in the "later" style override those in the "earlier" style.
 */

@Composable
fun MergingExample() {
    val baseStyle = Style {
        size(100.dp)
        background(Color.Gray)
        shape(RoundedCornerShape(16.dp))
    }

    val overrideStyle = Style {
        background(Color.Green) // Overrides Gray
        border(2.dp, Color.Black) // Adds border
    }

    // Using 'then' to combine
    val combinedStyle = baseStyle then overrideStyle

    Box(modifier = Modifier.styleable(style = combinedStyle)) {
        Text("Merged", color = Color.White, modifier = Modifier.padding(8.dp))
    }
}

/**
 * 5. PRECEDENCE (PRIORITY)
 *
 * Demonstrates the order of priority when multiple styling layers are applied:
 * 1 (Highest): Direct arguments (Text(color = Color.Red))
 * 2          : Style parameter (Text(style = Style { contentColor(...) }))
 * 3          : Modifier chain (Modifier.styleable { ... })
 * 4 (Lowest) : Parent styles (Inherited from Row/Column)
 */

@Composable
fun PrecedenceExample() {
    val parentStyle = Style { contentColor(Color.Gray) } // Priority 4
    val modifierStyle = Style { contentColor(Color.Cyan) } // Priority 3
    val parameterStyle = Style { contentColor(Color.Green) } // Priority 2

    Column(modifier = Modifier.styleable(style = parentStyle)) {
        Text("1. Parent Only (Gray)")

        Text(
            text = "2. Parent vs Modifier (Cyan Wins)",
            modifier = Modifier.styleable(style = modifierStyle)
        )

        // Using a custom styleable component to show Priority 2
        MyStyleableText(
            text = "3. Modifier vs Style Param (Green Wins)",
            modifier = Modifier.styleable(style = modifierStyle),
            style = parameterStyle
        )

        MyStyleableText(
            text = "4. Style Param vs Direct Param (Red Wins)",
            style = parameterStyle,
            color = Color.Red // Priority 1
        )
    }
}

/**
 * A simple wrapper to demonstrate level 2 (Style parameter) priority.
 * In a real component, the 'style' parameter is applied AFTER the 'modifier'
 * in the internal chain, giving it higher precedence.
 */

@Composable
fun MyStyleableText(
    text: String,
    modifier: Modifier = Modifier,
    style: Style = Style,
    color: Color = Color.Unspecified
) {
    Text(
        text = text,
        // The component internalizes the Style parameter by applying it
        // to the modifier chain AFTER the external modifier.
        modifier = modifier.styleable(style = style),
        color = color
    )
}

/**
 * A custom state styling with StateStyle
 * */

enum class PlayerState {
    Stopped,
    Playing,
    Paused
}

val playerStateKey = StyleStateKey(PlayerState.Stopped)
var MutableStyleState.playerState
    get() = this[playerStateKey]
    set(value) {
        this[playerStateKey] = value
    }

fun StyleScope.playerPlaying(value: Style) {
    state(playerStateKey, value, { key, state -> state[key] == PlayerState.Playing })
}

fun StyleScope.playerPaused(value: Style) {
    state(playerStateKey, value, { key, state -> state[key] == PlayerState.Paused })

}

@Composable
fun MediaPlayer(
    url: String,
    modifier: Modifier = Modifier,
    style: Style = Style,
    // We pass the styleState directly to avoid recomposition when properties change
    styleState: MutableStyleState = remember { MutableStyleState(null) }
) {
    Box(
        modifier = modifier.styleable(
            styleState,
            {
                size(100.dp)
                border(2.dp, Color.Red)
            },
            style,
        )
    ) {
    }
}

@Composable
fun StyleStateKeySample() {
    val styleState = remember { MutableStyleState(null) }

    val style = Style {
        borderColor(Color.Gray)
        playerPlaying {
            animate {
                borderColor(Color.Green)
            }
        }
        playerPaused {
            animate {
                borderColor(Color.Blue)
            }
        }
    }

    // Notice that MediaPlayer and StyleStateKeySample DO NOT recompose when we click.
    // The state change is handled entirely within the Style system.
    MediaPlayer(
        url = "https://example.com/media/video",
        modifier =  Modifier.clickable {
            styleState.playerState = when (styleState.playerState) {
                PlayerState.Stopped -> PlayerState.Playing
                PlayerState.Playing -> PlayerState.Paused
                PlayerState.Paused -> PlayerState.Stopped
            }
        },
        style = style,
        styleState = styleState
    )
}
