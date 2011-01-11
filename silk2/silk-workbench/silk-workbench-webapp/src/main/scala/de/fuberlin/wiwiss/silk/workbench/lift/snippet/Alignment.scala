package de.fuberlin.wiwiss.silk.workbench.lift.snippet

import xml.NodeSeq
import net.liftweb.http.{S, SHtml, FileParamHolder}
import de.fuberlin.wiwiss.silk.workbench.project.Project
import de.fuberlin.wiwiss.silk.evaluation.AlignmentReader
import java.io.ByteArrayInputStream
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds.{Script, OnLoad}

class Alignment
{
  def menu(xhtml : NodeSeq) : NodeSeq =
  {
    def showOpenDialog() =
    {
      JsRaw("$('#openAlignmentDialog').dialog('open')").cmd
    }

    val initOpenDialog = Script(OnLoad(JsRaw("$('#openAlignmentDialog').dialog({ autoOpen: false, width: 700, modal: true })").cmd))

    bind("entry", xhtml,
         "open" -> (initOpenDialog ++ SHtml.ajaxButton("Open", showOpenDialog _)))
  }
  
  def openAlignmentDialog(xhtml : NodeSeq) : NodeSeq =
  {
    var fileHolder : FileParamHolder = null

    def submit()
    {
      fileHolder match
      {
        case FileParamHolder(_, mime, _, data) =>
        {
          Project.updateAlignment(AlignmentReader.readAlignment(new ByteArrayInputStream(data)))
          S.redirectTo("alignment")
        }
        case _ =>
      }
    }

    bind("entry", xhtml,
         "file" -> SHtml.fileUpload(fileHolder = _),
         "submit" -> SHtml.submit("Open", submit, "style" -> "float:right;"))
  }

  def show(xhtml : NodeSeq) : NodeSeq =
  {
    <table border="1">
      <tr>
        <th>Source</th>
        <th>Target</th>
        <th>Confidence</th>
        <th>Type</th>
      </tr>
      {
        for(link <- Project().alignment.positiveLinks) yield
        {
          <tr>
            <td>{link.sourceUri}</td>
            <td>{link.targetUri}</td>
            <td>{link.confidence}</td>
            <td>positive</td>
          </tr>
        }
      }
      {
        for(link <- Project().alignment.negativeLinks) yield
        {
          <tr>
            <td>{link.sourceUri}</td>
            <td>{link.targetUri}</td>
            <td>{link.confidence}</td>
            <td>negative</td>
          </tr>
        }
      }
    </table>
  }
}