import java.util.ArrayList;
/**
 *
 * @author 1305309
 * For simple non-repeating characters String of kind "xyz"
 */
public class SuffixTree2 {
    public static void main(String[] args){
        SuffixTree1 st2 = new SuffixTree2("xyz".toCharArray());
        st1.build();
    }
    
    private static char[] input;
    private int remainingSuffixCount = 0;
    private End end;
    private SuffixNode root;
    private Active active;
    
    
    SuffixTree1(char[] in){
        input = new char[in.length + 1];
        int i;
        for(i = 0;i < in.length;i++)
            input[i] = in[i];
        input[i] = '$';
    }
    
    class End{
        int e;
        End(int value){
            e = value;
        }
    }
    
    static class SuffixNode{
      SuffixNode suffixLink;
      SuffixNode child[] = new SuffixNode[256];
      
      int start;
      End end;
      int index;
      
      private SuffixNode(){
          
      }
      
      public static SuffixNode createNode(int st,End en){
          SuffixNode node = new SuffixNode();
          node.start = st;
          node.end = en;
          return node;
      }
      
    }
    
    class Active{
        SuffixNode activeNode;
        int activeEdge;
        int activeLength;
        
        Active(SuffixNode node){
            activeNode = node;      //just referring
            activeEdge = -1;
            activeLength = 0;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////
    
    public void build(){
        root = SuffixNode.createNode(1,new End(0));
        root.index = -1;
        active = new Active(root);
        
        end = new End(-1);          //Global End
        
        for(int i = 0;i < input.length;i++)
            startPhase(i);
        
        setIndexUsingDfs(root,0,input.length);
    }
    
    ////////////////////////////////////////////////////////////////////////////////
    
    public void startPhase(int i){
        remainingSuffixCount++;   end.e++;  SuffixNode lastCreatedInternalNode = null;
        while(remainingSuffixCount > 0){
            //looking directly from root or from any internal node
            if(active.activeLength == 0){
                //check if that character already exist..and take corresponding action
                if(selectNode(i) != null){
                    active.activeLength = 1;
                    active.activeEdge = selectNode(i).start;
                    break;
                }
                else{
                    root.child[input[i]] = SuffixNode.createNode(i, end);
                    remainingSuffixCount--;
                }
            }
            
            
            //else if active length is not 0 means we are somwhere in between ,so check if next character is same or not
            //and take corresponding action
           else{
            }
        }
        
    }
    
    private void walkDown(int i)  {
        SuffixNode node = selectNode();
        if(diff(node) < active.activeLength){
            active.activeNode = node;
            active.activeLength = active.activeLength - diff(node) - 1;
            active.activeEdge = active.activeEdge + diff(node) + 1;
        }
        else{
            active.activeLength++;
        }
    }
    
    private char nextChar(int i) throws Exception {
        SuffixNode node = selectNode();
        if(diff(node) >= active.activeLength){
            return input[active.activeNode.child[input[active.activeEdge]].start + active.activeLength];
        }
        
        //need to be checked for other cases
        if((diff(node) + 1) == active.activeLength){
            if(node.child[input[i]] != null)
                return input[i];
            else
                return '@';
        }
        
        else{
            active.activeNode = node;
            active.activeEdge = active.activeEdge + diff(node) + 1;
            active.activeLength = active.activeLength - diff(node) - 1;
        }
         
        throw new Exception();
    }
    
    private int diff(SuffixNode node){
        return node.end.e - node.start;
    }
    
    private SuffixNode selectNode(){
        return active.activeNode.child[input[active.activeEdge]];
    }
    
    private SuffixNode selectNode(int i){
        return active.activeNode.child[input[i]];
    }
    
    private void setIndexUsingDfs(SuffixNode root,int val,int size){
        if(root == null)
            return;
        
        val += root.end.e - root.start + 1;
        if(root.index != -1){
            root.index = size - val;
            return;
        }
        
        for(SuffixNode node : root.child)
            setIndexUsingDfs(node,val,size);
        
        
    }
    
    public void dfsTraversal(){
        ArrayList<Character> result = new ArrayList();
        for(SuffixNode node : root.child)
            dfsTraversal(node,result);
    }
    
    public void dfsTraversal(SuffixNode root,ArrayList al){
        if(root == null)
            return;
        
        if (root.index != -1) {
            for (int i = root.start; i <= root.end.e; i++)
                al.add(input[i]);
            al.stream().forEach(System.out::print);
            System.out.println(" " + root.index);
            for (int i = root.start; i <= root.end.e; i++) 
                al.remove(al.size() - 1);
        }
        for(int i = root.start;i <= root.end.e;i++)
            al.add(input[i]);
        
        for(SuffixNode node : root.child)
            dfsTraversal(node,al);
        
        for(int i = root.start;i <= root.end.e;i++)
            al.remove(al.size() - 1);
    }
}
