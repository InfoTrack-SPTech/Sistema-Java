package dados.apaches3;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFBReader;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class LeitorExcel{

    public static List<List<Object>> extrairDadosPlanilha(String caminhoArquivo) throws Exception{

        IOUtils.setByteArrayMaxOverride(1_000_000_000);

        List<List<Object>> linhasPlanilha = new ArrayList<>();
        try(OPCPackage pacoteExcel = OPCPackage.open(new BufferedInputStream(new FileInputStream(caminhoArquivo)))){

            XSSFReader leitorExcel = new XSSFReader(pacoteExcel);
            XMLReader leitorXML = org.apache.poi.util.XMLHelper.newXMLReader();
            XSSFSheetXMLHandler manipularPlanilha = new XSSFSheetXMLHandler(leitorExcel.getStylesTable(), new ReadOnlySharedStringsTable(pacoteExcel), new LeitorDados(linhasPlanilha), false);

            try(InputStream fluxoFolha = leitorExcel.getSheetsData().next()){
                leitorXML.setContentHandler(manipularPlanilha);
                leitorXML.parse(new InputSource(fluxoFolha));
            }
        }
        return linhasPlanilha;
    }
}