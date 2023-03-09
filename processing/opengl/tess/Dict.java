// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package processing.opengl.tess;


// Referenced classes of package processing.opengl.tess:
//            DictNode

class Dict
{
    public static interface DictLeq
    {

        public abstract boolean leq(Object obj, Object obj1, Object obj2);
    }


    private Dict()
    {
    }

    static void dictDelete(Dict dict, DictNode dictnode)
    {
        dictnode.next.prev = dictnode.prev;
        dictnode.prev.next = dictnode.next;
    }

    static void dictDeleteDict(Dict dict)
    {
        dict.head = null;
        dict.frame = null;
        dict.leq = null;
    }

    static DictNode dictInsert(Dict dict, Object obj)
    {
        return dictInsertBefore(dict, dict.head, obj);
    }

    static DictNode dictInsertBefore(Dict dict, DictNode dictnode, Object obj)
    {
        DictNode dictnode1;
        do
        {
            dictnode1 = dictnode.prev;
            if(dictnode1.key == null)
                break;
            dictnode = dictnode1;
        } while(!dict.leq.leq(dict.frame, dictnode1.key, obj));
        dict = new DictNode();
        dict.key = obj;
        dict.next = dictnode1.next;
        dictnode1.next.prev = dict;
        dict.prev = dictnode1;
        dictnode1.next = dict;
        return dict;
    }

    static Object dictKey(DictNode dictnode)
    {
        return dictnode.key;
    }

    static DictNode dictMax(Dict dict)
    {
        return dict.head.prev;
    }

    static DictNode dictMin(Dict dict)
    {
        return dict.head.next;
    }

    static Dict dictNewDict(Object obj, DictLeq dictleq)
    {
        Dict dict = new Dict();
        dict.head = new DictNode();
        dict.head.key = null;
        dict.head.next = dict.head;
        dict.head.prev = dict.head;
        dict.frame = obj;
        dict.leq = dictleq;
        return dict;
    }

    static DictNode dictPred(DictNode dictnode)
    {
        return dictnode.prev;
    }

    static DictNode dictSearch(Dict dict, Object obj)
    {
        DictNode dictnode = dict.head;
        DictNode dictnode1;
        do
        {
            dictnode1 = dictnode.next;
            if(dictnode1.key == null)
                break;
            dictnode = dictnode1;
        } while(!dict.leq.leq(dict.frame, obj, dictnode1.key));
        return dictnode1;
    }

    static DictNode dictSucc(DictNode dictnode)
    {
        return dictnode.next;
    }

    Object frame;
    DictNode head;
    DictLeq leq;
}
