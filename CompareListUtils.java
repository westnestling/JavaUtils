
/**
 * @Author daley
 * @Date 2018/6/21 14:50
 */
public class CompareListUtils {

    public static final String Result_EQUAL = "EQUAL";
    public static final String Result_INSERT = "INSERT";
    public static final String Result_DELETE = "DELETE";
    public static final String Result_CHANGE = "CHANGE";

    private static List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<String>();
        String line = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /***
     * 对文件进行比较
     * INSERT, DELETE, CHANGE, EQUAL
     * @param fromFileName 原来的文件名
     * @param toFileName 新的文件名
     */
    public static List<CompareMo> compareAll(String fromFileName, String toFileName) {
        List<String> original = fileToLines(fromFileName);
        List<String> revised = fileToLines(toFileName);
        final DiffRowGenerator.Builder builder = new DiffRowGenerator.Builder();
        final DiffRowGenerator dfg = builder.build();
        final List<DiffRow> rows = dfg.generateDiffRows(original, revised);
        List<CompareMo> listCompareMo = new ArrayList<CompareMo>();
        int i=1;
        int oldSize = original.size();
        int newSize = revised.size();
        int insertSize = 0;
        int deleteSize = 0;
        for (final DiffRow diffRow : rows) {
            String tag = diffRow.getTag().toString();
            String oldLine = diffRow.getOldLine();
            String newLine = diffRow.getNewLine();
            if(Result_CHANGE.equals(tag)){
                boolean isInset = false;
                if ((i-insertSize) <= oldSize) {
                    if(oldLine!=null&& oldLine.trim().length()==0){
                        if(!original.get(i-1-insertSize).equals(oldLine)){
                            tag = Result_INSERT;
                            isInset = true;
                            insertSize ++;
                        }
                    }
                }
                if (!isInset) {
                    if ((i-deleteSize) <= newSize) {
                        if(newLine!=null&& newLine.trim().length()==0){
                            if(!revised.get(i-1-deleteSize).equals(oldLine)){
                                tag = Result_DELETE;
                                isInset = true;
                                deleteSize ++;
                            }
                        }
                    }
                }
            }
            listCompareMo.add(new CompareMo(
                    i, oldLine,
                    newLine,
                    tag
            ));
            i++;
        }
        return listCompareMo;
    }

}
