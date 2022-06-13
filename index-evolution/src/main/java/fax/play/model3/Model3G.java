package fax.play.model3;

import fax.play.service.Model;
import org.infinispan.api.annotations.indexing.Basic;
import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoName;

@ProtoDoc("@Indexed")
@ProtoName("Model3")
public class Model3G implements Model {

    @ProtoField(number = 1)
    @Basic(projectable = true)
    public Integer entityVersion;

    @ProtoField(number = 2)
    public String id;

    @ProtoField(number = 3)
    @Deprecated
    @ProtoDoc("@Field(index = Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = \"lowercase\"))")
    public String name;

    @ProtoField(number = 4)
    @Basic(projectable = true)
    public String nameNonAnalyzed;

    @Override
    public String getId() {
        return id;
    }
}
