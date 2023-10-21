package andrasferenczi.action.my

import andrasferenczi.action.BaseAnAction
import andrasferenczi.action.StaticActionProcessor
import andrasferenczi.action.data.GenerationData
import andrasferenczi.action.data.PerformAction
import andrasferenczi.action.init.ActionData
import andrasferenczi.action.utils.selectFieldsWithDialog
import andrasferenczi.ext.addNewLine
import andrasferenczi.ext.addSemicolon
import andrasferenczi.ext.addSpace
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.withParentheses
import andrasferenczi.templater.TemplateConstants
import andrasferenczi.templater.TemplateType
import andrasferenczi.utils.mergeCalls
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.lang.dart.psi.DartClassDefinition

class JsonAction : BaseAnAction() {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun processAction(
        event: AnActionEvent,
        actionData: ActionData,
        dartClass: DartClassDefinition
    ): PerformAction? {
        val declarations = selectFieldsWithDialog(actionData.project, dartClass) ?: return null

        return Companion.processAction(
            GenerationData(actionData, dartClass, declarations)
        )
    }

    companion object : StaticActionProcessor {
        private fun createDeleteCall(dartClass: DartClassDefinition): (() -> Unit)? {
            val toJsonMethod = dartClass.findMethodByName(TemplateConstants.TO_JSON_METHOD_NAME)
            val fromJsonMethod = dartClass.findNamedConstructor(TemplateConstants.FROM_JSON_METHOD_NAME)

            return listOfNotNull(
                toJsonMethod,
                fromJsonMethod
            )
                .map { { it.delete() } }
                .mergeCalls()
        }

        override fun processAction(generationData: GenerationData): PerformAction? {
            val (actionData, dartClass, declarations) = generationData

            val project = actionData.project

            val templateManager = TemplateManager.getInstance(project)
            val dartClassName = dartClass.extractClassName()

            val template = createJsonTemplate(
                templateManager = templateManager,
                className = dartClassName,
            )

            return PerformAction(
                createDeleteCall(dartClass),
                template
            )
        }

    }
}

fun createJsonTemplate(
    templateManager: TemplateManager,
    className: String
): Template {

    return templateManager.createTemplate(
        TemplateType.JsonTemplate.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        addToJson()
        addNewLine()
        addNewLine()
        addFromJson(className)
    }
}

private fun Template.addToJson() {
    isToReformat = true

    addTextSegment("String ${TemplateConstants.TO_JSON_METHOD_NAME}")
    withParentheses { }
    addTextSegment(" => ")
    addTextSegment("json.encode(${TemplateConstants.TO_MAP_METHOD_NAME}())")
    addSemicolon()
}

private fun Template.addFromJson(className: String) {
    isToReformat = true

    addTextSegment("factory")
    addSpace()
    addTextSegment(className)
    addTextSegment(".")
    addTextSegment(TemplateConstants.FROM_JSON_METHOD_NAME)
    withParentheses {
        addTextSegment("String source")
    }
    addTextSegment(" => ")
    addNewLine()
    addTextSegment("${className}.${TemplateConstants.FROM_MAP_METHOD_NAME}")
    withParentheses {
        addTextSegment("json.decode(source) as Map<String, dynamic>")
    }
    addSemicolon()
}
