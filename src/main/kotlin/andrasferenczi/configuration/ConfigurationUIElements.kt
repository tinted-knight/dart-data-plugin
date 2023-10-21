package andrasferenczi.configuration

import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JTextField

// Output
class ConfigurationUIElements constructor(
    val jComponent: JComponent,

    private val copyWithNameTextField: JTextField,
    private val useRequiredAnnotationCheckBox: JCheckBox,
    private val useNewKeywordCheckbox: JCheckBox,
    private val useConstKeywordForConstructorCheckbox: JCheckBox,
    private val optimizeConstCopyCheckbox: JCheckBox,
    private val functionsCopyCheckbox: JCheckBox,
    private val addKeyMapperForMapCheckbox: JCheckBox,
    private val noImplicitCastsCheckbox: JCheckBox,
    private val nullSafety: JCheckBox
) {

    fun extractCurrentConfigurationData(): ConfigurationData {
        return ConfigurationData(
            copyWithMethodName = copyWithNameTextField.text,
            useRequiredAnnotation = useRequiredAnnotationCheckBox.isSelected,
            useNewKeyword = useNewKeywordCheckbox.isSelected,
            useConstForConstructor = useConstKeywordForConstructorCheckbox.isSelected,
            optimizeConstCopy = optimizeConstCopyCheckbox.isSelected,
            functionsCopy = functionsCopyCheckbox.isSelected,
            addKeyMapperForMap = addKeyMapperForMapCheckbox.isSelected,
            noImplicitCasts = noImplicitCastsCheckbox.isSelected,
            nullSafety = nullSafety.isSelected
        )
    }

    fun setFields(configurationData: ConfigurationData) {
        copyWithNameTextField.text = configurationData.copyWithMethodName
        useRequiredAnnotationCheckBox.isSelected = configurationData.useRequiredAnnotation
        useNewKeywordCheckbox.isSelected = configurationData.useNewKeyword
        useConstKeywordForConstructorCheckbox.isSelected = configurationData.useConstForConstructor
        optimizeConstCopyCheckbox.isSelected = configurationData.optimizeConstCopy
        functionsCopyCheckbox.isSelected = configurationData.functionsCopy
        addKeyMapperForMapCheckbox.isSelected = configurationData.addKeyMapperForMap
        noImplicitCastsCheckbox.isSelected = configurationData.noImplicitCasts
        nullSafety.isSelected = configurationData.nullSafety
    }
}
