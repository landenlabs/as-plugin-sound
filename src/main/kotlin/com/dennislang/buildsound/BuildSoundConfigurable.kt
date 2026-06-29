package com.dennislang.buildsound

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

class BuildSoundConfigurable : Configurable {

    private lateinit var panel: JPanel

    private lateinit var enabledCheck: JBCheckBox
    private lateinit var successCheck: JBCheckBox
    private lateinit var failureCheck: JBCheckBox
    private lateinit var volumeSlider: JSlider
    private lateinit var durationSlider: JSlider

    override fun getDisplayName() = "Build Sound"

    override fun createComponent(): JComponent {
        panel = JPanel(GridBagLayout())
        val gbc = GridBagConstraints().apply {
            insets = Insets(4, 8, 4, 8)
            anchor = GridBagConstraints.WEST
        }

        enabledCheck = JBCheckBox("Enable build sound notifications")
        successCheck = JBCheckBox("Play sound on build success  ✔")
        failureCheck = JBCheckBox("Play sound on build failure  ✘")

        volumeSlider = JSlider(0, 100, 70).apply {
            majorTickSpacing = 25
            minorTickSpacing = 5
            paintTicks = true
            paintLabels = true
        }
        durationSlider = JSlider(80, 400, 180).apply {
            majorTickSpacing = 80
            paintTicks = true
            paintLabels = true
        }

        fun row(y: Int, vararg comps: JComponent) {
            comps.forEachIndexed { x, c ->
                gbc.gridx = x; gbc.gridy = y
                gbc.fill = if (c is JSlider) GridBagConstraints.HORIZONTAL else GridBagConstraints.NONE
                gbc.weightx = if (c is JSlider) 1.0 else 0.0
                panel.add(c, GridBagConstraints().apply {
                    gridx = x; gridy = y
                    insets = Insets(4, 8, 4, 8)
                    anchor = GridBagConstraints.WEST
                    fill = if (c is JSlider) GridBagConstraints.HORIZONTAL else GridBagConstraints.NONE
                    weightx = if (c is JSlider) 1.0 else 0.0
                })
            }
        }

        row(0, enabledCheck)
        row(1, successCheck)
        row(2, failureCheck)
        row(3, JBLabel("Volume:"), volumeSlider)
        row(4, JBLabel("Tone duration (ms):"), durationSlider)

        // Test buttons
        val testSuccessBtn = JButton("▶  Test success sound").apply {
            addActionListener { SoundPlayer.playSuccessSound(currentState()) }
        }
        val testFailureBtn = JButton("▶  Test failure sound").apply {
            addActionListener { SoundPlayer.playFailureSound(currentState()) }
        }
        val btnPanel = JPanel().apply { add(testSuccessBtn); add(testFailureBtn) }

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.insets = Insets(12, 8, 4, 8)
        panel.add(btnPanel, gbc)

        // Push everything to the top
        gbc.gridy = 6; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.VERTICAL
        panel.add(JPanel(), gbc)

        reset()
        return panel
    }

    private fun currentState(): BuildSoundSettings.State {
        val s = BuildSoundSettings.getInstance().state
        return s.copy(
            volume = volumeSlider.value,
            toneDurationMs = durationSlider.value
        )
    }

    override fun isModified(): Boolean {
        val s = BuildSoundSettings.getInstance().state
        return enabledCheck.isSelected != s.enabled ||
               successCheck.isSelected != s.playOnSuccess ||
               failureCheck.isSelected != s.playOnFailure ||
               volumeSlider.value != s.volume ||
               durationSlider.value != s.toneDurationMs
    }

    override fun apply() {
        val s = BuildSoundSettings.getInstance().state
        s.enabled        = enabledCheck.isSelected
        s.playOnSuccess  = successCheck.isSelected
        s.playOnFailure  = failureCheck.isSelected
        s.volume         = volumeSlider.value
        s.toneDurationMs = durationSlider.value
    }

    override fun reset() {
        val s = BuildSoundSettings.getInstance().state
        enabledCheck.isSelected  = s.enabled
        successCheck.isSelected  = s.playOnSuccess
        failureCheck.isSelected  = s.playOnFailure
        volumeSlider.value       = s.volume
        durationSlider.value     = s.toneDurationMs
    }
}
